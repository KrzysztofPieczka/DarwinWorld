package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map at a valid position.
     * The animal will only be placed if the position is valid and not occupied.
     * If the position is not valid or occupied, the method throws an IncorrectPositionException.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was successfully placed, false otherwise.
     * @throws IncorrectPositionException if the position is invalid or occupied.
     */
    boolean place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal according to the specified direction.
     * If the move is not possible (e.g., the new position is invalid or occupied),
     * the method has no effect and the animal remains at its current position.
     *
     * @param animal The animal to move.
     */
    void move(Animal animal);

    /**
     * Returns true if the given position on the map is occupied.
     * This method checks if there is an element (animal or plant) occupying the position.
     * It is different from checking if an animal can move to a position.
     *
     * @param position The position to check.
     * @return True if the position is occupied, false otherwise.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Returns the WorldElement (e.g., animal or plant) at a given position.
     * If the position is not occupied, it returns null.
     *
     * @param position The position of the WorldElement.
     * @return The WorldElement at the position, or null if the position is empty.
     */
    WorldElement objectAt(Vector2d position);

    /**
     * Returns a list of animals located at a given position.
     * If no animals are present at that position, it returns an empty list.
     *
     * @param position The position to check for animals.
     * @return A list of animals at the given position.
     */
    List<Animal> animalsAt(Vector2d position);

    /**
     * Returns a list of all WorldElements present on the map,
     * including animals and plants.
     *
     * @return A list of all WorldElements on the map.
     */
    List<WorldElement> getElements();

    /**
     * Returns a map of all animals on the map, where the key is the animal
     * and the value is its current position.
     *
     * @return A map of animals and their positions.
     */
    Map<Animal, Vector2d> getAnimals();

    /**
     * Returns a map of all plants on the map, where the key is the position
     * and the value is the plant at that position.
     *
     * @return A map of plant positions and plants.
     */
    Map<Vector2d, Plant> getPlants();

    /**
     * Returns a unique identifier for the map.
     *
     * @return The UUID of the map.
     */
    UUID getID();

    /**
     * Returns the boundaries (limits) of the map, which define the size and
     * the valid range of positions on the map.
     *
     * @return The boundaries of the map.
     */
    Boundary getBounds();

    /**
     * Generates a specified number of plants and places them randomly on the map.
     * The generated plants are placed at valid, unoccupied positions.
     *
     * @param numberOfPlants The number of plants to generate.
     */
    void generatePlants(int numberOfPlants);
}
