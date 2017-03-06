package main.java;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * TODO: Modify calculation of time.
 * Creates a model for the representation of a car.
 */
public class Car extends Rectangle {


    /**
     * Components of a car. Contribute to time
     * TODO: Implement these components.
     * TODO: Create relationships between them and how fast a car travels over distance.
     */
    private int engine, suspension, boost, weight;


    /** TODO: Turbo boost?? */
    private boolean isBoosted;


    /** The total time the car has driven for. Proportional to the distance traveled and components. */
    private double time;


    /** Reference to the locations a particular car has visited. */
    private ArrayList<Location> stops;


    /** Reference to locations. */
    private Location start, end, currentLocation;


    /** Reference to the turn order and identification. TODO: Associate a car to a users name ?? */
    private int id;

    private boolean isFinished;

    /**
     * Creates a car with,
     * @param x The location in the x direction
     * @param y The location in the y direction
     * @param offset The sizing offset of the car
     * @param start The start location
     * @param end The end location
     * @param id The identification of the car/user order/identification.
     * TODO: HANDLE EXCEPTIONS FOR InvalidArgumentException, NullPointerException, and FileNotFoundException.
     */
    public Car(double x, double y, double offset, Location start, Location end, int id) {
        super(x, y, offset, offset);
        this.start = start;
        this.end = end;
        this.id = id;

        String[] names = { "bug", "blue", "black", "yellow","red"};
        URL resource = getClass().getResource("/main/resources/images/" + names[id % names.length] + ".png");
        this.setFill(new ImagePattern(new Image(resource.toString())));

        // Alternative fill, replace above setFill with the one below, and uncomment the above colors array
        // Color[] colors = { Color.CYAN, Color.PURPLE, Color.ORANGE, Color.YELLOW, Color.BLACK};
        // this.setFill(colors[id%colors.length]);

        currentLocation = start;
        // start.setActive(false); // Likely unnecessary.
        // end.setActive(false);
        stops = new ArrayList<Location>();
        stops.add(start);
        isFinished = false;
    }

    /**
     * @return The end locatino
     */
    public Location getEnd() { return end; }

    /**
     * @return The start location
     */
    public Location getStart() { return start; }

    /**
     * @return The total time the car has traveled over.
     */
    public double getTime() { return time; }

    /**
     * @return The current location
     */
    public Location getCurrentLocation() { return currentLocation; }

    /**
     * @return The car's identification.
     */
    public int getIdentifier() {
        return id;
    }

    /**
     * Checks to see if the car has visited a specific location.
     * @param location The specified location.
     * @return True if visited, false otherwise.
     */
    public boolean getVisited(Location location) { return stops.contains(location); }

    /**
     * Checks to see if the car has visited every stop except the "end" stop.
     * @param stop The number of locations.
     * @return True if the car has only the end location to visit before completion otherwise, false.
     */
    public boolean checkLast(int stop) {
        return stops.size() + 1 >= stop;
    }

    public boolean isFinished() { return isFinished; }

    /**
     * Moves a car from one location to another.
     * Updates the time.
     * TODO: Decide on how to compute time. Currently computed by distance traveled each time * 0.32.
     * TODO: The below calculation is completely arbitrary.
     * @param location The location to move to.
     */
    public void newLocation(Location location) {
        double distance = currentLocation.getDistanceToLocation(location);

        // System.out.println(
        // "Current location:" + currentLocation.getName()
        // + " time current" + time + " distance to new location : " + distance);

        stops.add(location);

        // TODO: Here is where the components like engine could be calculated
        time += distance * 0.0032;

        this.setX(location.getCenterX());
        this.setY(location.getCenterY());

        currentLocation = location;

        location.setActive(false, false);

        if (location == end) isFinished = true;

        // System.out.println("New Location " + location.getName() + " new time: " + time); //Useful for debugging
    }

    /**
     * @return The textual representation of a car.
     */
    @Override
    public String toString() {
        // for(Location l : stops) System.out.println(l.getName()); // Useful for debugging
        return "Car #:" + id + "\tTime traveled: " + time + "\tcurrent location " + currentLocation.getName();
    }

}