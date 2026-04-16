package agh.ics.oop.model;

public interface MapChangeListener {

    /**
     * This method is triggered whenever a change occurs on the world map.
     * It receives the updated WorldMap and a message describing the change.
     * The message can be used to provide additional details or context about the change.
     *
     * @param worldMap The updated WorldMap instance.
     * @param message  A message describing the change or event that occurred.
     */
    void mapChanged(WorldMap worldMap, String message);
}
