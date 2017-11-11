CREATE TABLE IF NOT EXISTS Form (id INTEGER IDENTITY PRIMARY KEY,
									quote varchar(255) UNIQUE,
									votes INTEGER,
									date TIMESTAMP);