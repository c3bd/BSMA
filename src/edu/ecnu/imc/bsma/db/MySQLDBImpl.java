package edu.ecnu.imc.bsma.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySQLDBImpl extends DB {
	Connection conn = null;

	public MySQLDBImpl() {

	}

	@Override
	public void init() throws DBException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(_p.getProperty("hserver",
					"jdbc:mysql://localhost:3306/bsmadata"), _p.getProperty(
					"huser", "root"), _p.getProperty("hpasswd", ""));
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public String BSMAQuery1(String userID, int returncount) {
		String sql = String.format(SQLTemplate.QUERY1, userID);
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			StringBuffer buf = new StringBuffer();
			while (result.next()) {
				buf.append(result.getString(1));
				buf.append(",");
				buf.append(result.getInt(2));
				buf.append(",");
			}
			return buf.toString();
		} catch (SQLException e) {
			System.err.println(sql);
			e.printStackTrace();
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

	public static void main(String[] args) throws DBException {
		MySQLDBImpl db = new MySQLDBImpl();
		db.setProperties(new Properties());
		db.init();
		System.out.println(db.BSMAQuery1("", 1));
	}
}
