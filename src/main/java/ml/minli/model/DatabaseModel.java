/*
 * Copyright 2021 Minli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.minli.model;

import com.mysql.cj.util.StringUtils;

/**
 * @author Minli
 */
public class DatabaseModel {

    private String name;

    private String url;

    private int port;

    private String database;

    private String param;

    private String username;

    private String password;

    /**
     * type=0: connectHistory
     * type=1: database
     * type=2: table
     */
    private int type;

    public DatabaseModel() {
    }

    public DatabaseModel(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public DatabaseModel(String name, String url, int port, String database, String param, String username, String password, int type) {
        this.name = name;
        this.url = url;
        this.port = port;
        this.database = database;
        this.param = param;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toConnectUrl() {
        String fullUrl = this.getUrl() + ":" + this.getPort() + "/" + this.getDatabase();
        if (!StringUtils.isNullOrEmpty(this.param)) {
            return fullUrl + "?" + this.param;
        }
        return fullUrl;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
