package ru.mnx.gostkeeper.data.encryption;

import android.content.Context;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * KeyStore helper class
 */
public class GostKeeperKeystore {

    private static final Logger log = Logger.getLogger(GostKeeperKeystore.class.getName());

    private static final String KEYSTORE_NAME = "UBER";
    private static final String KEYSTORE_PATH = ".keystore";
    private static final String AES_KEY_ALIAS = "aes";
    private static final String GOST28147_KEY_ALIAS = "gost28147";

    private Context appContext;
    private KeyStore keyStore;
    private KeyStore.PasswordProtection keystorePassword;

    /**
     * @throws KeystoreException on keystore initialization error
     */
    public GostKeeperKeystore(Context appContext, KeyStore.PasswordProtection keystorePassword) throws KeystoreException {
        this.appContext = appContext;
        this.keystorePassword = keystorePassword;
        this.keyStore = getKeyStore();
        log.info("Keystore initialized");
    }

    private KeyStore getKeyStore() throws KeystoreException {
        if (getKeystorePath().exists()) {
            log.info("Reading keystore");
            return readKeystore();
        } else {
            log.info("Creating new keystore");
            return createKeystore();
        }
    }

    private File getKeystorePath() {
        return new File(appContext.getFilesDir(), KEYSTORE_PATH);
    }

    private KeyStore readKeystore() throws KeystoreException {
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_NAME);
            ks.load(new FileInputStream(getKeystorePath()), keystorePassword.getPassword());
            return ks;
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            throw new KeystoreException("Keystore initialization error", e);
        }
    }

    private KeyStore createKeystore() throws KeystoreException {
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_NAME);
            ks.load(null, keystorePassword.getPassword());
            createKeys(ks);
            saveKeystore(ks);
            return ks;
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            throw new KeystoreException("Keystore creation error", e);
        }
    }

    private void createKeys(KeyStore keyStore) throws KeystoreException {
        try {
            keyStore.setEntry(AES_KEY_ALIAS, new KeyStore.SecretKeyEntry(createAESKey()), null);
            keyStore.setEntry(GOST28147_KEY_ALIAS, new KeyStore.SecretKeyEntry(createGost28147Key()), null);
        } catch (KeyStoreException e) {
            throw new KeystoreException("Keys creation error", e);
        }
    }

    private SecretKey createAESKey() throws KeystoreException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.AES.getName(), new BouncyCastleProvider());
            keyGenerator.init(EncryptionAlgorithm.AES.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new KeystoreException("AES key creation error", e);
        }
    }

    private SecretKey createGost28147Key() throws KeystoreException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.GOST28147.getName(), new BouncyCastleProvider());
            keyGenerator.init(EncryptionAlgorithm.GOST28147.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new KeystoreException("Gost28147 key creation error", e);
        }
    }

    private void saveKeystore(KeyStore keyStore) throws KeystoreException {
        try {
            FileOutputStream fos = new FileOutputStream(getKeystorePath());
            keyStore.store(fos, keystorePassword.getPassword());
            fos.close();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            throw new KeystoreException("Keystore saving error", e);
        }
    }


    public SecretKey getAESKey() throws KeystoreException {
        try {
            KeyStore.Entry entry = keyStore.getEntry(AES_KEY_ALIAS, null);
            if (entry instanceof KeyStore.SecretKeyEntry) {
                return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
            }
            throw new KeystoreException("Unknown entry type");
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new KeystoreException("Error while getting key", e);
        }
    }

    public SecretKey getGost28147Key() throws KeystoreException {
        try {
            KeyStore.Entry entry = keyStore.getEntry(GOST28147_KEY_ALIAS, null);
            if (entry instanceof KeyStore.SecretKeyEntry) {
                return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
            }
            throw new KeystoreException("Unknown entry type");
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new KeystoreException("Error while getting key", e);
        }
    }


    /**
     * Error while talking with KeyStore
     */
    public static class KeystoreException extends Exception {

        public KeystoreException(String message) {
            super(message);
        }

        public KeystoreException(String message, Throwable e) {
            super(message, e);
        }
    }
}
