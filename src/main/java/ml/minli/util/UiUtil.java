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

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * @author Minli
 */
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
