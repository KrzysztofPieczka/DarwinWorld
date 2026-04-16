package agh.ics.oop.presenter;

import agh.ics.oop.model.SphericalMap;
import agh.ics.oop.model.WaterMap;
import agh.ics.oop.model.util.Parameters;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewWorldPresenter extends AbstractPresenter {

    @FXML private TextField mapWidthField;
    @FXML private TextField mapHeightField;
    @FXML private ComboBox<String> mapVariantBox;
    @FXML private TextField initialPlantCountField;
    @FXML private TextField plantEnergyField;
    @FXML private TextField dailyPlantGrowthField;
    @FXML private TextField initialAnimalCountField;
    @FXML private TextField initialAnimalEnergyField;
    @FXML private TextField reproductionEnergyField;
    @FXML private TextField offspringEnergyField;
    @FXML private TextField genomeLengthField;
    @FXML private ComboBox<String> animalBehaviorBox;
    @FXML private CheckBox saveToFileCheckBox;
    @FXML
    private void initialize() {
        // Ustawienie domyślnych wartości w ComboBox
        mapVariantBox.setValue("Spherical World");
        animalBehaviorBox.setValue("Randomness");
    }

    @FXML
    private void onStartClick() {
        if (isFormValid()) {
            try {
                // Pobieranie wartości z formularza
                int mapWidth = Integer.parseInt(mapWidthField.getText());
                int mapHeight = Integer.parseInt(mapHeightField.getText());
                int initialPlantCount = Integer.parseInt(initialPlantCountField.getText());
                int plantEnergy = Integer.parseInt(plantEnergyField.getText());
                int dailyPlantGrowth = Integer.parseInt(dailyPlantGrowthField.getText());
                int initialAnimalCount = Integer.parseInt(initialAnimalCountField.getText());
                int initialAnimalEnergy = Integer.parseInt(initialAnimalEnergyField.getText());
                int reproductionEnergy = Integer.parseInt(reproductionEnergyField.getText());
                int offspringEnergy = Integer.parseInt(offspringEnergyField.getText());
                int genomeLength = Integer.parseInt(genomeLengthField.getText());
                String animalBehavior = animalBehaviorBox.getValue();
                String mapVariant = mapVariantBox.getValue();
                boolean saveToFile = saveToFileCheckBox.isSelected();

                FXMLLoader simulationLoader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
                Scene simulationScene = new Scene(simulationLoader.load());
                SimulationPresenter simulationPresenter = simulationLoader.getController();
                simulationPresenter.setWorldMap(new SphericalMap(mapWidth, mapHeight, initialPlantCount));
                if(Objects.equals(mapVariant, "Spherical World")) {
                    simulationPresenter.setWorldMap(new SphericalMap(mapWidth, mapHeight, initialPlantCount));
                }
                else {
                    simulationPresenter.setWorldMap(new WaterMap(mapWidth, mapHeight, initialPlantCount));
                }

                simulationPresenter.setSimulation(new Parameters(
                        dailyPlantGrowth,
                        initialAnimalCount,
                        initialAnimalEnergy,
                        plantEnergy,
                        reproductionEnergy,
                        offspringEnergy,
                        genomeLength,
                        animalBehavior));
                simulationPresenter.setSaving(saveToFile);
                simulationPresenter.addObserver(simulationPresenter);

                Stage simulationStage = new Stage();
                simulationStage.setScene(simulationScene);
                simulationStage.setTitle("Simulation");
                simulationStage.setResizable(false);
                simulationStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));

                LoadMainMenu();
                simulationStage.show();
                simulationPresenter.drawMap(false,false);
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
            }
        } else {
            showError("All fields must be filled with valid integer values.");
        }
    }
    @FXML
    private TextField configurationNameField;
    @FXML
    private void onSaveClick() {
        if (isFormValid() && configurationNameField.getText() != null) {
            try {
                // Pobieranie wartości z formularza
                int mapWidth = Integer.parseInt(mapWidthField.getText());
                int mapHeight = Integer.parseInt(mapHeightField.getText());
                int initialPlantCount = Integer.parseInt(initialPlantCountField.getText());
                int plantEnergy = Integer.parseInt(plantEnergyField.getText());
                int dailyPlantGrowth = Integer.parseInt(dailyPlantGrowthField.getText());
                int initialAnimalCount = Integer.parseInt(initialAnimalCountField.getText());
                int initialAnimalEnergy = Integer.parseInt(initialAnimalEnergyField.getText());
                int reproductionEnergy = Integer.parseInt(reproductionEnergyField.getText());
                int offspringEnergy = Integer.parseInt(offspringEnergyField.getText());
                int genomeLength = Integer.parseInt(genomeLengthField.getText());
                String animalBehavior = animalBehaviorBox.getValue();
                String mapVariant = mapVariantBox.getValue();
                String configurationName = configurationNameField.getText();

                // Tworzenie mapy wartości
                Map<String, Object> configuration = new HashMap<>();
                configuration.put("mapWidth", mapWidth);
                configuration.put("mapHeight", mapHeight);
                configuration.put("initialPlantCount", initialPlantCount);
                configuration.put("plantEnergy", plantEnergy);
                configuration.put("dailyPlantGrowth", dailyPlantGrowth);
                configuration.put("initialAnimalCount", initialAnimalCount);
                configuration.put("initialAnimalEnergy", initialAnimalEnergy);
                configuration.put("reproductionEnergy", reproductionEnergy);
                configuration.put("offspringEnergy", offspringEnergy);
                configuration.put("genomeLength", genomeLength);
                configuration.put("animalBehaviour", animalBehavior);
                configuration.put("mapVariant", mapVariant);

                // Serializacja do pliku JSON
                ObjectMapper objectMapper = new ObjectMapper();
                File file = new File("saved_configurations/" + configurationName + ".json");
                objectMapper.writeValue(file, configuration);

                showSuccess("Configuration saved as: " + file.getName());
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
                showError("Error saving configuration: " + e.getMessage());
            }

        } else {
            showError("All fields must be filled with valid integer values.");
        }
    }

    private boolean isFormValid() {
        return isInteger(mapWidthField) && isInteger(mapHeightField)
                && isInteger(initialPlantCountField) && isInteger(plantEnergyField)
                && isInteger(dailyPlantGrowthField) && isInteger(initialAnimalCountField)
                && isInteger(initialAnimalEnergyField) && isInteger(reproductionEnergyField)
                && isInteger(offspringEnergyField) && isInteger(genomeLengthField);
    }

    private boolean isInteger(TextField field) {
        try {
            Integer.parseInt(field.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
