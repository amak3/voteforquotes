package com.voteforquotes;

public enum SortOrderBy {
	DATE {
		@Override
		public String toSqlColumn() {
			return "date";
		}
	},
	VOTES {
		@Override
		public String toSqlColumn() {
			return "votes";			
		}
	};
	
	public abstract String toSqlColumn();

}
