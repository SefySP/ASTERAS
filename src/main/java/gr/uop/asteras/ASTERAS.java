package gr.uop.asteras;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ASTERAS extends Application
{
    private final double MIN_HEIGHT = 400.0;
    private final double MIN_WIDTH  = 400.0;

    private Stage primary;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ASTERAS.class.getResource("ASTERAS.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primary = stage;
        initializeStage(scene);

        AddingBib addingBib = new AddingBib(new File("src/main/resources/gr/uop/asteras/temp/bibfiles/platis.bib"));
        System.out.println(addingBib.getBibFieldData("publisher"));
    }

    public static void main(String[] args)
    {
        launch();
    }

    private void setIcon() throws IOException
    {
        File iconFile = new File("src/main/resources/gr/uop/asteras/icons/search32.png");
        Image icon = new Image(iconFile.toURI().toURL().toString());

        primary.getIcons().add(icon);
    }

    private void initializeStage(Scene scene) throws IOException
    {
        primary.setTitle("ASTERAS");
        primary.setScene(scene);
        setIcon();

        primary.show();

        primary.setMinHeight(MIN_HEIGHT);
        primary.setMinWidth(MIN_WIDTH);
    }
}