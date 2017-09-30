package com.voteforquotes.repository;

import java.util.List;

public interface FormRepository {
	void createForm(String quote, int votes);
	List<FormDTO> obtainData();
	Integer obtainVotes(String quote);
	void updateForm(String quote, int votes);
}
