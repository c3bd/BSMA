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
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver")
					.newInstance();
			conn = DriverManager.getConnection(_p.getProperty("hserver"),
					_p.getProperty("huser", ""), _p.getProperty("hpasswd", ""));
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public String BSMAQuery1(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY1, userID));
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

	@Override
	public String BSMAQuery2(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY2, userID));
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

	@Override
	public String BSMAQuery3(String userID, int returncount) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY3, userID));
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
	public String BSMAQuery4(String userID1, String userID2) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY4, userID1, userID2));
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
	public String BSMAQuery5(String userID1, String userID2) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY5, userID1, userID2));
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
	public String BSMAQuery6(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY6, userID, datetime, datetime
							+ timespan));
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
	public String BSMAQuery7(int returncount, long datetime, long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY7, datetime, datetime + timespan));
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
	public String BSMAQuery8(int returncount, String userID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY8, userID));
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
			long datetime, long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY9, userID, tag, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery10(int returncount, long datetime, long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY10, datetime, datetime + timespan));
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

	@Override
	public String BSMAQuery11(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY11, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery12(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY12, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery13(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY13, userID, datetime, datetime
							+ timespan));
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
	public String BSMAQuery14(int returncount, long datetime, long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY14, datetime, datetime + timespan));
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

	@Override
	public String BSMAQuery15(String tag, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt
					.executeQuery(String.format(HiveQLTemplate.QUERY15, tag,
							datetime, datetime + timespan));
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

	@Override
	public String BSMAQuery16(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY16, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery17(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY17, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery18(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY18, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery19(String userID, int returncount, long datetime,
			long timespan) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY19, userID, datetime, datetime
							+ timespan));
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

	@Override
	public String BSMAQuery20(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY20, eventID));
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

	@Override
	public String BSMAQuery21(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY21, eventID));
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

	@Override
	public String BSMAQuery22(String mid) {
		return "";
	}

	@Override
	public String BSMAQuery23(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY23, eventID));
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

	@Override
	public String BSMAQuery24(String eventID) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(String.format(
					HiveQLTemplate.QUERY24, eventID));
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
				"./doc/hiveql.template"));
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
