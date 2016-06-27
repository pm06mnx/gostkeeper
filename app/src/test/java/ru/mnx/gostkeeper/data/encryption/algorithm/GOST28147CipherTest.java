package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import ru.mnx.gostkeeper.data.encryption.EncryptionAlgorithm;

import static org.junit.Assert.assertArrayEquals;

public class GOST28147CipherTest {

    private static final byte[] SALT = "salt".getBytes();
    private static final Key KEY = new SecretKeySpec(
            Hex.decode("3031323334353637383931323334353630313233343536373839313233343536"),
            EncryptionAlgorithm.GOST28147.getName()
    );
    private static final byte[] DATA = "secureddatanopad".getBytes();
    private static final byte[] ENCODED_DATA = Hex.decode("a63c928e4a7c64af824825ae457f7264104fa7bf05aba077");

    private GOST28147Cipher cipher;

    @Before
    public void init() {
        cipher = new GOST28147Cipher();
    }

    @Test
    public void encryptionTest() {
        assertArrayEquals(cipher.encrypt(DATA, KEY, SALT), ENCODED_DATA);
    }

    @Test
    public void decryptionTest() {
        assertArrayEquals(cipher.decrypt(ENCODED_DATA, KEY, SALT), DATA);
    }
}
