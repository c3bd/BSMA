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
package edu.ecnu.imc.bsma.measurements.exporter;

import java.io.Closeable;
import java.io.IOException;

/**
 * Used to export the collected measurements into a useful format, for example
 * human readable text
 */
public interface MeasurementsExporter extends Closeable
{

	/**
	 * Write a measurement to the exported format.
	 * 
	 * @param metric
	 *            Metric name, for example "Query1 LATENCY".
	 * @param measurement
	 *            Measurement name, for example "Average latency".
	 * @param i
	 *            Measurement to write.
	 * @throws IOException
	 *             if writing failed
	 */
	public void write(String metric, String measurement, int i) throws IOException;

	/**
	 * Write a measurement to the exported format.
	 * 
	 * @param metric
	 *            Metric name, for example "Query LATENCY".
	 * @param measurement
	 *            Measurement name, for example "Average latency".
	 * @param d
	 *            Measurement to write.
	 * @throws IOException
	 *             if writing failed
	 */
	public void write(String metric, String measurement, double d) throws IOException;

}
