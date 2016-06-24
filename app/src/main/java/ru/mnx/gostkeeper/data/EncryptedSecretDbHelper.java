package ru.mnx.gostkeeper.data;

import android.content.Context;
import android.util.Base64;

import java.util.logging.Logger;

import javax.crypto.SecretKey;

import ru.mnx.gostkeeper.GostKeeperApplication;
import ru.mnx.gostkeeper.data.encryption.GostKeeperKeystore;
import ru.mnx.gostkeeper.data.encryption.algorithm.GOST28147Cipher;
import ru.mnx.gostkeeper.data.entity.SecretWithData;

/**
 * Class for store and retrieve information about Secret from DB;
 * Encrypt and decrypt information before send it to DB;
 */
public class EncryptedSecretDbHelper extends SecretDbHelper {

    private static final Logger log = Logger.getLogger(EncryptedSecretDbHelper.class.getName());

    public EncryptedSecretDbHelper(Context context) {
        super(context);
    }

    @Override
    public long createSecret(byte[] name, byte[] data) throws GostKeeperKeystore.KeystoreException {
        final SecretKey key = getKey();
        log.info("Original data: " + new String(data));
        final byte[] encryptedData = new GOST28147Cipher().encrypt(data, key, name);
        String encryptedDataAsString = Base64.encodeToString(encryptedData, Base64.DEFAULT);
        log.info("Encrypted data: "+encryptedDataAsString);
        return super.createSecret(name, encryptedDataAsString.getBytes());
    }

    private SecretKey getKey() throws GostKeeperKeystore.KeystoreException {
        return GostKeeperApplication.getKeystore().getGost28147Key();
    }

    @Override
    public SecretWithData getSecretWithData(int id) throws GostKeeperKeystore.KeystoreException {
        SecretWithData rawSecret = super.getSecretWithData(id);
        //TODO: wroooong
        String dataAsString = rawSecret.getData();
        byte[] data = Base64.decode(dataAsString, Base64.DEFAULT);
        log.info("Database data: "+dataAsString);
        byte[] name = rawSecret.getName().getBytes();
        final SecretKey key = getKey();
        final byte[] decryptedData = new GOST28147Cipher().decrypt(data, key, name);
        log.info("Decrypted data: "+new String(decryptedData));
        return new SecretWithData(
                rawSecret.getId(),
                rawSecret.getName(),
                new String(decryptedData)
        );
    }
}
