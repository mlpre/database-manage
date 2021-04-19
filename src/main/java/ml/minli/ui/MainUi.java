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
package ml.minli.ui;

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
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.FileOutputStream;
import java.util.Optional;

/**
 * @author Minli
 */
public class MainUi extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ConfigUtil.initConfig();
        Parent main = FXMLLoader.load(ResourceUtil.getResource("fxml/main.fxml"), LanguageUtil.resourceBundle);
        stage.setTitle("数据库管理工具");
        FontIcon titleIcon = new FontIcon("fas-database");
        titleIcon.setIconColor(Paint.valueOf("#FFFFFF"));
        Scene scene = new Scene(main);
        stage.getIcons().add(new Image(ResourceUtil.getInputStream("img/logo.png")));
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("退出");
            FontIcon fontIcon = new FontIcon(FontAwesomeSolid.DATABASE);
            fontIcon.setIconSize(50);
            fontIcon.setIconColor(Color.valueOf("#338ecc"));
            alert.setGraphic(fontIcon);
            alert.setContentText("是否退出?");
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
