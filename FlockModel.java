/*
 * Circle Model.java
 */

import java.lang.Thread;
import java.util.ArrayList;

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
                Flockalignment();
                simulation.getContentPane().repaint();
            }
            try {
                Thread.sleep(stepSize);
            } catch (Exception e) {

            }
        }
    }

    //Tests for overlapping circles
    public void testOverlap(){
        for(int i = 0; i < count; i++){
            for(int j = i + 1; j < count; j++){
                circles.get(j).overlaps(circles.get(i));
            }
        }
    }
    
    public void testCohesion(){
        for(int i = 0; i < count; i++){
            for(int j = 0; j < count; j++){
                
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

    /**
     * Set new vector direction to overlapped circles into same direction
     */
    public void Flockalignment() {
        System.out.println("Alignment Testing");
		int count = 0;
        Vector2D v = new Vector2D(0, 0); // New direction the circles will be following
		for(int x = 0; x < count; x++){
            for(int y = x+1; y < count; y++){
                // calculates the difference of overlapped circles directions into a new one 
                if(circles.get(x).overlaps(circles.get(y)) == true){
                    double circlexdifference = circles.get(x).direction.x - circles.get(y).direction.x;
                    double circleydifference = circles.get(x).direction.y - circles.get(y).direction.y;
                    v.x = circlexdifference;
                    v.y = circleydifference;
                    circles.get(x).direction.x = (int) v.x;
                    circles.get(x).direction.y = (int) v.y;
                    circles.get(y).direction.x = (int) v.x;
                    circles.get(y).direction.y = (int) v.y;
                    count++;
                }
            }
        }
		if (count > 0) {
			v = v.divide(count);
		}
    }

    /**
     * Set new vector direction to separate circles into different direction
     */
    public void flockSeparation() {
        double desiredSeparation = 20.0; // distance between circles at which separation should be maximized

        for (int i = 0; i < count; i++) {
            Vector2D sum = new Vector2D(0, 0);
            int countSeparation = 0;

            for (int j = 0; j < count; j++) {
                if (i != j) {
                    double d = circles.get(i).distance(circles.get(j));
                    if (d < desiredSeparation) {
                        Vector2D diff = circles.get(i).subtract(circles.get(j));
                        diff = diff.divide(d);
                        sum = sum.add(diff);
                        countSeparation++;
                    }
                }
            }

            if (countSeparation > 0) {
                sum = sum.divide(countSeparation);
                sum = sum.normalize().multiply(circles.get(i).getMaxSpeed()).subtract(circles.get(i).getVelocity());
                circles.get(i).applyForce(sum);
            }
        }
    }

}
