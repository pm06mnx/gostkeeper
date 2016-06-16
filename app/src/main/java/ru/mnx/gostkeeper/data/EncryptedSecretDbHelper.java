package ru.mnx.gostkeeper.data;

import android.content.Context;
import android.util.Base64;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ru.mnx.gostkeeper.data.entity.SecretWithData;

/**
 * Class for store and retrieve information about Secret from DB;
 * Encrypt and decrypt information before send it to DB;
 */
public class EncryptedSecretDbHelper extends SecretDbHelper {

    private static final Logger log = Logger.getLogger(EncryptedSecretDbHelper.class.getName());

    //based on block size of algorithm
    //ГОСТ Р 34.12-2015 Magma - 8
    //AES - 16
    private final static int IV_SIZE = 8;
    //TODO: make factory
    //ГОСТ Р 34.12-2015 Magma - "GOST28147/CBC/PKCS5Padding"
    //AES - "AES/CBC/PKCS5Padding"
    private final static String ALGORITHM = "GOST28147/CBC/PKCS5Padding";
    //ГОСТ Р 34.12-2015 Magma - "GOST28147"
    //AES - "AES"
    private final static String KEY_ALGORITHM = "GOST28147";

    public EncryptedSecretDbHelper(Context context) {
        super(context);
    }

    @Override
    public long createSecret(byte[] name, byte[] data) {
        final byte[] keyBytes = getKey();
        final byte[] ivBytes = getInitVector(name);
        log.info("Original data: "+new String(data));
        final byte[] encryptedData = encrypt(data, keyBytes, ivBytes);
        String encryptedDataAsString = Base64.encodeToString(encryptedData, Base64.DEFAULT);
        log.info("Encrypted data: "+encryptedDataAsString);
        return super.createSecret(name, encryptedDataAsString.getBytes());
    }

    private byte[] getKey() {
        //TODO: use real key
        return "12345678901234561234567890123456".getBytes();
    }

    private byte[] getInitVector(byte[] name) {
        //TODO: hash?
        return Arrays.copyOf(name, IV_SIZE);
    }

    private byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.SEVERE, "Encrypting error", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public SecretWithData getSecretWithData(int id) {
        SecretWithData rawSecret = super.getSecretWithData(id);
        //TODO: wroooong
        String dataAsString = rawSecret.getData();
        byte[] data = Base64.decode(dataAsString, Base64.DEFAULT);
        log.info("Database data: "+dataAsString);
        byte[] name = rawSecret.getName().getBytes();
        final byte[] keyBytes = getKey();
        final byte[] ivBytes = getInitVector(name);
        final byte[] decryptedData = decrypt(data, keyBytes, ivBytes);
        log.info("Decrypted data: "+new String(decryptedData));
        return new SecretWithData(
                rawSecret.getId(),
                rawSecret.getName(),
                new String(decryptedData)
        );
    }

    private byte[] decrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            log.log(Level.SEVERE, "Decrypting error", e);
            throw new IllegalStateException(e);
        }
    }
}
