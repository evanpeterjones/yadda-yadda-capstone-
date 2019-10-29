-- :name upgrade
-- :command :execute
-- :doc run all database upgrades
DO $$
BEGIN

ALTER TABLE Sessions DROP COLUMN SES_USR_ID_FK;

ALTER TABLE SESSIONS
ADD COLUMN SES_USR_ID_FK integer REFERENCES USERS(USR_ID_PK);

DROP TABLE Location;

CREATE TABLE IF NOT EXISTS LOCATION (
       LOC_ID varchar(10) PRIMARY KEY,
       LOC_ID_PK serial,
       LOC_Alias varchar(20) NOT NULL,
       LOC_State varchar(2)
);

INSERT INTO Location (LOC_ID, LOC_ALIAS, LOC_STATE)
VALUES ('28607', 'Boone', 'NC');

END $$;
-- yeet
