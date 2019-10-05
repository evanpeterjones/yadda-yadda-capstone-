-- name: create-user!
-- create a new user object given {:username :email}
DO $$
BEGIN 

IF EXISTS (SELECT 1 FROM Users WHERE USR_Username = :username) THEN
    RAISE EXCEPTION 'User with that username already exists';
END IF;

-- TODO: need to generate a password to email/text to users in order to reset
-- INSERT INTO PASSWORDS(PWD_Hash, PWD_Reset, PWD_CreatedOn)
-- VALUES(/*somefunction*/'a32id$d', 1, NOW());

INSERT INTO USERS(USR_Username, USR_Email, USR_CreatedOn)
VALUES(:username, :email, NOW());

END$$;

-- name: image-type!
-- insert a new image type
INSERT INTO IMAGE_ASSOC_TYPES(IAT_PK, IAT_Assoc) VALUES (1, 'profile');


-- name: update-password!
-- update a password hash given {:password :user-id &optional :update-old-passwords}
DECLARE newhash varchar(64) = f_PBKDF2(:password);
DECLARE oldhash varchar(64) = (SELECT PWD_Hash
FROM Passwords
WHERE PWD_USR_FK = :user_id
LIMIT 1);

IF :update_old_passwords THEN

  IF EXISTS (SELECT 1 FROM Old_Passwords WHERE OPD_USR_FK = :user_id AND OPD_Hash = newhash)
  THEN
  RAISE ERROR 'New Password cannot be an old password';
  END IF;

-- move current password into old passwords --
INSERT INTO OLD_Passwords(OPD_Hash, OPD_USR_FK)
VALUES (oldhash, :user_id);

END IF;

-- insert new password --
INSERT INTO Passwords(PWD_Hash, PWD_USR_FK, PWD_UpdatedOn)
VALUES (newHash, :user_id, NOW());

-- name: get-posts
-- return posts for feed
-- TODO: select next posts between certain values
SELECT * FROM POSTS

-- name: create-post!
-- TODO: calls to NOW() need a replacement resource
INSERT INTO POSTS(PST_USR_ID_FK, PST_Content, PST_Time, PST_Decentral)
VALUES(:usr_id, :content, NOW(), :decentral);

-- name: delete-post!
-- delete a post when given {:post_key}
DELETE FROM POSTS WHERE PST_ID_PK = :post_key
