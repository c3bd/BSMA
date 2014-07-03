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

import edu.ecnu.imc.bsma.UnknownDBException;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.measurements.Measurements;

/**
 * Creates a DB layer by dynamically classloading the specified DB class.
 */
public class DBFactory {
	public static DB newDB(JobInfo job, Properties properties,
			Measurements measurements) throws UnknownDBException {
		ClassLoader classLoader = DBFactory.class.getClassLoader();

		DB ret = null;
		try {
			Class dbClass = job.getDBClass();
			ret = (DB) dbClass.newInstance();
		} catch (Exception e) {
			throw new UnknownDBException(e);
		}

		ret.setProperties(properties);

		return new DBWrapper(ret, measurements);
	}

}
