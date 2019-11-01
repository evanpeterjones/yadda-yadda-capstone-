-- :name upgrade
-- :doc run all database upgrades
DO $$
BEGIN

-- UPGRADE 1 --
IF (:version < 1) THEN 
ALTER TABLE Posts ALTER COLUMN pst_time TYPE timestampz;
ALTER TABLE Posts DROP COLUMN pst_shorturl;

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

SELECT F_DBVERSION(1);

END IF;


-- UPGRADE 2 --
IF (:version < 2) THEN

   ALTER TABLE POSTS DROP COLUMN PST_LOC_FK;
   ALTER TABLE POSTS ADD COLUMN PST_LOC_FK INT;

   ALTER TABLE SESSIONS DROP COLUMN SES_LOC_FK;
   ALTER TABLE SESSIONS ADD COLUMN SES_LOC_FK INT;

   SELECT F_DBVERSION(2);

END IF;


END $$;
-- yeet
