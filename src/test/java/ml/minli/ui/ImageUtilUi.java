package ml.minli.ui;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Minli
 */
public class ImageUtilUi extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        List<FontIcon> allFont = ImageUtilUi.getOneAwesomeFont("fas-arrows-alt-h", Color.valueOf("#338ecc"));
        for (FontIcon fontIcon : allFont) {
            fontIcon.setIconSize(100);
            TextFlow textFlow = new TextFlow(fontIcon);
            textFlow.setTextAlignment(TextAlignment.CENTER);
            Scene scene = new Scene(textFlow);
            scene.setFill(null);
            stage.setScene(scene);
            stage.show();
            File file = new File("D://" + fontIcon.getIconLiteral() + ".png");
            WritableImage writableImage = new WritableImage((int) textFlow.getWidth(), (int) textFlow.getHeight());
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            textFlow.snapshot(snapshotParameters, writableImage);
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            stage.close();
        }
    }

    public static List<FontIcon> getOneAwesomeFont(String code, Color color) {
        List<FontIcon> fontIconList = new ArrayList<>();
        FontIcon fontIcon = new FontIcon(code);
        fontIcon.setIconColor(color);
        fontIconList.add(fontIcon);
        return fontIconList;
    }

    public static List<FontIcon> getAllAwesomeFont() {
        List<FontIcon> fontIconList = new ArrayList<>();
        FontAwesomeBrands[] fontAwesomeBrands = FontAwesomeBrands.values();
        for (FontAwesomeBrands value : fontAwesomeBrands) {
            fontIconList.add(new FontIcon(value));
        }
        FontAwesomeRegular[] fontAwesomeRegulars = FontAwesomeRegular.values();
        for (FontAwesomeRegular value : fontAwesomeRegulars) {
            fontIconList.add(new FontIcon(value));
        }
        FontAwesomeSolid[] fontAwesomeSolids = FontAwesomeSolid.values();
        for (FontAwesomeSolid value : fontAwesomeSolids) {
            fontIconList.add(new FontIcon(value));
        }
        return fontIconList;
    }
}
