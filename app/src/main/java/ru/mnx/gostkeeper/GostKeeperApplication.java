package ru.mnx.gostkeeper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyStore;
import java.security.Security;
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


    private SecretDbHelper secretDbHelper;
    private GostKeeperKeystore keystore;

    @Override
    public void onCreate() {
        super.onCreate();
        setBouncyCastleAsDefaultProvider();
        try {
            this.keystore = new GostKeeperKeystore(getApplicationContext(), new KeyStore.PasswordProtection(new char[]{'1', '2', '3', '4'}));
        } catch (GostKeeperKeystore.KeystoreException e) {
            log.log(Level.SEVERE, "Application initialization error", e);
        }
        this.secretDbHelper = new EncryptedSecretDbHelper(getApplicationContext());
    }

    private void setBouncyCastleAsDefaultProvider() {
        Security.removeProvider(BOUNCY_CASTLE_PROVIDER_NAME);
        int pos = Security.insertProviderAt(new BouncyCastleProvider(), 1);
        log.info("Registered bouncy castle provider at position " + pos);
    }

    public SecretDbHelper getSecretDbHelper() {
        return secretDbHelper;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
