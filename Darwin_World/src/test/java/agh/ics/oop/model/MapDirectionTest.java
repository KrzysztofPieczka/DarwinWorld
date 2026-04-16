package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void changeDirectionTest(){
        //given
        MapDirection direction1 = MapDirection.EAST;
        MapDirection direction2 = MapDirection.NORTH;
        int turn1 = 2;
        int turn2 = 9;

        //when
        MapDirection newDir1 = direction1.change(turn1);
        MapDirection newDir2 = direction2.change(turn2);

        //then
        assertEquals(MapDirection.SOUTH, newDir1);
        assertEquals(MapDirection.NORTH_EAST, newDir2);
    }

}