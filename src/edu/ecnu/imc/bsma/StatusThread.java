package edu.ecnu.imc.bsma;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.measurements.Measurements;
import edu.ecnu.imc.bsma.measurements.exporter.DBExporter;
import edu.ecnu.imc.bsma.measurements.exporter.MeasurementsExporter;
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
	JobInfo _jobInfo;
	Vector<Thread> _threads;
	Measurements _measurements;
	DBExporter _exporter;

	/**
	 * The interval for reporting status. To set it to 100000 means to report
	 * status every 100 seconds.
	 */
	// public static final long sleeptime = 10000;
	public static final String sleeptime = "100000";

	public StatusThread(JobInfo jobInfo, Vector<Thread> threads,
			Measurements measurements) {
		_jobInfo = jobInfo;
		_exporter = new DBExporter(_jobInfo, Scheduler.instance.getProps());
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
		long period = Long.parseLong(_jobInfo.getProperties().getProperty(
				"report_period", sleeptime));
		do {
			alldone = true;

			int totalops = 0;

			// terminate this thread when all the worker threads are done
			for (Thread t : _threads) {
				if (t.getState() != Thread.State.TERMINATED) {
					alldone = false;
				}

				QueryExecThread ct = (QueryExecThread) t;
				totalops += ct.getOpsDone();
			}

			long en = System.currentTimeMillis();

			long interval = en - st;
			// double throughput=1000.0*((double)totalops)/((double)interval);

			double curthroughput = 1000.0 * (((double) (totalops - lasttotalops)) / ((double) (en - lasten)));

			lasttotalops = totalops;
			lasten = en;

			_exporter.newReport();
			_exporter.reportOverall(interval / 1000, totalops,
					(float) curthroughput);
			_measurements.getSummary(_exporter);
			try {
				_exporter.endReport();
			} catch (SQLException e1) {
				// we can do nothing
				e1.printStackTrace();
			}

			try {
				sleep(period);
			} catch (InterruptedException e) {
			}

		} while (!alldone);
	}

	/**
	 * Exports the measurements to either sysout or a file using the exporter
	 * loaded from conf.
	 * 
	 * @throws IOException
	 *             Either failed to write to output stream or failed to close
	 *             it.
	 * @throws SQLException
	 */
	private void exportMeasurements(long runtime, Measurements measurements)
			throws IOException, SQLException {
		try {
			if (_jobInfo.getCurJob().getOpCount() > 1) {
				_exporter.reportJobResult(runtime / 1000, _jobInfo.getCurJob()
						.getOpCount());
			}// modified out by WeiJinxian
			measurements.exportMeasurements(_exporter);

		} finally {
			if (_exporter != null) {
				_exporter.close();
			}
		}
	}
}
