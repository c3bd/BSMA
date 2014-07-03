package edu.ecnu.imc.bsma;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpc.BSMAService;
import rpc.Job;
import rpc.SubJob;
import edu.ecnu.imc.bsma.dao.Dao;

/**
 * TODO: 接受任务
 * 
 * @author xiafan
 * 
 */
public class Scheduler implements BSMAService.Iface {
	public static Logger logger = LoggerFactory.getLogger(Scheduler.class);

	public static Scheduler instance = new Scheduler();

	private AtomicInteger jobIDGen = new AtomicInteger(0);
	private AtomicInteger subJobIDGen = new AtomicInteger(0);

	ConcurrentHashMap<Integer, Client> jobs = new ConcurrentHashMap<Integer, Client>();

	private Scheduler() {

	}

	public void init() {
		// TODO 初始化jobID,subJobID
		props.setProperty("server", "jdbc:mysql://localhost:3306/bsma");
		props.setProperty("user", "root");
		props.setProperty("passwd", "Hadoop123");

		// init id seed
		Dao dao = new Dao(props);
		try {
			jobIDGen.set(dao.getMaxJobID());
			subJobIDGen.set(dao.getMaxSubJobID());
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.exit(e.getErrorCode());
		}

	}

	Properties props = new Properties();

	/**
	 * 系统配置
	 * 
	 * @return
	 */
	public Properties getProps() {
		return props;
	}

	private void launchJob(Job job) {
		Client client = new Client(job);
		client.start();
		jobs.put(job.getJobID(), client);
	}

	@Override
	public Job submit(Job job) throws TException {
		logger.info("job submitted:" + job);
		job.setJobID(jobIDGen.incrementAndGet());
		for (SubJob subJob : job.getSubJobs()) {
			subJob.setSubJobID(subJobIDGen.incrementAndGet());
		}
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
