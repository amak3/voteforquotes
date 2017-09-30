package com.voteforquotes.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


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
		jdbcTemplate.update("INSERT INTO Form(quote, votes) VALUES (?, ?)", quote, votes);
	}


	@Override
	public List<FormDTO> obtainData() {
		List<FormDTO> data = jdbcTemplate.query("SELECT quote, votes FROM form", FORM_MAPPER);
		return data;
	}

}
