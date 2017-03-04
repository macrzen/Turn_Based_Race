package main.java;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * TODO: Resolve the end location.
 * TODO: Display the user's "end" location.
 * The track is the layer where the turn based sequence is controlled.
 * A group of graphical components, together creates the game aspect of the program
 */
public class Track extends Group {

    /** A reference to all of the locations */
    private ArrayList<Location> locations;

    /** Reference to all of the cars (proportional to the number of players) */
    private ArrayList<Car> cars;

    /** Keeps track of which cars turn it is. */
    private Car activeCar;

    /**
     * gridPane contains a grid-pane of cars, and one of locations
     * Contains the information about the active car's current location,
     * the distances from the active car's current location,
     * as well as the time that each player has driven.
     */
    private GridPane gridPane, gpCars, gpLocations;

    /**
     * @param locations The collection of locations.
     * @param cars The collection of cars.
     *             Useful for creating the class manually (i.e. debugging)
     */
    public Track(ArrayList<Location> locations, ArrayList<Car> cars) {
        this.locations = locations;
        this.cars = cars;
        for (Location location : locations) this.getChildren().add(location);
        for (Car car : cars) this.getChildren().add(car);
    }

    /** Initializes the collections for locations and cars. */
    public Track() {
        locations = new ArrayList<>();
        cars = new ArrayList<>();
    }

    /**
     * Responsible for updating the "stats" of the components within the gridPane
     * for example gpLocations -- the location information
     * as well as  gpCars      -- the cars information (time)
     */
    public void updateStats() {
        ObservableList<Node> children = gpLocations.getChildren();
        for (int i = 0; i < gpLocations.getChildren().size() / 2; i++)
            for (Node n : children)
                if (gpLocations.getRowIndex(n) == i + 1 && gpLocations.getColumnIndex(n) == 1) {
                    Text t = (Text) n;
                    t.setText(String.format(
                            "\t\t%.1f", activeCar.getCurrentLocation().getDistanceToLocation(locations.get(i))));
                }

        children = gpCars.getChildren();
        for (int j = 0; j < gpCars.getChildren().size() / 2; j++)
            for (Node m : children)
                if (gpCars.getRowIndex(m) == j + 1 && gpCars.getColumnIndex(m) == 1) {
                    Text tmpM = (Text) m;
                    tmpM.setText(String.format("\t\t%.1f", cars.get(j).getTime()));
                }

        children = gridPane.getChildren();
        for (Node o : children)
            if (gridPane.getRowIndex(o) == 5 && gpCars.getColumnIndex(o) == 0) {
                Text tmpO = (Text) o;
                tmpO.setText("Active Car\t" + activeCar.getIdentifier());
            }
    }


    /**
     * TODO: Clean up this code, some areas need to be more modular.
     * TODO: Decide if/how this method should be refactored ( i.e. it could likely be broken up. )
     * TODO: On second thought, this method is likely to cause problems.
     * TODO:                                                    ^^^^^^ ->> CLEAN UP THIS CODE!
     * The way that the cars and locations are initialized.
     * Takes in a number,
     * @param numPlayers The number of locations corresponds to combo-box selection from StartPrompt in View.
     * @param sceneX The width of the main Screen from View.
     * @param sceneY The height of the main Screen from View.
     */
    public void setTrack(int numPlayers, double sceneX, double sceneY) {

        //
        // TODO: decide if the "offset" should be taken in as a parameter for modularity
        // offset is responsible for the sizing of the locations and the cars
        // it has an arbitrary size, and can be changed
        //
        double offset = 32;

        Random rand = new Random();

        //
        // for the number of players, the number of locations changes, the addition of 2 or 3 extra locations
        // is arbitrary
        //
        int numLocation = (numPlayers & 1) == 1 ? numPlayers + 2 : numPlayers + 3;

        ArrayList<Integer> forStart = new ArrayList<>();
        ArrayList<Integer> forEnd = new ArrayList<>();

        //
        // Initializes the locations.
        //
        for (int k = 0; k < numLocation; k++) {
            int tmpOffSetX = (int) sceneX - ((int) offset *2);
            int tmpOffsetY = (int) sceneY - ((int) offset *2);

            double x = rand.nextInt(tmpOffSetX - 300);
            double y = rand.nextInt(tmpOffsetY);

            for (Location lo : locations)
                if (lo.intersects(new BoundingBox(x, y, offset * 2, offset * 2))) {
                    x = rand.nextInt(tmpOffSetX - 300);
                    y = rand.nextInt(tmpOffsetY);
                }

            // makes sure the locations are not displayed off-screen.
            if (x < offset) x += (offset + 10);
            if (y < offset) y += (offset + 10);
            if (y > sceneY - offset) y -= (offset + 10);
            if (x > sceneX - offset) x -= (offset + 10);

            // adds new locations
            locations.add(new Location(x, y, offset, "Location " + k));
        }

        for (int j = 0; j < numPlayers; j++) {
            int start = rand.nextInt(locations.size());
            int end = rand.nextInt(locations.size());

            while (forStart.contains(start)) start = rand.nextInt(locations.size());
            while (forEnd.contains(end)) end = rand.nextInt(locations.size());

            forStart.add(start);
            forEnd.add(end);

            Location s = locations.get(start);
            Location e = locations.get(end);

            cars.add(new Car(s.getCenterX(), s.getCenterY(), (offset * 2) - 16, s, e, j));
        }

        for (Location location : locations) {
            this.getChildren().add(location);
            this.getChildren().add(new Text(
                    location.getCenterX() - offset, location.getCenterY() + (offset * 2), location.getName()));
            location.setOnMouseClicked(locationEvent);
        }

        for (Car car : cars) this.getChildren().add(car);

        activeCar = cars.get(0);
        System.out.println("Active Car: " + activeCar.getIdentifier() + "\n\n");

        gridPane = new GridPane();
        gridPane.setMinSize(200, sceneY);
        gridPane.setMaxSize(200, sceneY);
        gridPane.setAlignment(Pos.CENTER);
        gpLocations = new GridPane();
        gpLocations.setMaxSize(200, sceneY / 3);
        gpCars = new GridPane();
        gpCars.setMaxSize(200, sceneY / 3);

        gpLocations.add(new Text("Location"), 0, 0);
        gpLocations.add(new Text("\tDistance (km)"), 1, 0);

        for (int q = 0; q < locations.size(); q++) {
            gpLocations.add(new Text(locations.get(q).getName()), 0, q + 1);
            String dist = String.format("%.1f", activeCar.getCurrentLocation().getDistanceToLocation(locations.get(q)));
            gpLocations.add(new Text("\t\t" + dist), 1, q + 1);
        }


        gpCars.add(new Text("Car"), 0, 0);
        gpCars.add(new Text("\tTime (hr)"), 1, 0);

        for (int nums = 0; nums < cars.size(); nums++) {
            gpCars.add(new Text(Integer.toString(cars.get(nums).getIdentifier())), 0, nums + 1);
            String dist = String.format("%.1f", cars.get(nums).getTime());
            gpCars.add(new Text("\t\t" + dist), 1, nums + 1);
        }


        gridPane.add(gpLocations, 0, 1);
        gridPane.add(new Rectangle(200, 200, Color.TRANSPARENT), 0, 2);
        gridPane.add(gpCars, 0, 3);
        gridPane.add(new Rectangle(200, 200, Color.TRANSPARENT), 0, 3);
        gridPane.add(new Text("Active Car\t" + activeCar.getIdentifier()), 0, 5);
        gridPane.setLayoutX(sceneX - 300);
        this.getChildren().add(gridPane);

        for (Location location : locations)
            if (location != activeCar.getCurrentLocation() && location != activeCar.getEnd())
                location.setActive(true);

        activeCar.getEnd().setFill(Color.ORANGE);
    }

    /**
     * Handles changing the active car's location.
     * TODO: Resolve "end" location. Currently cannot go to the last location.
     */
    private EventHandler<MouseEvent> locationEvent = mouseEvent -> {
        Location l = (Location) mouseEvent.getSource();

        if (!activeCar.getVisited(l) && l != activeCar.getEnd()) { // TODO: This is the cause of the above TODO
            l.setClicked(true);              // TODO: ^^^ Specifically this part of the conditional
            l.setActive(false);
            activeCar.newLocation(l);
            activeCar = cars.get((cars.indexOf(activeCar) + 1) % cars.size());
            System.out.println("Active Car: " + activeCar.getIdentifier() + "\n\n");
            updateStats();
        }
//        else if (l == activeCar.getEnd() && activeCar.checkLast(locations.size())) {
//            l.setClicked(true);
//            l.setActive(false);
//            activeCar.newLocation(l);
//            activeCar = cars.get((cars.indexOf(activeCar) + 1) % cars.size());
//            System.out.println("Active Car: " + activeCar.getIdentifier() + "\n\n");
//            updateStats();
//        }
        for (Location location : locations) {
            if (activeCar.checkLast(locations.size()) && location == activeCar.getEnd())
                location.setActive(true);
            else if (location == activeCar.getEnd() && !activeCar.checkLast(locations.size()))
                activeCar.getEnd().setFill(Color.ORANGE);
            else if (activeCar.getVisited(location))
                location.setActive(false);
            else
                location.setActive(true);
        }

        for (Car c : cars) System.out.println(c.toString());
    };
}