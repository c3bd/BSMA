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
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ecnu.imc.bsma.measurements.exporter.DBExporter;

/**
 * Collects latency measurements, and reports them when requested.
 * 
 * @author cooperb
 * @author wjx
 * 
 * @version 2014-06-25
 * @author xiafan
 * 
 */
public class Measurements {
	public static final Logger logger = LoggerFactory
			.getLogger(Measurements.class);

	static final String OVERALL = "OVERALL";

	static Properties measurementproperties = null;

	public static void setProperties(Properties props) {
		measurementproperties = props;
	}

	ConcurrentHashMap<String, OneMeasurement> data;

	private Properties _props;

	/**
	 * Create a new object with the specified properties.
	 */
	public Measurements(Properties props) {
		data = new ConcurrentHashMap<String, OneMeasurement>();

		_props = props;

	}

	OneMeasurement constructOneMeasurement(String name) {
		return new OneMeasurementHistogram(name, _props);
	}

	/**
	 * Report a single value of a single metric. E.g. for BSMAQuery1 latency,
	 * operation="Query1" and latency is the measured value.
	 */
	public void measure(String operation, int latency) {
		measure_intern(operation, latency);
		measure_intern(OVERALL, latency);
	}

	/**
	 * Report a single value of a single metric. E.g. for BSMAQuery1 latency,
	 * operation="Query1" and latency is the measured value.
	 */
	public void measure_intern(String operation, int latency) {
		OneMeasurement one = null;
		if (!data.containsKey(operation)) {
			one = constructOneMeasurement(operation);
			OneMeasurement tmp = data.putIfAbsent(operation, one);
			if (tmp != null)
				one = tmp;
		} else {
			one = data.get(operation);
		}

		try {
			one.measure(latency);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			logger.error("java.lang.ArrayIndexOutOfBoundsException - ignoring and continuing");
			logger.error(e.getMessage());
		}
	}

	/**
	 * Export the current measurements to a suitable format.
	 * 
	 * @param exporter
	 *            Exporter representing the type of format to write to.
	 * @throws IOException
	 *             Thrown if the export failed.
	 */
	public void exportMeasurements(DBExporter exporter) throws IOException {
		for (OneMeasurement measurement : data.values()) {
			measurement.exportMeasurements(exporter);
		}
	}

	/**
	 * Return the summary of the measurements.
	 */
	public void getSummary(DBExporter reporter) {
		for (OneMeasurement m : data.values()) {
			m.getSummary(reporter);
		}
	}

	/**
	 * Return a one line summary of the measurements.
	 */
	public String getSummary() {
		String ret = "";
		for (OneMeasurement m : data.values()) {
			ret += m.getSummary() + " ";
		}

		return ret;
	}
}
