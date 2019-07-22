package com.geekbrains.cloud.server.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthHandler {
	public static Boolean login(String login, String password) {
		try {
			PreparedStatement query = DBHandler.getPStmt("SELECT id FROM users WHERE login = ? AND password = ?");

			query.setString(1, login);
			query.setString(2, password);

			return query.executeQuery().next();
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
