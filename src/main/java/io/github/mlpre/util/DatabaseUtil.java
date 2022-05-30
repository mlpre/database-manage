package io.github.mlpre.util;

import io.github.mlpre.controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import io.github.mlpre.model.Database;

import java.sql.*;
import java.util.*;

public class DatabaseUtil {

    public static Connection connection;

    public static LinkedHashMap<String, Connection> connectionList = new LinkedHashMap<>();

    public synchronized static Connection getConnect(Database database) {
        try {
            if (!connectionList.isEmpty() &&
                    connectionList.get(database.getName()) != null &&
                    !connectionList.get(database.getName()).isClosed()) {
                connection = connectionList.get(database.getName());
            } else {
                connection = DriverManager.getConnection(database.toConnectUrl(), database.getUsername(), database.getPassword());
                connectionList.put(database.getName(), connection);
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
            UiUtil.alertException(e);
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
            UiUtil.alertException(e);
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

    public static ObservableList<Database> loadConnectHistory() {
        ObservableList<Database> observableList = FXCollections.observableArrayList();
        Properties properties = ConfigUtil.properties;
        if (properties != null) {
            String databaseConnect = properties.getProperty("database-connect");
            if (databaseConnect != null && !databaseConnect.isEmpty()) {
                List<Database> list = ObjectUtil.toList(ObjectUtil.bytesToObject(Base64.getDecoder().decode(databaseConnect)), Database.class);
                observableList.addAll(list);
            }
        }
        return observableList;
    }

    public static void saveConnectHistory() {
        ObservableList<Database> databaseList = MainController.databaseList;
        List<Database> list = new ArrayList<>(databaseList);
        ConfigUtil.properties.setProperty("database-connect", Base64.getEncoder().encodeToString(ObjectUtil.objectToBytes(list)));
    }

}
