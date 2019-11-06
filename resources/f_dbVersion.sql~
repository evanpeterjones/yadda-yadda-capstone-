CREATE OR REPLACE FUNCTION F_DBVERSION(V INT) RETURNS INT AS $$
BEGIN

        -- lmao this whole file is so extra, but I need 
        -- a stored procedure to call inside the upgrade script
        UPDATE VERSION SET CURR = V;

        return (SELECT CURR FROM VERSION);

END;
$$ LANGUAGE PLPGSQL;
