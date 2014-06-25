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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import edu.ecnu.imc.bsma.dao.BasicJobInfo;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.measurements.Measurements;
import edu.ecnu.imc.bsma.measurements.exporter.MeasurementsExporter;
import edu.ecnu.imc.bsma.measurements.exporter.RuntimeExporter;
import edu.ecnu.imc.bsma.measurements.exporter.TextMeasurementsExporter;

/**
 * A thread to periodically show the status of the experiment, to reassure you
 * that progress is being made.
 * 
 * @author cooperb
 * @author wjx
 * 
 */
class StatusThread extends Thread {
	BasicJobInfo _jobInfo;
	Vector<Thread> _threads;
	Measurements _measurements;
	RuntimeExporter _exporter;

	/**
	 * The interval for reporting status. To set it to 100000 means to report
	 * status every 100 seconds.
	 */
	// public static final long sleeptime = 10000;
	public static final long sleeptime = 100000;

	public StatusThread(BasicJobInfo jobInfo, Vector<Thread> threads,
			Measurements measurements) {
		_jobInfo = jobInfo;
		_threads = threads;
		_measurements = measurements;
	}

	/**
	 * Run and periodically report status.
	 */
	public void run() {
		long st = System.currentTimeMillis();

		long lasten = st;
		long lasttotalops = 0;

		boolean alldone;

		do {
			alldone = true;

			int totalops = 0;

			// terminate this thread when all the worker threads are done
			for (Thread t : _threads) {
				if (t.getState() != Thread.State.TERMINATED) {
					alldone = false;
				}

				ClientThread ct = (ClientThread) t;
				totalops += ct.getOpsDone();
			}

			long en = System.currentTimeMillis();

			long interval = en - st;
			// double throughput=1000.0*((double)totalops)/((double)interval);

			double curthroughput = 1000.0 * (((double) (totalops - lasttotalops)) / ((double) (en - lasten)));

			lasttotalops = totalops;
			lasten = en;

			_exporter.newReport();
			_exporter.reportOverall(interval / 1000, totalops, curthroughput);
			_measurements.getSummary(_exporter);
			try {
				_exporter.endReport();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			try {
				sleep(sleeptime);
			} catch (InterruptedException e) {
				// do nothing
			}

		} while (!alldone);
	}
}

/**
 * A thread for executing queries.
 * 
 * @author cooperb
 * @author wjx
 * 
 */
class ClientThread extends Thread {
	static Random random = new Random();

	DB _db;
	Workload _workload;
	int _opcount;
	double _target;

	int _opsdone;
	int _threadid;
	int _threadcount;
	Object _workloadstate;
	Properties _props;
	Measurements _measurements;

	/**
	 * Constructor.
	 * 
	 * @param db
	 *            the DB implementation to use
	 * @param workload
	 *            the workload to use
	 * @param threadid
	 *            the id of this thread
	 * @param threadcount
	 *            the total number of threads
	 * @param props
	 *            the properties defining the experiment
	 * @param opcount
	 *            the number of operations (transactions or inserts) to do
	 * @param targetperthreadperms
	 *            target number of operations per thread per ms
	 */
	public ClientThread(DB db, Workload workload, int threadid,
			int threadcount, Properties props, int opcount,
			double targetperthreadperms, Measurements measurements) {
		// TODO: consider removing threadcount and threadid
		_db = db;
		_workload = workload;
		_opcount = opcount;
		_opsdone = 0;
		_target = targetperthreadperms;
		_threadid = threadid;
		_threadcount = threadcount;
		_props = props;
		_measurements = measurements;
	}

	public int getOpsDone() {
		return _opsdone;
	}

	public void run() {
		try {
			_db.init();
		} catch (DBException e) {
			e.printStackTrace();
			e.printStackTrace(System.out);
			return;
		}

		try {
			_workloadstate = _workload.initThread(_props, _threadid,
					_threadcount);
		} catch (WorkloadException e) {
			e.printStackTrace();
			e.printStackTrace(System.out);
			return;
		}

		// spread the thread operations out so they don't all hit the DB at the
		// same time
		try {
			// GH issue 4 - throws exception if _target>1 because random.nextInt
			// argument must be >0
			// and the sleep() doesn't make sense for granularities < 1 ms
			// anyway
			if ((_target > 0) && (_target <= 1.0)) {
				sleep(random.nextInt((int) (1.0 / _target)));
			}
		} catch (InterruptedException e) {
			// do nothing
		}

		try {
			long st = System.currentTimeMillis();

			while ((_opcount == 0) || (_opsdone < _opcount)) {

				if (!_workload.doAnalysis(_db, _workloadstate)) {
					break;
				}

				_opsdone++;

				// throttle the operations
				if (_target > 0) {
					// this is more accurate than other throttling
					// approaches we have tried,
					// like sleeping for (1/target throughput)-operation
					// latency,
					// because it smooths timing inaccuracies (from sleep()
					// taking an int,
					// current time in millis) over many operations
					while (System.currentTimeMillis() - st < ((double) _opsdone)
							/ _target) {
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// do nothing
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace(System.out);
			System.exit(0);
		}

		try {
			_db.cleanup();
		} catch (DBException e) {
			e.printStackTrace();
			e.printStackTrace(System.out);
			return;
		}
	}
}

/**
 * Main class for executing BSMA.
 */
public class Client extends Thread {
	BasicJobInfo basicJobInfo;
	JobInfo jobInfo;

	public Client(JobInfo jobInfo, BasicJobInfo basicJobInfo) {
		this.basicJobInfo = basicJobInfo;
		this.jobInfo = jobInfo;
	}

	/**
	 * Exports the measurements to either sysout or a file using the exporter
	 * loaded from conf.
	 * 
	 * @throws IOException
	 *             Either failed to write to output stream or failed to close
	 *             it.
	 */
	private void exportMeasurements(Properties props, int opcount,
			long runtime, Measurements measurements) throws IOException {
		MeasurementsExporter exporter = null;
		try {
			// if no destination file is provided the results will be written to
			// stdout
			OutputStream out;
			String exportFile = props.getProperty("exportfile");
			if (exportFile == null) {
				out = System.out;
			} else {
				out = new FileOutputStream(exportFile);
			}

			// if no exporter is provided the default text one will be used
			String exporterStr = props
					.getProperty("exporter",
							"edu.ecnu.imc.bsma.measurements.exporter.TextMeasurementsExporter");
			try {
				exporter = (MeasurementsExporter) Class.forName(exporterStr)
						.getConstructor(OutputStream.class).newInstance(out);
			} catch (Exception e) {
				System.err.println("Could not find exporter " + exporterStr
						+ ", will use default text reporter.");
				e.printStackTrace();
				exporter = new TextMeasurementsExporter(out);
			}

			exporter.write("OVERALL", "RunTime(ms)", runtime);
			if (opcount > 1) {
				double throughput = 1000.0 * ((double) opcount)
						/ ((double) runtime);

				exporter.write("OVERALL", "Throughput(ops/sec)", throughput);
			}// modified out by WeiJinxian
			measurements.exportMeasurements(exporter);

		} finally {
			if (exporter != null) {
				exporter.close();
			}
		}
	}

	@Override
	public void run() {
		// compute the target throughput
		double targetperthreadperms = -1;
		if (basicJobInfo.opCount > 0) {
			double targetperthread = ((double) basicJobInfo.opCount)
					/ ((double) basicJobInfo.threadCount);
			targetperthreadperms = targetperthread / 1000.0;
		}

		System.out.println("BSMA Client 0.1");
		System.out.print("Command line:");
		System.out.println();
		System.err.println("Loading workload...");

		// show a warning message that creating the workload is taking a while
		// but only do so if it is taking longer than 2 seconds
		// (showing the message right away if the setup wasn't taking very long
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
			Class workloadclass = classLoader.loadClass(jobInfo.wrapper);

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

		int opcount = Integer.parseInt(props.getProperty(
				OPERATION_COUNT_PROPERTY, "0"));

		// if the workload has more threads than operations, the number of
		// threads will be changed to the number of operations.
		if (opcount < basicJobInfo.threadCount) {
			System.err
					.println("warning: the number of threads is bigger than that of operations!"
							.toUpperCase());
			basicJobInfo.threadCount = opcount;
		}

		Vector<Thread> threads = new Vector<Thread>();
		Measurements measurements = new Measurements(props);
		for (int threadid = 0; threadid < basicJobInfo.threadCount; threadid++) {
			DB db = null;
			try {
				db = DBFactory.newDB(dbname, props, measurements);
			} catch (UnknownDBException e) {
				System.out.println("Unknown DB " + dbname);
				System.exit(0);
			}

			Thread t = new ClientThread(db, workload, threadid, threadcount,
					props, opcount / threadcount, targetperthreadperms,
					measurements);

			threads.add(t);
			// t.start();
		}

		StatusThread statusthread = null;

		if (status) {
			statusthread = new StatusThread(jobInfo, threads, measurements);
			statusthread.start();
		}

		long st = System.currentTimeMillis();

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}

		long en = System.currentTimeMillis();

		if (status) {
			statusthread.interrupt();
		}

		try {
			workload.cleanup();
		} catch (WorkloadException e) {
			e.printStackTrace();
			e.printStackTrace(System.out);
			System.exit(0);
		}

		try {
			exportMeasurements(props, opcount, en - st, measurements);
		} catch (IOException e) {
			System.err.println("Could not export measurements, error: "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}

	Properties constructProp() {
		return null;
	}
}
