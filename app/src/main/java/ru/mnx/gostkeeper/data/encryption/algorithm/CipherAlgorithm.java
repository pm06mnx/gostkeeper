package ru.mnx.gostkeeper.data.encryption.algorithm;

import java.security.Key;

/**
 * Interface for cipher implementations
 */
public interface CipherAlgorithm {

    /**
     * @param data data to encryption
     * @param key  key using for encryption
     * @param salt salt for the data or null
     * @return encrypted data
     */
    byte[] encrypt(byte[] data, Key key, byte[] salt);

    /**
     * @param data encrypted data
     * @param key key using for decryption
     * @param salt salt for the data or null
     * @return decrypted data
     */
    byte[] decrypt(byte[] data, Key key, byte[] salt);
}
