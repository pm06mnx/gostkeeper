package ru.mnx.gostkeeper.data.encryption;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Wrapper on
 */
public class GostKeeperKeystore {

    private static final Logger log = Logger.getLogger(GostKeeperKeystore.class.getName());

    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private static final String AES_KEY_ALIAS = "aes";
    private static final String GOST28147_KEY_ALIAS = "gost28147";

    private KeyStore keyStore;
    private char[] password;

    public GostKeeperKeystore() throws KeyStoreException {
        this.keyStore = KeyStore.getInstance(KEYSTORE_NAME);

        keyStore.setKeyEntry(AES_KEY_ALIAS, createAESKey(), password, null);
    }

    private SecretKey createAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.AES.getName());
            keyGenerator.init(EncryptionAlgorithm.AES.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "AES key creation error", e);
            throw new IllegalStateException(e);
        }
    }

    private SecretKey createGost28147Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.GOST28147.getName());
            keyGenerator.init(EncryptionAlgorithm.GOST28147.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "Gost28147 key creation error", e);
            throw new IllegalStateException(e);
        }
    }
}
