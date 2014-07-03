package edu.ecnu.imc.bsma.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiveDBImpl extends DB {
	Connection conn = null;

	public HiveDBImpl() {

	}

	@Override
	public void init() throws DBException {
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver").newInstance();
			conn = DriverManager.getConnection(_p.getProperty("server"),
					_p.getProperty("user"), _p.getProperty("passwd"));
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	private static final String QUERY1 = "SELECT f1.uid, COUNT(f2.uid) num"
			+ "FROM"
			+ "(SELECT a.friendId AS uid"
			+ "FROM friendList a JOIN friendList b"
			+ "ON a.friendId = b.uid"
			+ "WHERE a.uid = b.friendId AND a.uid = '%s')  f JOIN friendList f2   ON (f2.uid = f.uid)"
			+ "JOIN friendList f1 ON (f1.friendId = f2.uid) "
			+ "WHERE  f1.friendID = f2.uid AND" + "f1.uid = f2.friendId AND"
			+ "f1.uid!='%1' AND" + "f1.uid!=f.uid" + "GROUP BY f1.uid"
			+ "ORDER BY num DESC ;";

	@Override
	public String BSMAQuery1(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY1, userID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getInt(2));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY2 = "SELECT  followers.uid, count(followers.uid) as num "
			+ "FROM(SELECT friendID FROM friendListWHERE uid = \"%s\") "
			+ " friends LEFT OUTER JOIN (SELECT f1.uid, f1.friendID "
			+ "FROM (SELECT friendIDFROM friendListWHERE uid = \"%1\")"
			+ " f2 JOIN friendList f1 ON (f1.friendID = f2.friendID )"
			+ "WHERE f1.uid != \"%1\") followers ON (followers.uid = friends.friendID )"
			+ "where friends.friendID is null "
			+ "GROUP BY followers.uid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery2(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY2, userID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getInt(2));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY3 = "SELECT f.friendID FROM "
			+ "(SELECT f.friendID FROM " + "friendList LEFT OUTER JOIN "
			+ "(SELECT friendID FROM friendlist WHERE uid = \"%s\") friends "
			+ "ON (friendlist.uid = friends.friendID) "
			+ "WHERE friends.friendID is not null) f LEFTER OUTER JOIN"
			+ " (SELECT friendID FROM friendList WHERE uid = \"%1\") friends "
			+ "ON (f.friendID = friends.friendID) WHERE f.friendID <> \"%1\" "
			+ "AND friends.friendID is null "
			+ "GROUP BY f.friendID ORDER BY COUNT(f.uid) DESC LIMIT 10;";

	@Override
	public String BSMAQuery3(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY3, userID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY4 = "SELECT DISTINCT f1.friendID "
			+ "FROM (SELECT friendIDFROM friendList WHERE uid = \"%s\")  f1 JOIN"
			+ "(SELECT friendID FROM friendList WHERE uid = \"%s\")  f2 "
			+ "ON(f1.friendID = f2.friendID);";

	@Override
	public String BSMAQuery4(String userID1, String userID2) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY4, userID1,
					userID2));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY5 = "SELECT uid FROM friendList LEFT OUTER"
			+ " JOIN (SELECT friendID FROM friendList AS a WHERE a.uid = \"%s\") "
			+ "friends ON (friendList.uid = friends.friendID) "
			+ "WHERE friendID = \"%s\"AND friends.friendID is not null;";

	@Override
	public String BSMAQuery5(String userID1, String userID2) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY5, userID1,
					userID2));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY6 = "SELECT microblog.uid FROM "
			+ "microblog JOIN mention ON(microblog.mid = mention.mid) "
			+ "JOIN (SELECT DISTINCT mention.uid AS uid FROM microblog "
			+ "JOIN mention ON (microblog.mid = mention.mid) "
			+ "WHERE  microblog.uid = \"%s\") AS x ON (mention.uid = x.uid)"
			+ " WHERE microblog.uid<> \"%1\" AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND "
			+ "DATE_ADD('%2',INTERVAL 1HOUR) "
			+ "GROUP BY microblog.uid ORDER BY COUNT(mention.uid) DESC LIMIT 10;";

	@Override
	public String BSMAQuery6(String userID, int returncount, String datetime,
			String timespan) {
		// TODO timespan
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY5, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY7 = "SELECT mention.uid "
			+ "FROM microblog JOIN mention ON (microblog.mid = mention.mid) "
			+ "WHERE microblog.time BETWEEN TO_DAYS('%s') "
			+ "AND DATE_ADD('%1',INTERVAL 1HOUR) "
			+ "GROUP BY mention.uid ORDER BY COUNT(*) DESC LIMIT 10;";

	@Override
	public String BSMAQuery7(int returncount, String datetime, String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY7,
					datetime, timespan));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY8 = "SELECT mid FROM microblog "
			+ "LEFT OUTER JOIN "
			+ "((SELECT friendID FROM friendList WHERE uid = \"%s\") "
			+ "UNION (SELECT b.friendID as friendID FROM "
			+ "friendList a JOIN friendList b ON ( a.friendID = b.uid) WHERE a.uid = \"%1\")) "
			+ "friends ON (microblog.uid in friends.friendID) "
			+ "WHERE friends.friendID is not null ORDER BY microblog.time DESC LIMIT 10;";

	@Override
	public String BSMAQuery8(int returncount, String userID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY8, userID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	@Override
	public String BSMAQuery9(String userID, String tag, int returncount,
			String datetime, String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery10(int returncount, String datetime, String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery11(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery12(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery13(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery14(int returncount, String datetime, String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery15(String tag, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery16(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery17(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery18(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String BSMAQuery19(String userID, int returncount, String datetime,
			String timespan) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws IOException {
		transform();
	}

	public static void transform() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"./doc/hiveql.txt"));
		String line = "";
		int i = 1;
		StringBuffer buf = new StringBuffer();
		Pattern pattern = Pattern.compile("Q[0-9]+");
		while ((line = br.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				if (!buf.toString().isEmpty()) {
					System.out.println("Query" + i);
					i++;
					System.out.println(buf.toString().trim());
					buf = new StringBuffer();
				}
			} else {
				buf.append(line.trim().replace("\"", "\\\""));
				buf.append(" ");
			}
		}
		System.out.println(buf.toString());
		br.close();
	}
}
