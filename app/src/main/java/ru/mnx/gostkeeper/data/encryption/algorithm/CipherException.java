package ru.mnx.gostkeeper.data.encryption.algorithm;


/**
 * Encryption/decryption error
 */
public class CipherException extends RuntimeException {

    public CipherException(Throwable e) {
        super(e);
    }
}
