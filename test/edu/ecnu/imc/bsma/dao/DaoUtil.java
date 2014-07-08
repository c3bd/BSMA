package edu.ecnu.imc.bsma.dao;

import java.sql.SQLException;
import java.util.Properties;

public class DaoUtil {
	public static Dao getTestDao() {
		Properties props = new Properties();
		props.setProperty("report_server", "jdbc:mysql://localhost:3306/bsma");
		props.setProperty("report_user", "root");
		props.setProperty("report_passwd", "Hadoop123");
		Dao dao = new Dao(props);

		return dao;
	}

	public static void emptyDB() throws SQLException {
		Properties props = new Properties();
		props.setProperty("report_server", "jdbc:mysql://localhost:3306/bsma");
		props.setProperty("report_user", "root");
		props.setProperty("report_passwd", "Hadoop123");
		Dao dao = new Dao(props);
		dao.clear();
	}

	public static void main(String[] args) throws SQLException {
		emptyDB();
	}
}
