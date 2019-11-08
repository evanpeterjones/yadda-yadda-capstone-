/*
*  script to initialize database tables
*/

CREATE TABLE IF NOT EXISTS USERS (
       USR_ID_PK SERIAL PRIMARY KEY,
       USR_Username VARCHAR (50) UNIQUE, -- if these can be null then the client will have to generate user info
       USR_Ses_FK VARCHAR(30) REFERENCES SESSIONS (SES_ID_PK)
       USR_Email VARCHAR (100) UNIQUE NOT NULL,
       USR_CreatedOn DATE NOT NULL,
       USR_LastLogin DATE,
       USR_Validated BOOLEAN,
);

CREATE TABLE IF NOT EXISTS OLD_PASSWORDS (
      OPD_Hash VARCHAR(64),
      OPD_USR_FK INTEGER REFERENCES USERS(USR_ID_PK) ON DELETE CASCADE,
      PRIMARY KEY (OPD_Hash, OPD_USR_FK),
);

CREATE TABLE IF NOT EXISTS PASSWORDS (
  PWD_PK SERIAL PRIMARY KEY,
  PWD_Hash VARCHAR(64), -- TODO: need to generate magic link email to set password
  -- also need createUser to generate a PWD_Hash which we email to the user
  PWD_USR_FK INTEGER REFERENCES USERS(USR_ID_PK) ON DELETE CASCADE,
  PWD_UpdatedOn DATE,
  PWD_CreatedOn DATE,
  PWD_LastLogin DATE,
  PWD_Locked boolean,
  PWD_Reset boolean
);

-- Image Association Types 
-- = describes what type of data an image is associated with
CREATE TABLE IF NOT EXISTS IMAGE_ASSOC_TYPES (
       IAT_PK INT PRIMARY KEY,
       IAT_Type VARCHAR(10)
);

DO $$
BEGIN
  IF (1 NOT IN (SELECT IAT_PK FROM IMAGE_ASSOC_TYPES)) THEN
    INSERT INTO IMAGE_ASSOC_TYPES(IAT_PK, IAT_Type) VALUES (0, 'post'   );
    INSERT INTO IMAGE_ASSOC_TYPES(IAT_PK, IAT_Type) VALUES (1, 'profile');
  END IF;
END $$;

CREATE TABLE IF NOT EXISTS IMAGES (
       IMG_ID_PK SERIAL,
       -- secondary FK to link with a post? Images could be stored without post
       IMG_Type_FK INT REFERENCES IMAGE_ASSOC_TYPES(IAT_PK),
       IMG_Hash_PK CHAR(64),
       IMG_File VARCHAR(200), -- FILE ON DISK
       IMG_Thumbnail VARCHAR(200), -- ALSO ON DISK
       IMG_Meta VARCHAR, -- IS Text type too much space in disk?
       IMG_Source VARCHAR,
       PRIMARY KEY (IMG_ID_PK)
);

CREATE TABLE IF NOT EXISTS POSTS (
  PST_ID_PK SERIAL PRIMARY KEY,
  PST_USR_ID_FK INTEGER REFERENCES USERS(USR_ID_PK),
  PST_LOC_FK VARCHAR(10),
  PST_Content VARCHAR(140),
  PST_Time TIMESTAMPTZ NOT NULL,
  PST_EditTime DATE NULL,
  PST_ShortUrl VARCHAR(50),
  PST_IsDecentral BOOLEAN DEFAULT FALSE
);

-- INSERT INTO Posts(PST_Content, PST_Time) VALUES ('Database Migrated', now());

CREATE TABLE IF NOT EXISTS LOCATION (
       LOC_ID_PK SERIAL PRIMARY KEY,
       LOC_ID varchar(10), -- zipcode, maybe rename to be more specific?
       LOC_Alias varchar(20), -- this needs to be generated with server utility
       LOC_State varchar(2)
);

CREATE TABLE IF NOT EXISTS SESSIONS (
       SES_ID_PK SERIAL,
       SES_ID VARCHAR(64) UNIQUE, -- This is a generated random hash using the UserID and time?
       SES_USR_ID_FK INT NULL,
       SES_CreatedOn DATE,
       SES_LOC_FK integer,
       FOREIGN KEY (SES_USR_ID_FK) REFERENCES USERS(USR_ID_PK),
       PRIMARY KEY (SES_ID_PK)
);

/* Always create my god-mode account on init ;) */
-- CALL sp_CreateUser('evan', 'Avogadro6.02', 'evanpeterjones@gmail.com');
