package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConfig {
	private static String DB_URL = "jdbc:mysql://localhost:3306/examination";
    private static String USER_NAME = "root";
    private static String PASSWORD = "lttsm42000";
    private static Connection conn;
 
    public static synchronized Connection getConnection() {
    	if (conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                System.out.println("connect successfully!");
            } catch (Exception ex) {
                System.out.println("connect failure!");
                ex.printStackTrace();
            }
    	}
    	
		return conn;
    }
}
