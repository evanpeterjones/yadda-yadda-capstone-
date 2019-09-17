CREATE OR REPLACE FUNCTION f_CreateUser(uname varchar, pass varchar, uemail varchar) RETURNS integer AS $$
BEGIN

   IF 1 <> 0 THEN
         RAISE EXCEPTION 'User already exists';
   ELSE
        INSERT INTO USERS(USR_Username, USR_Email, USR_CreatedOn)
        VALUES(uname, uemail, NOW());
   END IF;

   RETURN 1;

END;
$$ LANGUAGE plpgsql;
