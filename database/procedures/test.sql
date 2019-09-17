CREATE OR REPLACE FUNCTION test(a integer, b integer) RETURNS integer AS $$
BEGIN
        return a + b;
END;
$$ LANGUAGE plpgsql;
