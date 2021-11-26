package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import configuration.DatabaseConfig;
import entity.Account;

public class AccountDao {
	private static AccountDao instance;
	
	public static AccountDao getInstance() {
		if (instance == null) {
			instance = new AccountDao();
		}
		return instance;
	}
	
	public boolean isAccountExisted(String email) {
		return getAccountByEmail(email) == null ? false : true;
	}
	
	public Account getAccountByEmail(String email) {
		Account account = null;
		Connection conn = DatabaseConfig.getConnection();
		PreparedStatement pst;
		try {
			pst = conn.prepareStatement("SELECT * FROM accounts WHERE email=?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				account = new Account(rs.getString(1), rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return account;
	}
	
	public boolean createAccount(Account account) {
		Connection conn = DatabaseConfig.getConnection();
		PreparedStatement pst;
		try {
			pst = conn.prepareStatement("INSERT INTO accounts(email, name, role) VALUES (?,?,?);");
			pst.setString(1, account.getEmail());
			pst.setString(2, account.getName());
			pst.setInt(3, account.getRole() == null ? 3 : account.getRole());
			int rs = pst.executeUpdate();
			if (rs == -1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
