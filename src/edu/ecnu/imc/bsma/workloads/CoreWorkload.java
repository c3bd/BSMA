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

package edu.ecnu.imc.bsma.workloads;

import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.db.DB;
import edu.ecnu.imc.bsma.generator.DiscreteGenerator;

/**
 * The core benchmark scenario. Represents a set of clients doing simple CRUD
 * operations. The relative proportion of different kinds of operations, and
 * other properties of the workload, are controlled by parameters specified at
 * runtime.
 * 
 * Properties to control the client:
 * <UL>
 * <LI><b>queryNproportion</b>: what proportion of operations should be queryNs
 * (default: 0)
 * <LI><b>returncount</b>: for Query1,2,3,6,7,8,9,10,11,12,13,14,15,16,17,18,19,
 * what is the maximum number of records to return (default: 99999999)
 * <LI><b>timespan</b>: for Query6,7,9,10,11,12,13,14,15,16,17,18,19, results
 * are the proper records generated in a time span, such as one month.(default:
 * NOT GIVEN)
 * </ul>
 */
public class CoreWorkload extends Workload {
	public static Logger logger = LoggerFactory.getLogger(CoreWorkload.class);
	/**
	 * The name of the file stores all the selected user IDs, which will be used
	 * in most queries
	 */
	public static final String USER_ID_FILE_NAME = "selected_user_IDs.txt";

	/**
	 * The name of the file stores all the selected event tags, which will be
	 * used in some queries
	 */
	public static final String EVENT_TAG_FILE_NAME = "selected_event_tags.txt";

	/**
	 * The name of the file stores all the selected date times, which will be
	 * used in some queries
	 */
	public static final String TIME_FILE_NAME = "selected_times.txt";

	/**
	 * The name of the file stores all the selected datetimes for users, which
	 * will be used in some queries
	 */
	public static final String USER_TIME_FILE_NAME = "selected_times_for_users.txt";

	/**
	 * The number of date times listed for one user
	 */
	public static final int TIME_COUNT_FOR_USER = 3;

	/**
	 * The name of the file stores all the selected datetimes for event tags,
	 * which will be used in some queries
	 */
	public static final String TAG_TIME_FILE_NAME = "selected_times_for_tags.txt";

	/**
	 * The number of date times listed for one tag
	 */
	public static final int TIME_COUNT_FOR_TAG = 1;

	/**
	 * The separator of user(tag) and times
	 */
	public static final String SEPARATOR = ":";

	/**
	 * The separator between times
	 */
	public static final String SUBSEPARATOR = "$";

	public static final String[] PROPORTION_PROPERTY = { "query1proportion",
			"query2proportion", "query3proportion", "query4proportion",
			"query5proportion", "query6proportion", "query7proportion",
			"query8proportion", "query9proportion", "query10proportion",
			"query11proportion", "query12proportion", "query13proportion",
			"query14proportion", "query15proportion", "query16proportion",
			"query17proportion", "query18proportion", "query19proportion",
			"query20proportion", "query21proportion", "query22proportion",
			"query23proportion", "query24proportion" };
	public static final String[] PROPORTION_DEFAULT = { "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0", "0" };

	/**
	 * The default time span is NOT GIVEN.
	 */
	public static final String TIME_SPAN_PROPERTY_DEFAULT = "NOT GIVEN";

	DiscreteGenerator<String> operationchooser;
	HashMap<String, QueryWrapper> wrappers = new HashMap<String, QueryWrapper>();

	/**
	 * Initialize the scenario. Called once, in the main client thread, before
	 * any operations are started.
	 */
	@SuppressWarnings("unchecked")
	public void init(JobInfo jobInfo, Properties p) throws WorkloadException {
		operationchooser = new DiscreteGenerator<String>();
		for (int i = 1; i <= PROPORTION_PROPERTY.length; i++) {
			double weight = Double.parseDouble(p.getProperty(
					PROPORTION_PROPERTY[i - 1], PROPORTION_DEFAULT[i - 1]));
			if (weight > 0.0) {
				operationchooser.addValue(weight, "Query" + i);
				try {
					wrappers.put(
							"Query" + i,
							QueryWrapperFactory.create(jobInfo,
									jobInfo.getQuery(i)));
				} catch (Exception e) {
					throw new WorkloadException(e);
				}
			}
		}

	}

	/**
	 * Do one analysis operation(i.e., one query). Because it will be called
	 * concurrently from multiple client threads, this function must be thread
	 * safe. However, avoid synchronized, or the threads will block waiting for
	 * each other, and it will be difficult to reach the target throughput.
	 * Ideally, this function would have no side effects other than DB
	 * operations.
	 */
	public boolean doAnalysis(DB db, Object threadstate) {
		String op = operationchooser.nextString();
		if (wrappers.containsKey(op)) {
			wrappers.get(op).doAnalysisQuery(db);
		} else {
			// TODO error
		}

		return true;
	}

}
