package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import configuration.DatabaseConfig;
import entity.Choice;
import entity.History;
import entity.Question;

public class QuetionDao {
	private static QuetionDao instance;
	
	public static QuetionDao getInstance() {
		if (instance == null) {
			instance = new QuetionDao();
		}
		return instance;
	}
	
	public boolean saveNewRecord(History history) {
		Connection conn = DatabaseConfig.getConnection();
		try {
			PreparedStatement pst = conn.prepareStatement(
					"INSERT INTO histories(correctAnswerAmount, totalQuestionAmount, account_id, examination_id)\r\n"
					+ "VALUES (?, ?, ?, ?);");
			pst.setInt(1, history.getCorrectAnswerAmount());
			pst.setInt(2, history.getTotalQuestionAmount());
			pst.setString(3, history.getAccountId());
			pst.setLong(4, history.getExaminationId());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public List<Question> getQuestionsByExamId(Long examId) {
		List<Question> questions = new ArrayList<>();
		Connection conn = DatabaseConfig.getConnection();
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT *\r\n"
					+ "FROM questions qs\r\n"
					+ "WHERE qs.examination_id = ?;");
			pst.setLong(1, examId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int i = 1;
				Question question = new Question(
						rs.getLong(i++),
						rs.getString(i++), 
						rs.getLong(i++));
				
				List<Choice> choices = new ArrayList<>();
				Long questionId = question.getId();
				pst = conn.prepareStatement("SELECT *\r\n"
						+ "FROM choices\r\n"
						+ "WHERE question_id = ?;");
				pst.setLong(1, questionId);
				ResultSet rs2 = pst.executeQuery();
				while (rs2.next()) {
					Choice choice = new Choice(
							rs2.getLong(1),
							rs2.getString(2),
							rs2.getBoolean(3),
							rs2.getLong(4));
					choices.add(choice);
				}
				
				question.setChoices(choices);
				questions.add(question);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		questions.forEach(System.out::println);
		return questions;
	}
}
