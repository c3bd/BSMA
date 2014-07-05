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
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery2(String userID, int returncount) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery3(String userID, int returncount) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery4(String userID1, String userID2) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery5(String userID1, String userID2) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery6(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery7(int returncount, String datetime, String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery8(int returncount, String userID) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery9(String userID, String tag, int returncount,
			String datetime, String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery10(int returncount, String datetime, String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery11(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery12(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery13(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery14(int returncount, String datetime, String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery15(String tag, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery16(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery17(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery18(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery19(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";

	}

	@Override
	public String BSMAQuery20(String eventID) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery21(String eventID) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery22(String mid) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery23(String eventID) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String BSMAQuery24(String eventID) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return "";
	}

}
