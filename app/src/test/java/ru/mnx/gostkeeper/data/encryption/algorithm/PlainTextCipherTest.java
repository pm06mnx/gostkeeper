package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import ru.mnx.gostkeeper.data.encryption.EncryptionAlgorithm;

import static org.junit.Assert.assertArrayEquals;

public class PlainTextCipherTest {

    private static final byte[] SALT = "salt".getBytes();
    private static final Key KEY = new SecretKeySpec(new byte[]{'1'}, EncryptionAlgorithm.PLAIN_TEXT.getName());
    private static final byte[] DATA = "secureddata".getBytes();

    private PlainTextCipher cipher;

    @Before
    public void init() {
        cipher = new PlainTextCipher();
    }

    @Test
    public void encryptionTest() {
        assertArrayEquals(cipher.encrypt(DATA, KEY, SALT), DATA);
    }

    @Test
    public void decryptionTest() {
        assertArrayEquals(cipher.decrypt(DATA, KEY, SALT), DATA);
    }
}
