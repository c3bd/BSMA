package edu.ecnu.imc.bsma;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpc.BSMAService;
import rpc.Job;
import rpc.SubJob;
import edu.ecnu.imc.bsma.dao.Dao;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.util.Config;
import edu.ecnu.imc.bsma.util.LoggerInitializer;
import edu.ecnu.imc.bsma.util.SeedFileLoader;

/**
 * TODO: 接受任务
 * 
 * 1. 如果用户提供自定义的db实现，那么所有的jar文件都会复制到workingdir 下面的当前任务对应的目录的jar目录下面
 * 
 * @author xiafan
 * 
 */
public class Scheduler implements BSMAService.Iface {
	// created first
	public static LoggerInitializer init = new LoggerInitializer();

	public static Logger logger = LoggerFactory.getLogger(Scheduler.class);

	public static Scheduler instance = new Scheduler();

	private AtomicInteger jobIDGen = new AtomicInteger(0);
	private AtomicInteger subJobIDGen = new AtomicInteger(0);

	ConcurrentHashMap<Integer, JobCordinator> runningJobs = new ConcurrentHashMap<Integer, JobCordinator>();
	ConcurrentLinkedQueue<JobInfo> waitingJobs = new ConcurrentLinkedQueue<JobInfo>();

	private Scheduler() {

	}

	Dao dao = null;

	public void init() {
		// 初始化logger

		// 初始化config
		try {
			Config.instance.init();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SeedFileLoader.instance.init(Config.instance.getProps());
		// 初始化jobID,subJobID
		dao = new Dao(Scheduler.instance.getProps());

		try {
			jobIDGen.set(dao.getMaxJobID());
			subJobIDGen.set(dao.getMaxSubJobID());
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.exit(e.getErrorCode());
		}

		/**
		 * TODO if recover from failure, need to do the following cleaning: 1.
		 * reset all running job to be waiting, delete their related status
		 * report
		 */

		/**
		 * TODO cleaning resources of finished jobs 1. delete related jars
		 */
	}

	/**
	 * 系统配置
	 * 
	 * @return
	 */
	public Properties getProps() {
		return Config.instance.getProps();
	}

	private void launchJob(Job job) {
		JobCordinator client = new JobCordinator(job);
		client.start();
		runningJobs.put(job.getJobID(), client);
	}

	public void scheduleJob(Job job) {
		JobInfo newJob = new JobInfo(job, dao);
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
		JobCordinator client = runningJobs.remove(jobID);
		if (client != null) {
			if (client.cancel()) {

			}
		}
	}

	@Override
	public void cancelSubJob(int jobID, int subID) throws TException {
		JobCordinator client = runningJobs.get(jobID);
		if (client != null) {
			if (client.cancel(subID)) {

			}
		}
	}
}
