package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.Parameters;


import java.util.*;


public class Simulation {
    private final WorldMap map;
    private final LinkedList<Animal> aliveAnimals = new LinkedList<>();
    private final int dailyGrowth;
    private final Parameters parameters;
    private final List<MapChangeListener> observers = new ArrayList<>();
    private int day=1;

    //Constuctor
    public Simulation(WorldMap map, Parameters parameters) throws IncorrectPositionException {
        this.map = map;
        this.dailyGrowth = parameters.plantGrowth();
        this.parameters = parameters;
        generateAnimals();
    }

    //Getters
    public LinkedList<Animal> getAliveAnimals() {
        return aliveAnimals;
    }
    public WorldMap getMap() {
        return map;
    }
    public int getDay() {
        return day;
    }

    private void generateAnimals() throws IncorrectPositionException {
        Random random = new Random();
        int x, y;
        Animal newborn;
        for (int i = 0; i < parameters.startingAnimals(); i++) {
            do {
                x = random.nextInt(map.getBounds().upperRight().getX() + 1);
                y = random.nextInt(map.getBounds().upperRight().getY() + 1);
                newborn = AnimalFactory.createAnimal(parameters.animalType(), new Vector2d(x,y),parameters);
            } while (!map.place(newborn));
            aliveAnimals.add(newborn);
        }
    }

    private void grabCorpses() {
        List<Animal> corpses = new ArrayList<>();
        for(Animal animal : aliveAnimals){
            if(animal.getEnergy() <= 0){
                corpses.add(animal);
                map.getAnimals().remove(animal);
            }
        }
        aliveAnimals.removeAll(corpses);
    }

    private void moving() {
        for(Animal animal : aliveAnimals) {
            map.move(animal);
        }
    }

    private void consumption() {
        Map<Vector2d, Plant> plants = map.getPlants();
        for(Animal animal : aliveAnimals){
            Vector2d position = animal.getPosition();
            if(plants.containsKey(position)) {
                Animal winner = animalPriority(map.animalsAt(position));
                if(winner == animal){
                    plants.remove(position);
                    animal.eat();
                }
            }
        }
    }

    private void breeding() {
        List<Animal> children = new ArrayList<>();

        int width = map.getBounds().upperRight().getX()+1;
        int height = map.getBounds().upperRight().getY()+1;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Vector2d place = new Vector2d(i,j);
                List<Animal> animals = new ArrayList<>(List.copyOf(map.animalsAt(place)));
                for(int k = 0; k < (animals.size()/2); k++) {
                    Animal mate1 = animalPriority(animals);
                    animals.remove(mate1);
                    Animal mate2 = animalPriority(animals);
                    animals.remove(mate2);
                    if(mate1.getEnergy() < parameters.breedingEnergy() || mate2.getEnergy() < parameters.breedingEnergy()){
                        break;
                    }
                    Animal child = mate1.breed(mate2);
                    try {
                        if (map.place(child)) {
                            children.add(child);
                        }
                    } catch (IncorrectPositionException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        aliveAnimals.addAll(children);
    }

    private Animal animalPriority(List<Animal> competitors){
        Animal winner = competitors.getFirst();
        for(Animal animal : competitors){
            if(animal.getEnergy() > winner.getEnergy()){
                winner = animal;
            }
            else if (animal.getEnergy() == winner.getEnergy() && winner != animal){
                if(animal.getAge() > winner.getAge()){
                    winner = animal;
                }
                else if(animal.getAge() == winner.getAge()){
                    if(animal.getOffspring() > winner.getOffspring()){
                        winner = animal;
                    }
                    else if(animal.getOffspring() == winner.getOffspring()){
                        if(Math.random() > 0.5){
                            winner = animal;
                        }
                    }
                }
            }
        }
        return winner;
    }

    private void growth () {
        map.generatePlants(dailyGrowth);
    }

    private synchronized void day()  {
        grabCorpses();
        checkPause();
        moving();
        checkPause();
        mapChanged("Day: "+day+", Animals have moved to new positions.");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        consumption();
        checkPause();
        breeding();
        checkPause();
        mapChanged("Day: "+day+", Animals have consumed plants, bred and new offspring appeared.");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        growth();
        checkPause();
        mapChanged("Day: "+day+", Plants have grown.");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(map instanceof WaterMap){
            ((WaterMap) map).cleanUp(day);
            checkPause();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void run() {
        while(!aliveAnimals.isEmpty())
        {
            day();
            day++;
        }
    }
    private boolean paused = false;
    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    private void checkPause() {
        if (paused) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void mapChanged(String message) {
        for(MapChangeListener observer : observers) {
            observer.mapChanged(map, message);
        }
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }
}
