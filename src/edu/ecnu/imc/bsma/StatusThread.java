package edu.ecnu.imc.bsma;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ecnu.imc.bsma.dao.BasicJobInfo;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.measurements.Measurements;
import edu.ecnu.imc.bsma.measurements.exporter.DBExporter;

/**
 * A thread to periodically show the status of the experiment, to reassure you
 * that progress is being made.
 * 
 * @author cooperb
 * @author wjx
 * 
 */
class StatusThread extends Thread {
	// public static final long sleeptime = 10000;
	public static final String sleeptime = "1000";
	public static final Logger logger = LoggerFactory
			.getLogger(StatusThread.class);
	JobInfo _jobInfo;
	BasicJobInfo _subJob;
	Measurements _measurements;
	DBExporter _exporter;
	AtomicBoolean _state = null;
	JobCordinator coord = null;

	/**
	 * The interval for reporting status. To set it to 100000 means to report
	 * status every 100 seconds.
	 */

	public StatusThread(JobCordinator coord, Measurements measurements) {
		super("status " + coord.getJobInfo().getJobID());
		this.coord = coord;
		_state = coord.getWorkingState();
		_jobInfo = coord.getJobInfo();
		_subJob = _jobInfo.getCurJob();
		_exporter = new DBExporter(_jobInfo, Scheduler.instance.getProps());
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
			for (Thread t : coord.getQueryThreads()) {
				if (t.getState() != Thread.State.TERMINATED) {
					alldone = false;
				}

				QueryExecThread ct = (QueryExecThread) t;
				totalops += ct.getOpsDone();
			}

			long en = System.currentTimeMillis();

			long interval = en - st;
			// double throughput=1000.0*((double)totalops)/((double)interval);

			double curthroughput = 0.0;
			if (en - lasten > 0)
				curthroughput = 1000.0 * (((double) (totalops - lasttotalops)) / ((double) (en - lasten)));

			lasttotalops = totalops;
			lasten = en;
			if (!alldone) {
				_exporter.newReport();
				_exporter.reportOverall(interval / 1000, totalops,
						(float) curthroughput);
				_measurements.getSummary(_exporter);
				try {
					_exporter.endReport();
				} catch (SQLException e) {
					coord.error(e);
				}
			}

			try {
				sleep(period);
			} catch (InterruptedException e) {
			}

		} while (!alldone && _state.get());

		try {
			exportMeasurements((System.currentTimeMillis() - st) / 1000);
		} catch (Exception e) {
			logger.error(e.getMessage());
			coord.error(e);
		}
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
	private void exportMeasurements(long runtime) throws IOException,
			SQLException {
		try {
			if (_subJob.getOpCount() >= 1) {
				_exporter.reportSubJobResult(runtime / 1000,
						_subJob.getOpCount());
			}// modified out by WeiJinxian
			_measurements.exportMeasurements(_exporter);
			_exporter.reportFinalReport();
		} finally {
			if (_exporter != null) {
				_exporter.close();
			}
		}
	}
}
