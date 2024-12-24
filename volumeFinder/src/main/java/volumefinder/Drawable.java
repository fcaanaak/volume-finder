package volumefinder;

import java.util.ArrayList;
import java.awt.Point;
import java.awt.Graphics2D;

// A class for drawable objects such as general drawing,
// axis creation, or dimensioning
abstract public class Drawable {

    // The arraylist that will hold all the important points for the
    // drawable object
    protected ArrayList<Point>points = new ArrayList<Point>();

    // Graphics2D object used to draw across all objects
    protected static Graphics2D g2d;

    // Method for handling how objects are drawn
    abstract public void draw();

    // Method for handling how we add points to the array
    abstract public void add(Point newPoint);

    // Wrapper method for clearing all points
    public void clearPoints() {
        points.clear();
    }

    public void graphicsInit(Graphics2D g2d) {
        this.g2d = g2d;

    }

    public ArrayList<Point> getPoints() {
        return points;
    }




}
