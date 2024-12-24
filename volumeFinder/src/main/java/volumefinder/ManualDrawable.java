package volumefinder;

import java.awt.Point;

public class ManualDrawable extends Drawable{


    private int brushSize;

    public ManualDrawable(int brushSize) {
        this.brushSize = brushSize;
    }

    @Override
    public void draw() {
        points.forEach(point->g2d.fillOval(point.x - brushSize/2, point.y - brushSize/2, brushSize, brushSize));

    }

    @Override
    public void add(Point newPoint) {

        points.add(newPoint);

    }


}

