package demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import configuration.DatabaseConfig;

public class TestDB {
	public static void main(String[] args) throws SQLException {
		Connection conn = DatabaseConfig.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE email='huyviet2582000@gmail.com'");
		while(rs.next()) {
			System.out.println(rs.getString(1) + ", " + rs.getInt(2));
		}
	}
}
