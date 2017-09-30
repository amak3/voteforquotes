package com.voteforquotes.repository;

public class FormDTO {
	private String quote;
	private int votes;
	
	public FormDTO(String quote, int votes) {
		super();
		this.quote = quote;
		this.votes = votes;
	}
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}

}
