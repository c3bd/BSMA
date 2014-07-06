package edu.ecnu.imc.bsma.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 最终的表报是由总体的吞吐量和每个的查询的延迟构成的，其中延迟最终报表包含一个fake的全局报表，统计了全局的延迟信息
 * @author xiafan
 *
 */
public class JobFinalReport {
	public int jobid = 0;
	public long totaltime = 0;
	public int ops = 0;
	public List<QueryFinalReport> qReports = new ArrayList<QueryFinalReport>();
	int idx = 0; // current empty slot

	/**
	 * 返回一个新的queryreport对象
	 * @return
	 */
	public QueryFinalReport newQueryFinalReport() {
		QueryFinalReport ret = null;
		if (idx == qReports.size()) {
			ret = new QueryFinalReport();
			qReports.add(ret);
		} else {
			ret = qReports.get(idx);
		}
		idx++;
		return ret;
	}

	public List<QueryFinalReport> getQueryFReportList() {
		if (idx < qReports.size())
			qReports.subList(idx, qReports.size()).clear();
		return qReports;
	}
}
