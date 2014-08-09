package edu.ecnu.imc.bsma.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 最终的子任务表报是由总体的吞吐量和每个的查询的延迟构成的，其中延迟最终报表包含一个fake的全局报表，统计了全局的延迟信息
 * 
 * @author xiafan
 * 
 */
public class SubJobFinalReport {
	public int jobid = 0;
	public long totaltime = 0;
	public int ops = 0;
	public List<QueryFinalReport> qReports;
	int idx = 0; // current empty slot

	public SubJobFinalReport() {
		qReports = new ArrayList<QueryFinalReport>();
	}

	/**
	 * used when selected from database
	 * 
	 * @param jobid
	 * @param totaltime
	 * @param ops
	 * @param qReports
	 */
	public SubJobFinalReport(int jobid, long totaltime, int ops,
			List<QueryFinalReport> qReports) {
		super();
		this.jobid = jobid;
		this.totaltime = totaltime;
		this.ops = ops;
		this.qReports = qReports;
	}

	/**
	 * 返回一个新的queryreport对象
	 * 
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

	public List<QueryFinalReport> queryFReportList() {
		if (idx < qReports.size())
			qReports.subList(idx, qReports.size()).clear();
		return qReports;
	}

	public int getJobid() {
		return jobid;
	}

	public void setJobid(int jobid) {
		this.jobid = jobid;
	}

	public long getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(long totaltime) {
		this.totaltime = totaltime;
	}

	public int getOps() {
		return ops;
	}

	public void setOps(int ops) {
		this.ops = ops;
	}

	public List<QueryFinalReport> getqReports() {
		return qReports;
	}

	public void setqReports(List<QueryFinalReport> qReports) {
		this.qReports = qReports;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "SubJobFinalReport [jobid=" + jobid + ", totaltime=" + totaltime
				+ ", ops=" + ops + ", qReports=" + qReports + ", idx=" + idx
				+ "]";
	}

	public JSONObject asJson() {
		return JSONObject.fromObject(this);
	}

	public static void main(String[] args) {
		SubJobFinalReport rp = new SubJobFinalReport();
		rp.qReports.add(new QueryFinalReport());
		System.out.println(rp.asJson());
	}
}
