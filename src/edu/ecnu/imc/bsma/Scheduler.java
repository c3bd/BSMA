package edu.ecnu.imc.bsma;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;

import rpc.BSMAService;
import rpc.Job;
import rpc.SubJob;
import edu.ecnu.imc.bsma.dao.JobInfo;

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

	private void launchJob(Job job) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Client.main(new String[] {});
			}
		};
		thread.start();
		Client client = new Client(new JobInfo(job));
		jobs.put(jobID, thread);
	}

	@Override
	public Job submit(Job job) throws TException {
		job.setJobID(jobIDGen.incrementAndGet());
		for (SubJob subJob : job.getSubJobs()) {
			subJob.setSubJobID(subJobIDGen.incrementAndGet());
		}
		//TODO write to mysql
		
		
		//start the job
		return job;
	}

	@Override
	public void cancelJob(int jobID) throws TException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelSubJob(int jobID, int subID) throws TException {
		// TODO Auto-generated method stub

	}
}
