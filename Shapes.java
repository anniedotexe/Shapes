/**
 * Author:          Annie Wu
 * Class:           CS 4450 - Computer Graphics
 * 
 * Assignment:      1
 * Date:            11 Feb 2019
 *   
 * Purpose:         This file contains the Java test program for determining 
 *                  the scope of a variable declared in a for statement.
 *
 * EXTRA:           Hold down either key C or V to change colors of the shapes.
 *                       
 */

import java.io.*;
import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class Shapes {

    /*
    Method: start
    Purpose: run the methods we need to draw with OpenGL
    */
    public void start() {
        try {
            createWindow();
            initGL();
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    Method: createWindow
    Purpose: create a 640x840 window
    */
    private void createWindow() throws Exception {
        //no full screen
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("~~Stream Wind Flower by Mamamoo on YouTube~~");
        Display.create();
    }
    
    /*
    Method: initGL
    Purpose: initialize the graphics
    */
    private void initGL() {
        //background color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //load camera with projection
        glMatrixMode(GL_PROJECTION);
        //load Identity matrix
        glLoadIdentity();
        //matrix size and clipping
        glOrtho(0, 640, 0, 480, 1, -1);
        //set up Model view
        glMatrixMode(GL_MODELVIEW);
        //rendering hints
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    /*
    Method: render
    Purpose: render what we want to draw
    */
    private void render() {
        System.out.println("~~Stream Wind Flower by Mamamoo on YouTube~~ has been opened.\n");
        System.out.println("Hold down either key C or V to change colors of the shapes.\n");
        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                loadCoordinates();
                Display.update();
                Display.sync(60);
            } catch (Exception e) {
            }
        }
        
        Display.destroy();
        System.out.println("~~Stream Wind Flower by Mamamoo on YouTube~~ has been closed.\n");
    }
    
    /*
    Method: loadCoordinates
    Purpose: load coordinates from text file and 
             call functions for drawing shapes accordingly
    */
    private void loadCoordinates() {
        try {
            File file = new File("coordinates.txt");
            Scanner scan = new Scanner(file);
            
            String nextInput;
            String coordinates[];
            char letter;
            
            int endpoint1X, endpoint1Y, endpoint2X,endpoint2Y;
            int centerX, centerY, radius, radiusX, radiusY;
            
            while (scan.hasNextLine()) {
                letter = scan.next().charAt(0);
                switch (letter) {
                    case 'l':
                        //line endpoint 1
                        nextInput = scan.next();
                        coordinates = nextInput.split(",");
                        endpoint1X = Integer.parseInt(coordinates[0]);
                        endpoint1Y = Integer.parseInt(coordinates[1]);
                        
                        //line endpoint 2
                        nextInput = scan.next();
                        coordinates = nextInput.split(",");
                        endpoint2X = Integer.parseInt(coordinates[0]);
                        endpoint2Y = Integer.parseInt(coordinates[1]);
                        
                        glColor3f(255.0f,0.0f,0.0f); //red
                        
                        //hold down c to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                            glColor3f(0.0f,0.0f,255.0f); //blue
                        }
                        
                        //hold down v to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
                            glColor3f(0.0f,255.0f,0.0f); //green
                        }
                        
                        glBegin(GL_POINTS);
                        line(endpoint1X, endpoint1Y, endpoint2X, endpoint2Y);
                        glEnd();
                        break;
                        
                    case 'c':
                        //circle center
                        nextInput = scan.next();
                        coordinates = nextInput.split(",");
                        centerX = Integer.parseInt(coordinates[0]);
                        centerY = Integer.parseInt(coordinates[1]);
                        
                        //circle radius
                        nextInput = scan.next();
                        radius = Integer.parseInt(nextInput);
                        
                        glColor3f(0.0f,0.0f,255.0f); //blue
                        
                        //hold down c to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                            glColor3f(0.0f,255.0f,0.0f); //green
                        }
                        
                        //hold down v to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
                            glColor3f(255.0f,0.0f,0.0f); //red
                        }
                        
                        glBegin(GL_POINTS);
                        circle(centerX, centerY, radius);
                        glEnd();
                        break;
                        
                    case 'e':
                        //ellipse center
                        nextInput = scan.next();
                        coordinates = nextInput.split(",");
                        centerX = Integer.parseInt(coordinates[0]);
                        centerY = Integer.parseInt(coordinates[1]);
                        
                        //ellipse radii
                        nextInput = scan.next();
                        coordinates = nextInput.split(",");
                        radiusX = Integer.parseInt(coordinates[0]);
                        radiusY = Integer.parseInt(coordinates[1]);
                        
                        glColor3f(0.0f,255.0f,0.0f); //green
                        
                        //hold down c to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                            glColor3f(255.0f,0.0f,0.0f); //red
                        }
                        
                        //hold down v to change color
                        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
                            glColor3f(0.0f,0.0f,255.0f); //blue
                        }
                        
                        glBegin(GL_POINTS);
                        ellipse(centerX, centerY, radiusX, radiusY);
                        glEnd();
                        break;
                }
            }
        } catch (FileNotFoundException | NumberFormatException e) {
        }
    }
    
    /*
    Method: line
    Purpose: draw a line with midpoint line algorithm
    */
    private void line(int endpoint1X, int endpoint1Y, int endpoint2X, int endpoint2Y) {
        int dx = endpoint2X - endpoint1X; //change in x
        int dy = endpoint2Y - endpoint1Y; //change in y
        int incrementRight = 2*dy; //how much to move right (E)
        int incrementUpRight = 2*(dy - dx); //how much to move up right (NE)
        int d; //the distance to the midpoint
        int x = endpoint1X; //the current x value to plot
        int y = endpoint1Y; //the current y value to plot    
        
        double m = dy/dx;
        int incrementDown = 2*dx; //how much to move down (E)
        int incrementDownRight = 2*(dx - dy); //how much to move down right (NE)

        glVertex2f(x, y); //draw first endpoint
        
        if (m > -1 && m < 1) { //-1 < m < 1 range
            d = (2*dy) - dx;
            while (x < endpoint2X) {
                if (d > 0) { //NE, move up right
                    d += incrementUpRight;
                    x += 1;
                    y += 1;
                    glVertex2f(x, y); //draw point
                }
                else { //E, move right
                    d += incrementRight;
                    x += 1;
                    glVertex2f(x, y); //draw point
                }
            }
        }
        
        else {
            while (y > endpoint2Y) {
                d = (2*dx) - dy;
                if (d > 0) { //NE, move down right
                    d += incrementDownRight;
                    x += 1; //right 
                    y -= 1; //down
                    glVertex2f(x, y); //draw point
                }
                else { //E, move down
                    d += incrementDown;
                    y -= 1; //down
                    glVertex2f(x, y); //draw point
                }
            }
        }
    }
    
    /*
    Method: circle
    Purpose: draw a circle
    */
    private void circle(int centerX, int centerY, int radius) {
        int x,y;
        
        for (int theta = 0; theta < 360; theta++) {
            x = centerX + (int)(radius * Math.cos(theta)); //offset x-value
            y = centerY + (int)(radius * Math.sin(theta)); //offset y-value
            
            glVertex2f(x, y); //draw point
        }
    }

    /*
    Method: ellipse
    Purpose: draw an ellipse
    */
    private void ellipse(int centerX, int centerY, int radiusX, int radiusY) {
        int x,y;
        
        for (int theta = 0; theta < 360; theta++) {
            x = centerX + (int)(radiusX * Math.cos(theta)); //offset x-value
            y = centerY + (int)(radiusY * Math.sin(theta)); //offset y-value
            
            glVertex2f(x, y); //draw point
        }
    }
    
    public static void main(String[] args) {
        Shapes program = new Shapes();
        program.start();
    }
}
