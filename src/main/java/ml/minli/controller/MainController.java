package ml.minli.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ml.minli.model.Constant;
import ml.minli.model.DatabaseModel;
import ml.minli.util.DatabaseUtil;
import ml.minli.util.BaseUtil;
import ml.minli.util.ResourceUtil;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public StackPane root;
    @FXML
    public TreeView<DatabaseModel> connectHistory;
    @FXML
    public TextArea sqlText;
    @FXML
    public TableView<LinkedHashMap<String, Object>> sqlColumn;

    public static ObservableList<DatabaseModel> connectHistoryList = FXCollections.observableArrayList();

    @FXML
    public MenuBar top;
    @FXML
    public ToolBar bottom;
    @FXML
    public VBox left;
    @FXML
    public ListView<String> right;
    @FXML
    public VBox center;

    /**
     * 打开MySQL窗口
     *
     * @throws Exception
     */
    public void mysql() throws Exception {
        Parent mysql = FXMLLoader.load(ResourceUtil.getResource("fxml/connect.fxml"));
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("连接MySQL");
        Scene scene = new Scene(mysql);
        stage.getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public void about() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initProperty();
        connectHistoryList = DatabaseUtil.loadConnectHistory();
        connectHistory.setRoot(new TreeItem<>());
        connectHistory.setShowRoot(false);
        connectHistory.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == Constant.DOUBLE_CLICK) {
                new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        connectTree();
                        return null;
                    }
                }).start();
            }
        });

        loadMenu();

        connectHistoryList.addListener((ListChangeListener<DatabaseModel>) changeList -> {
            while (changeList.next()) {
                if (changeList.wasAdded()) {
                    List<? extends DatabaseModel> list = changeList.getAddedSubList();
                    list.forEach(databaseModel -> {
                        TreeItem<DatabaseModel> treeItem = new TreeItem<>(databaseModel);
                        FontIcon fontIcon = new FontIcon(FontAwesomeSolid.BOOKMARK);
                        fontIcon.setIconColor(Color.valueOf("#61c22c"));
                        treeItem.setGraphic(fontIcon);
                        treeItem.setExpanded(false);
                        connectHistory.getRoot().getChildren().add(treeItem);
                    });
                }
                if (changeList.wasRemoved()) {
                    List<? extends DatabaseModel> removed = changeList.getRemoved();
                    removed.forEach(databaseModel -> {
                        ObservableList<TreeItem<DatabaseModel>> children = connectHistory.getRoot().getChildren();
                        children.forEach(databaseModelTreeItem -> {
                            if (databaseModelTreeItem.getValue().equals(databaseModel)) {
                                Platform.runLater(() -> children.remove(databaseModelTreeItem));
                            }
                        });
                    });
                }
            }
        });

        connectHistoryList.forEach(databaseModel -> {
            TreeItem<DatabaseModel> treeItem = new TreeItem<>(databaseModel);
            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.BOOKMARK);
            fontIcon.setIconColor(Color.valueOf("#61c22c"));
            treeItem.setGraphic(fontIcon);
            treeItem.setExpanded(false);
            connectHistory.getRoot().getChildren().add(treeItem);
        });
    }

    private void loadMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("删除");
        menuItem.setOnAction(actionEvent -> {
            connectHistoryList.remove(connectHistory.getSelectionModel().getSelectedItem().getValue());
            connectHistory.refresh();
            DatabaseUtil.saveConnectHistory();
        });
        contextMenu.getItems().add(menuItem);
        connectHistory.setContextMenu(contextMenu);
    }

    public void execSql() {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<LinkedHashMap<String, Object>> list = new ArrayList<>();
                ResultSet resultSet = DatabaseUtil.execute(sqlText.getText());
                if (resultSet != null) {
                    while (resultSet.next()) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int columnCount = resultSetMetaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
                        }
                        list.add(map);
                    }
                    DatabaseUtil.close(resultSet);
                }
                if (!list.isEmpty()) {
                    Platform.runLater(() -> {
                        sqlColumn.getColumns().clear();
                        sqlColumn.setItems(null);
                        Set<String> keySet = list.get(0).keySet();
                        keySet.forEach(string -> {
                            TableColumn<LinkedHashMap<String, Object>, String> tableColumn = new TableColumn<>(string);
                            tableColumn.setMinWidth(100);
                            tableColumn.setCellValueFactory(factory -> new SimpleStringProperty(
                                    factory.getValue().get(string) != null ? factory.getValue().get(string).toString() : null));
                            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                            sqlColumn.getColumns().add(tableColumn);
                        });
                        sqlColumn.getColumns().forEach(tableColumn -> {
                            NumberBinding divide = Bindings.divide(sqlColumn.widthProperty(), new SimpleIntegerProperty(sqlColumn.getColumns().size()));
                            tableColumn.prefWidthProperty().bind(divide);
                        });
                        ObservableList<LinkedHashMap<String, Object>> observableArrayList = FXCollections.observableArrayList();
                        observableArrayList.addAll(list);
                        sqlColumn.setItems(observableArrayList);
                        sqlColumn.setEditable(true);
                    });
                } else {
                    Platform.runLater(() -> {
                        sqlColumn.getColumns().clear();
                        sqlColumn.setItems(null);
                    });
                }
                return null;
            }
        }).start();
    }

    public void initProperty() {
        //动态绑定窗口宽度
        Platform.runLater(() -> {
            left.minWidthProperty().bind(root.getScene().widthProperty().multiply(0.1));
            left.maxWidthProperty().bind(root.getScene().widthProperty().multiply(0.4));
            left.prefWidthProperty().bind(root.getScene().widthProperty().multiply(0.15));
            right.minWidthProperty().bind(root.getScene().widthProperty().multiply(0.05));
            right.maxWidthProperty().bind(root.getScene().widthProperty().multiply(0.4));
            right.prefWidthProperty().bind(root.getScene().widthProperty().multiply(0.1));
            sqlText.minHeightProperty().bind(root.getScene().heightProperty().multiply(0.2));
            sqlText.maxHeightProperty().bind(root.getScene().heightProperty().multiply(0.6));
            sqlText.prefHeightProperty().bind(root.getScene().heightProperty().multiply(0.2));
            bottom.minHeightProperty().bind(root.getScene().heightProperty().multiply(0.02));
        });

        //鼠标移入left右边界，更换鼠标控件
        left.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (event.getX() >= left.getWidth() - 5) {
                left.setCursor(Cursor.E_RESIZE);
            } else {
                left.setCursor(Cursor.DEFAULT);
            }
        });

        //鼠标移入right左边界，更换鼠标控件
        right.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (event.getX() <= 5) {
                right.setCursor(Cursor.W_RESIZE);
            } else {
                right.setCursor(Cursor.DEFAULT);
            }
        });

        //鼠标移入sqlText下边界，更换鼠标控件
        sqlText.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (event.getY() >= sqlText.getHeight() - 5) {
                sqlText.setCursor(Cursor.S_RESIZE);
            } else {
                sqlText.setCursor(Cursor.DEFAULT);
            }
        });

        //鼠标拖动，自动更新left宽度
        left.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (left.getCursor() == Cursor.E_RESIZE) {
                if (left.prefWidthProperty().isBound()) {
                    left.prefWidthProperty().unbind();
                }
                left.setPrefWidth(event.getX());
            }
        });

        //鼠标拖动，自动更新right宽度
        right.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (right.getCursor() == Cursor.W_RESIZE) {
                if (right.prefWidthProperty().isBound()) {
                    right.prefWidthProperty().unbind();
                }
                right.setPrefWidth(right.getWidth() - event.getX());
            }
        });

        //鼠标拖动，自动更新sqlText高度
        sqlText.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (sqlText.getCursor() == Cursor.S_RESIZE) {
                if (sqlText.prefHeightProperty().isBound()) {
                    sqlText.prefHeightProperty().unbind();
                }
                sqlText.setPrefHeight(event.getY());
            }
        });
    }

    public void connectTree() throws Exception {
        TreeItem<DatabaseModel> treeItem = connectHistory.getSelectionModel().getSelectedItem();
        switch (treeItem.getValue().getType()) {
            case Constant.SHOW_DATABASES -> connectTreeShowDatabases(treeItem);
            case Constant.SHOW_TABLES -> connectTreeShowTables(treeItem);
            case Constant.SHOW_DATA -> connectTreeShowData(treeItem);
        }
        Platform.runLater(() -> treeItem.setExpanded(true));
    }

    public void connectTreeShowDatabases(TreeItem<DatabaseModel> treeItem) throws Exception {
        DatabaseUtil.getConnect(treeItem.getValue());
        ResultSet resultSet = DatabaseUtil.execute("SHOW DATABASES;");
        if (resultSet != null) {
            while (resultSet.next()) {
                String result = resultSet.getString("database");
                if (!BaseUtil.isEmpty(result)) {
                    ObservableList<TreeItem<DatabaseModel>> children = treeItem.getChildren();
                    long count = children.stream().map(TreeItem::getValue)
                            .filter(databaseModel -> databaseModel.getName().equals(result) && databaseModel.getType() == 1)
                            .count();
                    if (count == 0) {
                        Platform.runLater(() -> {
                            TreeItem<DatabaseModel> child = new TreeItem<>(new DatabaseModel(result, 1));
                            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.DATABASE);
                            fontIcon.setIconColor(Color.valueOf("#338ecc"));
                            child.setGraphic(fontIcon);
                            children.add(child);
                        });
                    }
                }
            }
            DatabaseUtil.close(resultSet);
        }
    }

    public void connectTreeShowTables(TreeItem<DatabaseModel> treeItem) throws Exception {
        DatabaseModel parent = treeItem.getParent().getValue();
        parent.setDatabase(treeItem.getValue().getName());
        DatabaseUtil.getConnect(parent);
        ResultSet resultSet = DatabaseUtil.execute(
                "SELECT information_schema.`tables`.`table_name` FROM information_schema.`tables` WHERE information_schema.`tables`.`table_schema`='" + treeItem.getValue().getName() + "'");
        if (resultSet != null) {
            while (resultSet.next()) {
                String result = resultSet.getString("table_name");
                if (!BaseUtil.isEmpty(result)) {
                    ObservableList<TreeItem<DatabaseModel>> children = treeItem.getChildren();
                    long count = children.stream().map(TreeItem::getValue)
                            .filter(databaseModel -> databaseModel.getName().equals(result) && databaseModel.getType() == 2)
                            .count();
                    if (count == 0) {
                        Platform.runLater(() -> {
                            TreeItem<DatabaseModel> child = new TreeItem<>(new DatabaseModel(result, 2));
                            child.setGraphic(new FontIcon(FontAwesomeSolid.TABLE));
                            children.add(child);
                        });
                    }
                }
            }
            DatabaseUtil.close(resultSet);
        }
    }

    public void connectTreeShowData(TreeItem<DatabaseModel> treeItem) throws Exception {
        DatabaseModel parent = treeItem.getParent().getParent().getValue();
        parent.setDatabase(treeItem.getParent().getValue().getName());
        DatabaseUtil.getConnect(parent);
        ResultSet resultSet = DatabaseUtil.execute(
                "SELECT * FROM `" + treeItem.getValue().getName() + "` LIMIT 1000;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        if (resultSet != null) {
            if (resultSet.next()) {
                resultSet.previous();
                while (resultSet.next()) {
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
                    }
                    list.add(map);
                }
            } else {
                resultSet = DatabaseUtil.execute("SELECT * FROM information_schema.`columns` WHERE information_schema.`columns`.`table_schema`='"
                        + treeItem.getParent().getValue().getName() + "' AND information_schema.`columns`.`table_name`='" + treeItem.getValue().getName() + "';");
                if (resultSet != null) {
                    while (resultSet.next()) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int columnCount = resultSetMetaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
                        }
                        list.add(map);
                    }
                }
            }
            DatabaseUtil.close(resultSet);
        }
        if (!list.isEmpty()) {
            Platform.runLater(() -> {
                sqlColumn.getColumns().clear();
                sqlColumn.setItems(null);
                Set<String> keySet = list.get(0).keySet();
                keySet.forEach(string -> {
                    TableColumn<LinkedHashMap<String, Object>, String> tableColumn = new TableColumn<>(string);
                    tableColumn.setMinWidth(100);
                    tableColumn.setCellValueFactory(factory -> new SimpleStringProperty(
                            factory.getValue().get(string) != null ? factory.getValue().get(string).toString() : null));
                    tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                    sqlColumn.getColumns().add(tableColumn);
                });
                sqlColumn.getColumns().forEach(tableColumn -> {
                    NumberBinding divide = Bindings.divide(sqlColumn.widthProperty(), new SimpleIntegerProperty(sqlColumn.getColumns().size()));
                    tableColumn.prefWidthProperty().bind(divide);
                });
                ObservableList<LinkedHashMap<String, Object>> observableArrayList = FXCollections.observableArrayList();
                observableArrayList.addAll(list);
                sqlColumn.setItems(observableArrayList);
                sqlColumn.setEditable(true);
            });
        } else {
            Platform.runLater(() -> {
                sqlColumn.getColumns().clear();
                sqlColumn.setItems(null);
            });
        }
    }

}
