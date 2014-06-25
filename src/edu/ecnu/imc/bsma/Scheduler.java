package edu.ecnu.imc.bsma;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;

import rpc.BSMAService;
import rpc.Job;

/**
 * TODO:
 * 接受任务
 * @author xiafan
 *
 */
public class Scheduler implements BSMAService.Iface {
	private AtomicInteger jobIDGen = new AtomicInteger(0);
	private AtomicInteger subJobIDGen = new AtomicInteger(0);

	HashMap<Integer, Thread> jobs = new HashMap<Integer, Thread>();

	public void init() {
		// TODO 初始化jobID,subJobID

	}

	@Override
	public Job start(String job) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancel(int jobID) throws TException {
		// TODO Auto-generated method stub

	}

	private void launchJob(int jobID) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Client.main(new String[] {});
			}
		};
		thread.start();
		jobs.put(jobID, thread);
	}
}
