package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpc.Query;
import rpc.SubJob;

public class Dao {
	public static Logger logger = LoggerFactory.getLogger(Dao.class);
	JDBC jdbc;

	public Dao(Properties props) {
		try {
			jdbc = new JDBC(props.getProperty("report_server"),
					props.getProperty("report_user"),
					props.getProperty("report_passwd"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("init jdbc object fails:"
					+ e.getMessage());
		}
	}

	public void insertQueryFinalResult(Statement stmt, QueryFinalReport report)
			throws SQLException {
		String sql = String.format(
				"insert into QueryFinalReport(subjobid,queryid,latency50, latency75,latency95,"
						+ "latency99,avglatency,minlatency,maxlatency) "
						+ "values(%d,%d,%d,%d,%d,%d,%f,%f,%f);",
				report.subjobid, report.queryid, report.latency50,
				report.latency75, report.latency95, report.latency99,
				report.AverageLatency, report.MinLatency, report.MaxLatency);
		stmt.addBatch(sql);
	}

	public void insertJobFinalResult(JobFinalReport report) throws SQLException {
		String sql = String.format(
				"insert into SubJobFinalReport(subjobid,totalops,totaltime) "
						+ "values(%d,%d,%d);", report.jobid, report.ops,
				report.totaltime);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql);
		for (QueryFinalReport subReport : report.getQueryFReportList()) {
			insertQueryFinalResult(stmt, subReport);
		}
		stmt.executeBatch();
		jdbc.putCon(conn);
	}
	
	

	public void insertRunningResults(RunningReport report) throws SQLException {
		String sql = String.format(
				"insert into RunningReport(subjobid,time,totalops,throughput)"
						+ "values(%d,%d,%d,%f);", report.subJobId, report.time,
				report.totalOps, report.curThroughput);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql);
		for (QueryRunningReport qReport : report.qStatus) {
			sql = String.format(
					"insert into QueryRunningReport(subjobid,queryid,time,avglatency)"
							+ "values(%d,%d,%d,%f);", report.subJobId,
					qReport.queryId, report.time, qReport.avgLatency);
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
		jdbc.putCon(conn);
	}

	private static final String MAXJOBID_SQL = "select max(jobId) from JobInfo;";

	public int getMaxJobID() throws SQLException {
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(MAXJOBID_SQL);
		int ret = 0;
		if (set.next()) {
			ret = set.getInt(1);
		}
		jdbc.putCon(conn);
		return ret;
	}

	private static final String MAXSUBJOBID_SQL = "select max(subjobid) from Rel_Job_SubJob;";

	public int getMaxSubJobID() throws SQLException {
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(MAXSUBJOBID_SQL);
		int ret = 0;
		if (set.next()) {
			ret = set.getInt(1);
		}
		jdbc.putCon(conn);
		return ret;
	}

	/**
	 * the table of jobInfo , queries and jobUnitInfo
	 * 
	 * @param jobInfo
	 * @throws SQLException
	 */
	public void insertJobInfo(JobInfo jobInfo) throws SQLException {
		String sql = String.format(
				"insert into JobInfo(jobid, name,dbImpl,custDBImpl, props, description, state)"
						+ "values(%d,'%s',%d,'%s','%s', '%s', %d);",
				jobInfo.getJobID(), jobInfo.getName(), jobInfo.getDbImpl(),
				jobInfo.getCustDbImpl(), jobInfo.getProps(),
				jobInfo.getDescription(), jobInfo.getState());
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql);
		for (Query query : jobInfo.getQueries()) {
			sql = String.format(
					"insert into Rel_Job_Query(jobID,queryID,fraction)"
							+ "values(%d,%d,%f);", jobInfo.getJobID(),
					query.getQID(), query.getFrac());
			stmt.addBatch(sql);
		}

		for (SubJob subJob : jobInfo.getSubJobs()) {
			sql = String.format(
					"insert into Rel_Job_SubJob(jobID,subJobID,opCount, threadNum, state)"
							+ "values(%d,%d,%d,%d,%d);", jobInfo.getJobID(),
					subJob.getSubJobID(), subJob.getOpCount(),
					subJob.getThreadNum(), ((BasicJobInfo) subJob).getState());
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
		jdbc.putCon(conn);
	}

	public JobInfo getJobInfo(int jobId) throws SQLException {
		String sql = String.format("select * from JobInfo where jobid = %d;",
				jobId);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		JobInfo ret = null;
		while (set.next()) {
			ret = new JobInfo(this, set.getByte(7));
			ret.setJobID(set.getInt(1));
			ret.setName(set.getString(2));
			ret.setDbImpl(set.getByte(3));
			ret.setCustDbImpl(set.getString(4));
			// TODO props
			ret.setDescription(set.getString(6));
			ret.setQueries(getQueries(jobId));
			ret.setBasicJobs(getSubJobs(jobId));
		}
		jdbc.putCon(conn);
		return ret;
	}

	public List<Query> getQueries(int jobId) throws SQLException {
		List<Query> ret = new ArrayList<Query>();
		String sql = String.format(
				"select * from Rel_Job_Query where jobid = %d;", jobId);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		while (set.next()) {
			ret.add(new Query(set.getByte(1), set.getDouble(2)));
		}
		jdbc.putCon(conn);
		return ret;
	}

	/**
	 * the jobInfo of the returned basicjobinfo is not set
	 * 
	 * @param jobId
	 * @return
	 * @throws SQLException
	 */
	public List<BasicJobInfo> getSubJobs(int jobId) throws SQLException {
		List<BasicJobInfo> ret = new ArrayList<BasicJobInfo>();
		String sql = String.format(
				"select * from Rel_Job_SubJob where jobid = %d;", jobId);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		if (set.next()) {
			BasicJobInfo subJob = new BasicJobInfo(this, set.getByte(4));
			subJob.setSubJobID(set.getInt(1));
			subJob.setOpCount(set.getInt(2));
			subJob.setThreadNum(set.getInt(3));

			ret.add(subJob);
		}
		jdbc.putCon(conn);
		return ret;
	}

	/**
	 * 更新子任务的状态
	 * 
	 * @param job
	 * @throws SQLException
	 */
	public void updateSubJobStatus(BasicJobInfo job) throws SQLException {
		String sql = String
				.format("update Rel_Job_SubJob set state=%d where jobID = %d and subJobID = %d",
						job.getState(), job.getJobInfo().getJobID(),
						job.getSubJobID());
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		jdbc.putCon(conn);
	}

	public void updateJobState(JobInfo jobInfo) throws SQLException {
		String sql = String.format(
				"update JobInfo set state=%d, msg = \"%s\"where jobID = %d;",
				jobInfo.getState(), jobInfo.getMsg(), jobInfo.getJobID());
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		jdbc.putCon(conn);
	}

	public static final String[] tables = new String[] { "JobInfo",
			"Rel_Job_Query", "Rel_Job_SubJob", "SubJobFinalReport",
			"QueryFinalReport", "RunningReport", "QueryRunningReport" };

	public void clear() throws SQLException {
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		for (String table : tables) {
			String sql = String.format(String.format("delete from %s;", table));
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
		jdbc.putCon(conn);
	}

	public void close() throws SQLException {
		jdbc.close();
	}
}
