package ru.mnx.gostkeeper.data.encryption;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * KeyStore helper class
 */
public class GostKeeperKeystore {

    private static final Logger log = Logger.getLogger(GostKeeperKeystore.class.getName());

    private static final String KEYSTORE_NAME = KeyStore.getDefaultType();
    private static final String KEYSTORE_PATH = "store.jks";
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

        try {
            Enumeration<String> aliases = this.keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                log.info("Alias - " + alias);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
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
            log.log(Level.SEVERE, "Keystore initialization error", e);
            throw new KeystoreException(e);
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
            log.log(Level.SEVERE, "Keystore creation error", e);
            throw new KeystoreException(e);
        }
    }

    private void createKeys(KeyStore keyStore) throws KeystoreException {
        try {
            keyStore.setEntry(AES_KEY_ALIAS, new KeyStore.SecretKeyEntry(createAESKey()), null);
            keyStore.setEntry(GOST28147_KEY_ALIAS, new KeyStore.SecretKeyEntry(createGost28147Key()), null);
        } catch (KeyStoreException e) {
            log.log(Level.SEVERE, "Keys creation error", e);
            throw new KeystoreException(e);
        }
    }

    private SecretKey createAESKey() throws KeystoreException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.AES.getName());
            keyGenerator.init(EncryptionAlgorithm.AES.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "AES key creation error", e);
            throw new KeystoreException(e);
        }
    }

    private SecretKey createGost28147Key() throws KeystoreException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptionAlgorithm.GOST28147.getName());
            keyGenerator.init(EncryptionAlgorithm.GOST28147.getKeySize());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "Gost28147 key creation error", e);
            throw new KeystoreException(e);
        }
    }

    private void saveKeystore(KeyStore keyStore) throws KeystoreException {
        try {
            FileOutputStream fos = new FileOutputStream(getKeystorePath());
            keyStore.store(fos, keystorePassword.getPassword());
            fos.close();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            log.log(Level.SEVERE, "Keystore saving error", e);
            throw new KeystoreException(e);
        }
    }


    /**
     * Error while talking with KeyStore
     */
    public static class KeystoreException extends Exception {

        public KeystoreException(Throwable e) {
            super(e);
        }
    }
}
