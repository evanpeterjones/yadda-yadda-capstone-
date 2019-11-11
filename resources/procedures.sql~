-- :name create-user :<!
-- :doc create a new user object given {:username :email}

-- TODO: need to generate a password to email/text to users in order to reset
-- INSERT INTO PASSWORDS(PWD_Hash, PWD_Reset, PWD_CreatedOn)
-- VALUES(/*somefunction*/'a32id$d', 1, NOW());
INSERT INTO USERS(USR_CreatedOn)
VALUES(NOW()) RETURNING usr_id_pk;


-- :name update-password!
-- :doc update a password hash given {:password :user-id &optional :update-old-passwords}
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

-- :name get-posts
-- :result :json
-- TODO: select next posts between certain values {:location int}
SELECT json_agg(posts)
FROM POSTS
WHERE PST_LOC_FK = :location
GROUP BY PST_TIME
ORDER BY PST_TIME DESC;

-- :name get-post
-- :doc get data for post of a certain ID
SELECT json_agg(posts)
FROM POSTS
WHERE PST_ID_PK = :post_id
GROUP BY PST_TIME
ORDER BY PST_TIME DESC;

-- :name create-post<!
-- :doc create a new post returning the id of the post
INSERT INTO POSTS(pst_usr_id_fk, pst_content, pst_loc_fk, pst_time)
VALUES(:user, :content, :location, now()) RETURNING PST_ID_PK;

-- :name delete-post!
-- :doc delete a post when given {:post_key String}
DELETE FROM POSTS WHERE PST_ID_PK = :post_key
