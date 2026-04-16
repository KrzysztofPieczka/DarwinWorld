package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.Parameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimulationPresenter implements MapChangeListener {

    private static final int MAP_MAX_HEIGHT = 450;
    private static final int MAP_MAX_WIDTH = 450;
    private int xMax;
    private int yMax;
    private int width ;
    private int height;

    private int freeSpaces; //Zmienna potrzebna do statystyk śledzenia ilości wolnych pól
    private Animal selectedAnimal = null;  // Zmienna potrzebna do funkcyjności śledzenia zwierzaka

    private WorldMap map;
    private Simulation simulation;
    public void setWorldMap(WorldMap worldMap) {
        this.map = worldMap;
        yMax = map.getBounds().upperRight().getY();
        xMax = map.getBounds().upperRight().getX();
        width = Math.min(MAP_MAX_WIDTH / (xMax + 1), MAP_MAX_HEIGHT / (yMax + 1));
        height = width; // Ensure cells remain square.

    }

    public void setSimulation(Parameters parameters) throws IncorrectPositionException {
        this.simulation = new Simulation(map, parameters);
    }

    public void addObserver(MapChangeListener observer) {
        this.simulation.addObserver(observer);
        this.simulation.addObserver(new ConsoleMapDisplay());
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(false,false);
            updateStatistics();
        });
    }

    @FXML
    private GridPane mapGrid;

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void addElements() {
        freeSpaces = 0;
        for (int i = 0; i <= xMax; i++) {
            for (int j = yMax; j >= 0; j--) {
                if (map.isOccupied(new Vector2d(i, j))) {
                    int finalI1 = i;
                    int finalJ1 = j;
                    switch (map.objectAt(new Vector2d(i, j))) {
                        case Animal animal -> {
                            StackPane cellContent = new StackPane();
                            ImageView animalImage = toImageView("/monkeyW.png", width, height);
                            cellContent.getChildren().add(animalImage);
                            ImageView energyBar = getEnergyBar(animal.getEnergy());
                            StackPane.setAlignment(energyBar, Pos.BOTTOM_CENTER);
                            cellContent.getChildren().add(energyBar);

                            //Funkcyjność śledzenia zwierzaka - .setOnMouseClicked
                            cellContent.setOnMouseClicked(_ -> {
                                if (map.objectAt(new Vector2d(finalI1, finalJ1)) instanceof Animal clickedAnimal) {
                                    selectedAnimal = clickedAnimal;
                                    drawMap(false,false);
                                }
                            });

                            mapGrid.add(cellContent, i + 1, yMax - j + 1);
                        }
                        case Water water -> mapGrid.add(toImageView("/water.png", width, height), i + 1, yMax - j + 1);
                        case Plant plant -> mapGrid.add(toImageView("/grass.png", width, height), i + 1, yMax - j + 1);
                        default -> throw new IllegalStateException("Unexpected value: " + map.objectAt(new Vector2d(i, j)));
                    }
                } else {
                    freeSpaces++;
                }
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
    }

    public ImageView toImageView(String imagePath, int width, int height) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    public ImageView getEnergyBar(int energy) {
        String imagePath;
        if (energy >= 50) {
            imagePath = "/energy_full.png";
        } else if (energy >= 10) {
            imagePath = "/energy_half.png";
        } else {
            imagePath = "/energy_empty.png";
        }

        return toImageView(imagePath, width, height / 5);
    }

    public void drawMap(boolean drawEquator, boolean drawCommonGene) {
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(20));
        mapGrid.getRowConstraints().add(new RowConstraints(20));
        for (int i = 0; i <= yMax; i++) {
            Label label = new Label(Integer.toString(yMax - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(height));
            mapGrid.add(label, 0, i + 1);
        }
        for (int i = 0; i <= xMax; i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
            mapGrid.add(label, i + 1, 0);
        }
        if (drawEquator) {
            highlightEquatorZone();
        }

        if (drawCommonGene) {
            drawMapWithCommonGeneHighlight();
        }

        addElements();

        // Funkycjność śledzenia zwierzaka
        if (selectedAnimal != null) {
            Vector2d position = selectedAnimal.getPosition();
            StackPane selectedCell = (StackPane) mapGrid.getChildren().stream()
                    .filter(node -> GridPane.getColumnIndex(node) != null &&
                            GridPane.getRowIndex(node) != null &&
                            GridPane.getColumnIndex(node) == position.getX() + 1 &&
                            GridPane.getRowIndex(node) == yMax - position.getY() + 1)
                    .findFirst()
                    .orElse(null);
            if (selectedCell != null) {
                selectedCell.setStyle("-fx-border-color: #f11010; -fx-border-width: 3px;");
            }
            updateSelectedAnimalStats();
        }
    }

    @FXML private Label animalsCountLabel;
    @FXML private Label plantsCountLabel;
    @FXML private Label freeSpacesCountLabel;
    @FXML private Label mostCommonGenotypeLabel;
    @FXML private Label avgEnergyLabel;
    @FXML private Label avgLifespanLabel;
    @FXML private Label avgOffspringLabel;

    public void updateStatistics() {
        int aliveAnimals = simulation.getAliveAnimals().size();
        int totalPlants = simulation.getMap().getElements().size() - simulation.getMap().getAnimals().size();
        int freeSpaces = this.freeSpaces;

        Map<String, Long> genotypeCount = simulation.getMap().getAnimals().keySet().stream()
                .map(animal -> animal.getGenes().toString())
                .collect(Collectors.groupingBy(genotype -> genotype, Collectors.counting()));
        String mostCommonGenotype = genotypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Genotype");

        double averageEnergy = simulation.getAliveAnimals().stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0);

        double averageLifespanOfDeadAnimals = Math.round((simulation.getMap().getAnimals().keySet().stream()
                .filter(Animal::isDead)
                .mapToLong(Animal::getAge)
                .average()
                .orElse(-1)
        ) * 100.0) / 100.0;

        double averageOffspringOfLivingAnimals = Math.round((simulation.getMap().getAnimals().keySet().stream()
                .filter(animal -> !animal.isDead())
                .mapToInt(Animal::getOffspring)
                .average()
                .orElse(-1)
        ) * 100.0) / 100.0;

        //GUI
        animalsCountLabel.setText("Alive animals: " + aliveAnimals);
        plantsCountLabel.setText("Total Plants: " + totalPlants);
        freeSpacesCountLabel.setText("Free Spaces: " + freeSpaces);
        mostCommonGenotypeLabel.setText("Most Common Genotype: " + mostCommonGenotype);
        avgEnergyLabel.setText("Average Energy: " + String.format("%.2f", averageEnergy));
        avgLifespanLabel.setText("Average Lifespan (dead): " + averageLifespanOfDeadAnimals);
        avgOffspringLabel.setText("Average Offspring: " + averageOffspringOfLivingAnimals);

        //CSV
        if (csvWriter != null && saving) {
            csvWriter.printf(
                    "\"%d\";\"%d\";\"%d\";\"%d\";\"%s\";\"%.2f\";\"%.2f\";\"%.2f\"%n",
                    simulation.getDay(),
                    aliveAnimals,
                    totalPlants,
                    freeSpaces,
                    mostCommonGenotype.replace("\"", "\"\""),
                    averageEnergy,
                    averageLifespanOfDeadAnimals,
                    averageOffspringOfLivingAnimals
            );
            csvWriter.flush();
        }

    }

    @FXML private Label genotypeLabel;
    @FXML private Label activeGenePartLabel;
    @FXML private Label energyLabel;
    @FXML private Label plantsEatenLabel;
    @FXML private Label childrenCountLabel;
    @FXML private Label ageLabel;
    @FXML private Label deathDayLabel;
    @FXML private Label descendantsLabel;
    public void updateSelectedAnimalStats() {
        if (selectedAnimal != null) {
            genotypeLabel.setText("Genotype: " + selectedAnimal.getGenes());
            activeGenePartLabel.setText("Active Gene Part: " + selectedAnimal.getActiveGene());
            energyLabel.setText("Energy: " + selectedAnimal.getEnergy());
            plantsEatenLabel.setText("Plants Eaten: " + selectedAnimal.getPlantsEaten());
            childrenCountLabel.setText("Children: " + selectedAnimal.getOffspring());
            descendantsLabel.setText("Descendants: " + selectedAnimal.getDescendants());
            ageLabel.setText("Age: " + selectedAnimal.getAge());
            deathDayLabel.setText("Death Day: " + (selectedAnimal.isDead() ? selectedAnimal.getAge() : "Still Alive"));
        }
    }

    private boolean saving = false;

    public void setSaving(boolean saving) {
        this.saving = saving;
        if (saving) {
            initializeCsvFile();
        }
    }

    private PrintWriter csvWriter;

    private void initializeCsvFile() {
        try {
            csvWriter = new PrintWriter(new FileWriter("statistics/simulation_statistics" + simulation.getMap().getID() + ".csv", true));
            // Nagłówki
            csvWriter.println("Day;Alive Animals;Total Plants;Free Spaces;Most Common Genotype;Average Energy;Average Lifespan;Average Offspring");
            csvWriter.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void onSimulationStartClicked() {
        Thread simulationThread = new Thread(simulation::run);
        simulationThread.start();
    }

    @FXML private Button pauseButton;
    @FXML private Button highlightGenotypeButton;
    @FXML private Button highlightPlantsButton;
    @FXML
    public void onPauseButtonClicked() {
        if (pauseButton.getText().equals("PAUSE")) {
            simulation.pause();
            pauseButton.setText("RESUME");
            highlightGenotypeButton.setVisible(true);//Najpopularniejszy genotyp
            highlightPlantsButton.setVisible(true);//Preferowane pola przez rośliny
            //Funkcyjność śledzenia zwierzaka
            if (selectedAnimal != null) {
                drawMap(false,false);
            }
        } else {
            simulation.resume();
            pauseButton.setText("PAUSE");
            highlightGenotypeButton.setVisible(false);//Najpopularniejszy genotyp
            highlightPlantsButton.setVisible(false);//Preferowane pola przez rośliny
        }
    }

    //Najpopularniejszy genotyp
    @FXML
    public void onHighlightButtonClicked() {
            drawMap(false,true);
    }

    private void drawMapWithCommonGeneHighlight() {
        List<Animal> animalsWithDominantGenotype = getAnimalsWithDominantGenotype();

        for (int i = 0; i <= xMax; i++) {
            for (int j = yMax; j >= 0; j--) {
                if (map.isOccupied(new Vector2d(i, j))) {
                    switch (map.objectAt(new Vector2d(i, j))) {
                        case Animal animal -> {
                            StackPane cellContent = new StackPane();
                            ImageView animalImage = toImageView("/monkeyW.png", width, height);
                            if (animalsWithDominantGenotype.contains(animal)) {
                                animalImage.setStyle("-fx-effect: dropshadow(gaussian, rgb(241,16,16), 5, 1, 0, 0);");
                            }
                            cellContent.getChildren().add(animalImage);
                            ImageView energyBar = getEnergyBar(animal.getEnergy());
                            StackPane.setAlignment(energyBar, Pos.BOTTOM_CENTER);
                            cellContent.getChildren().add(energyBar);
                            mapGrid.add(cellContent, i + 1, yMax - j + 1);
                        }
                        case Water water -> mapGrid.add(toImageView("/water.png", width, height), i + 1, yMax - j + 1);
                        case Plant plant -> mapGrid.add(toImageView("/grass.png", width, height), i + 1, yMax - j + 1);
                        default -> throw new IllegalStateException("Unexpected value: " + map.objectAt(new Vector2d(i, j)));
                    }
                }
            }
        }
    }

    private List<Animal> getAnimalsWithDominantGenotype() {
        Map<String, Long> genotypeCount = simulation.getMap().getAnimals().keySet().stream()
                .map(animal -> animal.getGenes().toString())
                .collect(Collectors.groupingBy(genotype -> genotype, Collectors.counting()));

        String mostCommonGenotype = genotypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Genotype");

        return simulation.getMap().getAnimals().keySet().stream()
                .filter(animal -> animal.getGenes().toString().equals(mostCommonGenotype))
                .collect(Collectors.toList());
    }


    //Pokazanie preferowanych pól przez rośliny
    @FXML
    public void onHighlightPlantsButtonClicked() {
        drawMap(true,false);
    }

    private void highlightEquatorZone() {
        int height = map.getBounds().upperRight().getY();
        int width = map.getBounds().upperRight().getX();
        int middleStart = (int) Math.floor(height * 0.4);
        int middleEnd = (int) Math.ceil(height * 0.6);

        // Wyróżnienie wszystkich pól w strefie równikowej
        for (int i = 0; i <= width; i++) {
            for (int j = middleStart; j <= middleEnd; j++) {
                StackPane equatorCell = new StackPane();
                equatorCell.setStyle("-fx-background-color: rgba(0, 128, 0, 0.2);");
                mapGrid.add(equatorCell, i + 1, yMax - j + 1);
            }
        }
    }
}
