package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
	Connection con;

	public void insertJobFinalResult(JobFinalReport report) throws SQLException {
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
}
