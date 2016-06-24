package ru.mnx.gostkeeper.data.encryption.algorithm;

import java.security.Key;

/**
 * No encryption cipher
 */
public class PlainTextCipher implements CipherAlgorithm {

    @Override
    public byte[] encrypt(byte[] data, Key key, byte[] salt) {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data, Key key, byte[] salt) {
        return data;
    }
}
