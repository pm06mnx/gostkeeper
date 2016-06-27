package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import ru.mnx.gostkeeper.data.encryption.EncryptionAlgorithm;

import static org.junit.Assert.assertArrayEquals;

public class GOST28147AESCipherTest {

    private static final byte[] SALT = "salt".getBytes();
    private static final Key KEY = new SecretKeySpec(
            Hex.decode("3031323334353637383931323334353630313233343536373839313233343536"),
            EncryptionAlgorithm.GOST28147.getName()
    );
    private static final byte[] DATA = "secureddatanopad".getBytes();
    private static final byte[] ENCODED_DATA = Hex.decode("fb58559e202cdb8e1afa75e436944c839bd622e85ffd5d13e6fbcfb3a794940a");

    private GOST28147AESCipher cipher;

    @Before
    public void init() {
        cipher = new GOST28147AESCipher();
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
