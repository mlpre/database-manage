package io.github.mlpre.model;

import io.github.mlpre.util.BaseUtil;

public class Database {

    private String name;

    private String url;

    private int port;

    private String param;

    private String username;

    private String password;

    public Database() {
    }

    public Database(String name, String url, int port, String param, String username, String password) {
        this.name = name;
        this.url = url;
        this.port = port;
        this.param = param;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Database setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Database setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Database setPort(int port) {
        this.port = port;
        return this;
    }

    public String getParam() {
        return param;
    }

    public Database setParam(String param) {
        this.param = param;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Database setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Database setPassword(String password) {
        this.password = password;
        return this;
    }

    public String toConnectUrl() {
        String fullUrl = this.getUrl() + ":" + this.getPort();
        if (!BaseUtil.isEmpty(this.param)) {
            return fullUrl + "?" + this.param;
        }
        return fullUrl;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
