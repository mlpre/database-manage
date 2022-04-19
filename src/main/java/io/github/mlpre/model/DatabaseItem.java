package io.github.mlpre.model;

public class DatabaseItem {

    private String name;

    /**
     * type=0: connectHistory
     * type=1: database
     * type=2: table
     */
    private int type;

    private Database database;

    public DatabaseItem() {
    }

    public DatabaseItem(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public DatabaseItem(String name, int type, Database database) {
        this.name = name;
        this.type = type;
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public DatabaseItem setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public DatabaseItem setType(int type) {
        this.type = type;
        return this;
    }

    public Database getDatabase() {
        return database;
    }

    public DatabaseItem setDatabase(Database database) {
        this.database = database;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
