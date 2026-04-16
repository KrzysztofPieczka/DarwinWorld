package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MenuPresenter {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pobierz kontroler nowej sceny i ustaw główne okno, jeśli konieczne
            Object controller = loader.getController();
            if (controller instanceof MenuPresenter) {
                ((MenuPresenter) controller).setPrimaryStage(primaryStage);
            }

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    @FXML
    private void onCreateNewWorldClick() {
        switchScene("/NewWorldMenu.fxml");
    }

    @FXML
    private void onLoadWorldClick() {
        switchScene("/LoadWorldMenu.fxml");
    }

    @FXML
    private void onQuitClick() {
        System.exit(0);
    }
    @FXML
    private void onCreditsClick() {
        switchScene("/Credits.fxml");
    }
}
