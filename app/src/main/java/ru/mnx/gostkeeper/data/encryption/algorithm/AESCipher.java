package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

/**
 * AES cipher implementation
 */
public class AESCipher extends CipherAlgorithm {

    private static final Logger log = Logger.getLogger(AESCipher.class.getName());

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    @Override
    public byte[] encrypt(byte[] data, Key key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key, makeIv(salt, cipher));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "AES encryption error", e);
            throw new CipherException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, Key key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, key, makeIv(salt, cipher));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "AES decryption error", e);
            throw new CipherException(e);
        }
    }
}
