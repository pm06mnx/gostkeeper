package ru.mnx.gostkeeper.data.entity;

/**
 * Секрет с защищаемыми данными
 */
public class SecretWithData extends Secret {

    private final String data;

    public SecretWithData(int id, String name, String data) {
        super(id, name);
        this.data = data;
    }

    /**
     * @return защищаемая информация
     */
    public String getData() {
        return data;
    }
}
