-- name: update-password!
DECLARE newhash varchar(64) := f_PBKDF2(:password);
DECLARE oldhash varchar(64) := (SELECT PWD_Hash
FROM Passwords
WHERE PWD_USR_FK = :userid
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
