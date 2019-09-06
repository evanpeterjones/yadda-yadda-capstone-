-- name: create-user
IF EXISTS (SELECT 1 FROM Users WHERE USR_Username = :username)
THEN
    RAISE EXCEPTION 'User with that email already exists';
END IF;

INSERT INTO USERS(USR_Username, USR_Email, USR_CreatedOn)
VALUES(:username, :email, NOW());


