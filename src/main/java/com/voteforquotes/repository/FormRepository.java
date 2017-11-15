package com.voteforquotes.repository;

import java.util.List;

import com.voteforquotes.SortEnum;

public interface FormRepository {
	void createForm(String quote, int votes);
	void createUser(String username, String password, String userrole);
	String obtainUserRole(String username);
	List<FormDTO> obtainDataOrderBy(SortEnum sort,String orderBy, int page);
	Integer obtainVotes(String quote);
	void updateForm(String quote, int votes);
	int obtainNumberOfPages();
	String obtainPassword(String username);
}
