package edu.ecnu.imc.bsma.db;

/**
 * 所有的查询实现都只是将当前线程sleep
 * 
 * @author xiafan
 * 
 */
public class TestDBImpl extends DB {
	int sleepTime = 30;

	@Override
	public String BSMAQuery1(String userID, int returncount) {
		System.out.println(String.format(HiveQLTemplate.QUERY1, userID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery2(String userID, int returncount) {
		System.out.println(String.format(HiveQLTemplate.QUERY2, userID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery3(String userID, int returncount) {
		System.out.println(String.format(HiveQLTemplate.QUERY3, userID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery4(String userID1, String userID2) {
		System.out.println(String.format(HiveQLTemplate.QUERY4, userID1,
				userID2));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery5(String userID1, String userID2) {
		System.out.println(String.format(HiveQLTemplate.QUERY5, userID1,
				userID2));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery6(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY6, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery7(int returncount, long datetime, long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY7, datetime,
				datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery8(int returncount, String userID) {
		System.out.println(String.format(HiveQLTemplate.QUERY8, userID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery9(String userID, String tag, int returncount,
			long datetime, long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY9, userID, tag,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery10(int returncount, long datetime, long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY10, datetime,
				datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery11(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY11, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery12(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY12, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery13(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY13, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery14(int returncount, long datetime, long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY14, datetime,
				datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery15(String tag, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY15, tag, datetime,
				datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery16(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY16, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery17(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY17, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery18(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY18, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery19(String userID, int returncount, long datetime,
			long timespan) {
		System.out.println(String.format(HiveQLTemplate.QUERY19, userID,
				datetime, datetime + timespan));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery20(String eventID) {
		System.out.println(String.format(HiveQLTemplate.QUERY20, eventID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery21(String eventID) {
		System.out.println(String.format(HiveQLTemplate.QUERY21, eventID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery22(String mid) {
		System.out.println(String.format(HiveQLTemplate.QUERY22, mid));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery23(String eventID) {
		System.out.println(String.format(HiveQLTemplate.QUERY23, eventID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery24(String eventID) {
		System.out.println(String.format(HiveQLTemplate.QUERY24, eventID));
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

}
