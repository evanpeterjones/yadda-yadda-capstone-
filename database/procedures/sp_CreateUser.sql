CREATE OR REPLACE PROCEDURE sp_CreateUser(uname varchar, pass varchar, uemail varchar)
LANGUAGE plpgsql
AS $func$
BEGIN

    IF EXISTS (SELECT 1 FROM Users WHERE USR_Username = uname)
    THEN
        RAISE EXCEPTION 'User with that email already exists';
    END IF;

    INSERT INTO USERS(USR_Username, USR_Email, USR_CreatedOn)
    VALUES(uname, uemail, NOW());

END
$func$;
