/**                                                                                                                                                                                
 * Copyright (c) 2012 Institute of Massive Computing, East China Normal University.
 * NOTICE: This file is based on the corresponding one in YCSB [Copyright (c) 2010 Yahoo! Inc.]                                                                                                                             
 *                                                                                                                                                                                 
 * Licensed under the Apache License, Version 2.0 (the "License"); you                                                                                                             
 * may not use this file except in compliance with the License. You                                                                                                                
 * may obtain a copy of the License at                                                                                                                                             
 *                                                                                                                                                                                 
 * http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                      
 *                                                                                                                                                                                 
 * Unless required by applicable law or agreed to in writing, software                                                                                                             
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                                               
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or                                                                                                                 
 * implied. See the License for the specific language governing                                                                                                                    
 * permissions and limitations under the License. See accompanying                                                                                                                 
 * LICENSE file.                                                                                                                                                                   
 */

package edu.ecnu.imc.bsma.measurements;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.ecnu.imc.bsma.measurements.exporter.DBExporter;

/**
 * Take measurements and maintain a histogram of a given metric, such as Query1
 * LATENCY.
 * 
 * @author cooperb
 * @author wjx
 */
public class OneMeasurementHistogram extends OneMeasurement {
	public static final String BUCKETS = "histogram.buckets";

	/**
	 * The default number of buckets that all latencies will be partitioned
	 * into.
	 */
	public static final String BUCKETS_DEFAULT = "1000";

	/**
	 * The interval (ms) from one bucket to the next one. To set it to 100 means
	 * to put the latencies bigger than 0ms but smaller than 100ms into bucket1,
	 * and 100ms to 200ms into bucket2...
	 */
	// final int interval = 1;
	final int interval = 100;

	int _buckets;
	int[] histogram;
	int histogramoverflow;
	int operations;
	long totallatency;

	// keep a windowed version of these stats for printing status
	int windowoperations;
	long windowtotallatency;

	int min;
	int max;

	public OneMeasurementHistogram(String name, Properties props) {
		super(name);
		_buckets = Integer
				.parseInt(props.getProperty(BUCKETS, BUCKETS_DEFAULT));
		histogram = new int[_buckets];
		histogramoverflow = 0;
		operations = 0;
		totallatency = 0;
		windowoperations = 0;
		windowtotallatency = 0;
		min = -1;
		max = -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ecnu.imc.bsma.measurements.OneMeasurement#measure(int)
	 */
	public synchronized void measure(int latency) {
		if (latency >= _buckets * interval) {
			histogramoverflow++;
		} else {
			int tmp = latency / interval;
			histogram[tmp]++;
		}
		operations++;
		totallatency += latency;
		windowoperations++;
		windowtotallatency += latency;

		if ((min < 0) || (latency < min)) {
			min = latency;
		}

		if ((max < 0) || (latency > max)) {
			max = latency;
		}
	}

	public static final HashMap<String, Byte> qIDs = new HashMap<String, Byte>();
	static {
		for (Byte i = 1; i < 20; i++)
			qIDs.put("Query" + i, i);
	}

	public byte getQID(String query) {
		if (qIDs.containsKey(query)) {
			return qIDs.get(query);
		} else if (query.equals("overall")) {
			return 0;
		} else {
			// unknown type
			return -1;
		}
	}

	public byte getQueryID() {
		return getQID(getName());
	}

	@Override
	public void exportMeasurements(DBExporter exporter) throws IOException {
		List<Integer> latencyHist = new ArrayList<Integer>(4);
		List<Float> latencyStats = new ArrayList<Float>(3);
		for (int i = 0; i < 4; i++)
			latencyHist.add(0);
		// Just in case there's only one query.

		latencyStats.add((float) min);
		latencyStats.add((float) (((double) totallatency) / operations));
		latencyStats.add((float) max);

		int opcounter = 0;
		boolean done50th = false;
		boolean done75th = false;
		boolean done95th = false;
		for (int i = 0; i < _buckets; i++) {
			opcounter += histogram[i];
			int latency = (i + 1) * interval;
			if ((!done50th)
					&& (((double) opcounter) / ((double) operations) >= 0.50)) {
				latencyHist.set(0, latency);
				done50th = true;
			}
			if ((!done75th)
					&& (((double) opcounter) / ((double) operations) >= 0.75)) {
				latencyHist.set(1, latency);
				done75th = true;
			}
			if ((!done95th)
					&& (((double) opcounter) / ((double) operations) >= 0.95)) {

				latencyHist.set(2, latency);
				done95th = true;
			}
			if (((double) opcounter) / ((double) operations) >= 0.99) {
				latencyHist.set(3, latency);
				break;
			}
		}
		try {
			exporter.reportSubJobResult(getQueryID(), latencyHist, latencyStats);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getSummary() {
		if (windowoperations == 0) {
			return "";
		}
		DecimalFormat d = new DecimalFormat("#.##");
		double report = ((double) windowtotallatency)
				/ ((double) windowoperations);
		windowtotallatency = 0;
		windowoperations = 0;
		return "[" + getName() + " AverageLatency(ms)=" + d.format(report)
				+ "]";
	}

	@Override
	public void getSummary(DBExporter exporter) {
		double report = 0;
		if (windowoperations != 0) {
			report = ((double) windowtotallatency)
					/ ((double) windowoperations);
			windowtotallatency = 0;
			windowoperations = 0;
		}
		exporter.reportOneMeasure(getQueryID(), report);
	}
}
