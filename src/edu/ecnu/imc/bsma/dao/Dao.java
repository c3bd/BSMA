package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import rpc.Query;
import rpc.SubJob;

public class Dao {
	Connection con;

	public void insertJobFinalResult(SubJobFinalReport report)
			throws SQLException {
		String sql = String.format(
				"insert into FinalReport(subjobid,queryid,latency50,75latency,95latency,"
						+ "99latency,AverageLatency,MinLatency,MaxLatency) "
						+ "values(%d,%d,%d,%d,%d,%d,%f,%f,%f);",
				report.subjobid, report.queryid, report.latency50,
				report.latency75, report.latency95, report.latency99,
				report.AverageLatency, report.MinLatency, report.MaxLatency);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
	}

	public void insertRunningResults(RunningReport report) throws SQLException {
		String sql = String.format(
				"insert into RuningReport(subJobId,time,totalOps,throughput)"
						+ "values(%d,%d,%d,%f);", report.subJobId, report.time,
				report.throughput, report.totalOps);
		Statement stmt = con.createStatement();
		stmt.addBatch(sql);
		for (QueryRunningReport qReport : report.qStatus) {
			sql = String.format(
					"insert into QueryRuningReport(subJobId,time,totalOps,throughput)"
							+ "values(%d,%d,%d,%f);", report.subJobId,
					qReport.queryId, report.time, qReport.avgLatency);
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
	}

	private static final String MAXJOBID_SQL = "select max(jobId) from JobInfo;";

	public int getMaxJobID() throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet set = stmt.executeQuery(MAXJOBID_SQL);
		if (set.next()) {
			return set.getInt(0);
		} else {
			return 0;
		}
	}

	private static final String MAXSUBJOBID_SQL = "select max(jobId) from JobInfo;";

	public int getMaxSubJobID() throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet set = stmt.executeQuery(MAXSUBJOBID_SQL);
		if (set.next()) {
			return set.getInt(0);
		} else {
			return 0;
		}
	}

	/**
	 * the table of jobInfo , queries and jobUnitInfo
	 * @param jobInfo
	 * @throws SQLException 
	 */
	public void insertJobInfo(JobInfo jobInfo) throws SQLException {
		String sql = String.format("insert into JobInfo(id,dbImpl,custDBImpl)"
				+ "values(%d,%d,%s);", jobInfo.getJobID(), jobInfo.getDbImpl(),
				jobInfo.getCustDbImpl());
		Statement stmt = con.createStatement();
		stmt.addBatch(sql);
		for (Query query : jobInfo.getQueries()) {
			sql = String.format("insert into Query(jobID,queryID,fraction)"
					+ "values(%d,%d,%f);", jobInfo.getJobID(), query.getQID(),
					query.getFrac());
			stmt.addBatch(sql);
		}

		for (BasicJobInfo subJob : jobInfo.getJobUnits()) {
			sql = String.format(
					"insert into SubJobInfo(jobID,subJobID,opCount, threadNum, state)"
							+ "values(%d,%d,%d,%d,%d);", jobInfo.getJobID(),
					subJob.getSubJobID(), subJob.getOpCount(),
					subJob.getThreadNum(), subJob.getState());
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
	}

	/**
	 * 更新子任务的状态
	 * 
	 * @param job
	 * @throws SQLException
	 */
	public void updateSubJobStatus(BasicJobInfo job) throws SQLException {
		String sql = String
				.format("update SubJobInfo set state=%d where jobID = %d and subJobID = %d",
						job.getState(), job.getJobInfo().getJobID(),
						job.getSubJobID());
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
	}
}
