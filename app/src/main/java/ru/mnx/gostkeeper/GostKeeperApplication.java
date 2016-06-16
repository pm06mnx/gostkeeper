package ru.mnx.gostkeeper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import org.bouncycastle.jce.interfaces.GOST3410Key;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import ru.mnx.gostkeeper.data.EncryptedSecretDbHelper;
import ru.mnx.gostkeeper.data.SecretDbHelper;

/**
 * Main application class
 */
public class GostKeeperApplication extends Application {

    private static final Logger log = Logger.getLogger(GostKeeperApplication.class.getName());
    private static final String BOUNCY_CASTLE_PROVIDER_NAME = "BC";


    private SecretDbHelper secretDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        setBouncyCastleAsDefaultProvider();
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
