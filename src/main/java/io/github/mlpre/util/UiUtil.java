package io.github.mlpre.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UiUtil {

    public static void alert(String headerText, String content, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            UiUtil.setCss(alert.getDialogPane());
            UiUtil.setIcon(alert.getDialogPane());
            alert.setHeaderText(headerText);
            alert.setContentText(content);
            alert.show();
        });
    }

    public static void alertException(Throwable throwable) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            UiUtil.setCss(alert.getDialogPane());
            UiUtil.setIcon(alert.getDialogPane());
            TextArea textArea = new TextArea(throwable.getMessage());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
            alert.show();
        });
    }

    public static String getAppCss() {
        return ResourceUtil.getResource("css/app.css").toExternalForm();
    }

    public static void setCss(Scene scene) {
        Platform.runLater(() -> scene.getStylesheets().add(UiUtil.getAppCss()));
    }

    public static void setCss(Node node) {
        Platform.runLater(() -> node.getScene().getStylesheets().add(UiUtil.getAppCss()));
    }

    public static void setIcon(Scene scene) {
        Platform.runLater(() -> ((Stage) scene.getWindow()).getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png"))));
    }

    public static void setIcon(Node node) {
        Platform.runLater(() -> ((Stage) node.getScene().getWindow()).getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png"))));
    }

}
