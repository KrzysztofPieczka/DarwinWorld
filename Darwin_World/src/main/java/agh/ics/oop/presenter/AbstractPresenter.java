package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public abstract class AbstractPresenter{
    @FXML
    protected void onBackToMenuClick() {
        try {
            LoadMainMenu();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
    @FXML
    private Button backButton;
    protected void LoadMainMenu() throws IOException {
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenuLoader.load());
        MenuPresenter menuPresenter = mainMenuLoader.getController();
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        menuPresenter.setPrimaryStage(currentStage);
        currentStage.setScene(mainMenuScene);
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
