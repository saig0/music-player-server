
# --- !Ups

CREATE TABLE music (
    id 				serial PRIMARY KEY,
    source 			varchar(255) NOT NULL,
	artist	 		varchar(255) NOT NULL,
	album		 	varchar(255) NOT NULL,
	title			varchar(255) NOT NULL
);

# --- !Downs

