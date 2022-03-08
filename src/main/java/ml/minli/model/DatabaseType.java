package ml.minli.model;

import java.util.TimeZone;

public enum DatabaseType {

    MySQL("jdbc:mysql://", 3306, "mysql", "serverTimezone=" + TimeZone.getDefault().getID()),
    MariaDB("jdbc:mysql://", 3306, "mysql", "serverTimezone=" + TimeZone.getDefault().getID());

    DatabaseType(String url, int defaultPort, String defaultDatabase, String defaultParam) {
        this.url = url;
        this.defaultPort = defaultPort;
        this.defaultDatabase = defaultDatabase;
        this.defaultParam = defaultParam;
    }

    private String url;

    private int defaultPort;

    private String defaultDatabase;

    private String defaultParam;

    public String getUrl() {
        return url;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    public String getDefaultParam() {
        return defaultParam;
    }
}
