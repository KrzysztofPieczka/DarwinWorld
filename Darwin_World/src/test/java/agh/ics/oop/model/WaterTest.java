package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterTest {

    @Test
    void getPositionTest(){
        //given
        Water water = new Water(new Vector2d(2,2));

        //then
        assertEquals(new Vector2d(2,2), water.getPosition());
    }

    @Test
    void isAtTest(){
        //given
        Water water = new Water(new Vector2d(2,2));

        //then
        assertTrue(water.isAt(new Vector2d(2,2)));
    }

    @Test
    void flowTest(){
        //given
        Water water = new Water(new Vector2d(2,2));

        //when
        water.ebbOrFlow(3);

        //then
        assertTrue(water.isAt(new Vector2d(1,1)));
        assertTrue(water.isAt(new Vector2d(3,3)));
        assertFalse(water.isAt(new Vector2d(3,4)));
    }

    @Test
    void ebbTest(){
        //given
        Water water = new Water(new Vector2d(2,2));

        //when
        water.ebbOrFlow(3);
        water.ebbOrFlow(3);
        water.ebbOrFlow(3);
        water.ebbOrFlow(3);

        //then
        assertFalse(water.isAt(new Vector2d(3,3)));
        assertTrue(water.isAt(new Vector2d(2,2)));


    }

}