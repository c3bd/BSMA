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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpc.Job;
import edu.ecnu.imc.bsma.dao.BasicJobInfo;
import edu.ecnu.imc.bsma.dao.Dao;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.db.DB;
import edu.ecnu.imc.bsma.db.DBFactory;
import edu.ecnu.imc.bsma.measurements.Measurements;

/**
 * Thread responsible for managing the execution of a benchmark job a benchmark
 * job consists of a set of small jobs, each consists of a threadnum and target
 * operation
 */
public class JobCordinator extends Thread {
	public static Logger logger = LoggerFactory.getLogger(JobCordinator.class);
	JobInfo jobInfo;
	Dao dao = new Dao(Scheduler.instance.getProps());
	String exitCode = "";

	List<QueryExecThread> qThreads = new ArrayList<QueryExecThread>();
	StatusThread statusthread = null;

	AtomicBoolean state = new AtomicBoolean(true);

	public JobCordinator(Job job) {
		this.jobInfo = new JobInfo(job, dao);
	}

	// CountDownLatch latch = null;

	@Override
	public void run() {
		// load argument files
		prepareJob(jobInfo);
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
			while (!jobInfo.haveExited()
					&& null != (basicJobInfo = jobInfo.runNextJob())) {
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
						logger.info(" (might take a few minutes for large data sets)");
					}
				};
				warningthread.start();

				// load the workload
				ClassLoader classLoader = JobCordinator.class.getClassLoader();
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

				logger.info("Starting test.");

				int opcount = basicJobInfo.getOpCount();

				// if the workload has more threads than operations, the number
				// of
				// threads will be changed to the number of operations.
				if (opcount < basicJobInfo.threadNum) {
					logger.warn("warning: the number of threads is bigger than that of operations!"
							.toUpperCase());
					basicJobInfo.threadNum = opcount;
				}

				Measurements measurements = new Measurements(props);
				qThreads.clear();
				// latch = new CountDownLatch(basicJobInfo.threadNum + 1);
				for (int threadid = 0; threadid < basicJobInfo.threadNum; threadid++) {
					DB db = null;
					try {
						db = DBFactory.newDB(jobInfo, props, measurements);
					} catch (UnknownDBException ex) {

					}

					QueryExecThread t = new QueryExecThread(state, db,
							workload, threadid, basicJobInfo.getThreadNum(),
							props, opcount / basicJobInfo.getThreadNum(),
							targetperthreadperms, measurements);
					qThreads.add(t);
				}

				// start the status thread
				statusthread = new StatusThread(this, measurements);
				statusthread.start();

				for (Thread t : qThreads) {
					t.start();
				}

				waitForFinish();

				logger.info("query executor and status thread have terminated");
				try {
					workload.cleanup();
				} catch (WorkloadException e) {
					e.printStackTrace();
					e.printStackTrace(System.out);
					System.exit(0);
				}
			}
			if (!jobInfo.haveExited())
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

	private void prepareJob(JobInfo jobInfo) {
		/**
		 * TODO collect needed resources
		 * 
		 */

		try {
			jobInfo.getDBClass();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 处理出错
	 * 
	 * @param ex
	 */
	public void error(Exception ex) {
		state.set(false);
		// if wait for termination, may never stop
		// waitForFinish();

		exitCode = ex.getMessage();
		try {
			jobInfo.cancel(ex.getMessage());
		} catch (SQLException e) {
			// 和db的连接出错，无法继续执行
			throw new RuntimeException(
					"fatal error caused by connection to mysql server:"
							+ e.getMessage());
		}
	}

	/**
	 * 等待所有相关的线程结束
	 */
	private void waitForFinish() {

		for (QueryExecThread thread : qThreads) {
			while (true) {
				try {
					thread.join();
					break;
				} catch (InterruptedException e) {
				}
			}
		}

		while (true) {
			try {
				statusthread.join();
				break;
			} catch (InterruptedException e) {
			}
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

	public AtomicBoolean getWorkingState() {
		return state;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}

	public List<QueryExecThread> getQueryThreads() {
		return qThreads;
	}
}
