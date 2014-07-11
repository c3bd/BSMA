package edu.ecnu.imc.bsma;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import rpc.BSMAService;
import rpc.Job;
import rpc.Query;
import rpc.SubJob;

public class BSMAClient {
	public static void main(String[] args) {
		try {
			TTransport transport;

			transport = new TSocket("localhost", 10000);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			BSMAService.Client client = new BSMAService.Client(protocol);
			Job job = new Job();
			job.setDbImpl((byte) 0);
			Map<String, String> props = new HashMap<String, String>();
			props.put("hserver", "jdbc:hive://10.11.1.190:10000/bsma");
			job.setProps(props);
			job.setJobID(-1);
			job.setName("test");
			job.setDescription("hello");
			job.setCustDbImpl("");
			for (int i = 1; i <= 1; i++) {
				SubJob subjob = new SubJob();
				subjob.setSubJobID(i);
				subjob.setOpCount(24);
				subjob.setThreadNum(i);
				job.addToSubJobs(subjob);
			}

			int queries = 1;
			for (int i = 1; i <= queries; i++) {
				job.addToQueries(new Query((byte) i, 1.0f / queries, "uniform"));
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
