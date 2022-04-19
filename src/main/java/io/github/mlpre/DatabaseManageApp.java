package io.github.mlpre;

import io.github.mlpre.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.util.Optional;

public class DatabaseManageApp extends Application {

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConfigUtil.initConfig();
        Scene scene = new Scene(FXMLLoader.load(ResourceUtil.getResource("fxml/main.fxml"), LanguageUtil.resourceBundle));
        UiUtil.setCss(scene);
        UiUtil.setIcon(scene);
        stage.setTitle(LanguageUtil.getValue("title"));
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            UiUtil.setCss(alert.getDialogPane());
            UiUtil.setIcon(alert.getDialogPane());
            alert.setContentText(LanguageUtil.getValue("is.exit"));
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    Platform.exit();
                } else {
                    event.consume();
                }
            });
        });
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        ConfigUtil.properties.store(new FileOutputStream(ConfigUtil.DATABASE_CONFIG_FILE), null);
    }

}
