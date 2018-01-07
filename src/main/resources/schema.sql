CREATE TABLE IF NOT EXISTS Form (id INTEGER IDENTITY PRIMARY KEY,
									quote varchar(255) UNIQUE,
									date TIMESTAMP);
									
CREATE TABLE IF NOT EXISTS User (id INTEGER IDENTITY PRIMARY KEY,
									username varchar(255) UNIQUE,
									password varchar(255),
									userrole varchar(255));
										
CREATE TABLE IF NOT EXISTS Votes (id INTEGER IDENTITY PRIMARY KEY,
									quoteID INTEGER,
									score INTEGER,
									userID INTEGER,
									FOREIGN KEY (quoteID) REFERENCES Form(id),
									FOREIGN KEY (userID) REFERENCES User(id));
									
			
			