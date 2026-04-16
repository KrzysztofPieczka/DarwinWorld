package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener{


    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.println("Map: " + worldMap.getID());
        System.out.println(message);
        System.out.println(worldMap);
    }
}
