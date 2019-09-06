CREATE OR REPLACE PROCEDURE sp_UpdatePassword (userId integer, password varchar, updateOldPasswords boolean)
LANGUAGE plpgsql
AS $$
BEGIN

    DECLARE newhash varchar(64) := f_PBKDF2(password);
    DECLARE oldhash varchar(64) := (SELECT PWD_Hash 
                                    FROM Passwords 
                                    WHERE PWD_USR_FK = userId 
                                    LIMIT 1);
                                    
    IF updateOldPasswords THEN

        IF EXISTS (SELECT 1 FROM Old_Passwords WHERE OPD_USR_FK = userId AND OPD_Hash = newhash)
        THEN
            RAISE ERROR 'New Password cannot be an old password';
        END IF;

        -- move current password into old passwords --
        INSERT INTO OLD_Passwords(OPD_Hash, OPD_USR_FK)
        VALUES (oldHash, userId);

    END IF;

    -- insert new password --
    INSERT INTO Passwords(PWD_Hash, PWD_USR_FK, PWD_UpdatedOn)
    VALUES (newHash, userId, NOW());

END;
$$
