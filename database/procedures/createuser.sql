-- name: create-user!
IF EXISTS (SELECT 1 FROM Users WHERE USR_Username = :username)
THEN
    RAISE EXCEPTION 'That username is already taken';
END IF;

IF EXISTS (SELECT 1 FROM Users WHERE USR_Email = :email)
THEN
    RAISE EXCEPTION 'User with that email already exists';
END IF;

INSERT INTO USERS(USR_Username, USR_Email, USR_CreatedOn)
VALUES(:username, :email, NOW());

RETURN 'success';