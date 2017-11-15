CREATE TABLE IF NOT EXISTS Form (id INTEGER IDENTITY PRIMARY KEY,
									quote varchar(255) UNIQUE,
									votes INTEGER,
									date TIMESTAMP);
									
CREATE TABLE IF NOT EXISTS User (id INTEGER IDENTITY PRIMARY KEY,
									username varchar(255) UNIQUE,
									password varchar(255),
									userrole varchar(255));									