package agh.ics.oop.model;

import agh.ics.oop.model.util.Parameters;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {


    @Test
    void isAtTest(){
        //given
        Animal animal = new Animal(new Vector2d(2,8),List.of(0));

        //then
        assertTrue(animal.isAt(new Vector2d(2,8)));
        assertFalse(animal.isAt(new Vector2d(1,2)));
    }

    Parameters parameters = new Parameters(0,2,20,8,15,10,6,"Randomness");
    @Test
    void goodMoveTest(){
        //given
        WorldMap map = new SphericalMap(5,5,0);
        List<Integer> genes = List.of(0,1);
        Animal animal = new Animal(new Vector2d(2,2), genes,parameters,0);
        Animal animal1 = new Animal(new Vector2d(0,0), genes,parameters,0);

        //when
        animal.move(map);
        animal1.move(map);
        animal1.move(map);

        //then
        assertEquals(new Vector2d(2,3), animal.getPosition());
        assertEquals(MapDirection.NORTH, animal.getCurrentDirection());
        assertEquals(new Vector2d(1,2), animal1.getPosition());
        assertEquals(MapDirection.NORTH_EAST, animal1.getCurrentDirection());
    }

    @Test
    void outOfBoundsMoveTest(){
        //given
        WorldMap map = new SphericalMap(2,2,0);
        Animal animal = new Animal(new Vector2d(2,2), List.of(0,0),parameters,0);

        //when
        animal.move(map);

        //then
        assertEquals(new Vector2d(2,2), animal.getPosition());
        assertEquals(MapDirection.SOUTH, animal.getCurrentDirection());
    }

    @Test
    void breedTest(){
        //given

        Animal animal1 = new Animal(new Vector2d(0,0), List.of(0,0,0,0,0,0),parameters,0);
        Animal animal2 = new Animal(new Vector2d(0,0), List.of(1,1,1,1,1,1),parameters,0);

        //when
        Animal child = animal1.breed(animal2);

        //then
        assertEquals(new Vector2d(0,0), child.getPosition());
        assertEquals(-10,animal1.getEnergy());
    }

}