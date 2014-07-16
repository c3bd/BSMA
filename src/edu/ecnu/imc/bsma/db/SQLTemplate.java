package edu.ecnu.imc.bsma.db;

public class SQLTemplate {
	public static final String QUERY1 = "SELECT f1.uid, COUNT(f2.uid) as num FROM "
			+ "(SELECT a.followeeID AS uid "
			+ "FROM friendList as a JOIN friendList as  b "
			+ "ON (a.followeeID = b.uid) "
			+ "WHERE a.uid = b.followeeID AND a.uid = '%s') as f JOIN friendList as f2 ON (f2.uid = f.uid) "
			+ "JOIN friendList as f1 ON (f1.followeeID = f2.uid) "
			+ "WHERE  f1.followeeID = f2.uid AND "
			+ "f1.uid = f2.followeeID AND "
			+ "f1.uid!='%1$s' AND "
			+ "f1.uid!=f.uid " + "GROUP BY f1.uid " + "ORDER BY num DESC";

	public static final String QUERY2 = "SELECT  followers.uid, count(followers.uid) as num "
			+ "FROM(SELECT followeeID FROM friendListWHERE uid = \"%s\") "
			+ " friends LEFT OUTER JOIN (SELECT f1.uid, f1.followeeID "
			+ "FROM (SELECT followeeIDFROM friendListWHERE uid = \"%1$s\")"
			+ " f2 JOIN friendList f1 ON (f1.followeeID = f2.followeeID )"
			+ "WHERE f1.uid != \"%1$s\") followers ON (followers.uid = friends.followeeID )"
			+ "where friends.followeeID is null "
			+ "GROUP BY followers.uid ORDER BY num DESC LIMIT 10;";

	public static final String QUERY3 = "SELECT f.followeeID FROM "
			+ "(SELECT f.followeeID FROM "
			+ "friendList LEFT OUTER JOIN "
			+ "(SELECT followeeID FROM friendlist WHERE uid = \"%s\") friends "
			+ "ON (friendlist.uid = friends.followeeID) "
			+ "WHERE friends.followeeID is not null) f LEFTER OUTER JOIN"
			+ " (SELECT followeeID FROM friendList WHERE uid = \"%1$s\") friends "
			+ "ON (f.followeeID = friends.followeeID) WHERE f.followeeID <> \"%1$s\" "
			+ "AND friends.followeeID is null "
			+ "GROUP BY f.followeeID ORDER BY COUNT(f.uid) DESC LIMIT 10;";

	public static final String QUERY4 = "SELECT DISTINCT f1.followeeID "
			+ "FROM (SELECT followeeIDFROM friendList WHERE uid = \"%s\")  f1 JOIN "
			+ "(SELECT followeeID FROM friendList WHERE uid = \"%s\")  f2 "
			+ "ON(f1.followeeID = f2.followeeID);";

	public static final String QUERY5 = "SELECT uid FROM friendList LEFT OUTER"
			+ " JOIN (SELECT followeeID FROM friendList AS a WHERE a.uid = \"%s\") "
			+ "friends ON (friendList.uid = friends.followeeID) "
			+ "WHERE followeeID = \"%s\"AND friends.followeeID is not null;";

	public static final String QUERY6 = "SELECT microblog.uid FROM "
			+ "microblog JOIN mention ON(microblog.mid = mention.mid) "
			+ "JOIN (SELECT DISTINCT mention.uid AS uid FROM microblog "
			+ "JOIN mention ON (microblog.mid = mention.mid) "
			+ "WHERE  microblog.uid = \"%s\") AS x ON (mention.uid = x.uid)"
			+ " WHERE microblog.uid<> \"%1$s\" AND "
			+ "microblog.time >= %d AND microblog.time <= %d "
			+ "GROUP BY microblog.uid ORDER BY COUNT(mention.uid) DESC LIMIT 10;";

	public static final String QUERY7 = "SELECT mention.uid FROM "
			+ " microblog JOIN mention ON " + "(microblog.mid = mention.mid) "
			+ "microblog.time >= %d " + "AND microblog.time <= %d "
			+ "GROUP BY mention.uid ORDER BY COUNT(*) DESC LIMIT 10;";

	public static final String QUERY8 = "SELECT mid FROM microblog "
			+ "LEFT OUTER JOIN "
			+ "((SELECT followeeID FROM friendList WHERE uid = \"%s\") "
			+ "UNION (SELECT b.followeeID as followeeID FROM "
			+ "friendList a JOIN friendList b ON ( a.followeeID = b.uid) WHERE a.uid = \"%1$s\")) "
			+ "friends ON (microblog.uid in friends.followeeID) "
			+ "WHERE friends.followeeID is not null ORDER BY microblog.time DESC LIMIT 10;";

	public static final String QUERY9 = "SELECT microblog.uid FROM microblog "
			+ "JOIN rel_event_microblog "
			+ "ON (microblog.mid = rel_event_microblog.mid) "
			+ "LEFT OUTER JOIN ((SELECT followeeID FROM "
			+ "friendList WHERE uid = \"%s\") UNION "
			+ "(SELECT b.followeeID as followeeID FROM "
			+ "friendList a join friendList b WHERE "
			+ " a.uid = \"%1$s\" a.followeeID = b.uid)) "
			+ "friends on (microblog.uid= friends.followeeID) "
			+ " WHERE rel_event_microblog.eventID = \"%s\" AND "
			+ " friends.followeeID is not null AND "
			+ "microblog.time >= %d AND" + " microblog.time <= %d "
			+ "GROUP BY microblog.uid ORDER BY COUNT(*) DESC LIMIT 10;";

	public static final String QUERY10 = "SELECT x.reuid,COUNT(*) FROM "
			+ "microblog JOIN (SELECT retweet.mid as mid, microblog.uid "
			+ "as reuid FROM microblog JOIN retweet ON "
			+ "(microblog.mid = retweet.remid)) x ON "
			+ "(microblog.mid = x.mid) WHERE " + "microblog.time >= %d AND "
			+ "microblog.time <= %d "
			+ "GROUP BY x.reuid ORDER BY num DESC LIMIT 10;";

	public static final String QUERY11 = "SELECT microblog.uid, COUNT(*) num "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid as mid,microblog.uid as reuid FROM microblog "
			+ "JOIN retweet ON (microblog.mid = retweet.remid)) x "
			+ "ON (microblog.mid = x.mid) WHERE x.reuid = \"%s\" "
			+ "microblog.time >= %d AND microblog.time <= %d "
			+ "GROUP BY microblog.uid ORDER BY num DESC LIMIT 10;";

	public static final String QUERY12 = "SELECT x.remid, COUNT(*) num "
			+ "FROM microblog JOIN "
			+ "(SELECT retweet.mid AS mid,retweet.remid AS remid "
			+ "FROM microblog JOIN retweet "
			+ " ON microblog.mid = retweet.remid) x "
			+ "ON (microblog.mid = x.mid ) JOIN "
			+ "((SELECT followeeID FROM friendList WHERE uid = \"%s\") "
			+ " UNION (SELECT b.followeeID as followeeID "
			+ "FROM friendList a join friendList b "
			+ " WHERE a.uid = \"%1$s\" a.followeeID = b.uid)) "
			+ "friends ON  (microblog.uid = friends.followeeID) "
			+ "JOIN WHERE microblog.time >= %d " + "AND microblog.time <= %d "
			+ "GROUP BY x.remid ORDER BY  COUNT(*) DESC LIMIT 10;";

	public static final String QUERY13 = "SELECT microblog.uid FROM microblog "
			+ "JOIN rel_event_microblog ON "
			+ "(microblog.mid = rel_event_microblog.mid) "
			+ "JOIN (SELECT DISTINCT eventID FROM rel_event_microblog "
			+ " JOIN microblog ON (microblog.mid = rel_event_microblog.mid) "
			+ " WHERE microblog.uid = \"%s\") atags ON "
			+ "(rel_event_microblog.eventID = atags.eventID) "
			+ " LEFT OUTER JOIN (SELECT followeeID FROM "
			+ "friendList WHERE uid = \"%1$s\") afriends ON "
			+ "(microblog.uid = afriends.uid) WHERE "
			+ "microblog.uid<> \"%1$s\" AND afriends.uid is null "
			+ "AND microblog.time >= %d AND "
			+ "microblog.time <= %d GROUP BY microblog.uid ORDER BY "
			+ "COUNT(rel_event_microblog.id)DESC LIMIT 10;";

	public static final String QUERY14 = "SELECT event.tag,COUNT(*) num "
			+ "FROM microblog JOIN rel_event_microblog ON "
			+ "(microblog.mid = rel_event_microblog.mid) "
			+ "JOIN event ON (rel_event_microblog.eventID = event.eventID) "
			+ "WHERE microblog.time >= %d AND "
			+ "microblog.time <= %d GROUP BY event.tag "
			+ " ORDER BY num DESC LIMIT 10;";

	public static final String QUERY15 = "SELECT microblog.uid FROM "
			+ "microblog JOIN rel_event_microblog ON "
			+ "(microblog.mid = rel_event_microblog.mid) "
			+ "JOIN event ON (rel_event_microblog.eventID = event.eventID) "
			+ "WHERE event.tag=\"%s\" AND microblog.time >= %d "
			+ "AND microblog.time <= %d GROUP BY microblog.uid "
			+ "ORDER BY COUNT(*)DESC LIMIT 10;";

	public static final String QUERY16 = "SELECT x.reuid,COUNT(*) num FROM "
			+ "microblog JOIN (SELECT retweet.mid as mid,microblog.uid as reuid "
			+ "FROM microblog JOIN retweet ON(microblog.mid = retweet.remid)) x "
			+ " ON ( microblog.mid = x.mid) LEFT OUTER JOIN "
			+ "(SELECT followeeID FROM friendList WHERE uid = \"%s\") "
			+ "afriends ON (microblog.uid = afriends.followeeID) "
			+ " WHERE afriends.followeeID is not null AND "
			+ "microblog.time >= %d AND microblog.time <= %d "
			+ "GROUP BY x.reuid ORDER BY num DESC LIMIT 10;";

	public static final String QUERY17 = "SELECT mention.uid,COUNT(*) num "
			+ "FROM microblog JOIN mention ON "
			+ "(microblog.mid = mention.mid) LEFT OUTER JOIN "
			+ "(SELECT followeeID FROM friendList WHERE uid = \"%s\") "
			+ "afriends ON (mention.uid = afriends.followeeID) "
			+ "WHERE microblog.time >= %d AND "
			+ "microblog.time <= %d GROUP BY mention.uid "
			+ "ORDER BY num DESC LIMIT 10;";

	public static final String QUERY18 = "SELECT mention.uid,COUNT(*) num "
			+ "FROM microblog JOIN mention ON (microblog.mid = mention.mid) "
			+ " JOIN (SELECT uid FROM friendList WHERE followeeID = \"%s\") "
			+ "afans ON (microblog.uid = afans.uid) WHERE mention.uid=\"%1$s\" "
			+ " AND microblog.time >= %d AND " + "microblog.time <= %d "
			+ "GROUP BY mention.uid ORDER BY num DESC LIMIT 10;";

	public static final String QUERY19 = "SELECT event.tag,COUNT(*) num FROM microblog"
			+ " JOIN event ON ( microblog.mid = event.mid) "
			+ "LEFT OUTER JOIN ((SELECT followeeID FROM friendList "
			+ "WHERE uid = \"%s\") UNION (SELECT b.followeeID as followeeID "
			+ "FROM friendList a join friendList b WHERE a.uid = \"%1$s\" "
			+ " a.followeeID = b.uid)) friends ON "
			+ "(microblog.uid = friends.followeeID) WHERE "
			+ " microblog.uid<> \"%1$s\" AND friends.followeeID is not null "
			+ "AND microblog.time >= %d "
			+ "AND microblog.time <= %d "
			+ "GROUP BY event.tag ORDER BY num DESC LIMIT 10;";

	public static final String QUERY20 = "SELECT time, count(*) RTTweet "
			+ " FROM Microblog JOIN Rel_Event_Microblog ON "
			+ "(Microblog. mid = Rel_Event_Microblog. mid) JOIN "
			+ "Retweet ON (Microblog. mid = Retweet. mid) "
			+ "where Rel_Event_Microblog.eventID = \"$s\" "
			+ " and Retweet.reMid != -1 group by time";

	public static final String QUERY21 = "SELECT mood, time, count(*) RTTweet "
			+ "FROM Microblog JOIN Rel_Event_Microblog "
			+ "ON (Microblog. mid = Rel_Event_Microblog. mid) "
			+ "JOIN Rel_Mood_Microblog ON (Mood. mid = Rel_Mood_Microblog. mid) "
			+ "JOIN Mood ON (Rel_Mood_Microblog.moodID = Mood.moodID) "
			+ "where Rel_Event_Microblog.eventID = \"%s\" "
			+ "and Retweet.reMid != -1 group by mood,time";

	public static final String QUERY22 = "WITH RECURSIVE rtTree(id, rtMid) As( Select id ,"
			+ " reMid From microblog Where id = $id$ UNION ALL Select id, "
			+ "rtMid From Retweet JOIN rtTree on Retweet.rtMid = rtTree.id ) "
			+ "Select id, rtMid, time from rtTree, "
			+ "Tweet where rtTree.rtMid = Tweet.mid.";

	public static final String QUERY23 = "select city,gender, count(*) as RTTweet "
			+ "from Tweet JOIN TweetEventMapping "
			+ " ON (Tweet. mid = TweetEventMapping. mid) "
			+ "JOIN Location ON (Tweet.locationId= Location.locationId) "
			+ "JOIN User (Tweet.uid = User.uid) "
			+ " where TweetEventMapping.eventID = \"%s\" group by city, gender;";

	public static final String QUERY24 = "select friendsNumber, count(*) as userNumber "
			+ "from Tweet JOIN TweetEventMapping ON "
			+ " (Tweet. mid = TweetEventMapping. mid) "
			+ "JOIN User (Tweet.uid = User.uid) where "
			+ " TweetEventMapping.eventID = \"%s\" "
			+ " group by friendsNumber  ";

}
