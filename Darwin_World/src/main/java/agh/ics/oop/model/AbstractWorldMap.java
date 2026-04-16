package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Map<Animal, Vector2d> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final UUID mapID = UUID.randomUUID();
    protected Boundary bounds;

    AbstractWorldMap(int width, int height, int numberOfPlants){
        Vector2d upperRight = new Vector2d(width-1,height-1);
        Vector2d lowerLeft = new Vector2d(0, 0);
        this.bounds = new Boundary(lowerLeft, upperRight);
        generatePlants(numberOfPlants);
    }

    @Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition())) {
            this.animals.put(animal, animal.getPosition());
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal) {
        animals.remove(animal);
        animal.move(this);
        animals.put(animal, animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        for(Animal entry : animals.keySet()) {
            if(entry.getPosition().equals(position)) {
                return entry;
            }
        }
        return plants.get(position);
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        List<Animal> objects = new ArrayList<>();
        for(Animal entry : animals.keySet()) {
            if(entry.getPosition().equals(position)) {
                objects.add(entry);
            }
        }
        return objects;
    }

    public abstract boolean canMoveTo(Vector2d position);

    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>(List.copyOf(animals.keySet()));
        elements.addAll(plants.values());
        return elements;
    }
    @Override
    public Map<Vector2d, Plant> getPlants(){
        return plants;
    }
    @Override
    public Map<Animal,Vector2d> getAnimals() {
        return animals;
    }

    public Boundary getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        Boundary bounds = getBounds();
        return visualizer.draw(bounds.lowerLeft(), bounds.upperRight());
    }

    @Override
    public void generatePlants(int numberOfPlants) {
        Random random = new Random();
        int height = getBounds().upperRight().getY();
        int width = getBounds().upperRight().getX();
        int middleStart = (int) Math.floor(height * 0.4);
        int middleEnd = (int) Math.ceil(height * 0.6);
        int x, y;
        int maxPlantsInEquator = (middleEnd - middleStart+1)*(width+1);
        int plantsInEquator = countPlantsInEquator();
        for (int i = 0; i < numberOfPlants; i++) {
            if(plants.size()==(height+1)*(width+1)) {
                return;
            }
            double randomValue = random.nextDouble();
            boolean switchPlace = randomValue >= 0.2;
            do {
                x = random.nextInt(width + 1);
                if (switchPlace && plantsInEquator<maxPlantsInEquator) {
                    y = random.nextInt(middleEnd - middleStart + 1) + middleStart;
                    plantsInEquator++;
                } else {
                    if (random.nextBoolean()) {
                        // Zakres od 0 do middleStart
                        y = random.nextInt(middleStart);
                    } else {
                        // Zakres od middleEnd do Height
                        y = random.nextInt(height - middleEnd) + middleEnd+1;
                    }
                }
            }
            while (plants.containsKey(new Vector2d(x, y)) );
            plants.put(new Vector2d(x, y), new Plant(new Vector2d(x, y)));
        }
    }

    private int countPlantsInEquator() {
        int height = getBounds().upperRight().getY();
        int width = getBounds().upperRight().getX();
        int middleStart = (int) Math.floor(height * 0.4);
        int middleEnd = (int) Math.ceil(height * 0.6);
        int count = 0;
        for(int i=0;i<=width;i++) {
            for (int j=middleStart;j<=middleEnd;j++) {
                if (plants.containsKey(new Vector2d(i,j))){
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public UUID getID(){
        return mapID;
    }

}
