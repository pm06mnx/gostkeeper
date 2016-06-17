package ru.mnx.gostkeeper.data.encryption;

/**
 * Supported encryption algorithms
 */
public enum  EncryptionAlgorithm {
    PLAIN_TEXT(0, "", 0, 0),
    AES(1, "AES", 256, 128),
    GOST28147(2, "GOST28147", 256, 64),
    GOST28147_AES(3, "", 0, 0);

    private final int code;
    private final String name;
    private final int keySize;
    private final int blockSize;

    EncryptionAlgorithm(int code, String name, int keySize, int blockSize) {
        this.code = code;
        this.name = name;
        this.keySize = keySize;
        this.blockSize = blockSize;
    }

    /**
     * @return ordinal number of algorithm used in the database
     */
    public int getCode() {
        return code;
    }

    /**
     * @return name of algorithm
     */
    public String getName() {
        return name;
    }

    /**
     * @return size of key used with this algorithm
     */
    public int getKeySize() {
        return keySize;
    }

    /**
     * @return size of block used with this algorithm
     */
    public int getBlockSize() {
        return blockSize;
    }
}
