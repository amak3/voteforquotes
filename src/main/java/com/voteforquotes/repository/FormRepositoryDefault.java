package com.voteforquotes.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

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
	public void createForm(String quote, int votes){
		jdbcTemplate.update("INSERT INTO Form(quote, votes, date) VALUES (?, ?, ?)", quote, votes, Timestamp.valueOf(LocalDateTime.now()));
	}

	@Override
	public List<FormDTO> obtainDataOrderBy(SortEnum sort, String orderBy, int page) { 
		int offset = (page - 1) * 10;
		List<FormDTO> data = jdbcTemplate.query("SELECT quote, votes FROM form ORDER By " + orderBy + " "+ sort + " OFFSET ? FETCH NEXT 10 ROWS ONLY", FORM_MAPPER, offset);
		return data;
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
	
	public int obtainNumberOfPages() {
		float countPages = jdbcTemplate.queryForObject("SELECT count(*) FROM form",  Integer.class);
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
