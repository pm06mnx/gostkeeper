package ru.mnx.gostkeeper.data.encryption.algorithm;

import java.security.Key;
import java.util.logging.Logger;

/**
 * GOST28147 + AES cipher implementation
 */
public class GOST28147AESCipher implements CipherAlgorithm {

    private static final Logger log = Logger.getLogger(GOST28147Cipher.class.getName());

    @Override
    public byte[] encrypt(byte[] data, Key key, byte[] salt) {
        byte[] gostData = new GOST28147Cipher().encrypt(data, key, salt);
        return new AESCipher().encrypt(gostData, key, salt);
    }

    @Override
    public byte[] decrypt(byte[] data, Key key, byte[] salt) {
        byte[] aesData = new AESCipher().decrypt(data, key, salt);
        return new GOST28147Cipher().decrypt(aesData, key, salt);
    }
}
