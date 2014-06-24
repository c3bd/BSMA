package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	Connection con = null; // 定义一个MYSQL链接对象

	public void connect(String server, String user, String passwd)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); // MYSQL驱动
		// con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
		// "root", "root"); // 链接本地MYSQL
		con = DriverManager.getConnection(server, user, passwd); // 链接本地MYSQL
	}

	public Connection getCon() {
		return con;
	}

	public void putCon(Connection con) {

	}

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

	public void insertRunningResult(RunningReport report) throws SQLException {
		String sql = String.format(
				"insert into FinalReport(SubJobId,Queryid,Seqid,AverageLatency,"
						+ "values(%d,%d,%d,%f);", report.SubJobId,
				report.Queryid, report.Seqid, report.AverageLatency);
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
	}
}
