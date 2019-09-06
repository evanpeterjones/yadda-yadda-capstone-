/*
*  script to initialize all database tables with init data
*/

CREATE TABLE IF NOT EXISTS USERS (
       USR_ID_PK SERIAL,
       USR_Username VARCHAR (50) UNIQUE NOT NULL,
       USR_PWD_FK INTEGER,
       USR_Email VARCHAR (100) UNIQUE NOT NULL,
       USR_CreatedOn DATE NOT NULL,
       USR_LastLogin DATE,
       PRIMARY KEY (USR_ID_PK)
);

CREATE TABLE IF NOT EXISTS OLD_PASSWORDS (
      OPD_Hash VARCHAR(64),
      OPD_USR_FK INTEGER,
      PRIMARY KEY (OPD_Hash, OPD_USR_FK),
      FOREIGN KEY (OPD_USR_FK) REFERENCES USERS(USR_ID_PK)
);

CREATE TABLE IF NOT EXISTS PASSWORDS (
      PWD_Hash VARCHAR(64), -- TODO: need to generate magic link email to set password
      PWD_PK SERIAL,
      PWD_USR_FK INTEGER,
      PWD_UpdatedOn DATE,
      PWD_LastLogin DATE,
      PWD_Locked boolean,
      PWD_Reset boolean,
      PRIMARY KEY (PWD_PK),
      FOREIGN KEY (OPD_USR_FK) REFERENCES USERS(USR_ID_PK)
);

CREATE TABLE IF NOT EXISTS IMAGES (
       IMG_ID_PK SERIAL,
       -- secondary FK to link with a post? Images could be stored without post
       IMG_Type_FK INT, 
       IMG_Hash_PK CHAR(64),
       IMG_File VARCHAR(200), -- FILE ON DISK
       IMG_Thumbnail VARCHAR(200), -- ALSO ON DISK
       IMG_Meta VARCHAR, -- IS Text type too much space in disk?
       IMG_Source VARCHAR,
       PRIMARY KEY (IMG_ID_PK)
);

-- Image Association Types 
-- = describes what type of data an image is associated with
CREATE TABLE IF NOT EXISTS IMAGE_ASSOC_TYPES (
       IAT_PK INT,
       IAT_Assoc VARCHAR(10),
       PRIMARY KEY (IAT_PK)
);

IF ( NOT IN (SELECT IAT_PK FROM IMAGE_ASSOC_TYPES)) THEN
   INSERT INTO IMAGE_ASSOC_TYPES(IAT_PK, IAT_Assoc) VALUES (0, 'post'   );
   INSERT INTO IMAGE_ASSOC_TYPES(IAT_PK, IAT_Assoc) VALUES (1, 'profile');
END IF;

CREATE TABLE IF NOT EXISTS POSTS (
       PST_ID_PK SERIAL,
       PST_USR_ID_FK INTEGER,
       PST_Time DATE NOT NULL,
       PST_EditTime DATE NULL,
       PST_ShortUrl VARCHAR(50),
       PRIMARY KEY (PST_ID_PK),
       FOREIGN KEY (PST_USR_ID_FK) REFERENCES USERS(USR_ID_PK)
);

CREATE TABLE IF NOT EXISTS LOCATION (
       AREA_ID_PK varchar(10) PRIMARY KEY,
       AREA_Alias varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS SESSIONS (
       SES_ID VARCHAR(64), -- This is a generated random hash using the UserID and time?
       SES_
       SES_USR_ID_FK INT,
       SES_CreatedOn DATE,
       FOREIGN KEY (SES_USR_ID_FK) REFERENCES USERS(USR_ID_PK),
       PRIMARY KEY (SES_ID, SES_USR_ID_FK)
);
     
/* Always create my god-mode account on init ;) */
CALL sp_CreateUser('evan', 'Avogadro6.02', 'evanpeterjones@gmail.com');
