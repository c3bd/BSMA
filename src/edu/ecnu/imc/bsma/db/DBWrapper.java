/**                                                                                                                                                                                
 * Copyright (c) 2012 Institute of Massive Computing, East China Normal University.
 * NOTICE: This file is based on the corresponding one in YCSB [Copyright (c) 2010 Yahoo! Inc.]                                                                                                                                                                                    
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

package edu.ecnu.imc.bsma.db;

import java.util.Properties;

import edu.ecnu.imc.bsma.measurements.Measurements;

/**
 * Wrapper around a "real" DB that measures latencies and then prints results
 */
/**
 * @author rhein
 * @author xiafan
 */
public class DBWrapper extends DB {
	DB _db;
	Measurements _measurements;

	public DBWrapper(DB db, Measurements measurements) {
		_db = db;
		_measurements = measurements;
	}

	/**
	 * Set the properties for this DB.
	 */
	public void setProperties(Properties p) {
		_db.setProperties(p);
	}

	/**
	 * Get the set of properties for this DB.
	 */
	public Properties getProperties() {
		return _db.getProperties();
	}

	/**
	 * Initialize any state for this DB. Called once per DB instance; there is
	 * one DB instance per client thread.
	 */
	public void init() throws DBException {
		_db.init();
	}

	/**
	 * Cleanup any state for this DB. Called once per DB instance; there is one
	 * DB instance per client thread.
	 */
	public void cleanup() throws DBException {
		_db.cleanup();
	}

	public String BSMAQuery1(String userID, int returncount) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery1(userID, returncount);
		long en = System.currentTimeMillis();
		_measurements.measure("Query1", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery2(String userID, int returncount) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery2(userID, returncount);
		long en = System.currentTimeMillis();
		_measurements.measure("Query2", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery3(String userID, int returncount) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery3(userID, returncount);
		long en = System.currentTimeMillis();
		_measurements.measure("Query3", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery4(String userID1, String userID2) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery4(userID1, userID2);
		long en = System.currentTimeMillis();
		_measurements.measure("Query4", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery5(String userID1, String userID2) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery5(userID1, userID2);
		long en = System.currentTimeMillis();
		_measurements.measure("Query5", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery6(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery6(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query6", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery7(int returncount, String datetime, String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery7(returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query7", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery8(int returncount, String userID) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery8(returncount, userID);
		long en = System.currentTimeMillis();
		_measurements.measure("Query8", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery9(String userID, String tag, int returncount,
			String datetime, String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery9(userID, tag, returncount, datetime,
				timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query9", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery10(int returncount, String datetime, String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery10(returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query10", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery11(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery11(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query11", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery12(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery12(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query12", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery13(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery13(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query13", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery14(int returncount, String datetime, String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery14(returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query14", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery15(String tag, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery15(tag, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query15", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery16(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery16(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query16", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery17(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery17(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query17", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery18(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery18(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query18", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	public String BSMAQuery19(String userID, int returncount, String datetime,
			String timespan) {
		long st = System.currentTimeMillis();
		String result = _db
				.BSMAQuery19(userID, returncount, datetime, timespan);
		long en = System.currentTimeMillis();
		_measurements.measure("Query19", (int) (en - st));
		// System.out.println(result);
		return result;
	}

	@Override
	public String BSMAQuery20(String eventID) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery20(eventID);
		long en = System.currentTimeMillis();
		_measurements.measure("Query20", (int) (en - st));
		return result;
	}

	@Override
	public String BSMAQuery21(String eventID) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery21(eventID);
		long en = System.currentTimeMillis();
		_measurements.measure("Query21", (int) (en - st));
		return result;
	}

	@Override
	public String BSMAQuery22(String mid) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery22(mid);
		long en = System.currentTimeMillis();
		_measurements.measure("Query22", (int) (en - st));
		return result;
	}

	@Override
	public String BSMAQuery23(String eventID) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery23(eventID);
		long en = System.currentTimeMillis();
		_measurements.measure("Query23", (int) (en - st));
		return result;
	}

	@Override
	public String BSMAQuery24(String eventID) {
		long st = System.currentTimeMillis();
		String result = _db.BSMAQuery24(eventID);
		long en = System.currentTimeMillis();
		_measurements.measure("Query24", (int) (en - st));
		return result;
	}
}
