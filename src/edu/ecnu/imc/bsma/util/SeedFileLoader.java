package edu.ecnu.imc.bsma.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.ecnu.imc.bsma.dao.JobInfo;

/**
 * 负责seed文件的加载，尽量避免每次工作都加载seed文件
 * @author xiafan
 *
 */
public class SeedFileLoader {
	public static String UBLOGS_SEED = "ublogs";
	public static String UEVENTS_SEED = "uevents";
	public static String URETWEETS_SEED = "uretweets";
	public static String UMENSIONS_SEED = "umensions";
	public static String EVENT_SEED = "events"; //
	public static String UFOLLOWERS_SEED = "ufollowers"; // 每个用户关注数分布
	
	public static SeedFileLoader instance = new SeedFileLoader();

	private Properties props;
	private String seedDir = null;

	private static final String DEFAULT_SEED = "default";
	private HashMap<String, HashMap<String, List<Pair<List<String>>>>> seedMap = new HashMap<String, HashMap<String, List<Pair<List<String>>>>>();

	private SeedFileLoader() {

	}

	public void init(Properties props) {
		this.props = props;
		if (!props.containsKey("seeddir")) {
			throw new RuntimeException("seeddir is not provided");
		}
		seedDir = props.getProperty("seeddir");
	}

	/**
	 * load the seed file for job
	 * @param jobInfo
	 * @param seed
	 * @return
	 */
	public synchronized List<Pair<List<String>>> getSeeds(JobInfo jobInfo,
			String seed) {
		List<Pair<List<String>>> ret = new ArrayList<Pair<List<String>>>();
		if (!seedMap.containsKey(DEFAULT_SEED)
				|| !seedMap.get(DEFAULT_SEED).containsKey(seed)) {
			File file = new File(seedDir, seed);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "utf-8"));
				String line = null;

				while (null != (line = reader.readLine())) {
					String[] fields = line.split("\t");
					List<String> tuple = new ArrayList<String>();
					for (int i = 0; i < fields.length - 1; i++) {
						tuple.add(fields[i]);
					}
					ret.add(new Pair<List<String>>(Double
							.parseDouble(fields[fields.length - 1]), tuple));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ret = seedMap.get(DEFAULT_SEED).get(seed);
		return ret;
	}

	/**
	 * 
	 */
	public void close() {
		seedMap.clear();
	}
}
