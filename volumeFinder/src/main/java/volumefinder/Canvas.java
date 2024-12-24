package volumefinder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Cursor;
import java.math.*;

class Canvas extends JPanel{

    private Image image;// The selected image chosen to be drawn

    // Arrays for the edge points and the axis points

    //private ArrayList<Point>axisPoints;



    // State instance
    private StateManager canvasState;

    private enum AxisStates {HORIZONTAL,VERTICAL};

    private AxisStates currentAxisState;

    //private States drawState;
    private int mouseX;
    private int mouseY;

    // Boolean to determine if we need to remove the
    // square boundary around the cursor when erasing
    private boolean removeEraserBoundary = false;

    private int brushSize;


    // Drawable child class instances
    private ManualDrawable manualDraw;
    private AxisDrawable axisDraw;

    private ArrayList<Point>axisPoints;

    public Canvas(int brushSize,Image image,StateManager canvasState) {

        this.brushSize = brushSize;
        this.image = image;
        this.canvasState = canvasState;



        // Create all DrawAble child instances
        manualDraw = new ManualDrawable(brushSize);
        axisDraw = new AxisDrawable();

        axisPoints = axisDraw.getPoints();

        // worry about this later
            /*
            try {
                 image = ImageIO.read(new File("test_vase.jpg"));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            */

        // Handle what happens when you drag the mouse
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (canvasState.getCurrentState() == StateManager.States.DRAW) {

                    // if we drag the mouse left click while in drawing mode
                    if (SwingUtilities.isLeftMouseButton(e)) {

                        manualDraw.add(e.getPoint());

                        // RE-ADD IF SHIT GOES SOUTH
                        //points.add(e.getPoint());

                        repaint();
                    }

                    // if we right click drag while in drawing mode
                    else if (SwingUtilities.isRightMouseButton(e)) {

                        canvasState.setCurrentState(StateManager.States.ERASE);//Set the state to erase

                    }
                }


                // if we right click drag while in erasing mode
                else if(SwingUtilities.isRightMouseButton(e) && canvasState.getCurrentState() == StateManager.States.ERASE) {

                    mouseX = e.getX();
                    mouseY= e.getY();

                    repaint();

                    eraseAround(mouseX,mouseY);
                }
            }

            // Behavior upon mouse movement
            @Override
            public void mouseMoved(MouseEvent e) {
                if(canvasState.getCurrentState()== StateManager.States.AXIS) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();

                }


            }

        });

        // Handle what happens when you release the mouse
        addMouseListener(new MouseAdapter() {

            // Whenever we release the mouse
            @Override
            public void mouseReleased(MouseEvent e) {

                // if we release the right mouse button while erasing
                if(SwingUtilities.isRightMouseButton(e) && canvasState.getCurrentState()== StateManager.States.ERASE) {

                    // go back to draw state and set a flag to note we need to get rid of the eraser box
                    canvasState.setCurrentState(StateManager.States.DRAW);
                    removeEraserBoundary = true;
                    repaint();

                }
            }

            // Whenever we click the mouse
            @Override
            public void mousePressed(MouseEvent e) {

                // If we simply left click and are in axis creation mode
                if(SwingUtilities.isLeftMouseButton(e) && canvasState.getCurrentState() == StateManager.States.AXIS) {

                    axisDraw.add(new Point( e.getX(), e.getY() ));

                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();
                    // Ensure we only have 2 axis points
            			/*
            			switch(axisPoints.size()) {


            				case 0:
            					axisPoints.add(new Point(e.getX(),e.getY()));

                				//mouseX = e.getX();
                				//mouseY = e.getY();


                				repaint();

            				break;

            				case 1:

            					// If we are in the vertical region
            					if (currentAxisState == AxisStates.VERTICAL) {
            						mouseX = (int)axisPoints.get(0).getX();
            						mouseY = e.getY();

            						axisPoints.add(new Point(mouseX,mouseY));


            					}

            					// If we arent in the vertical region we must be in the
            					// horizontal region :)
            					else {

            						mouseX = e.getX();
            						mouseY = (int)axisPoints.get(0).getY();

            						axisPoints.add(new Point(mouseX,mouseY));
            					}

            					repaint();

            				break;
            			}
            			*/


                }

            }
        });
    }

    // Check if there is a single point created in the axis
    public boolean singlePointCheck() {
        return axisDraw.getPoints().size() == 1;
    }

    // Drawing code
    @Override
    protected void paintComponent(Graphics g) {


        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        manualDraw.graphicsInit(g2d);

        if (image != null) {
            g2d.drawImage(image, 0, 0, this);
        }

        // If we are currently erasing, first draw the erasing rectangle
        // around the mouse then check to see if we have stopped erasing
        if (canvasState.getCurrentState() == StateManager.States.ERASE) {
            g2d.drawRect(mouseX-10, mouseY-10, 20, 20);

            // If condition checked if we want to get rid of the lil
            //  box to show the region we are erasing
            if (removeEraserBoundary) {
                g2d.clearRect(mouseX-10, mouseY-10, 20, 20);
                removeEraserBoundary = !removeEraserBoundary;
                repaint();
            }

        }

        // Check to see if we have placed an axis down
        if (axisPoints.size() == 1) {
            Point startPoint = axisPoints.get(0);
            double angle = Math.atan2(mouseY-startPoint.getY(),mouseX-startPoint.getX());

            // Condition to see whether we should snap the axis to be horizontal or vertical

            // If we are within the horizontal region
            if(Math.abs(angle) - Math.PI/4.0 < 0 || (Math.PI - Math.abs(angle)) - Math.PI/4.0  < 0) {
                //g2d.setColor(Color.CYAN);
                currentAxisState = AxisStates.HORIZONTAL;
                g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(), mouseX, (int)startPoint.getY());
            }

            // If we are in the vertical region
            else {
                currentAxisState = AxisStates.VERTICAL;
                g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)startPoint.getX(), mouseY);
            }





        }

        else if (axisPoints.size() == 2) {
            // Get start and end points
            Point startPoint = axisPoints.get(0);
            Point endPoint = axisPoints.get(1);

            g2d.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)endPoint.getX(), (int)endPoint.getY());


        }


        // Default code that must be placed if we want drawings we make to always stay onscreen
        g2d.setColor(Color.RED);

        manualDraw.draw();


        // Add back in if shit goes south


        g2d.setColor(Color.BLACK);
        // Draw the axis points in
        for(Point axisPoint:axisDraw.getPoints()) {
            g2d.drawOval(axisPoint.x-10,axisPoint.y-10,20,20);

        }

        g2d.dispose();

    }

    void clear() {

        switch(canvasState.getCurrentState()) {
            case DRAW:
                manualDraw.clearPoints();

                break;

            case AXIS:
                axisDraw.clearPoints();

                break;
        }
        repaint();

    }

    // A method for erasing all points that are nearby
    void eraseAround(int x, int y) {

        Point p;

        ArrayList<Point>manualDrawPoints = manualDraw.getPoints();

        // Iterate through the points array and remove
        // all points within a distance of the cursor
        for (int i =0; i <manualDrawPoints.size();i++) {

            p = manualDrawPoints.get(i);

            if (Math.abs(p.x - x) < 10 && Math.abs(p.y - y) < 10) {
                manualDrawPoints.remove(p);
                repaint();
            }

        }

    }



}

