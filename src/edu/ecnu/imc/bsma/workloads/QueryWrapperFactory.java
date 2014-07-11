package edu.ecnu.imc.bsma.workloads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rpc.Query;
import edu.ecnu.imc.bsma.dao.JobInfo;
import edu.ecnu.imc.bsma.db.DB;
import edu.ecnu.imc.bsma.generator.Generator;
import edu.ecnu.imc.bsma.generator.GeneratorFactory;
import edu.ecnu.imc.bsma.util.Pair;
import edu.ecnu.imc.bsma.util.SeedFileLoader;

public class QueryWrapperFactory {
	private static final long HOUR = 3600000;

	public static QueryWrapper create(JobInfo jobInfo, Query query)
			throws Exception {
		QueryWrapper ret = null;
		String genType = query.getType();
		if (query.getQID() == 1) {
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			Generator gen = GeneratorFactory.create(genType, seeds);
			ret = new Query1Wrapper(Arrays.asList(gen));
		} else if (query.getQID() == 2) {
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			Generator gen = GeneratorFactory.create(genType, seeds);
			ret = new Query2Wrapper(Arrays.asList(gen));
		} else if (query.getQID() == 3) {
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			Generator gen = GeneratorFactory.create(genType, seeds);
			ret = new Query3Wrapper(Arrays.asList(gen));
		} else if (query.getQID() == 4) {
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			Generator gen = GeneratorFactory.create(genType, seeds);
			ret = new Query4Wrapper(Arrays.asList(gen));
		} else if (query.getQID() == 5) {
			List<Generator> gens = new ArrayList<Generator>(2);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo, "ufan_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query5Wrapper(gens);
		} else if (query.getQID() == 6) {
			List<Generator> gens = new ArrayList<Generator>(2);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"umensioned_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"mension_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query6Wrapper(gens);
		} else if (query.getQID() == 7) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"mension_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query7Wrapper(gens);
		} else if (query.getQID() == 8) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query8Wrapper(gens);
		} else if (query.getQID() == 9) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"eblogs_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query9Wrapper(gens);
		} else if (query.getQID() == 10) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"retweet_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query10Wrapper(gens);
		} else if (query.getQID() == 11) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"uretweeted_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"retweet_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query11Wrapper(gens);
		} else if (query.getQID() == 12) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"uretweeted_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"retweet_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query12Wrapper(gens);
		} else if (query.getQID() == 13) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"uretweeted_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"mension_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query13Wrapper(gens);
		} else if (query.getQID() == 14) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"microblog_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query14Wrapper(gens);
		} else if (query.getQID() == 15) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"eblogs_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query15Wrapper(gens);
		} else if (query.getQID() == 16) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"microblog_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query16Wrapper(gens);
		} else if (query.getQID() == 17) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"mension_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query17Wrapper(gens);
		} else if (query.getQID() == 18) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufan_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"mension_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query18Wrapper(gens);
		} else if (query.getQID() == 19) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"ufriend_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"microblog_time_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query19Wrapper(gens);
		} else if (query.getQID() == 20) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"event_blogs_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query20Wrapper(gens);
		} else if (query.getQID() == 21) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"event_blogs_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query21Wrapper(gens);
		} else if (query.getQID() == 22) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"microblog_rt_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query22Wrapper(gens);
		} else if (query.getQID() == 23) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"event_blogs_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query23Wrapper(gens);
		} else if (query.getQID() == 24) {
			List<Generator> gens = new ArrayList<Generator>(1);
			List<Pair> seeds = SeedFileLoader.instance.getSeeds(jobInfo,
					"event_blogs_dist");
			gens.add(GeneratorFactory.create(genType, seeds));
			ret = new Query24Wrapper(gens);
		}

		return ret;
	}

	private static class Query1Wrapper extends QueryWrapper {
		public Query1Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			db.BSMAQuery1(uid, 10);
		}
	}

	private static class Query2Wrapper extends QueryWrapper {
		public Query2Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			db.BSMAQuery2(uid, 10);
		}
	}

	private static class Query3Wrapper extends QueryWrapper {
		public Query3Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			db.BSMAQuery3(uid, 10);
		}
	}

	private static class Query4Wrapper extends QueryWrapper {
		public Query4Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String a = (String) gens.get(0).nextString();
			String b = (String) gens.get(0).nextString();
			db.BSMAQuery4(a, b);
		}
	}

	private static class Query5Wrapper extends QueryWrapper {
		public Query5Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String a = (String) gens.get(0).nextString();
			String b = (String) gens.get(1).nextString();
			db.BSMAQuery5(a, b);
		}
	}

	private static class Query6Wrapper extends QueryWrapper {
		public Query6Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String a = (String) gens.get(0).nextString();
			long b = Long.parseLong((String) gens.get(1).nextString()) * HOUR;
			db.BSMAQuery6(a, 10, b, HOUR * 24);
		}
	}

	private static class Query7Wrapper extends QueryWrapper {
		public Query7Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			long time = Long.parseLong((String) gens.get(0).nextString())
					* HOUR;
			db.BSMAQuery7(10, time, HOUR * 24);
		}
	}

	private static class Query8Wrapper extends QueryWrapper {
		public Query8Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			db.BSMAQuery8(100, uid);
		}
	}

	private static class Query9Wrapper extends QueryWrapper {
		public Query9Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			List<String> args = (List<String>) gens.get(1).nextString();
			long time = Long.parseLong(args.get(1)) * HOUR * 12;
			db.BSMAQuery9(args.get(0), uid, 10, time, 12 * HOUR);
		}
	}

	private static class Query10Wrapper extends QueryWrapper {
		public Query10Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			long time = Long.parseLong((String) gens.get(0).nextString())
					* HOUR * 10;
			db.BSMAQuery10(10, time, HOUR * 12);
		}
	}

	private static class Query11Wrapper extends QueryWrapper {
		public Query11Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR * 10;
			db.BSMAQuery11(uid, 10, time, HOUR * 10);
		}
	}

	private static class Query12Wrapper extends QueryWrapper {
		public Query12Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR * 10;
			db.BSMAQuery12(uid, 10, time, HOUR * 10);
		}
	}

	private static class Query13Wrapper extends QueryWrapper {
		public Query13Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR;
			db.BSMAQuery13(uid, 10, time, HOUR * 10);
		}
	}

	private static class Query14Wrapper extends QueryWrapper {
		public Query14Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			long time = Long.parseLong((String) gens.get(0).nextString())
					* HOUR;
			db.BSMAQuery14(10, time, HOUR * 10);
		}
	}

	private static class Query15Wrapper extends QueryWrapper {
		public Query15Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			List<String> pair = (List<String>) gens.get(0).nextString();
			db.BSMAQuery15(pair.get(0), 10, Long.parseLong(pair.get(1)) * HOUR
					* 12, HOUR * 12);
		}
	}

	private static class Query16Wrapper extends QueryWrapper {
		public Query16Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR;
			db.BSMAQuery16(uid, 10, time, HOUR * 12);
		}
	}

	private static class Query17Wrapper extends QueryWrapper {
		public Query17Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR;
			db.BSMAQuery17(uid, 10, time, HOUR * 12);
		}
	}

	private static class Query18Wrapper extends QueryWrapper {
		public Query18Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR;
			db.BSMAQuery18(uid, 10, time, HOUR * 12);
		}
	}

	private static class Query19Wrapper extends QueryWrapper {
		public Query19Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String uid = (String) gens.get(0).nextString();
			long time = Long.parseLong((String) gens.get(1).nextString())
					* HOUR;
			db.BSMAQuery19(uid, 10, time, HOUR * 12);
		}
	}

	private static class Query20Wrapper extends QueryWrapper {
		public Query20Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String event = (String) gens.get(0).nextString();
			db.BSMAQuery20(event);
		}
	}

	private static class Query21Wrapper extends QueryWrapper {
		public Query21Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String event = (String) gens.get(0).nextString();
			db.BSMAQuery21(event);
		}
	}

	private static class Query22Wrapper extends QueryWrapper {
		public Query22Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String event = (String) gens.get(0).nextString();
			db.BSMAQuery22(event);
		}
	}

	private static class Query23Wrapper extends QueryWrapper {
		public Query23Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String event = (String) gens.get(0).nextString();
			db.BSMAQuery23(event);
		}
	}

	private static class Query24Wrapper extends QueryWrapper {
		public Query24Wrapper(List<Generator> gens) {
			super(gens);
		}

		@Override
		public void doAnalysisQuery(DB db) {
			String event = (String) gens.get(0).nextString();
			db.BSMAQuery24(event);
		}
	}
}
