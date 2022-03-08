package ml.minli.controller;

import com.mysql.cj.util.StringUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ml.minli.model.DatabaseModel;
import ml.minli.model.DatabaseType;
import ml.minli.util.DatabaseUtil;
import ml.minli.util.UiUtil;
import ml.minli.util.BaseUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {

    @FXML
    public StackPane root;
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

    /**
     * 测试数据库连接
     */
    public synchronized void test() {
        if (BaseUtil.isEmpty(ip.getText())) {
            UiUtil.AlertWarning(null, "IP为空");
            return;
        }
        if (BaseUtil.isEmpty(port.getText())) {
            UiUtil.AlertWarning(null, "端口为空");
            return;
        }
        if (BaseUtil.isEmpty(username.getText())) {
            UiUtil.AlertWarning(null, "用户名为空");
            return;
        }
        if (BaseUtil.isEmpty(password.getText())) {
            UiUtil.AlertWarning(null, "密码为空");
            return;
        }
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    DatabaseUtil.getConnect(new DatabaseModel(
                            StringUtils.isNullOrEmpty(connectName.getText()) ? ip.getText() + "_" + port.getText() : connectName.getText(),
                            DatabaseType.MySQL.getUrl() + ip.getText(),
                            Integer.parseInt(port.getText()),
                            "mysql",
                            DatabaseType.MySQL.getDefaultParam(),
                            username.getText(),
                            password.getText(),
                            0));
                    boolean isValid = DatabaseUtil.connection.isValid(1000);
                    if (isValid) {
                        UiUtil.AlertInformation(null, "连接成功");
                    } else {
                        UiUtil.AlertError(null, "连接失败");
                    }
                } catch (Exception e) {
                    UiUtil.AlertException(e);
                    e.printStackTrace();
                }
                return null;
            }
        }).start();
    }

    /**
     * 保持数据库连接记录
     */
    public synchronized void save() {
        MainController.connectHistoryList.add(
                new DatabaseModel(
                        StringUtils.isNullOrEmpty(connectName.getText()) ? ip.getText() + "_" + port.getText() : connectName.getText(),
                        DatabaseType.MySQL.getUrl() + ip.getText(),
                        Integer.parseInt(port.getText()),
                        "mysql",
                        DatabaseType.MySQL.getDefaultParam(),
                        username.getText(),
                        password.getText(),
                        0)
        );
        DatabaseUtil.saveConnectHistory();
        ((Stage) root.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        port.setText(Integer.toString(DatabaseType.MySQL.getDefaultPort()));
    }
}
