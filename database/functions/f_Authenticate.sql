CREATE OR REPLACE FUNCTION f_Authenticate (userId integer, password varchar)
RETURNS TINYINT(1)
BEGIN

    IF (password <> NULL) THEN
        DECLARE @hash = f_PBKDF2(password);

        IF EXISTS (SELECT 1 FROM PASSWORDS WHERE PWD_USR_ID_FK = userId AND PWD_HASH = @hash)
        THEN
            RETURN 1;
        END IF;

    END IF;

    RETURN 0;

END;
