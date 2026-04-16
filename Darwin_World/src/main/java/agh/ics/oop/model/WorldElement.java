package agh.ics.oop.model;

public interface WorldElement {

    /**
     * Returns the position of the WorldElement on the map.
     * This position is represented as a Vector2d object, which contains
     * the x and y coordinates on the map.
     *
     * @return The position of the WorldElement.
     */
    Vector2d getPosition();

    /**
     * Checks if the WorldElement is located at a given position.
     * This method compares the current position of the WorldElement
     * with the specified position to determine if they are the same.
     *
     * @param position The position to check.
     * @return True if the WorldElement is at the specified position, false otherwise.
     */
    boolean isAt(Vector2d position);
}
