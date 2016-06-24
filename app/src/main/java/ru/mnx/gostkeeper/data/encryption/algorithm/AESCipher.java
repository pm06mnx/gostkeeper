package ru.mnx.gostkeeper.data.encryption.algorithm;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 * AES cipher implementation
 */
public class AESCipher implements CipherAlgorithm {

    private static final Logger log = Logger.getLogger(AESCipher.class.getName());

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    @Override
    public byte[] encrypt(byte[] data, Key key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, makeIv(salt));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "AES encryption error", e);
            throw new CipherException(e);
        }
    }

    private IvParameterSpec makeIv(byte[] salt) {
        if (salt == null) {
            return null;
        }
        if (salt.length < IV_SIZE) {
            salt = Arrays.copyOf(salt, IV_SIZE);
        }
        return new IvParameterSpec(salt, 0, IV_SIZE);
    }


    @Override
    public byte[] decrypt(byte[] data, Key key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, makeIv(salt));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "AES decryption error", e);
            throw new CipherException(e);
        }
    }
}
