package com.geekbrains.cloud.server.Util;

import java.sql.*;

public class DBHandler {
	private static Connection connection = null;

	private DBHandler() {}

	public static Connection connect() throws SQLException {
		if(connection == null)
			connection = DriverManager.getConnection("jdbc:sqlite:geekcloud.db");

		return connection;
	}

	public static PreparedStatement getPStmt(String sql) throws SQLException {
		return connect().prepareStatement(sql);
	}

	public static Statement getStmt() throws SQLException {
		return connect().createStatement();
	}

	public static void disconnect() {
		try {
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
