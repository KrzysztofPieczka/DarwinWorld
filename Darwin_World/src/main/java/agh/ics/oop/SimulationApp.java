package agh.ics.oop;

import agh.ics.oop.presenter.MenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/PixelifySans-VariableFont_wght.ttf"), 18);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Scene scene = new Scene(loader.load());

        MenuPresenter controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin World®");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        primaryStage.show();
    }
}
