package edu.ecnu.imc.bsma;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;

import rpc.BSMAService;
import rpc.Job;
import rpc.SubJob;
import edu.ecnu.imc.bsma.dao.JobInfo;

/**
 * TODO: 接受任务
 * 
 * @author xiafan
 * 
 */
public class Scheduler implements BSMAService.Iface {
	private AtomicInteger jobIDGen = new AtomicInteger(0);
	private AtomicInteger subJobIDGen = new AtomicInteger(0);

	ConcurrentHashMap<Integer, Client> jobs = new ConcurrentHashMap<Integer, Client>();

	public void init() {
		// TODO 初始化jobID,subJobID

	}

	private void launchJob(Job job) {
		Client client = new Client(new JobInfo(job));
		client.start();
		jobs.put(job.getJobID(), client);
	}

	@Override
	public Job submit(Job job) throws TException {
		job.setJobID(jobIDGen.incrementAndGet());
		for (SubJob subJob : job.getSubJobs()) {
			subJob.setSubJobID(subJobIDGen.incrementAndGet());
		}
		// TODO write to mysql

		// start the job
		launchJob(job);
		return job;
	}

	@Override
	public void cancelJob(int jobID) throws TException {
		Client client = jobs.remove(jobID);
		if (client != null) {
			if (client.cancel()) {

			}
		}
	}

	@Override
	public void cancelSubJob(int jobID, int subID) throws TException {
		Client client = jobs.get(jobID);
		if (client != null) {
			if (client.cancel(subID)) {

			}
		}
	}
}
