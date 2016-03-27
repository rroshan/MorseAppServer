package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO
{	
	private PreparedStatement pstmt;
	private ResultSet set;

	public boolean insertRegistrationToken(String user_id, String token)
	{
		Connection conn = ConnectionFactory.getConnection();
		
		if(conn == null)
		{
			return false;
		}
		
		String sql;
		try {
			if(checkIfExists(user_id)) {
				sql = "update registration set token=? where user_id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, token);
				pstmt.setString(2, user_id);
				pstmt.executeUpdate();
			}
			else
			{
				sql = "insert into registration(user_id, token) values (?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user_id);
				pstmt.setString(2, token);

				pstmt.executeUpdate();
			}
		} catch(SQLException sqlex) {
			sqlex.printStackTrace();
			return false;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionFactory.closeResources(set, pstmt);
		}

		return true;
	}

	public String fetchRegistrationToken(String user_id)
	{
		Connection conn = ConnectionFactory.getConnection();
		String sql;
		String token = null;

		try {
			sql = "SELECT token from registration where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);

			set = pstmt.executeQuery();

			while(set.next()){
				//Retrieve by column name
				token = set.getString(1);
			}

			set.close();

		} catch(SQLException sqlex) {
			sqlex.printStackTrace();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionFactory.closeResources(set, pstmt);
		}

		return token;
	}

	public boolean checkIfExists(String user_id)
	{
		Connection conn = ConnectionFactory.getConnection();
		int count = 0;
		try {
			String sql = "select count(*) from registration where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);

			set = pstmt.executeQuery();

			while(set.next()) {
				count = set.getInt(1);
			}

			if(count > 0)
			{
				return true;
			}
		} catch(SQLException sqlex) {
			sqlex.printStackTrace();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionFactory.closeResources(set, pstmt);
		}

		return false;
	}
}
