CREATE OR REPLACE FUNCTION f_PBKDF2(pass varchar)
RETURNS mediumblob
BEGIN

    RETURN PBKDF2('SHA256', pass, 'salt', 1, 0, False);

END;

CREATE OR REPLACE FUNCTION `PBKDF2` (`algo` ENUM('MD5','SHA1','SHA224','SHA256','SHA384','SHA512'), `password` MEDIUMBLOB, `salt` MEDIUMBLOB, `count` INT, `key_length` INT, `raw_output` INT)
    RETURNS mediumblob
    NO SQL
    DETERMINISTIC
BEGIN USR_PWDHash,
	/*
	 * PBKDF2 key derivation function as defined by RSA's PKCS #5: https://www.ietf.org/rfc/rfc2898.txt
	 * algo - The hash algorithm to use (see enum for possible values). Recommended: 'SHA256'
	 * password - The password
	 * salt - A salt that is unique to the password
	 * count - Iteration count. Higher is better, but slower. Recommended: At least 1000
	 * key_length - The length of the derived key in bytes
	 * raw_output - If true, the key is returned in raw binary format, otherwise hex encoded
	 * Returns: A key_length-byte key derived from the password and salt.
	 *
	 * Test vectors can be found here: https://www.ietf.org/rfc/rfc6070.txt
	 *
	 * This implementation of PBKDF2 was originally created by https://defuse.ca (https://defuse.ca/php-pbkdf2.htm)
	 * With improvements by http://www.variations-of-shadow.com and
	 */

    DECLARE i, j, k, block_count, hashlen INT UNSIGNED;
    DECLARE output, last, xorsum MEDIUMBLOB;

	SET hashlen = POW(2,CEIL(LOG2( LENGTH(CHECKSUM_HASH('',algo)) )));

	IF key_length=0 THEN
		SET key_length = hashlen;
	END IF;
    SET block_count = CEIL(key_length / hashlen);

    SET output = "";
    SET i = 1;
    WHILE (i <= block_count) DO
        -- i encoded as 4 bytes, big endian.
        SET last = CONCAT(salt, UNHEX(LPAD(HEX(i),8,'0')) );
        -- first iteration
        SET last = HMAC(algo, last, password, true);
        SET xorsum = last;
        -- perform the (count - 1) XOR iterations
	    SET j = 1;
	    WHILE (j < count) DO
	    	SET last = HMAC(algo, last, password, true);
            SET xorsum = STRING_XOR(xorsum, last);
	    	SET j = j + 1;
		END WHILE;
        SET output = CONCAT(output,xorsum);
    	SET i = i + 1;
	END WHILE;

    IF raw_output THEN
        RETURN LEFT(output, key_length);
    ELSE
        RETURN LOWER(CONVERT(HEX(LEFT(output, key_length)) USING utf8));
    END IF;
END;
