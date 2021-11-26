package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import configuration.DatabaseConfig;
import entity.Examination;

public class ExaminationDao {
	private static ExaminationDao instance;
	
	public static ExaminationDao getInstance() {
		if (instance == null) {
			instance = new ExaminationDao();
		}
		return instance;
	}

	public List<Examination> getAllExamination() {
		List<Examination> examinations = new ArrayList<>();
		Connection conn = DatabaseConfig.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT ex.*, acc.name, COUNT(qe.id) question_amount\r\n"
					+ "FROM examinations ex\r\n"
					+ "JOIN accounts acc ON ex.created_by_id = acc.email\r\n"
					+ "JOIN questions qe ON ex.id = qe.examination_id\r\n"
					+ "GROUP BY qe.examination_id;");
			while(rs.next()) {
				int i = 1;
				Examination examination = new Examination(
						rs.getLong(i++),
						rs.getTimestamp(i++), 
						rs.getTimestamp(i++), 
						rs.getString(i++), 
						rs.getString(i++), 
						rs.getInt(i++), 
						rs.getString(i++));
				examination.setCreatedByAccountName(rs.getString(i++));
				examination.setQuestionAmount(rs.getInt(i++));
				examinations.add(examination);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return examinations;
	}
}
