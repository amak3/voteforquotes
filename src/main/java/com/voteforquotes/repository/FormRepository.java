package com.voteforquotes.repository;

import java.util.List;
import java.util.Map;

import com.voteforquotes.DatabaseException;
import com.voteforquotes.SortEnum;

public interface FormRepository {
	void createForm(String quote) throws DatabaseException;
	void createUser(String username, String password, String userrole);
	String obtainUserRole(String username);
	List<Map<String, Object>> obtainDataForForm(int page, int userID);
	Integer obtainQuoteID(String quote);
	void insertScore(int quoteID, int score, int userID);
	Integer obtainUserID(String username);
	List<FormDTO> obtainDataForResult(SortEnum sort,String orderBy, int page);
	Integer obtainVotes(String quote);
	void updateForm(String quote, int votes);
	int obtainNumberOfPagesForResult();
	int obtainNumberOfPagesForForm(int userID);
	String obtainPassword(String username);
}
