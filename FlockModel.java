/*
 * Circle Model.java
 */

import java.util.ArrayList;
import java.util.Vector;
import java.lang.Thread;

/**
 * Models a collection of circles roaming about impacting other circles.
 * @author Amy Larson (with Erik Steinmetz)
 */
public class FlockModel extends Thread {

    private ArrayList<Circle> circles = new ArrayList<>();

    /** Time in ms. "Frame rate" for redrawing the circles. */
    private int stepSize = 200;
    /** Current number of circles visible in the window. */
    private int count = 0;
    /** Pauses simulation so circles do not move */
    private boolean paused = true;

    private SimulationGUI simulation;

    /** Default constructor. */
    public FlockModel() {
        // All circels that might appear in the graphics window are created, but are not visible.
        for (int i=0; i<20; i++) {
            circles.add(new Circle());
        }
    }
    public void setSim(SimulationGUI sim){
        simulation = sim;
    }

    @Override
    public void run() {
        // Forever run the simulation
        while(true) {
            // Move things only if the simulation is not paused
            if (!paused) {
                advanceCircles();
                testOverlap();
                simulation.getContentPane().repaint();
            }
            try {
                Thread.sleep(stepSize);
            } catch (Exception e) {

            }
        }
    }

    public void testOverlap(){
        for(int i = 0; i < count; i++){
            for(int j = i + 1; j < count; j++){
                circles.get(j).overlaps(circles.get(i));
            }
        }
    }

    /** Pause the simulation - circles freeze. */
    public void pause() {
        paused = true;
    }

    /** Circles move again */
    public void play() {
        System.out.println("Playing now");
        paused = false;
    }

    public void reverse(){
        System.out.println("Reversing now");
        for(int i = 0; i < count; i++){
            circles.get(i).reverse();
        }
    }


    /** Move circles to next location */
    public void advanceCircles() {
        for (int i=0; i<count; i++) {
            // Advance each circle
            circles.get(i).step();
            // Set the location, which prompts the viewer to newly display the circle
            circles.get(i).setLocation(circles.get(i).getXY().x, circles.get(i).getXY().y);
        }
    }


    public ArrayList<Circle> getCircles() {
        return circles;
    }

    /** Reset circles */
    public void setCount(int circleCount) {
        System.out.println("Making circles!");
        // Must be in bounds. Only 20 circles in the list.
        if (circleCount < 2) {
            circleCount = 2;
        } else if (circleCount > 20) {
            circleCount = 20;
        }
        // Reset "count" circles, making them visible
        count = circleCount;
        for (int i=0; i<count; i++) {
            circles.get(i).reset();
        }
        // Hide the rest
        for (int i=count; i<20; i++) {
            circles.get(i).hideCircle();
        }
    }

    /** Set speed of simulation from 1 (slow) to 5 (fast) */
    public void setSpeed(int newSpeed) {
        // speed is between 1 (slow) and 5 (fastest)
        // low speed = high step size
        if (newSpeed < 1) {
            newSpeed = 1;
        } else if (newSpeed > 5) {
            newSpeed = 5;
        }
        stepSize = (6-newSpeed)*80; // 80 to 400ms
    }

    public void alignment(ArrayList<Circle> circles) {
        Vector v = new Vector();
        int count = 0;
        for(Circle c: circles) {
            if(isNeighbor(c)){
                v.add(c.velocity);
                count++;
            }
        }
        if(count > 0) {
            v.div(count);
			v.sub(velocity);
			v.div(4);
        }
        return v;
    }

}