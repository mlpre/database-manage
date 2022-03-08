package ml.minli.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class UiUtil {

    public static void AlertInformation(String headerText, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("信息");
            alert.setHeaderText(headerText);
            alert.setContentText(content);
            alert.show();
        });
    }

    public static void AlertWarning(String headerText, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(headerText);
            alert.setContentText(content);
            alert.show();
        });
    }

    public static void AlertError(String headerText, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(headerText);
            alert.setContentText(content);
            alert.show();
        });
    }

    public static void AlertException(Exception exception) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("Exception");
            alert.setContentText(null);
            TextArea textArea = new TextArea(exception.getMessage());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
            alert.show();
        });
    }

}
