package ru.mnx.gostkeeper.data.entity;

/**
 * Запись с наименованием и id
 */
public class Secret {

    private final int id;
    private final String name;

    public Secret(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return идентификатор секрета
     */
    public int getId() {
        return id;
    }

    /**
     * @return наименование секрета
     */
    public String getName() {
        return name;
    }
}
