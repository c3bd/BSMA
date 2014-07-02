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

package edu.ecnu.imc.bsma.db;

import java.util.Properties;

/**
 * A layer for accessing a database to be benchmarked. Each thread in the client
 * will be given its own instance of whatever DB class is to be used in the
 * test. This class should be constructed using a no-argument constructor, so we
 * can load it dynamically. Any argument-based initialization should be done by
 * init().
 * 
 */
public abstract class DB
{
	/**
	 * Properties for configuring this DB.
	 */
	Properties _p = new Properties();

	/**
	 * Set the properties for this DB.
	 */
	public void setProperties(Properties p)
	{
		_p = p;

	}

	/**
	 * Get the set of properties for this DB.
	 */
	public Properties getProperties()
	{
		return _p;
	}

	/**
	 * Initialize any state for this DB. Called once per DB instance; there is
	 * one DB instance per client thread.
	 */
	public void init() throws DBException
	{
	}

	/**
	 * Cleanup any state for this DB. Called once per DB instance; there is one
	 * DB instance per client thread.
	 */
	public void cleanup() throws DBException
	{
	}

	/**
	 * Perform query1 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery1
	 * @param returncount
	 *            limit the number of returned records
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery1(String userID, int returncount);

	/**
	 * Perform query2 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery2
	 * @param returncount
	 *            limit the number of returned records
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery2(String userID, int returncount);

	/**
	 * Perform query3 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery3
	 * @param returncount
	 *            limit the number of returned records
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery3(String userID, int returncount);

	/**
	 * Perform query4 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID1
	 *            , userID2 specify two userID used in BSMAQuery4
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery4(String userID1, String userID2);

	/**
	 * Perform query5 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID1
	 *            , userID2 specify two userID used in BSMAQuery5
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery5(String userID1, String userID2);

	/**
	 * Perform query6 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery6
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery6 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery6
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery6(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query7 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery7 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery7
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery7(int returncount, String datetime, String timespan);

	/**
	 * Perform query8 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param returncount
	 *            limit the number of returned records
	 * @param userID
	 *            specify userID used in BSMAQuery8
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery8(int returncount,String userID);

	/**
	 * Perform query9 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery8
	 * @param tag
	 *            specify event tag used in BSMAQuery9
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery9 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery9
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery9(String userID, String tag, int returncount, String datetime, String timespan);

	/**
	 * Perform query10 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery10 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery10
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery10(int returncount, String datetime, String timespan);

	/**
	 * Perform query11 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery11
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery11
	 * @param timespan specify
	 *            specify time span used in BSMAQuery11
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery11(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query12 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery12
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery12 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery12
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery12(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query13 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery13
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery13 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery13
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery13(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query14 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery14 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery14
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery14(int returncount, String datetime, String timespan);

	/**
	 * Perform query15 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param tag
	 *            specify event tag used in BSMAQuery15
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery15 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery15
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery15(String tag, int returncount, String datetime, String timespan);

	/**
	 * Perform query16 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery16
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery16 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery16
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery16(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query17 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery17
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery17 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery17
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery17(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query18 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery18
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery18 
	 * @param timespan specify
	 *            specify time span used in BSMAQuery18
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery18(String userID, int returncount, String datetime, String timespan);

	/**
	 * Perform query19 for a set of records in the database. Result will be
	 * returned as proper String
	 * 
	 * @param userID
	 *            specify userID used in BSMAQuery19
	 * @param returncount
	 *            limit the number of returned records
	 * @param datetime
	 *            specify date time used in BSMAQuery19
	 * @param timespan
	 *            specify time span used in BSMAQuery19
	 * @return a proper string consists of current param value(s) and all the
	 *         returned record
	 * 
	 */
	public abstract String BSMAQuery19(String userID, int returncount, String datetime, String timespan);

}
