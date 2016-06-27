package ru.mnx.gostkeeper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.mnx.gostkeeper.data.EncryptedSecretDbHelper;
import ru.mnx.gostkeeper.data.SecretDbHelper;
import ru.mnx.gostkeeper.data.encryption.GostKeeperKeystore;

/**
 * Main application class
 */
public class GostKeeperApplication extends Application {

    private static final Logger log = Logger.getLogger(GostKeeperApplication.class.getName());
    private static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";


    private static SecretDbHelper secretDbHelper;
    private static GostKeeperKeystore keystore;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            keystore = new GostKeeperKeystore(getApplicationContext(), new KeyStore.PasswordProtection(new char[]{'1', '2', '3', '4'}));
        } catch (GostKeeperKeystore.KeystoreException e) {
            log.log(Level.SEVERE, "Application initialization error", e);
        }
        secretDbHelper = new EncryptedSecretDbHelper(getApplicationContext());
    }

    public static SecretDbHelper getSecretDbHelper() {
        return secretDbHelper;
    }

    public static GostKeeperKeystore getKeystore() {
        return keystore;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
