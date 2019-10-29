-- name: upgrade
-- run all database upgrades

ALTER TABLE Sessions DROP COLUMN SES_USR_ID_FK;

ALTER TABLE SESSIONS
ADD COLUMN SES_USR_ID_FK integer REFERENCES USERS(USR_ID_PK);

DROP TABLE Location;

CREATE TABLE IF NOT EXISTS LOCATION (
       LOC_ID_PK varchar(10) PRIMARY KEY,
       LOC_Alias varchar(20) NOT NULL,
       LOC_State varchar(2)
);

-- yeet
