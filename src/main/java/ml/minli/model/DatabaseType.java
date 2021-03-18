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

import java.util.TimeZone;

/**
 * @author Minli
 */
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
