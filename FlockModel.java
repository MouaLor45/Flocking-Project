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

int it = 0;

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
            System.out.println("**HERE** " + it++ + " " + count);
            // Move things only if the simulation is not paused
            if (!paused) {
                advanceCircles();
                finalVector();
                simulation.getContentPane().repaint();
            }
            try {
                //Thread.sleep(stepSize);
                Thread.sleep(100);
            } catch (Exception e) {

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
     * Set new vector direction to get sum directiton of all boids
     * @return average direction vector
     */
    Vector2D Flockalignment() {
        //System.out.println("Alignment Testing");
		int count = 0;
        double sumX = 0;
        double sumY = 0;
        Vector2D avgdirection = new Vector2D(0, 0); // New direction the circles will be following
		for(Circle c: circles){
            // calculates the average direction of circles directions into a new one 
            sumX += c.direction.x;
            sumY += c.direction.y;
            count++;
        }

            avgdirection.x = sumX;
            avgdirection.y = sumY;
            if (count > 0) {
                avgdirection = avgdirection.divide(count);
                avgdirection.normalize();
            }
            return avgdirection;
        }
    

    /**
     * Set new vector direction to separate circles into different direction
     * @return averageneighbors vector
     */
    Vector2D flockSeparation() {
        //System.out.println("Seperation Testing");
        double desiredSeparation = 20.0; // distance between circles at which separation should be maximized
        Vector2D avgneighbors = new Vector2D(0, 0);
        for (int i = 0; i < count; i++) {
            int countSeparation = 0;

            for (int j = 0; j < count; j++) {
                if (i != j) {
                    double d = circles.get(i).distance(circles.get(j));
                    if (d < desiredSeparation) {
                        Vector2D diff = circles.get(i).subtract(circles.get(j));
                        diff = diff.divide(d);
                        avgneighbors = avgneighbors.add(diff);
                        countSeparation++;
                    }
                }
            }

            if (countSeparation > 0) {
                avgneighbors = avgneighbors.divide(countSeparation);
                avgneighbors = avgneighbors.normalize().multiply(circles.get(i).getMaxSpeed()).subtract(circles.get(i).getVelocity());
                circles.get(i).applyForce(avgneighbors);
            }
        }
        return avgneighbors;
    }

    /**
     * Set new vector position to get sum position of all boids
     * @return average postion vector
     */
    public Vector2D flockCohesion(){
        Vector2D cohesionVector = new Vector2D(0, 0);
        int count = 0;
        for(Circle c: circles){
            cohesionVector.x += c.getXY().x;
            cohesionVector.y += c.getXY().y;
            count++;
        }
        cohesionVector.x = cohesionVector.x / count;
        if(count > 0){
            return cohesionVector;
        }
        else{
            return new Vector2D(0, 0);
        }
    }
    
    /**
     * Adds the vectors final direction values into all boids
     */
    public void finalVector(){
        // Vector that sums up all 3 methods vectors x and y vectors
        Vector2D cohesion = flockCohesion();
        Vector2D seperation = flockSeparation();
        Vector2D alignment = Flockalignment();
        for(Circle c: circles){
            cohesion.x -= c.getXY().x;
            cohesion.y -= c.getXY().y;

            //need to add the serperation and alignment variables
            c.direction.x += (int) cohesion.x + seperation.x + alignment.x;
            c.direction.y += (int) (cohesion.y + seperation.y + alignment.y);
        }
    }

}
