package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

	public void insertRunningResults(List<RunningReport> report)
			throws SQLException {
		String sql = String.format(
				"insert into FinalReport(SubJobId,Queryid,Seqid,AverageLatency,"
						+ "values(%d,%d,%d,%f);", report.SubJobId,
				report.Queryid, report.Seqid, report.AverageLatency);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
	}

	public int getMaxJobID() {

	}

	public int getMaxSubJobID() {

	}
}
