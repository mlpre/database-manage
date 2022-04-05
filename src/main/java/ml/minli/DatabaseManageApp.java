package ml.minli;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import ml.minli.util.ConfigUtil;
import ml.minli.util.LanguageUtil;
import ml.minli.util.ResourceUtil;
import ml.minli.util.UiUtil;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

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
        Parent main = FXMLLoader.load(ResourceUtil.getResource("fxml/main.fxml"), LanguageUtil.resourceBundle);
        stage.setTitle(LanguageUtil.getValue("title"));
        FontIcon titleIcon = new FontIcon(FontAwesomeSolid.DATABASE);
        titleIcon.setIconColor(Paint.valueOf("#FFFFFF"));
        Scene scene = new Scene(main);
        UiUtil.setCss(scene);
        UiUtil.setIcon(scene);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.DATABASE);
            UiUtil.setCss(alert.getDialogPane());
            UiUtil.setIcon(alert.getDialogPane());
            fontIcon.setIconSize(50);
            fontIcon.setIconColor(Color.valueOf("#338ecc"));
            alert.setGraphic(fontIcon);
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
