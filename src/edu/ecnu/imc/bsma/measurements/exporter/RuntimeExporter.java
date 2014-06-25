package edu.ecnu.imc.bsma.measurements.exporter;

import java.sql.SQLException;

import edu.ecnu.imc.bsma.dao.Dao;
import edu.ecnu.imc.bsma.dao.QueryRunningReport;
import edu.ecnu.imc.bsma.dao.RunningReport;

public class RuntimeExporter {
	public int SubJobId;
	Dao dao;
	RunningReport report = new RunningReport();
	int idx = 0;

	public void newReport() {
	}

	public void endReport() throws SQLException {
		if (idx < report.qStatus.size() - 1) {
			report.qStatus.subList(idx + 1, report.qStatus.size()).clear();
		}
		// TODO insert into mysql
		dao.insertRunningResults(report);
		idx = 0;
	}

	public void reportOverall(long time, int totalOps, double throughput) {
		report.time = time;
		report.totalOps = totalOps;
		report.throughput = throughput;
	}

	public void reportOneM(String Name, double latency) {
		QueryRunningReport cur;
		if (idx < report.qStatus.size()) {
			cur = new QueryRunningReport();
			report.qStatus.add(cur);
		} else {
			cur = report.qStatus.get(idx);
		}
		cur.queryId = Integer.parseInt(Name);
		cur.avgLatency = latency;

		idx++;
	}
}
