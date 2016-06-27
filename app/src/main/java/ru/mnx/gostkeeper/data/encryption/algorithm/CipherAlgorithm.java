package ru.mnx.gostkeeper.data.encryption.algorithm;

import org.bouncycastle.crypto.digests.SHA512Digest;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 * Interface for cipher implementations
 */
public abstract class CipherAlgorithm {

    /**
     * @param data data to encryption
     * @param key  key using for encryption
     * @param salt salt for the data or null
     * @return encrypted data
     */
    public abstract byte[] encrypt(byte[] data, Key key, byte[] salt);

    /**
     * @param data encrypted data
     * @param key key using for decryption
     * @param salt salt for the data or null
     * @return decrypted data
     */
    public abstract byte[] decrypt(byte[] data, Key key, byte[] salt);


    /**
     * @param salt init value for initialization vector
     * @param cipher cipher
     * @return initialization vector for the cipher
     */
    protected final IvParameterSpec makeIv(byte[] salt, Cipher cipher) {
        if (salt == null) {
            return null;
        }
        //hash salt for randomness
        byte[] saltHash = hash(salt);
        //cut length of hash
        int blockSize = cipher.getBlockSize();
        if (saltHash.length < blockSize) {
            saltHash = Arrays.copyOf(saltHash, blockSize);
        }
        return new IvParameterSpec(saltHash, 0, blockSize);
    }

    private byte[] hash(byte[] salt) {
        SHA512Digest digest = new SHA512Digest();
        digest.update(salt, 0, salt.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }
}
