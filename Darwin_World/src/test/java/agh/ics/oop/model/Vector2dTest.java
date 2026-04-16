package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void vectorStringVersion() {
        //given
        var vector = new Vector2d(1,2);

        //when
        var result = vector.toString();

        //then
        assertEquals("(1, 2)",result);
    }

    @Test
    void vectorsAreEqual() {
        //given
        var vector1 = new Vector2d(1,2);
        var vector2 = new Vector2d(1,2);
        var vector3 = new Vector2d(3,4);
        var vector4 = new Vector2d(1,1);
        var vector5 = new Vector2d(2,1);

        //when
        var result1 = vector1.equals(vector2);
        var result2 = vector1.equals(vector3);
        var result3 = vector1.equals(vector4);
        var result4 = vector1.equals(vector5);

        //then
        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    void vectorsCanBeAdded(){
        //given
        var vector1 = new Vector2d(1,2);
        var vector2 = new Vector2d(3,4);

        //when
        var resultVector = vector1.add(vector2);

        //then
        assertEquals(4, resultVector.getX());
        assertEquals(6, resultVector.getY());
    }

    @Test
    void vectorIsPrecededByOther(){
        //given
        var vector1 = new Vector2d(1,2);
        var vector2 = new Vector2d(3,4);

        //when
        var result = vector1.precedes(vector2);

        //then
        assertTrue(result);
    }

    @Test
    void vectorIsFollowedByOther(){
        //given
        var vector1 = new Vector2d(1,2);
        var vector2 = new Vector2d(3,4);

        //when
        var result = vector1.follows(vector2);

        //then
        assertFalse(result);
    }

    @Test
    void vectorsCanBeSubtracted(){
        //given
        var vector1 = new Vector2d(1,2);
        var vector2 = new Vector2d(3,4);

        //when
        var result = vector1.subtract(vector2);

        //then
        assertEquals(-2, result.getX());
        assertEquals(-2, result.getY());
    }

    @Test
    void vectorsUpperRight(){
        //given
        var vector1 = new Vector2d(5,1);
        var vector2 = new Vector2d(3,4);

        //when
        var result = vector1.upperRight(vector2);

        //then
        assertEquals(5, result.getX());
        assertEquals(4, result.getY());
    }

    @Test
    void vectorsLowerLeft(){
        //given
        var vector1 = new Vector2d(5,1);
        var vector2 = new Vector2d(3,4);

        //when
        var result = vector1.lowerLeft(vector2);

        //then
        assertEquals(3, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void vectorOpposite(){
        //given
        var vector1 = new Vector2d(5,1);

        //when
        var result = vector1.opposite();

        //then
        assertEquals(-5, result.getX());
        assertEquals(-1, result.getY());
    }
}