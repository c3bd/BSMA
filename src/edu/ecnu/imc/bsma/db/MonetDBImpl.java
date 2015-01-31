package edu.ecnu.imc.bsma.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MonetDBImpl extends DB {
	Connection conn = null;

	public MonetDBImpl() {

	}

	@Override
	public void init() throws DBException {
		try {
			Class.forName("nl.cwi.monetdb.jdbc.MonetDriver").newInstance();
			conn = DriverManager.getConnection(
					_p.getProperty("hserver", "jdbc:monetdb://127.0.0.1/bsma"),
					_p.getProperty("huser", "monetdb"),
					_p.getProperty("hpasswd", "monetdb"));
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	@Override
	public String BSMAQuery1(String userID, int returncount) {
		String sql = String.format(MySQLQueryTemplate.QUERY1, userID, returncount);
		display(1, sql);
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
					MySQLQueryTemplate.QUERY2, userID, returncount));
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
					MySQLQueryTemplate.QUERY3, userID, returncount));
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
					MySQLQueryTemplate.QUERY4, userID1, userID2));
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
					MySQLQueryTemplate.QUERY5, userID1, userID2));
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
					MySQLQueryTemplate.QUERY6, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY7, datetime, datetime + timespan));
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
					MySQLQueryTemplate.QUERY8, userID));
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
					MySQLQueryTemplate.QUERY9, userID, tag, datetime, datetime
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
					MySQLQueryTemplate.QUERY10, datetime, datetime + timespan));
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
					MySQLQueryTemplate.QUERY11, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY12, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY13, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY14, datetime, datetime + timespan));
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
					.executeQuery(String.format(MySQLQueryTemplate.QUERY15, tag,
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
					MySQLQueryTemplate.QUERY16, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY17, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY18, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY19, userID, datetime, datetime
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
					MySQLQueryTemplate.QUERY20, eventID));
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
					MySQLQueryTemplate.QUERY21, eventID));
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
					MySQLQueryTemplate.QUERY23, eventID));
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
					MySQLQueryTemplate.QUERY24, eventID));
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
		MonetDBImpl db = new MonetDBImpl();
		db.setProperties(new Properties());
		db.init();
		System.out.println(db.BSMAQuery1("", 1));
	}
}
