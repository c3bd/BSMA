package edu.ecnu.imc.bsma;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import edu.ecnu.imc.bsma.dao.JobInfo;
import rpc.BSMAService;
import rpc.Job;
import rpc.Query;
import rpc.SubJob;

public class BSMAClient {
	public static void main(String[] args) {
		for (int j = 0; j < 3; j++)
			for (int i = 1; i <= 5; i++)
				test("mysql", JobInfo.MYSQL_DB, i);

		for (int j = 0; j < 3; j++)
			for (int i = 1; i <= 5; i++)
				test("neo4j", JobInfo.NEO4J_DB, i);
	}

	private static void test(String name, byte db, int query) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10000);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			BSMAService.Client client = new BSMAService.Client(protocol);
			Job job = new Job();
			job.setDbImpl(db);
			Map<String, String> props = new HashMap<String, String>();
			// props.put("hserver", "jdbc:hive://10.11.1.190:10000/bsma");
			job.setProps(props);
			job.setJobID(-1);
			job.setName(name);
			job.setDescription("hello");
			job.setCustDbImpl("");
			for (int i = 1; i <= 1; i++) {
				SubJob subjob = new SubJob();
				subjob.setSubJobID(i);
				subjob.setOpCount(500);
				subjob.setThreadNum(i);
				job.addToSubJobs(subjob);
			}

			for (int i = query; i <= query; i++) {
				job.addToQueries(new Query((byte) i, 1.0f / 1, "uniform"));
			}
			System.out.println("before submit job is :" + job);
			job = client.submit(job);
			System.out.println("after submit job is : " + job);
			transport.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
