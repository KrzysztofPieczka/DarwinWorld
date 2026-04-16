package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterMap extends AbstractWorldMap {

    private final Map<Vector2d, Water> water = new HashMap<>();
    private final int time = 3;
    private final int range = 3;

    public WaterMap(int width, int height, int numberOfPlants) {
        super(width,height,numberOfPlants);
        generateWater((width*height)/30);
    }

    private void generateWater(int waterSources){
        int x,y;
        for(int i = 0; i < waterSources; i++){
            do{
                x = (int) Math.round(Math.random()*bounds.upperRight().getX());
                y = (int) Math.round(Math.random()*bounds.upperRight().getY());
            }while(water.containsKey(new Vector2d(x,y)));
            water.put(new Vector2d(x,y), new Water(new Vector2d(x,y)));
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(position.precedes(bounds.upperRight()) && position.follows(bounds.lowerLeft())){
            for(Water water : water.values()){
                if(water.isAt(position)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        for(Water water : water.values()){
            if(water.isAt(position)){
                return water;
            }
        }
        for(Animal entry : animals.keySet()) {
            if(entry.getPosition().equals(position)) {
                return entry;
            }
        }
        return plants.get(position);
    }

    private void ebbAndFlow(int day){
        if(day%time == 0){
            for(Water water : water.values()){
                water.ebbOrFlow(range);
            }
        }
    }

    public void cleanUp(int day){
        ebbAndFlow(day);
        for(Animal animal : animals.keySet()){
            Vector2d space = animal.getPosition();
            for(Water water : water.values()){
                if(water.isAt(space)){
                    animal.setEnergy(0);
                }
            }
        }
        for(Plant plant : List.copyOf(plants.values())){
            Vector2d space = plant.getPosition();
            for(Water water : water.values()){
                if(water.isAt(space)){
                    plants.remove(space);
                }
            }
        }
    }

    public List<Vector2d> getWaterSources() {
        List<Vector2d> sources = new ArrayList<>();
        for(Water water : water.values()){
            sources.add(water.getPosition());
        }
        return sources;
    }
}