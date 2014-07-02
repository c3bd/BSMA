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

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import edu.ecnu.imc.bsma.Utils;
import edu.ecnu.imc.bsma.Workload;
import edu.ecnu.imc.bsma.WorkloadException;
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
			"query17proportion", "query18proportion", "query19proportion" };
	public static final String[] PROPORTION_DEFAULT = { "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0" };

	/**
	 * The name of the property for time span. Options are 'h','d','w' and 'y'
	 */
	public static final String TIME_SPAN_PROPERTY = "timespan";

	/**
	 * The default time span is NOT GIVEN.
	 */
	public static final String TIME_SPAN_PROPERTY_DEFAULT = "NOT GIVEN";

	/**
	 * The name of property for return count. Options are 10,50 and 100
	 */
	public static final String RETURN_COUNT_PROPERTY = "returncount";

	/**
	 * The default maximum number of returned records is 99999999
	 */
	public static final String RETURN_COUNT_PROPERTY_DEFAULT = "99999999";

	DiscreteGenerator<String> operationchooser;
	int returncount;
	String timespan;
	Random random = new Random();
	ConcurrentHashMap<Integer, String> uidchm = new ConcurrentHashMap<Integer, String>();
	ConcurrentHashMap<Integer, String> tagchm = new ConcurrentHashMap<Integer, String>();
	ConcurrentHashMap<Integer, String> timechm = new ConcurrentHashMap<Integer, String>();
	ConcurrentHashMap<String, List<String>> uidtimechm = new ConcurrentHashMap<String, List<String>>();
	ConcurrentHashMap<String, List<String>> tagtimechm = new ConcurrentHashMap<String, List<String>>();

	int uidchmsize;
	int tagchmsize;
	int timechmsize;
	int uidtimechmsize;
	int tagtimechmsize;

	/**
	 * Initialize the scenario. Called once, in the main client thread, before
	 * any operations are started.
	 */
	@SuppressWarnings("unchecked")
	public void init(Properties p) throws WorkloadException {
		for (int i = 1; i <= PROPORTION_PROPERTY.length; i++) {
			double weight = Double.parseDouble(p.getProperty(
					PROPORTION_PROPERTY[i], PROPORTION_DEFAULT[i]));
			if (weight > 0.0)
				operationchooser.addValue(weight, "Query" + i);
		}

		returncount = Integer.parseInt(p.getProperty(RETURN_COUNT_PROPERTY,
				RETURN_COUNT_PROPERTY_DEFAULT));
		timespan = p
				.getProperty(TIME_SPAN_PROPERTY, TIME_SPAN_PROPERTY_DEFAULT);
		operationchooser = new DiscreteGenerator<String>();

		// prints below properties if exist
		StringBuilder sb = new StringBuilder();
		if (returncount != Integer.parseInt(RETURN_COUNT_PROPERTY_DEFAULT)) {
			sb.append("  current returncount: ");
			sb.append(returncount);
		}
		if (timespan.compareTo(TIME_SPAN_PROPERTY_DEFAULT) != 0) {
			sb.append("  current timespan: ");
			sb.append(timespan);
		}
		if (sb.length() != 0) {
			System.out.println(sb.toString());
		}
		sb.delete(0, sb.length());


		// Parse the file containing selected user IDs which will be used in
		// these queries into a concurrent hashmap
		if (query1proportion + query2proportion + query3proportion
				+ query4proportion + query5proportion + query6proportion
				+ query8proportion + query9proportion + query11proportion
				+ query12proportion + query13proportion + query16proportion
				+ query17proportion + query18proportion + query19proportion > 0) {
			uidchm = Utils.getConcurrentHashMap(Utils
					.getFilePath(USER_ID_FILE_NAME));
			uidchmsize = uidchm.size();
		}

		// Parse the file containing selected tags which will be used in these
		// queries into a concurrent hashmap
		if (query9proportion + query15proportion > 0) {
			tagchm = Utils.getConcurrentHashMap(Utils
					.getFilePath(EVENT_TAG_FILE_NAME));
			tagchmsize = tagchm.size();
		}

		// Parse the file containing selected date times which will be used in
		// these queries into a concurrent hashmap
		if (query7proportion + query10proportion + query14proportion > 0) {
			timechm = Utils.getConcurrentHashMap(Utils
					.getFilePath(TIME_FILE_NAME));
			timechmsize = timechm.size();
		}

		// Parse the file containing selected times for users which will be used
		// in these queries into a concurrent hashmap
		if (query6proportion + query11proportion + query12proportion
				+ query13proportion + query16proportion + query17proportion
				+ query18proportion + query19proportion > 0) {
			uidtimechm = Utils.getConcurrentHashMap(
					Utils.getFilePath(USER_TIME_FILE_NAME), SEPARATOR,
					SUBSEPARATOR, TIME_COUNT_FOR_USER);
			uidtimechmsize = uidtimechm.size();
		}

		// Parse the file containing selected times for tags which will be used
		// in these queries into a concurrent hashmap
		if (query9proportion + query15proportion > 0) {
			tagtimechm = Utils.getConcurrentHashMap(
					Utils.getFilePath(TAG_TIME_FILE_NAME), SEPARATOR,
					SUBSEPARATOR, TIME_COUNT_FOR_TAG);
			tagtimechmsize = tagtimechm.size();
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

		if (op.compareTo("Query1") == 0) {
			doAnalysisQuery1(db);
		} else if (op.compareTo("Query2") == 0) {
			doAnalysisQuery2(db);
		} else if (op.compareTo("Query3") == 0) {
			doAnalysisQuery3(db);
		} else if (op.compareTo("Query4") == 0) {
			doAnalysisQuery4(db);
		} else if (op.compareTo("Query5") == 0) {
			doAnalysisQuery5(db);
		} else if (op.compareTo("Query6") == 0) {
			doAnalysisQuery6(db);
		} else if (op.compareTo("Query7") == 0) {
			doAnalysisQuery7(db);
		} else if (op.compareTo("Query8") == 0) {
			doAnalysisQuery8(db);
		} else if (op.compareTo("Query9") == 0) {
			doAnalysisQuery9(db);
		} else if (op.compareTo("Query10") == 0) {
			doAnalysisQuery10(db);
		} else if (op.compareTo("Query11") == 0) {
			doAnalysisQuery11(db);
		} else if (op.compareTo("Query12") == 0) {
			doAnalysisQuery12(db);
		} else if (op.compareTo("Query13") == 0) {
			doAnalysisQuery13(db);
		} else if (op.compareTo("Query14") == 0) {
			doAnalysisQuery14(db);
		} else if (op.compareTo("Query15") == 0) {
			doAnalysisQuery15(db);
		} else if (op.compareTo("Query16") == 0) {
			doAnalysisQuery16(db);
		} else if (op.compareTo("Query17") == 0) {
			doAnalysisQuery17(db);
		} else if (op.compareTo("Query18") == 0) {
			doAnalysisQuery18(db);
		} else if (op.compareTo("Query19") == 0) {
			doAnalysisQuery19(db);
		}

		return true;
	}

	public void doAnalysisQuery1(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery1(userID, returncount);
	}

	public void doAnalysisQuery2(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery2(userID, returncount);
	}

	public void doAnalysisQuery3(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery3(userID, returncount);
	}

	public void doAnalysisQuery4(DB db) {
		// get two different userIDs randomly
		String userID1 = uidchm.get(random.nextInt(uidchmsize));
		String userID2 = null;
		do {
			userID2 = uidchm.get(random.nextInt(uidchmsize));
		} while (0 == userID2.compareTo(userID1));

		db.BSMAQuery4(userID1, userID2);
	}

	public void doAnalysisQuery5(DB db) {
		// get two different userIDs randomly
		String userID1 = uidchm.get(random.nextInt(uidchmsize));
		String userID2 = null;
		do {
			userID2 = uidchm.get(random.nextInt(uidchmsize));
		} while (0 == userID2.compareTo(userID1));
		db.BSMAQuery5(userID1, userID2);
	}

	public void doAnalysisQuery6(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		// uidtimechm.entrySet().
		db.BSMAQuery6(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery7(DB db) {
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery7(returncount, datetime, timespan);
	}

	public void doAnalysisQuery8(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery8(returncount, userID);
	}

	public void doAnalysisQuery9(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a tag randomly
		String tag = tagchm.get(random.nextInt(tagchmsize));
		// get a datetime randomly
		String datetime = tagtimechm.get(tag).get(
				random.nextInt(TIME_COUNT_FOR_TAG));
		db.BSMAQuery9(userID, tag, returncount, datetime, timespan);
	}

	public void doAnalysisQuery10(DB db) {
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery10(returncount, datetime, timespan);
	}

	public void doAnalysisQuery11(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery11(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery12(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery12(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery13(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery13(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery14(DB db) {
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery14(returncount, datetime, timespan);
	}

	public void doAnalysisQuery15(DB db) {
		// get a tag randomly
		String tag = tagchm.get(random.nextInt(tagchmsize));
		// get a datetime randomly
		String datetime = tagtimechm.get(tag).get(
				random.nextInt(TIME_COUNT_FOR_TAG));
		db.BSMAQuery15(tag, returncount, datetime, timespan);
	}

	public void doAnalysisQuery16(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery16(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery17(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery17(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery18(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery18(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery19(DB db) {
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(
				random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery19(userID, returncount, datetime, timespan);
	}

}
