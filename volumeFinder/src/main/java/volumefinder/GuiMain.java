package volumefinder;


// Importing swing related libraries
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;


import java.util.*;
import java.util.List;
//help me
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.MouseInfo;

public class GuiMain {

    private JFrame frame;
    private JPanel draw;

    private BufferedImage bufferedImage;

    static final int SCREEN_SIZE = 600;

    private ArrayList<JButton>controlButtons;

    public void setup() {

        lookAndFeelPrep();

        // Adding the StateManager
        StateManager stateManager = new StateManager();

        // Setting up the GUI Frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating widgets
        JPanel mainPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
        final JFileChooser fileChooser = new JFileChooser();

        // Adding buttons
        JButton fileOpener = new JButton("Open file");
        JButton drawButton = new JButton("Manual drawing");
        JButton clearButton = new JButton("Clear drawing");
        JButton axisButton = new JButton("Add axis");
        JButton dimensionButton = new JButton("Add dimension");

        // Adding all the main buttons to the buttonManager object
        ButtonManager buttonManager = new ButtonManager(
                Arrays.asList(fileOpener, drawButton,
                        axisButton,dimensionButton)
        );

        // Adding button behavior

        // Adding event listener to open file dialogue button
        fileOpener.addActionListener(event->fileChooser.showOpenDialog(fileOpener));

        // Disable the clear button until we are drawing
        clearButton.setEnabled(false);

        // Adding a filter to the fileChooser
        fileChooser.setFileFilter( new FileNameExtensionFilter("Images","jpg") );

        Image icon = resize("test_vase.jpg",SCREEN_SIZE,SCREEN_SIZE);
        draw = new Canvas(4,icon,stateManager);

        // Button action listeners


        // Adding event listener to change draw state
        // Might be best to just place this in a separate functional interface
        drawButton.addActionListener(event -> {

            switch (stateManager.getCurrentState()) {



                case NEUTRAL:
                    stateManager.setCurrentState(StateManager.States.DRAW);

                    //axisButton.setEnabled(false);
                    clearButton.setEnabled(true);

                    buttonManager.disableOthers(drawButton);


                    drawButton.setText("Stop");
                    break;

                case DRAW:

                    stateManager.revertToNeutral();

                    clearButton.setEnabled(false);

                    buttonManager.enableAll();

                    drawButton.setText("Manual drawing");
                    break;

            }
        });

        // Behavior for the axis button
        axisButton.addActionListener(event -> {

            switch(stateManager.getCurrentState()) {

                case NEUTRAL:

                    axisButton.setText("Stop");

                    stateManager.setCurrentState(StateManager.States.AXIS);

                    buttonManager.disableOthers(axisButton);
                    clearButton.setEnabled(true);

                    break;

                case AXIS:

                    if (!((Canvas) draw).singlePointCheck()) {
                        axisButton.setText("Add axis");

                        stateManager.revertToNeutral();

                        // Changing status of buttons
                        buttonManager.enableAll();
                        clearButton.setEnabled(false);
                    }

                    break;
            }
        });

        // Behavior for the dimensioning button
        dimensionButton.addActionListener(event -> {

            switch (stateManager.getCurrentState()) {

                case NEUTRAL:
                    dimensionButton.setText("Stop");

                    stateManager.setCurrentState(StateManager.States.DIMENSION);

                    buttonManager.disableOthers(dimensionButton);
                    clearButton.setEnabled(true);


                    break;

                case DIMENSION:

                    dimensionButton.setText("Set Dimension");

                    stateManager.revertToNeutral();

                    buttonManager.enableAll();
                    clearButton.setEnabled(false);

                    break;

            }
        });



        clearButton.addActionListener(event->((Canvas) draw).clear());
        // Resizing the image

        // Adding the pane to the frame
        frame.getContentPane().add(BorderLayout.NORTH,mainPanel);

        // Adding buttons to the main panel
        mainPanel.add(fileOpener);
        mainPanel.add(axisButton);
        mainPanel.add(drawButton);
        mainPanel.add(clearButton);
        mainPanel.add(dimensionButton);


        mainPanel.setBackground(Color.GRAY);


        // code for the drawing panel
        frame.getContentPane().add(BorderLayout.CENTER,draw);


        // Displaying the frame
        frame.setSize(SCREEN_SIZE,SCREEN_SIZE);
        frame.setVisible(true);


        // Image edge detection video

        // https://www.youtube.com/watch?v=YRz0sxCVx9A


    }




    public Image resize(String filepath,int width, int height) {

        try{
            bufferedImage = ImageIO.read(new File(filepath));
        }

        catch (IOException e){
            e.printStackTrace();
        }

        Image image = bufferedImage.getScaledInstance(width,height, Image.SCALE_DEFAULT);

        // In case we need an ImageIcon
        //return new ImageIcon(image);
        return image;
    }

    public void lookAndFeelPrep() {
        // Setting the look and feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            // handle exception
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            // handle exception
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // handle exception
            e.printStackTrace();
        }
    }





}


