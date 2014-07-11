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
 * 
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
	// private String seedDir = null;

	private static final String DEFAULT_SEED = "realdata";
	private HashMap<String, HashMap<String, List<Pair>>> seedMap = new HashMap<String, HashMap<String, List<Pair>>>();

	private SeedFileLoader() {

	}

	File seedDir = null;

	public void init(Properties props) {
		this.props = props;
		if (!props.containsKey("seeddir")) {
			throw new RuntimeException("seeddir is not provided");
		}
		seedDir = new File(props.getProperty("seeddir"));
	}

	/**
	 * load the seed file for job TODO for those only contains two fields, no
	 * need to return list
	 * 
	 * @param jobInfo
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public synchronized List<Pair> getSeeds(JobInfo jobInfo, String seed)
			throws Exception {
		if (!seedMap.containsKey(DEFAULT_SEED)) {
			seedMap.put(DEFAULT_SEED, new HashMap<String, List<Pair>>());
		}
		List<Pair> ret = new ArrayList<Pair>();
		if (!seedMap.get(DEFAULT_SEED).containsKey(seed)) {
			File file = new File(new File(seedDir, DEFAULT_SEED), seed);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "utf-8"));
				String line = null;
				double sum = 0.0;
				while (null != (line = reader.readLine())) {
					String[] fields = line.split("\t");
					if (fields.length == 2) {
						double weight = Double
								.parseDouble(fields[fields.length - 1]);
						ret.add(new Pair<String>(weight, fields[0]));
						sum += weight;
					} else if (fields.length == 1) {
						ret.add(new Pair<String>(1, fields[0]));
						sum += 1;
					} else {
						List<String> tuple = new ArrayList<String>();
						for (int i = 0; i < fields.length - 1; i++) {
							tuple.add(fields[i]);
						}
						double weight = Double
								.parseDouble(fields[fields.length - 1]);
						ret.add(new Pair<List<String>>(weight, tuple));
						sum += weight;
					}
				}
				for (Pair<List<String>> pair : ret) {
					pair._weight = pair._weight / sum;
				}
				seedMap.get(DEFAULT_SEED).put(seed, ret);
			} catch (Exception e) {
				throw e;
			} finally {
				try {
					if (reader != null)
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
