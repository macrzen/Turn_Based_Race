package main.java;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ArrayList;
//import java.util.Map;

/**
 * TODO: Decide on proper images for the locations.
 * ^ They are currently the greengif.gif and redgif.gif images in the main/resources/images sub-directory
 * Creates a destination for use within the track.
 */
public class Location extends Circle {

    /**
     * The name identifier of the location
     */
    private String name;


    /**
     * Stores the cars present at a location
     *   Thought to be needed for future improvements
     */
    private ArrayList<Car> cars;


    /**
     * Keeps track within the running loop of each turn whether
     * a location has been clicked
     * a location can be clicked (active)
     */
    private boolean clicked, active;


    // private Map<Location, Integer> neighbors; // TODO: See "getDistanceToLocation(Location location)" for details...


    /**
      * @param x The x coordinate of the location
     * @param y The y coordinate of the location
     * @param offset The width and height of the location
     * @param name THe name identifier of the location
     * TODO: HANDLE EXCEPTIONS FOR InvalidArgumentException, NullPointerException, and FileNotFoundException.
     */
    public Location(double x, double y, double offset, String name) { //, Map<Location, Integer> neighbors) {
        super(x, y,offset);
        this.name = name;
        URL resource = getClass().getResource("/main/resources/images/greengif.gif" );
        this.setFill(new ImagePattern(new Image(resource.toString())));
        // this.setFill(Color.LIME); // Alternative to the above setFill
        // this.neighbors = neighbors; // TODO: Make decision regarding Map.
        clicked = false;
        active = true;
        cars = new ArrayList<>();
    }


    /**
     * @return the name of the location
     */
    public String getName() { return name; }


    /**
     * TODO: Unused
     * @param car The car to be added as present. Useful for future growth and debugging
     */
    public void addCar(Car car) { cars.add(car); }


    /**
     * TODO: Unused
     * @return True if the location can be selected, otherwise false
     */
    public boolean isActive() { return active; }


    /**
     * TODO: Unused
     * @return True if the location has been clicked, otherwise false
     */
    public boolean getClicked() { return clicked; }


    /**
     * @param in The value for "clicked" to be changed to.
     */
    public void setClicked(boolean in) { clicked = in; }


    /**
     * @param in The value for "active" to be changed to.
     * @param orange True if the location to be set is the end location for a car.
     */
    public void setActive(boolean in, boolean orange) {
        active = in;
        URL green = getClass().getResource("/main/resources/images/greengif.gif" );
//        URL red = getClass().getResource("/main/resources/images/redgif.gif" );
        if (orange) this.setFill(Color.ORANGE); // TODO: Find image for this representation ?
        else {
            if (!active)
//                this.setFill(new ImagePattern(new Image(red.toString())));
             this.setFill(Color.CRIMSON); // Alternative to the above setFill
            else this.setFill(new ImagePattern(new Image(green.toString())));
            // this.setFill(Color.LIME); // Alternative to the above setFill
        }
    }

    /**
     * TODO: It might be clearer to initialize this when the locations are created
     * TODO:    possibly into the Map called neighbors (commented out at the very top).
     * Computes the distance on demand from "this" location to,
     * @param location The location to compute distance to
     * @return the hypotenuse of the difference of x and y values.
     */
    public double getDistanceToLocation(Location location) {
        double tmpX = location.getCenterX();
        double tmpY = location.getCenterY();
        double xThis = this.getCenterX();
        double yThis = this.getCenterY();
        double a = (tmpY-yThis) > 0 ? tmpY-yThis : yThis-tmpY;
        double b = (tmpX-xThis) > 0 ? tmpX-xThis : xThis-tmpX;
        return Math.hypot(a, b);
    }


    public String toString() {
        return name + "\tActive: " + active + "\tClicked: " + clicked;
    }

}
