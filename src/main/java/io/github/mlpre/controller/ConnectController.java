package io.github.mlpre.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import io.github.mlpre.model.Database;
import io.github.mlpre.model.DatabaseType;
import io.github.mlpre.util.DatabaseUtil;
import io.github.mlpre.util.LanguageUtil;
import io.github.mlpre.util.UiUtil;
import io.github.mlpre.util.BaseUtil;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {

    @FXML
    public VBox root;
    @FXML
    public TextField connectName;
    @FXML
    public TextField ip;
    @FXML
    public TextField port;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public TextField paramField;
    @FXML
    public TextField paramValue;
    @FXML
    public FlowPane paramList;

    /**
     * 测试数据库连接
     */
    public synchronized void test() {
        boolean isCheck = check();
        if (!isCheck) {
            return;
        }
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    DatabaseUtil.getConnect(new Database(
                            BaseUtil.isEmpty(connectName.getText()) ? ip.getText() + "_" + port.getText() : connectName.getText(),
                            DatabaseType.MySQL.getUrl() + ip.getText(),
                            Integer.parseInt(port.getText()),
                            DatabaseType.MySQL.getDefaultParam(),
                            username.getText(),
                            password.getText()));
                    boolean isValid = DatabaseUtil.connection.isValid(1000);
                    if (isValid) {
                        UiUtil.alert(null, "连接成功", Alert.AlertType.INFORMATION);
                    } else {
                        UiUtil.alert(null, "连接失败", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    UiUtil.alertException(e);
                    e.printStackTrace();
                }
                return null;
            }
        }).start();
    }

    public boolean check() {
        if (BaseUtil.isEmpty(ip.getText())) {
            UiUtil.alert(null, LanguageUtil.getValue("is.null.ip"), Alert.AlertType.WARNING);
            return false;
        }
        if (BaseUtil.isEmpty(port.getText())) {
            UiUtil.alert(null, LanguageUtil.getValue("is.null.port"), Alert.AlertType.WARNING);
            return false;
        }
        if (BaseUtil.isEmpty(username.getText())) {
            UiUtil.alert(null, LanguageUtil.getValue("is.null.username"), Alert.AlertType.WARNING);
            return false;
        }
        if (BaseUtil.isEmpty(password.getText())) {
            UiUtil.alert(null, LanguageUtil.getValue("is.null.password"), Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    /**
     * 保持数据库连接记录
     */
    public synchronized void save() {
        MainController.databaseList.add(
                new Database(
                        BaseUtil.isEmpty(connectName.getText()) ? ip.getText() + "_" + port.getText() : connectName.getText(),
                        DatabaseType.MySQL.getUrl() + ip.getText(),
                        Integer.parseInt(port.getText()),
                        DatabaseType.MySQL.getDefaultParam(),
                        username.getText(),
                        password.getText())
        );
        DatabaseUtil.saveConnectHistory();
        ((Stage) root.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        port.setText(Integer.toString(DatabaseType.MySQL.getDefaultPort()));
    }

    private static class Param extends HBox {
        public Param(FlowPane flowPane, String param) {
            super();
            HBox hBox = new HBox();
            hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
            HBox.setMargin(hBox, new Insets(5));
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label(param);
            label.setPadding(new Insets(5));
            Label button = new Label();
            FontIcon fontIcon = new FontIcon(FontAwesomeRegular.TIMES_CIRCLE);
            fontIcon.setIconSize(20);
            fontIcon.setIconColor(Color.valueOf("#338ecc"));
            button.setGraphic(fontIcon);
            button.setOnMouseClicked(actionEvent -> {
                flowPane.getChildren().remove(this);
                flowPane.getScene().getWindow().sizeToScene();
            });
            hBox.getChildren().addAll(label, button);
            this.getChildren().add(hBox);
        }
    }

    public void paramAdd() {
        paramList.getChildren().add(new Param(paramList, paramField.getText() + "=" + paramValue.getText()));
        paramList.getScene().getWindow().sizeToScene();
    }

}
