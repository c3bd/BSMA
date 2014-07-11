package edu.ecnu.imc.bsma.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import rpc.Query;

public class JobInfoPersistenceTest {

	@Before
	public void clear() {
		try {
			DaoUtil.emptyDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void persistenceTest() {
		Dao dao = DaoUtil.getTestDao();

		JobInfo jobInfo = new JobInfo(dao, JobInfo.WAITING);
		jobInfo.setDbImpl((byte) 1);
		jobInfo.setJobID(1);
		jobInfo.setName("test");
		jobInfo.setDescription("hello");
		jobInfo.setCustDbImpl("");
		Map<String, String> props = new HashMap<String, String>();
		props.put("a", "b");
		jobInfo.setProps(props);
		for (int i = 0; i < 4; i++) {
			BasicJobInfo subjob = new BasicJobInfo(dao, JobInfo.WAITING);
			subjob.setSubJobID(i);
			subjob.setOpCount(i);
			subjob.setThreadNum(i);
			jobInfo.addBasicJob(subjob);
		}

		for (int i = 0; i < 4; i++) {
			jobInfo.addToQueries(new Query((byte) i, 0.1, "uniform"));
		}

		try {
			jobInfo.save();
			jobInfo = dao.getJobInfo(jobInfo.getJobID());
			System.out.println(jobInfo);
			dao.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

	}
}
