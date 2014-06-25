package edu.ecnu.imc.bsma.measurements.exporter;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ecnu.imc.bsma.dao.RunningReport;

public class RuntimeExporter {
	public int SubJobId;
	int seqID = 0;
	Statement state;

	List<RunningReport> reports = new ArrayList<RunningReport>();
	int idx = 0;

	public void newReport() {
		seqID++;
	}

	public void endReport() {
		if (idx < reports.size() - 1) {
			reports.subList(idx + 1, reports.size()).clear();
		}
		// TODO insert into mysql

		idx = 0;
	}

	public void reportOverall(long time, long totalOps, double throughput) {

	}

	public void reportOneM(String Name, double latency) {
		RunningReport cur;
		if (idx < reports.size()) {
			cur = new RunningReport();
			reports.add(cur);
		} else {
			cur = reports.get(idx);
		}
		cur.SubJobId = SubJobId;
		cur.Queryid = Integer.parseInt(Name);
		cur.Seqid = seqID;
		cur.AverageLatency = latency;

		idx++;
	}
}
