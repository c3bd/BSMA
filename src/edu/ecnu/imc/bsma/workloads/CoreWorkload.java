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
public class CoreWorkload extends Workload
{

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
	
	/**
	 * The name of the property for the proportion of analyses that are query1s.
	 */
	public static final String QUERY1_PROPORTION_PROPERTY = "query1proportion";

	/**
	 * The default proportion of analyses that are query1s.
	 */
	public static final String QUERY1_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query2s.
	 */
	public static final String QUERY2_PROPORTION_PROPERTY = "query2proportion";

	/**
	 * The default proportion of analyses that are query2s.
	 */
	public static final String QUERY2_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query3s.
	 */
	public static final String QUERY3_PROPORTION_PROPERTY = "query3proportion";

	/**
	 * The default proportion of analyses that are query3s.
	 */
	public static final String QUERY3_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query4s.
	 */
	public static final String QUERY4_PROPORTION_PROPERTY = "query4proportion";

	/**
	 * The default proportion of analyses that are query4s.
	 */
	public static final String QUERY4_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query5s.
	 */
	public static final String QUERY5_PROPORTION_PROPERTY = "query5proportion";

	/**
	 * The default proportion of analyses that are query5s.
	 */
	public static final String QUERY5_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query6s.
	 */
	public static final String QUERY6_PROPORTION_PROPERTY = "query6proportion";

	/**
	 * The default proportion of analyses that are query6s.
	 */
	public static final String QUERY6_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query7s.
	 */
	public static final String QUERY7_PROPORTION_PROPERTY = "query7proportion";

	/**
	 * The default proportion of analyses that are query7s.
	 */
	public static final String QUERY7_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query8s.
	 */
	public static final String QUERY8_PROPORTION_PROPERTY = "query8proportion";

	/**
	 * The default proportion of analyses that are query8s.
	 */
	public static final String QUERY8_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are query9s.
	 */
	public static final String QUERY9_PROPORTION_PROPERTY = "query9proportion";

	/**
	 * The default proportion of analyses that are query9s.
	 */
	public static final String QUERY9_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query10s.
	 */
	public static final String QUERY10_PROPORTION_PROPERTY = "query10proportion";

	/**
	 * The default proportion of analyses that are query10s.
	 */
	public static final String QUERY10_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query11s.
	 */
	public static final String QUERY11_PROPORTION_PROPERTY = "query11proportion";

	/**
	 * The default proportion of analyses that are query11s.
	 */
	public static final String QUERY11_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query12s.
	 */
	public static final String QUERY12_PROPORTION_PROPERTY = "query12proportion";

	/**
	 * The default proportion of analyses that are query12s.
	 */
	public static final String QUERY12_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query13s.
	 */
	public static final String QUERY13_PROPORTION_PROPERTY = "query13proportion";

	/**
	 * The default proportion of analyses that are query13s.
	 */
	public static final String QUERY13_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query14s.
	 */
	public static final String QUERY14_PROPORTION_PROPERTY = "query14proportion";

	/**
	 * The default proportion of analyses that are query14s.
	 */
	public static final String QUERY14_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query15s.
	 */
	public static final String QUERY15_PROPORTION_PROPERTY = "query15proportion";

	/**
	 * The default proportion of analyses that are query15s.
	 */
	public static final String QUERY15_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query16s.
	 */
	public static final String QUERY16_PROPORTION_PROPERTY = "query16proportion";

	/**
	 * The default proportion of analyses that are query16s.
	 */
	public static final String QUERY16_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query17s.
	 */
	public static final String QUERY17_PROPORTION_PROPERTY = "query17proportion";

	/**
	 * The default proportion of analyses that are query17s.
	 */
	public static final String QUERY17_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query18s.
	 */
	public static final String QUERY18_PROPORTION_PROPERTY = "query18proportion";

	/**
	 * The default proportion of analyses that are query18s.
	 */
	public static final String QUERY18_PROPORTION_PROPERTY_DEFAULT = "0";

	/**
	 * The name of the property for the proportion of analyses that are
	 * query19s.
	 */
	public static final String QUERY19_PROPORTION_PROPERTY = "query19proportion";

	/**
	 * The default proportion of analyses that are query19s.
	 */
	public static final String QUERY19_PROPORTION_PROPERTY_DEFAULT = "0";

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
	public void init(Properties p) throws WorkloadException
	{
		double query1proportion = Double.parseDouble(p.getProperty(QUERY1_PROPORTION_PROPERTY, QUERY1_PROPORTION_PROPERTY_DEFAULT));
		double query2proportion = Double.parseDouble(p.getProperty(QUERY2_PROPORTION_PROPERTY, QUERY2_PROPORTION_PROPERTY_DEFAULT));
		double query3proportion = Double.parseDouble(p.getProperty(QUERY3_PROPORTION_PROPERTY, QUERY3_PROPORTION_PROPERTY_DEFAULT));
		double query4proportion = Double.parseDouble(p.getProperty(QUERY4_PROPORTION_PROPERTY, QUERY4_PROPORTION_PROPERTY_DEFAULT));
		double query5proportion = Double.parseDouble(p.getProperty(QUERY5_PROPORTION_PROPERTY, QUERY5_PROPORTION_PROPERTY_DEFAULT));
		double query6proportion = Double.parseDouble(p.getProperty(QUERY6_PROPORTION_PROPERTY, QUERY6_PROPORTION_PROPERTY_DEFAULT));
		double query7proportion = Double.parseDouble(p.getProperty(QUERY7_PROPORTION_PROPERTY, QUERY7_PROPORTION_PROPERTY_DEFAULT));
		double query8proportion = Double.parseDouble(p.getProperty(QUERY8_PROPORTION_PROPERTY, QUERY8_PROPORTION_PROPERTY_DEFAULT));
		double query9proportion = Double.parseDouble(p.getProperty(QUERY9_PROPORTION_PROPERTY, QUERY9_PROPORTION_PROPERTY_DEFAULT));
		double query10proportion = Double.parseDouble(p.getProperty(QUERY10_PROPORTION_PROPERTY, QUERY10_PROPORTION_PROPERTY_DEFAULT));
		double query11proportion = Double.parseDouble(p.getProperty(QUERY11_PROPORTION_PROPERTY, QUERY11_PROPORTION_PROPERTY_DEFAULT));
		double query12proportion = Double.parseDouble(p.getProperty(QUERY12_PROPORTION_PROPERTY, QUERY12_PROPORTION_PROPERTY_DEFAULT));
		double query13proportion = Double.parseDouble(p.getProperty(QUERY13_PROPORTION_PROPERTY, QUERY13_PROPORTION_PROPERTY_DEFAULT));
		double query14proportion = Double.parseDouble(p.getProperty(QUERY14_PROPORTION_PROPERTY, QUERY14_PROPORTION_PROPERTY_DEFAULT));
		double query15proportion = Double.parseDouble(p.getProperty(QUERY15_PROPORTION_PROPERTY, QUERY15_PROPORTION_PROPERTY_DEFAULT));
		double query16proportion = Double.parseDouble(p.getProperty(QUERY16_PROPORTION_PROPERTY, QUERY16_PROPORTION_PROPERTY_DEFAULT));
		double query17proportion = Double.parseDouble(p.getProperty(QUERY17_PROPORTION_PROPERTY, QUERY17_PROPORTION_PROPERTY_DEFAULT));
		double query18proportion = Double.parseDouble(p.getProperty(QUERY18_PROPORTION_PROPERTY, QUERY18_PROPORTION_PROPERTY_DEFAULT));
		double query19proportion = Double.parseDouble(p.getProperty(QUERY19_PROPORTION_PROPERTY, QUERY19_PROPORTION_PROPERTY_DEFAULT));

		returncount = Integer.parseInt(p.getProperty(RETURN_COUNT_PROPERTY, RETURN_COUNT_PROPERTY_DEFAULT));
		timespan = p.getProperty(TIME_SPAN_PROPERTY, TIME_SPAN_PROPERTY_DEFAULT);
		operationchooser = new DiscreteGenerator<String>();

		// prints below properties if exist
		StringBuilder sb = new StringBuilder();
		if (returncount != Integer.parseInt(RETURN_COUNT_PROPERTY_DEFAULT))
		{
			sb.append("  current returncount: ");
			sb.append(returncount);
		}
		if (timespan.compareTo(TIME_SPAN_PROPERTY_DEFAULT) != 0)
		{
			sb.append("  current timespan: ");
			sb.append(timespan);
		}
		if (sb.length() != 0)
		{
			System.out.println(sb.toString());
		}
		sb.delete(0, sb.length());

		if (query1proportion > 0)
		{
			operationchooser.addValue(query1proportion, "Query1");
		}
		if (query2proportion > 0)
		{
			operationchooser.addValue(query2proportion, "Query2");
		}
		if (query3proportion > 0)
		{
			operationchooser.addValue(query3proportion, "Query3");
		}
		if (query4proportion > 0)
		{
			operationchooser.addValue(query4proportion, "Query4");
		}
		if (query5proportion > 0)
		{
			operationchooser.addValue(query5proportion, "Query5");
		}
		if (query6proportion > 0)
		{
			operationchooser.addValue(query6proportion, "Query6");
		}
		if (query7proportion > 0)
		{
			operationchooser.addValue(query7proportion, "Query7");
		}
		if (query8proportion > 0)
		{
			operationchooser.addValue(query8proportion, "Query8");
		}
		if (query9proportion > 0)
		{
			operationchooser.addValue(query9proportion, "Query9");
		}
		if (query10proportion > 0)
		{
			operationchooser.addValue(query10proportion, "Query10");
		}
		if (query11proportion > 0)
		{
			operationchooser.addValue(query11proportion, "Query11");
		}
		if (query12proportion > 0)
		{
			operationchooser.addValue(query12proportion, "Query12");
		}
		if (query13proportion > 0)
		{
			operationchooser.addValue(query13proportion, "Query13");
		}
		if (query14proportion > 0)
		{
			operationchooser.addValue(query14proportion, "Query14");
		}
		if (query15proportion > 0)
		{
			operationchooser.addValue(query15proportion, "Query15");
		}
		if (query16proportion > 0)
		{
			operationchooser.addValue(query16proportion, "Query16");
		}
		if (query17proportion > 0)
		{
			operationchooser.addValue(query17proportion, "Query17");
		}
		if (query18proportion > 0)
		{
			operationchooser.addValue(query18proportion, "Query18");
		}
		if (query19proportion > 0)
		{
			operationchooser.addValue(query19proportion, "Query19");
		}

		// Parse the file containing selected user IDs which will be used in these queries into a concurrent hashmap
		if (query1proportion + query2proportion + query3proportion + query4proportion + query5proportion + query6proportion + query8proportion + query9proportion
				+ query11proportion + query12proportion + query13proportion + query16proportion + query17proportion + query18proportion + query19proportion > 0)
		{
			uidchm = Utils.getConcurrentHashMap(Utils.getFilePath(USER_ID_FILE_NAME));
			uidchmsize = uidchm.size();
		}
		
		// Parse the file containing selected tags which will be used in these queries into a concurrent hashmap
		if(query9proportion+query15proportion>0)
		{
			tagchm = Utils.getConcurrentHashMap(Utils.getFilePath(EVENT_TAG_FILE_NAME));
			tagchmsize = tagchm.size();
		}
		
		// Parse the file containing selected date times which will be used in these queries into a concurrent hashmap
		if(query7proportion+query10proportion+query14proportion>0)
		{
			timechm = Utils.getConcurrentHashMap(Utils.getFilePath(TIME_FILE_NAME));
			timechmsize = timechm.size();
		}
		
		// Parse the file containing selected times for users which will be used in these queries into a concurrent hashmap
		if(query6proportion + query11proportion + query12proportion + query13proportion + query16proportion + query17proportion + query18proportion + query19proportion>0)
		{
			uidtimechm = Utils.getConcurrentHashMap(Utils.getFilePath(USER_TIME_FILE_NAME), SEPARATOR, SUBSEPARATOR, TIME_COUNT_FOR_USER);
			uidtimechmsize = uidtimechm.size();
		}

		// Parse the file containing selected times for tags which will be used in these queries into a concurrent hashmap
		if(query9proportion +query15proportion>0)
		{
			tagtimechm = Utils.getConcurrentHashMap(Utils.getFilePath(TAG_TIME_FILE_NAME), SEPARATOR, SUBSEPARATOR, TIME_COUNT_FOR_TAG);
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
	public boolean doAnalysis(DB db, Object threadstate)
	{
		String op = operationchooser.nextString();

		if (op.compareTo("Query1") == 0)
		{
			doAnalysisQuery1(db);
		} else if (op.compareTo("Query2") == 0)
		{
			doAnalysisQuery2(db);
		} else if (op.compareTo("Query3") == 0)
		{
			doAnalysisQuery3(db);
		} else if (op.compareTo("Query4") == 0)
		{
			doAnalysisQuery4(db);
		} else if (op.compareTo("Query5") == 0)
		{
			doAnalysisQuery5(db);
		} else if (op.compareTo("Query6") == 0)
		{
			doAnalysisQuery6(db);
		} else if (op.compareTo("Query7") == 0)
		{
			doAnalysisQuery7(db);
		} else if (op.compareTo("Query8") == 0)
		{
			doAnalysisQuery8(db);
		} else if (op.compareTo("Query9") == 0)
		{
			doAnalysisQuery9(db);
		} else if (op.compareTo("Query10") == 0)
		{
			doAnalysisQuery10(db);
		} else if (op.compareTo("Query11") == 0)
		{
			doAnalysisQuery11(db);
		} else if (op.compareTo("Query12") == 0)
		{
			doAnalysisQuery12(db);
		} else if (op.compareTo("Query13") == 0)
		{
			doAnalysisQuery13(db);
		} else if (op.compareTo("Query14") == 0)
		{
			doAnalysisQuery14(db);
		} else if (op.compareTo("Query15") == 0)
		{
			doAnalysisQuery15(db);
		} else if (op.compareTo("Query16") == 0)
		{
			doAnalysisQuery16(db);
		} else if (op.compareTo("Query17") == 0)
		{
			doAnalysisQuery17(db);
		} else if (op.compareTo("Query18") == 0)
		{
			doAnalysisQuery18(db);
		} else if (op.compareTo("Query19") == 0)
		{
			doAnalysisQuery19(db);
		}

		return true;
	}

	public void doAnalysisQuery1(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery1(userID, returncount);
	}

	public void doAnalysisQuery2(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery2(userID, returncount);
	}

	public void doAnalysisQuery3(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery3(userID, returncount);
	}

	public void doAnalysisQuery4(DB db)
	{
		// get two different userIDs randomly
		String userID1 = uidchm.get(random.nextInt(uidchmsize));
		String userID2 = null;
		do
		{
			userID2 = uidchm.get(random.nextInt(uidchmsize));
		} while (0 == userID2.compareTo(userID1));

		db.BSMAQuery4(userID1, userID2);
	}

	public void doAnalysisQuery5(DB db)
	{
		// get two different userIDs randomly
		String userID1 = uidchm.get(random.nextInt(uidchmsize));
		String userID2 = null;
		do
		{
			userID2 = uidchm.get(random.nextInt(uidchmsize));
		} while (0 == userID2.compareTo(userID1));
		db.BSMAQuery5(userID1, userID2);
	}

	public void doAnalysisQuery6(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		// uidtimechm.entrySet().
		db.BSMAQuery6(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery7(DB db)
	{
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery7(returncount, datetime, timespan);
	}

	public void doAnalysisQuery8(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		db.BSMAQuery8(returncount, userID);
	}

	public void doAnalysisQuery9(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a tag randomly
		String tag = tagchm.get(random.nextInt(tagchmsize));
		// get a datetime randomly
		String datetime = tagtimechm.get(tag).get(random.nextInt(TIME_COUNT_FOR_TAG));
		db.BSMAQuery9(userID, tag, returncount, datetime, timespan);
	}

	public void doAnalysisQuery10(DB db)
	{
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery10(returncount, datetime, timespan);
	}

	public void doAnalysisQuery11(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery11(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery12(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery12(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery13(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery13(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery14(DB db)
	{
		// get a datetime randomly
		String datetime = timechm.get(random.nextInt(timechmsize));
		db.BSMAQuery14(returncount, datetime, timespan);
	}

	public void doAnalysisQuery15(DB db)
	{
		// get a tag randomly
		String tag = tagchm.get(random.nextInt(tagchmsize));
		// get a datetime randomly
		String datetime = tagtimechm.get(tag).get(random.nextInt(TIME_COUNT_FOR_TAG));
		db.BSMAQuery15(tag, returncount, datetime, timespan);
	}

	public void doAnalysisQuery16(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery16(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery17(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery17(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery18(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery18(userID, returncount, datetime, timespan);
	}

	public void doAnalysisQuery19(DB db)
	{
		// get a userID randomly
		String userID = uidchm.get(random.nextInt(uidchmsize));
		// get a datetime randomly
		String datetime = uidtimechm.get(userID).get(random.nextInt(TIME_COUNT_FOR_USER));
		db.BSMAQuery19(userID, returncount, datetime, timespan);
	}

}
