package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;
import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import ru.mnx.gostkeeper.data.encryption.EncryptionAlgorithm;

import static org.junit.Assert.assertArrayEquals;

public class AESCipherTest {

    private static final byte[] SALT = "salt".getBytes();
    private static final Key KEY = new SecretKeySpec("01234567891234560123456789123456".getBytes(), EncryptionAlgorithm.AES.getName());
    private static final byte[] DATA = "secureddata".getBytes();
    private static final byte[] ENCODED_DATA = Hex.decode("D563EBED76CEB67E1FF813E48692C7EB");

    private AESCipher cipher;

    @Before
    public void init() {
        setBouncyCastleAsDefaultProvider();
        cipher = new AESCipher();
    }

    private void setBouncyCastleAsDefaultProvider() {
        Security.removeProvider("BC");
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
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
