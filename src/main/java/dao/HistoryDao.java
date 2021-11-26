package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import configuration.DatabaseConfig;
import entity.Examination;
import entity.History;

public class HistoryDao {
	private static HistoryDao instance;
	
	public static HistoryDao getInstance() {
		if (instance == null) {
			instance = new HistoryDao();
		}
		return instance;
	}

	public List<History> getHistoryByAccount(String accountEmail) {
		List<History> histories = new ArrayList<>();
		Connection conn = DatabaseConfig.getConnection();
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT h.*, ex.*, acc.name \r\n"
					+ "FROM histories h\r\n"
					+ "JOIN examinations ex ON ex.id = h.examination_id\r\n"
					+ "JOIN accounts acc ON ex.created_by_id = acc.email\r\n"
					+ "WHERE account_id=?\r\n"
					+ "ORDER BY h.created_at DESC;");
			pst.setString(1, accountEmail);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int i = 1;
				History history = new History(rs.getLong(i++),
											rs.getInt(i++),
											rs.getInt(i++),
											rs.getString(i++),
											rs.getLong(i++),
											rs.getTimestamp(i++));
				Examination examination = new Examination(rs.getLong(i++),
														rs.getTimestamp(i++),
														rs.getTimestamp(i++),
														rs.getString(i++),
														rs.getString(i++),
														rs.getInt(i++),
														rs.getString(i++));
				examination.setCreatedByAccountName(rs.getString(i++));
				history.setExamination(examination);
				histories.add(history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return histories;
	}
}
