package agh.ics.oop.model;

import agh.ics.oop.model.util.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphericalMapTest {
    @Test
    void ifMapWorks() {
        //given
        WorldMap map = new SphericalMap(5,5,10);
        Animal animal = new Animal(new Vector2d(2,2), List.of(0));

        //when
        try {
            assertTrue(map.place(animal));
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        //then
        assertEquals(new Vector2d(2, 2), animal.getPosition());
    }
    @Test
    void generatePlantsTest(){
        //given
        WorldMap map = new SphericalMap(5,5,0);
        //when
        map.generatePlants(10);
        //then
        assertEquals(10,map.getElements().size());
    }
    @Test
    void generateTooManyPlantsPlantsTest(){
        //given
        int width =5;
        int height =5;
        WorldMap map = new SphericalMap(width,height,0);
        //when
        map.generatePlants(1000);
        //then
        assertEquals(width*height,map.getElements().size());
    }
    @Test
    void generateZeroPlantsTest(){
        //given
        int width =5;
        int height =5;
        WorldMap map = new SphericalMap(width,height,0);

        //then
        assertEquals(0,map.getElements().size());
    }

}