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
import java.util.HashMap;
import java.util.Properties;

import edu.ecnu.imc.bsma.measurements.exporter.MeasurementsExporter;
import edu.ecnu.imc.bsma.measurements.exporter.RuntimeExporter;

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

	// static Measurements singleton = null;

	static Properties measurementproperties = null;

	public static void setProperties(Properties props) {
		measurementproperties = props;
	}

	HashMap<String, OneMeasurement> data;

	private Properties _props;

	/**
	 * Create a new object with the specified properties.
	 */
	public Measurements(Properties props) {
		data = new HashMap<String, OneMeasurement>();

		_props = props;

	}

	OneMeasurement constructOneMeasurement(String name) {
		return new OneMeasurementHistogram(name, _props);
	}

	/**
	 * Report a single value of a single metric. E.g. for BSMAQuery1 latency,
	 * operation="Query1" and latency is the measured value.
	 */
	public synchronized void measure(String operation, int latency) {
		if (!data.containsKey(operation)) {
			synchronized (this) {
				if (!data.containsKey(operation)) {
					data.put(operation, constructOneMeasurement(operation));
				}
			}
		}
		try {
			data.get(operation).measure(latency);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.out
					.println("ERROR: java.lang.ArrayIndexOutOfBoundsException - ignoring and continuing");
			e.printStackTrace();
			e.printStackTrace(System.out);
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
	public void exportMeasurements(MeasurementsExporter exporter)
			throws IOException {
		for (OneMeasurement measurement : data.values()) {
			measurement.exportMeasurements(exporter);
		}
	}

	/**
	 * Return the summary of the measurements.
	 */
	public void getSummary(RuntimeExporter reporter) {
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
