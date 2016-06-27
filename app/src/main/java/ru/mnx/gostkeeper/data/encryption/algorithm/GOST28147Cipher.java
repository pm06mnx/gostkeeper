package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

/**
 * GOST28147 cipher implementation
 */
public class GOST28147Cipher extends CipherAlgorithm {

    private static final Logger log = Logger.getLogger(GOST28147Cipher.class.getName());

    private static final String ALGORITHM = "GOST28147/CBC/PKCS5Padding";

    @Override
    public byte[] encrypt(byte[] data, Key key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key, makeIv(salt, cipher));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.WARNING, "GOST28147 encryption error", e);
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
            log.log(Level.WARNING, "GOST28147 decryption error", e);
            throw new CipherException(e);
        }
    }
}
