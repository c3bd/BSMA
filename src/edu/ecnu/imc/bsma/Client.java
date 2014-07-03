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

package edu.ecnu.imc.bsma;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpc.Job;
import edu.ecnu.imc.bsma.dao.BasicJobInfo;
import edu.ecnu.imc.bsma.dao.Dao;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.db.DB;
import edu.ecnu.imc.bsma.db.DBFactory;
import edu.ecnu.imc.bsma.measurements.Measurements;
import edu.ecnu.imc.bsma.measurements.exporter.MeasurementsExporter;
import edu.ecnu.imc.bsma.measurements.exporter.TextMeasurementsExporter;

/**
 * Thread responsible for managing the execution of a benchmark job a benchmark
 * job consists of a set of small jobs, each consists of a threadnum and target
 * operation
 */
public class Client extends Thread {
	public static Logger logger = LoggerFactory.getLogger(Client.class);
	JobInfo jobInfo;
	Dao dao = new Dao(Scheduler.instance.getProps());
	String exitCode = "";

	public Client(Job job) {
		this.jobInfo = new JobInfo(job, dao);
	}

	@Override
	public void run() {

		// load argument files

		// update job status
		try {
			jobInfo.save();
			jobInfo.start();
		} catch (SQLException e1) {
			error(e1);
			return;
		}

		BasicJobInfo basicJobInfo = null;
		try {
			while (null != (basicJobInfo = jobInfo.runNextJob())) {
				// compute the target throughput
				double targetperthreadperms = -1;
				if (basicJobInfo.opCount > 0) {
					double targetperthread = ((double) basicJobInfo.opCount)
							/ ((double) basicJobInfo.threadNum);
					targetperthreadperms = targetperthread / 1000.0;
				}

				logger.info("BSMA Client 0.1");
				logger.info("Loading workload...");

				// show a warning message that creating the workload is taking a
				// while
				// but only do so if it is taking longer than 2 seconds
				// (showing the message right away if the setup wasn't taking
				// very
				// long
				// was confusing people)
				Thread warningthread = new Thread() {
					public void run() {
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							return;
						}
						System.err
								.println(" (might take a few minutes for large data sets)");
					}
				};
				warningthread.start();

				// load the workload
				ClassLoader classLoader = Client.class.getClassLoader();
				Workload workload = null;
				try {
					Class workloadclass = classLoader.loadClass(jobInfo
							.getWorkload());

					workload = (Workload) workloadclass.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
					e.printStackTrace(System.out);
					System.exit(0);
				}
				Properties props = constructProp();
				try {
					workload.init(props);
				} catch (WorkloadException e) {
					e.printStackTrace();
					e.printStackTrace(System.out);
					System.exit(0);
				}

				warningthread.interrupt();

				// run the workload

				System.err.println("Starting test.");
				System.out.println(" query results and latencies:");

				int opcount = basicJobInfo.getOpCount();

				// if the workload has more threads than operations, the number
				// of
				// threads will be changed to the number of operations.
				if (opcount < basicJobInfo.threadNum) {
					System.err
							.println("warning: the number of threads is bigger than that of operations!"
									.toUpperCase());
					basicJobInfo.threadNum = opcount;
				}

				Vector<Thread> threads = new Vector<Thread>();
				Measurements measurements = new Measurements(props);
				for (int threadid = 0; threadid < basicJobInfo.threadNum; threadid++) {
					DB db = null;
					try {
						db = DBFactory.newDB(jobInfo.getDBImpl(), props,
								measurements);
					} catch (UnknownDBException ex) {
						// TODO Handle this exception
						// System.exit(0);
					}

					Thread t = new QueryExecThread(db, workload, threadid,
							basicJobInfo.getThreadNum(), props, opcount
									/ basicJobInfo.getThreadNum(),
							targetperthreadperms, measurements);
					threads.add(t);
				}

				// start the status thread
				StatusThread statusthread = new StatusThread(jobInfo, threads,
						measurements);
				statusthread.start();

				for (Thread t : threads) {
					t.start();
				}

				for (Thread t : threads) {
					try {
						t.join();
					} catch (InterruptedException e) {
					}
				}

				while (statusthread.getState().equals(State.TERMINATED)) {
					try {
						statusthread.join();
						break;
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

				try {
					workload.cleanup();
				} catch (WorkloadException e) {
					e.printStackTrace();
					e.printStackTrace(System.out);
					System.exit(0);
				}

				// mark subjob as finish
				jobInfo.setState(JobInfo.FINISH);
			}
			jobInfo.finish();
		} catch (SQLException e) {
			error(e);
		} finally {
			try {
				dao.close();
			} catch (SQLException e) {

			}
		}
	}

	/**
	 * 处理出错
	 * 
	 * @param ex
	 */
	public void error(Exception ex) {
		exitCode = ex.getMessage();
		try {
			jobInfo.cancel();
		} catch (SQLException e) {
			// 和db的连接出错，无法继续执行
			throw new RuntimeException(
					"fatal error caused by connection to mysql server");
		}
	}

	Properties constructProp() {
		return jobInfo.getProperties();
	}

	/**
	 * stop the job
	 */
	public boolean cancel() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * cancel execution of subID
	 * 
	 * @param subID
	 * @return
	 */
	public boolean cancel(int subID) {
		// TODO Auto-generated method stub
		return true;
	}
}
