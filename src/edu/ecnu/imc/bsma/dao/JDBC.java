package edu.ecnu.imc.bsma.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
	Connection con = null; // 定义一个MYSQL链接对象

	public JDBC(String server, String user, String passwd)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); // MYSQL驱动
		// con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
		// "root", "root"); // 链接本地MYSQL
		con = DriverManager.getConnection(server, user, passwd); // 链接本地MYSQL
	}

	public Connection getCon() {
		return con;
	}

	public void putCon(Connection con) {

	}
}
