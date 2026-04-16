package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaterMapTest {

    @Test
    void placeTest(){
        //given
        WaterMap map = new WaterMap(5,6,0);
        Vector2d source = map.getWaterSources().getFirst();

        //then
        assertFalse(map.place(new Animal(source, List.of(1))));
        assertTrue(map.place(new Animal(source.add(new Vector2d(0,1)), List.of(1))));
    }

    @Test
    void canMoveToTest(){
        //given
        WaterMap map = new WaterMap(5,6,0);
        Vector2d source = map.getWaterSources().getFirst();

        //then
        assertFalse(map.canMoveTo(source));
        assertTrue(map.canMoveTo(source.add(new Vector2d(0,1))));
    }

    @Test
    void cleanUpTest(){
        //given
        WaterMap map = new WaterMap(5,6,0);
        Vector2d source = map.getWaterSources().getFirst();
        Animal animal = new Animal(source.add(new Vector2d(1,0)), List.of(0));
        map.place(animal);

        //when
        map.cleanUp(0);

        //then
        assertEquals(0, animal.getEnergy());
    }

}