package main.java;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.ArrayList;
//import java.util.Map;

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
     *
     * @param x The x coordinate of the location
     * @param y The y coordinate of the location
     * @param offset The width and height of the location
     * @param name THe name identifier of the location
     * TODO: HANDLE EXCEPTIONS FOR InvalidArgumentException, NullPointerException, and FileNotFoundException.
     */
    public Location(double x, double y, double offset, String name) { //, Map<Location, Integer> neighbors) {
        super(x, y,offset);
        this.name = name;
        File temp = new File("src/main/resources/images/greengif.gif" );
        this.setFill(new ImagePattern(new Image("file:" + temp.getAbsolutePath())));
        // Replace the above setFill with the one below if the file is not found
        // this.setFill(Color.LIME);

//        this.neighbors = neighbors;
        clicked = false;
        active = true;
        cars = new ArrayList<>();
    }

    /**
     * @return the name of the location
     */
    public String getName() { return name; }

    /**
     * @param car The car to be added as present.
     *            Useful for future growth and debugging
     */
    public void addCar(Car car) {
        cars.add(car);
    }

    /**
     * @return True if the location can be selected, otherwise false
     */
    public boolean isActive() { return active; }

    /**
     * @return True if the location has been clicked, otherwise false
     */
    public boolean getClicked() { return clicked; }

    /**
     * @param in The value for "clicked" to be changed to.
     */
    public void setClicked(boolean in) { clicked = in; }

    /**
     * @param in The value for "active" to be changed to.
     */
    public void setActive(boolean in) {
        active = in;
        File green = new File("src/main/resources/images/greengif.gif" );
        File red = new File("src/main/resources/images/redgif.gif" );
        if (!active) this.setFill(new ImagePattern(new Image("file:" + red.getAbsolutePath())));
        // replace above "setfill" with the below if for some reason the image cannot be found
        // this.setFill(Color.CRIMSON);
        else this.setFill(new ImagePattern(new Image("file:" + green.getAbsolutePath())));
        // Replace this "setFill" as well if image is not working.
        // this.setFill(Color.LIME);
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
}