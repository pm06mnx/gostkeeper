package ru.mnx.gostkeeper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.security.KeyStore;
import java.util.logging.Logger;

import ru.mnx.gostkeeper.data.EncryptedSecretDbHelper;
import ru.mnx.gostkeeper.data.SecretDbHelper;
import ru.mnx.gostkeeper.data.encryption.GostKeeperKeystore;

/**
 * Main application class
 */
public class GostKeeperApplication extends Application {

    private static final Logger log = Logger.getLogger(GostKeeperApplication.class.getName());

    private static SecretDbHelper secretDbHelper;
    private static GostKeeperKeystore keystore;

    @Override
    public void onCreate() {
        super.onCreate();
        secretDbHelper = new EncryptedSecretDbHelper(getApplicationContext());
        log.info("Application initialized");
    }

    /**
     * @return database helper
     */
    public static SecretDbHelper getSecretDbHelper() {
        return secretDbHelper;
    }

    /**
     * Open keystore with supplied password
     *
     * @param appContext application context for the keystore
     * @param pass password
     * @throws GostKeeperKeystore.KeystoreException if password is wrong or keystore is broken
     */
    public static void initKeystore(Context appContext, KeyStore.PasswordProtection pass) throws GostKeeperKeystore.KeystoreException {
        if (keystore != null) {
            throw new GostKeeperApplicationException("Keystore already initialized");
        }
        keystore = new GostKeeperKeystore(appContext, pass);
    }

    /**
     * @return true, if keystore already initialized
     */
    public static boolean isKeystoreInitialized() {
        return keystore != null;
    }

    /**
     * @return initialized keystore
     */
    public static GostKeeperKeystore getKeystore() {
        if (keystore == null) {
            throw new GostKeeperApplicationException("Keystore not initialized");
        }
        return keystore;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * Application fatal error
     */
    public static class GostKeeperApplicationException extends RuntimeException {

        public GostKeeperApplicationException(String message) {
            super(message);
        }
    }
}
