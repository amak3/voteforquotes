package com.voteforquotes.repository;

import java.util.List;

import com.voteforquotes.SortEnum;

public interface FormRepository {
	void createForm(String quote, int votes);
	List<FormDTO> obtainDataOrderBy(SortEnum sort,String orderBy, int page);
	Integer obtainVotes(String quote);
	void updateForm(String quote, int votes);
	int obtainNumberOfPages();
}
