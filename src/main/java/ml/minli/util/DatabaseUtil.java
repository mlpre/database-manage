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
package ml.minli.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ml.minli.controller.MainController;
import ml.minli.model.DatabaseModel;

import java.sql.*;
import java.util.*;

/**
 * @author Minli
 */
public class DatabaseUtil {

    public static Connection connection;

    public static LinkedHashMap<String, Connection> connectionList = new LinkedHashMap<>();

    public synchronized static Connection getConnect(DatabaseModel databaseModel) {
        try {
            if (!connectionList.isEmpty() &&
                    connectionList.get(databaseModel.getName()) != null &&
                    !connectionList.get(databaseModel.getName()).isClosed()) {
                connection = connectionList.get(databaseModel.getName());
            } else {
                connection = DriverManager.getConnection(databaseModel.toConnectUrl(), databaseModel.getUsername(), databaseModel.getPassword());
                connectionList.put(databaseModel.getName() + databaseModel.getDatabase(), connection);
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static ResultSet execute(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (statement.execute(sql)) {
                resultSet = statement.getResultSet();
            }
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            UiUtil.AlertException(e);
            return null;
        }
    }

    public synchronized static ResultSet execute(String sql, int resultSetType, int resultSetConcurrency) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement(resultSetType, resultSetConcurrency);
            resultSet = statement.executeQuery(sql);
            if (statement.execute(sql)) {
                resultSet = statement.getResultSet();
            }
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            UiUtil.AlertException(e);
            return null;
        }
    }

    public synchronized static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                Statement statement = resultSet.getStatement();
                resultSet.close();
                resultSet = null;
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<DatabaseModel> loadConnectHistory() {
        ObservableList<DatabaseModel> observableList = FXCollections.observableArrayList();
        Properties properties = ConfigUtil.properties;
        if (properties != null) {
            String databaseConnect = properties.getProperty("database-connect");
            if (databaseConnect != null && !databaseConnect.isEmpty()) {
                String json = new String(Base64.getDecoder().decode(databaseConnect));
                List<DatabaseModel> list = JSON.parseObject(json, new TypeReference<>() {
                });
                observableList.addAll(list);
            }
        }
        return observableList;
    }

    public static void saveConnectHistory() {
        ObservableList<DatabaseModel> connectHistoryList = MainController.connectHistoryList;
        String json = JSON.toJSONString(connectHistoryList);
        ConfigUtil.properties.setProperty("database-connect", Base64.getEncoder().encodeToString(json.getBytes()));
    }

}
