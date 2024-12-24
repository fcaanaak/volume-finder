package volumefinder;

import java.awt.Point;

public class AxisDrawable extends Drawable{

    enum AxisModes{
        HORIZONTAL,
        VERTICAL
    }

    AxisModes currentAxisMode;

    @Override
    public void draw() {
        // TODO Auto-generated method stub

    }

    @Override
    public void add(Point newPoint) {

        switch(points.size()) {

            // First cover the case in which there are no points
            case 0:
                points.add(newPoint);
                break;


            case 1:

				/*
				For either horizontal or vertical, the idea is to use
				one component from the starting point and the other
				component from the mouse to make sure the second point
				is in line with the other point
				*/
                System.out.println(currentAxisMode);
                if (currentAxisMode == AxisModes.VERTICAL) {
                    points.add( new Point(points.get(0).x, newPoint.y) );

                }

                // If we aren't vertical then we must be horizontal
                else {
                    points.add( new Point(newPoint.x, points.get(0).y) );
                }

                break;

        }

    }

    public void setAxisMode(AxisModes mode) {
        currentAxisMode = mode;
    }


    // A method to convert the angle between the starting point and mouse coordinate
    // into either a HORIZONTAL or VERTICAL axis mode
    public void angleToRegion(int x, int y) {

        Point startPoint = points.get(0);// Keep the starting point handy

        double angle = Math.atan2(y-startPoint.y, x-startPoint.x);// get angle between start coord and mouse


        if(Math.abs(angle) - Math.PI/4.0 < 0 || (Math.PI - Math.abs(angle)) - Math.PI/4.0  < 0) {

            currentAxisMode = AxisModes.HORIZONTAL;
            g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(), x, (int)startPoint.getY());
        }

        // If we are in the vertical region
        else {
            currentAxisMode = AxisModes.VERTICAL;
            g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)startPoint.getX(), y);
        }

    }

}


