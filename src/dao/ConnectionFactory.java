package dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class ConnectionFactory {

	private static Connection conn;

	private ConnectionFactory()
	{
		conn = null;
	}

	private static Connection createConnection()
	{
		if(conn == null)
		{
			if (System.getProperty("RDS_HOSTNAME") != null) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String dbName = "morsetalk_db";
					String userName = System.getProperty("RDS_USERNAME");
					String password = System.getProperty("RDS_PASSWORD");
					String hostname = System.getProperty("RDS_HOSTNAME");
					String port = System.getProperty("RDS_PORT");
					
					String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
					conn = (Connection) DriverManager.getConnection(jdbcUrl);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (SQLException e) { e.printStackTrace();}
			}
		}

		return conn;
	}   

	public static Connection getConnection() {
		return createConnection();
	}

	public static void closeResources(ResultSet set, Statement statement) {
		if (set != null) {
			try {
				set.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
