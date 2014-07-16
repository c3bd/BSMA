package edu.ecnu.imc.bsma.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import edu.ecnu.imc.bsma.util.JSONUtil;

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

	private static final String DB_EXIST = "select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='bsma';";

	/**
	 * create the database and tables
	 * 
	 * @throws SQLException
	 */
	public void initDB() throws SQLException {
		if (!dbExists()) {
			Connection conn = jdbc.getCon();
			Statement stmt = conn.createStatement();
			StringBuffer buf = new StringBuffer();

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						"./conf/initdb.sql"));

				String line = null;
				while (null != (line = reader.readLine())) {
					if (!line.startsWith("/*"))
						buf.append(line);
				}
			} catch (IOException e) {
				throw new RuntimeException(
						"couldn't find database initialize file");
			}
			String[] sqls = buf.toString().split(";");
			for (String sql : sqls) {
				int ret = stmt.executeUpdate(sql);
				if (ret < 0) {
					logger.error("report database initialization fails");
					throw new RuntimeException(
							"report database initialization fails");
				}
			}
		}
		logger.info("report database initialized");
	}

	private boolean dbExists() throws SQLException {
		boolean ret = true;
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(DB_EXIST);
		if (!set.next()) {
			ret = false;
		}
		return ret;
	}

	public void insertSubJobFinalResult(SubJobFinalReport report)
			throws SQLException {
		String sql = String.format(
				"insert into bsma.SubJobFinalReport(subjobid,totalops,totaltime) "
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

	public SubJobFinalReport getSubJobFinalResult(int subjobid)
			throws SQLException {
		SubJobFinalReport ret = null;

		List<QueryFinalReport> reports = getQueryFinalResults(subjobid);
		if (reports.isEmpty())
			return ret;

		String sql = String.format(
				"select * from bsma.SubJobFinalReport where subjobid = %d;",
				subjobid);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		if (set.next()) {

		}
		jdbc.putCon(conn);

		return ret;
	}

	public void insertQueryFinalResult(Statement stmt, QueryFinalReport report)
			throws SQLException {
		String sql = String
				.format("insert into bsma.QueryFinalReport(subjobid,queryid,latency50, latency75,latency95,"
						+ "latency99,avglatency,minlatency,maxlatency) "
						+ "values(%d,%d,%d,%d,%d,%d,%f,%f,%f);",
						report.subjobid, report.queryid, report.latency50,
						report.latency75, report.latency95, report.latency99,
						report.avglatency, report.minlatency, report.maxlatency);
		stmt.addBatch(sql);
	}

	public List<QueryFinalReport> getQueryFinalResults(int subJobID)
			throws SQLException {
		List<QueryFinalReport> ret = new ArrayList<QueryFinalReport>();

		String sql = String
				.format("select subjobid,queryid,latency50, latency75,latency95,"
						+ "latency99,avglatency,minlatency,maxlatency from bsma.QueryFinalReport where subjobid = %d",
						subJobID);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		while (set.next()) {
			QueryFinalReport report = new QueryFinalReport();
			report.subjobid = set.getInt("subjobid");
			report.queryid = set.getInt("queryid");
			report.latency50 = set.getInt("latency50");
			report.latency75 = set.getInt("latency75");
			report.latency95 = set.getInt("latency95");
			report.latency99 = set.getInt("latency99");
			report.avglatency = set.getInt("avglatency");
			report.minlatency = set.getInt("minlatency");
			report.maxlatency = set.getInt("maxlatency");
			ret.add(report);
		}
		jdbc.putCon(conn);
		return ret;
	}

	public void insertRunningResults(RunningReport report) throws SQLException {
		String sql = String.format(
				"insert into bsma.RunningReport(subjobid,time,totalops,throughput)"
						+ "values(%d,%d,%d,%f);", report.subJobId, report.time,
				report.totalOps, report.curThroughput);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql);
		for (QueryRunningReport qReport : report.qStatus) {
			sql = String.format(
					"insert into bsma.QueryRunningReport(subjobid,queryid,time,avglatency)"
							+ "values(%d,%d,%d,%f);", report.subJobId,
					qReport.queryId, report.time, qReport.avgLatency);
			stmt.addBatch(sql);
		}
		stmt.executeBatch();
		jdbc.putCon(conn);
	}

	private static final String MAXJOBID_SQL = "select max(jobId) from bsma.JobInfo;";

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

	private static final String MAXSUBJOBID_SQL = "select max(subjobid) from bsma.Rel_Job_SubJob;";

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
		String sql;
		try {
			sql = String
					.format("insert into bsma.JobInfo(jobid, name,dbImpl,custDBImpl, props, description, jars, state, msg)"
							+ "values(%d,'%s',%d,'%s','%s', '%s','%s', %d,'%s');",
							jobInfo.getJobID(), jobInfo.getName(),
							jobInfo.getDbImpl(), jobInfo.getCustDbImpl(),
							JSONObject.valueToString(jobInfo.getProps()),
							jobInfo.getDescription(),
							JSONObject.valueToString(jobInfo.getJars()),
							jobInfo.getState(), jobInfo.getMsg());
			Connection conn = jdbc.getCon();
			Statement stmt = conn.createStatement();
			stmt.addBatch(sql);
			for (Query query : jobInfo.getQueries()) {
				sql = String.format(
						"insert into bsma.Rel_Job_Query(jobID,queryID,fraction)"
								+ "values(%d,%d,%f);", jobInfo.getJobID(),
						query.getQID(), query.getFrac());
				stmt.addBatch(sql);
			}

			for (SubJob subJob : jobInfo.getSubJobs()) {
				sql = String.format(
						"insert into bsma.Rel_Job_SubJob(jobID,subJobID,opCount, threadNum, state)"
								+ "values(%d,%d,%d,%d,%d);",
						jobInfo.getJobID(), subJob.getSubJobID(),
						subJob.getOpCount(), subJob.getThreadNum(),
						((BasicJobInfo) subJob).getState());
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			jdbc.putCon(conn);
		} catch (JSONException e) {
			throw new SQLException(e.getMessage());
		}

	}

	public JobInfo getJobInfo(int jobId) throws SQLException {
		String sql = String.format(
				"select * from bsma.JobInfo where jobid = %d;", jobId);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		JobInfo ret = null;
		while (set.next()) {
			ret = new JobInfo(this, set.getByte("state"));
			ret.setJobID(set.getInt("jobid"));
			ret.setName(set.getString("name"));
			ret.setDbImpl(set.getByte("dbImpl"));
			ret.setCustDbImpl(set.getString("custDBImpl"));
			ret.setProps(JSONUtil.toMap(set.getString("props")));
			ret.setDescription(set.getString("description"));
			ret.setQueries(getQueries(jobId));
			ret.setBasicJobs(getSubJobs(jobId));
			ret.setJars(JSONUtil.toStringList(set.getString("jars")));
			ret.setMsg(set.getString("msg"));
		}
		jdbc.putCon(conn);
		return ret;
	}

	public List<Query> getQueries(int jobId) throws SQLException {
		List<Query> ret = new ArrayList<Query>();
		String sql = String.format(
				"select * from bsma.Rel_Job_Query where jobid = %d;", jobId);
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		while (set.next()) {
			ret.add(new Query(set.getByte(1), set.getDouble(2), set
					.getString(3)));
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
				"select * from bsma.Rel_Job_SubJob where jobid = %d;", jobId);
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
				.format("update bsma.Rel_Job_SubJob set state=%d where jobID = %d and subJobID = %d",
						job.getState(), job.getJobInfo().getJobID(),
						job.getSubJobID());
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		jdbc.putCon(conn);
	}

	public void updateJobState(JobInfo jobInfo) throws SQLException {
		String sql = String
				.format("update bsma.JobInfo set state=%d, msg = \"%s\"where jobID = %d;",
						jobInfo.getState(), jobInfo.getMsg(),
						jobInfo.getJobID());
		Connection conn = jdbc.getCon();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		jdbc.putCon(conn);
	}

	public static final String[] tables = new String[] { "bsma.JobInfo",
			"bsma.Rel_Job_Query", "bsma.Rel_Job_SubJob",
			"bsma.SubJobFinalReport", "bsma.QueryFinalReport",
			"bsma.RunningReport", "bsma.QueryRunningReport" };

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
