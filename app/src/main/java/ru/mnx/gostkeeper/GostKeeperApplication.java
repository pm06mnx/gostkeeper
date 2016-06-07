package ru.mnx.gostkeeper;

import android.app.Application;

import ru.mnx.gostkeeper.data.SecretDbHelper;

/**
 * Приложение
 */
public class GostKeeperApplication extends Application {

    private SecretDbHelper secretDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        this.secretDbHelper = new SecretDbHelper(getApplicationContext());
    }

    public SecretDbHelper getSecretDbHelper() {
        return secretDbHelper;
    }
}