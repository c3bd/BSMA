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
			conn = DriverManager.getConnection(_p.getProperty("hserver"),
					_p.getProperty("huser"), _p.getProperty("hpasswd"));
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

	public static final String QUERY9 = "SELECT x.reuid,COUNT(*) "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid as mid, microblog.uid as reuid "
			+ "FROM microblog JOIN retweet ON (microblog.mid = retweet.remid)) x "
			+ "ON (microblog.mid = x.mid) WHERE microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND " + "DATE_ADD('%1', INTERVAL 1HOUR) "
			+ "GROUP BY x.reuid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery9(String userID, String tag, int returncount,
			String datetime, String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY8, userID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY10 = "SELECT x.reuid,COUNT(*) "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid as mid, microblog.uid as reuid "
			+ "FROM microblog JOIN"
			+ " retweet ON (microblog.mid = retweet.remid)) x "
			+ "ON (microblog.mid = x.mid) WHERE "
			+ "microblog.time BETWEEN TO_DAYS('%s') "
			+ "AND DATE_ADD('%1', INTERVAL 1HOUR) "
			+ "GROUP BY x.reuid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery10(int returncount, String datetime, String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY10,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY11 = "SELECT microblog.uid, COUNT(*) num "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid as mid,microblog.uid as reuid FROM microblog "
			+ "JOIN retweet ON (microblog.mid = retweet.remid)) x "
			+ "ON (microblog.mid = x.mid) WHERE x.reuid = \"%s\" "
			+ "AND microblog.time BETWEEN TO_DAYS('%s') "
			+ "AND DATE_ADD('%2',INTERVAL 1HOUR) "
			+ "GROUP BY microblog.uid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery11(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY11, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY12 = "SELECT x.remid, COUNT(*) num "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid AS mid,retweet.remid AS remid "
			+ "FROM microblog,retweet WHERE microblog.mid = retweet.remid) x "
			+ "ON (microblog.mid = x.mid ) LEFT OUTER JOIN "
			+ "((SELECT friendID FROM friendList WHERE uid = \"%s\") "
			+ "UNION (SELECT b.friendID as friendID "
			+ "FROM friendList a join friendList b "
			+ "WHERE a.uid = \"%1\" a.friendID = b.uid)) friends ON "
			+ " (microblog.uid = friends.friendID) WHERE "
			+ "friends.friendID is not null AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND" + " DATE_ADD('%1',INTERVAL 1HOUR) "
			+ "GROUP BY x.remid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery12(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY11, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY13 = "SELECT microblog.uid FROM microblog "
			+ "JOIN event ON (microblog.mid = event.mid) "
			+ "LEFT OUTER JOIN (SELECT DISTINCT tag FROM "
			+ "event,microblog WHERE microblog.mid = event.mid "
			+ "AND microblog.uid = \"%s\") atags ON (event.tag = atags.tag) "
			+ "LEFT OUTER JOIN (SELECT friendID FROM friendList "
			+ "WHERE uid = \"%1\") afriends ON (microblog.uid = afriends.uid)"
			+ " WHERE microblog.uid<> \"%1\" AND atags is not null"
			+ " AND afriends.uid is null AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND " + "DATE_ADD('%2', INTERVAL 1HOUR) "
			+ "GROUP BY microblog.uid ORDER BY COUNT(event.tag) DESC LIMIT 10;";

	@Override
	public String BSMAQuery13(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY13, userID,
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

	public static final String QUERY14 = "SELECT event.tag,COUNT(*) num "
			+ "FROM microblog JOIN event " + "ON (microblog.mid = event.mid) "
			+ "WHERE microblog.time BETWEEN " + "TO_DAYS('%s) AND "
			+ "DATE_ADD('%1', INTERVAL 1HOUR)"
			+ " GROUP BY event.tag ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery14(int returncount, String datetime, String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY14,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY15 = "SELECT microblog.uid "
			+ "FROM microblog JOIN event ON (microblog.mid = event.mid)"
			+ " WHERE event.tag=\"%s\" AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND " + "DATE_ADD('%2',INTERVAL 1HOUR)"
			+ " GROUP BY microblog.uid ORDER BY COUNT(*) DESC LIMIT 10;";

	@Override
	public String BSMAQuery15(String tag, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY15, tag,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY16 = "SELECT x.reuid,COUNT(*) num FROM "
			+ "microblog JOIN (SELECT retweet.mid as mid,"
			+ "microblog.uid as reuid FROM microblog "
			+ "JOIN retweet ON(microblog.mid = retweet.remid)) x "
			+ "ON ( microblog.mid = x.mid) LEFT OUTER JOIN"
			+ " (SELECT friendID FROM friendList WHERE uid = \"%s\") "
			+ "afriends ON (microblog.uid = afriends.friendID) "
			+ "WHERE afriends.friendID is not null AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND " + "DATE_ADD('%2’,INTERVAL 1HOUR) "
			+ "GROUP BY x.reuid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery16(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY16, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY17 = "SELECT mention.uid,COUNT(*) num"
			+ " FROM microblog JOIN mention ON "
			+ "(microblog.mid = mention.mid) LEFT OUTER JOIN "
			+ "(SELECT friendID FROM friendList WHERE uid = \"%s\") "
			+ "afriends ON (microblog.uid = afriends.friendID) "
			+ "WHERE afriends.friendID is not null AND microblog.time "
			+ "BETWEEN TO_DAYS('%s') AND " + "DATE_ADD('%2',INTERVAL 1HOUR) "
			+ "GROUP BY mention.uid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery17(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY17, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String Query18 = "SELECT mention.uid,COUNT(*) num "
			+ "FROM microblog JOIN mention ON (microblog.mid = mention.mid) "
			+ "LEFT OUTER JOIN (SELECT uid FROM friendList WHERE friendID = \"%s\") "
			+ "afans ON (microblog.uid = afans.uid) WHERE "
			+ "mention.uid=\"%1\" AND afans.uid is not null "
			+ "AND microblog.time BETWEEN TO_DAYS('%s')"
			+ " AND DATE_ADD('%2', INTERVAL 1HOUR) "
			+ "GROUP BY mention.uid ORDER BY num DESC LIMIT 10;";

	@Override
	public String BSMAQuery18(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(Query18, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY19 = "SELECT event.tag,COUNT(*) num "
			+ "FROM microblog JOIN event ON ( microblog.mid = event.mid) "
			+ "LEFT OUTER JOIN ((SELECT friendID FROM friendList WHERE uid = \"%s\") "
			+ "UNION (SELECT b.friendID as friendID "
			+ "FROM friendList a join friendList b WHERE"
			+ " a.uid = \"%1\" a.friendID = b.uid)) friends "
			+ "ON (microblog.uid = friends.friendID) WHERE "
			+ "microblog.uid<> \"%1\" AND friends.friendID is not null "
			+ "AND microblog.time BETWEEN TO_DAYS('%s') "
			+ "AND DATE_ADD('%2', INTERVAL 1HOUR)"
			+ " GROUP BY event.tag ORDER BY num DESC LIMIT 10; ";

	@Override
	public String BSMAQuery19(String userID, int returncount, String datetime,
			String timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(QUERY19, userID,
					datetime));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY20 = "SELECT time, count(*) RTTweet"
			+ " FROM Microblog JOIN Rel_Event_Microblog ON "
			+ "(Microblog. mid = Rel_Event_Microblog. mid) JOIN "
			+ "Retweet ON (Microblog. mid = Retweet. mid) "
			+ "where Rel_Event_Microblog.eventID = \"$s\""
			+ " and Retweet.reMid != -1 group by time";

	@Override
	public String BSMAQuery20(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String
					.format(QUERY20, eventID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY21 = "SELECT mood, time, count(*) RTTweet "
			+ "FROM Microblog JOIN Rel_Event_Microblog "
			+ "ON (Microblog. mid = Rel_Event_Microblog. mid) "
			+ "JOIN Rel_Mood_Microblog ON (Mood. mid = Rel_Mood_Microblog. mid) "
			+ "JOIN Mood ON (Rel_Mood_Microblog.moodID = Mood.moodID) "
			+ "where Rel_Event_Microblog.eventID = \"%s\" "
			+ "and Retweet.reMid != -1 group by mood,time";

	@Override
	public String BSMAQuery21(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String
					.format(QUERY21, eventID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
				buf.append(",");
				buf.append(result.getString(3));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY22 = "WITH RECURSIVE rtTree(id, rtMid) As( Select id , reMid From microblog Where id = $id$ UNION ALL Select id, rtMid From Retweet JOIN rtTree on Retweet.rtMid = rtTree.id ) Select id, rtMid, time from rtTree, Tweet where rtTree.rtMid = Tweet.mid.";

	@Override
	public String BSMAQuery22(String mid) {
		return "";
	}

	public static final String QUERY23 = "select city,gender, count(*) as RTTweet "
			+ "from Tweet JOIN TweetEventMapping"
			+ " ON (Tweet. mid = TweetEventMapping. mid) "
			+ "JOIN Location ON (Tweet.locationId= Location.locationId) "
			+ "JOIN User (Tweet.uid = User.uid)"
			+ " where TweetEventMapping.eventID = \"%s\" group by city, gender;";

	@Override
	public String BSMAQuery23(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String
					.format(QUERY23, eventID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
				buf.append(",");
				buf.append(result.getString(3));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
	}

	public static final String QUERY24 = "select friendsNumber, count(*) as userNumber "
			+ "from Tweet JOIN TweetEventMapping ON"
			+ " (Tweet. mid = TweetEventMapping. mid) "
			+ "JOIN User (Tweet.uid = User.uid) where"
			+ " TweetEventMapping.eventID = \"%s\""
			+ " group by friendsNumber  ";

	@Override
	public String BSMAQuery24(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String
					.format(QUERY24, eventID));
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getString(2));
			}
			return buf.toString();
		} catch (SQLException e) {

		}
		return "";
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
