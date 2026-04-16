package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {
    @Test
    void toStringTest() {
        //given
        Plant plant = new Plant(new Vector2d(2,2));
        //then
        assertEquals("*", plant.toString());
    }
    @Test
    void getPositionTest() {
        //given
        Plant plant = new Plant(new Vector2d(2,2));
        //then
        assertEquals(new Vector2d(2,2), plant.getPosition());
    }
    @Test
    void isAtTest() {
        Plant plant = new Plant(new Vector2d(2,2));
        assertTrue(plant.isAt(new Vector2d(2,2)));
    }
}