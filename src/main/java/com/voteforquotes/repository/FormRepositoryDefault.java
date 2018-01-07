package com.voteforquotes.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.voteforquotes.DatabaseException;
import com.voteforquotes.SortEnum;

@Component
public class FormRepositoryDefault implements FormRepository{

	private static final RowMapper<FormDTO> FORM_MAPPER = new RowMapper<FormDTO>() {

		@Override
		public FormDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new FormDTO(rs.getString(1), rs.getInt(2));
		}
	};
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Override
	public void createForm(String quote) throws DatabaseException{
		try {
			jdbcTemplate.update("INSERT INTO Form(quote, date) VALUES (?, ?)", quote,
					Timestamp.valueOf(LocalDateTime.now()));
		} catch (RuntimeException e) {
			throw new DatabaseException();
		}
	}
	
	@Override
	public void createUser(String username, String password, String userrole){
		jdbcTemplate.update("INSERT INTO User(username, password, userrole) VALUES (?, ?, ?)", username, password, userrole);
	}
	
	@Override
	public String obtainPassword(String username){
		String password = jdbcTemplate.queryForObject("SELECT password from User where username = ?", new Object[] { username }, String.class);
		return password;
	}
	
	@Override
	public String obtainUserRole(String username) {
		String userRole = jdbcTemplate.queryForObject("SELECT userrole from User where username = ?", new Object[] { username }, String.class);
		return userRole;
	}

	@Override
	public List<Map<String, Object>> obtainDataForForm(int page, int userID) { 
		int offset = (page - 1) * 10;
		List<Map<String, Object>> data = jdbcTemplate.queryForList("SELECT quote FROM Form "
				+ "WHERE NOT EXISTS (SELECT * FROM Votes where Form.id = Votes.quoteID AND userID = ? )"
				+ "ORDER BY date DESC "
				+ "OFFSET ? FETCH NEXT 10 ROWS ONLY", userID, offset);
		return data;
	}
	
	@Override
	public List<FormDTO> obtainDataForResult(SortEnum sort, String orderBy, int page) { 
		int offset = (page - 1) * 10;
		List<FormDTO> data = jdbcTemplate.query("SELECT Form.quote as quote, SUM(Votes.score) as votes, Form.date as date from Form "
				+ "JOIN Votes ON Form.id = Votes.quoteID GROUP BY Form.quote, Form.date "
				+ "ORDER BY " + orderBy + " " + sort
				+ " OFFSET ? FETCH NEXT 10 ROWS ONLY", FORM_MAPPER, offset);
		return data;
	}
	
	@Override
	public Integer obtainQuoteID(String quote) {
		Integer quoteID = jdbcTemplate.queryForObject("SELECT id FROM Form WHERE quote = ?", new Object[] { quote }, Integer.class);
		return quoteID;
	}
	
	@Override
	public void insertScore(int quoteID, int score, int userID) {
		jdbcTemplate.update("INSERT INTO Votes(quoteID, score, userID) VALUES (?, ?, ?)", quoteID, score, userID);	
	}
	
	@Override
	public Integer obtainUserID(String username) {
		Integer userID = jdbcTemplate.queryForObject("SELECT id FROM User WHERE username = ?", new Object[] { username }, Integer.class);
		return userID;
	}
	

	@Override
	public Integer obtainVotes(String quote) {
		Integer votes = jdbcTemplate.queryForObject("SELECT votes FROM form WHERE quote = ?", new Object[] { quote }, Integer.class);
		return votes;
	}
	
	@Override
	public void updateForm(String quote, int votes) {
		jdbcTemplate.update("UPDATE Form SET votes = ? WHERE quote = ?", votes, quote);	
	}
	
	public int obtainNumberOfPagesForResult() {
		float countPages = jdbcTemplate.queryForObject("SELECT count(*) FROM Form",  Integer.class);
		float checkRest = countPages % 10;
		if (checkRest != 0) {
			countPages = countPages/10 + 1;
		}
		else {
			countPages = countPages/10;
		}
		int result = (int)countPages;
		return result;
	}

	public int obtainNumberOfPagesForForm(int userID) {
		float countPages = jdbcTemplate.queryForObject("SELECT count(quote) FROM Form "
				+ "WHERE NOT EXISTS (SELECT * FROM Votes where Form.id = Votes.quoteID AND userID = ? )", new Object[] { userID }, Integer.class);
		float checkRest = countPages % 10;
		if (checkRest != 0) {
			countPages = countPages/10 + 1;
		}
		else {
			countPages = countPages/10;
		}
		int result = (int)countPages;
		return result;
	}


}
