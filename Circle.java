import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import java.util.Random;

/** Circle for drawing in a JFrame
 *
 * @author Amy Larson
 */
public class Circle extends JPanel {

    /** Unique id (for debugging) */
    static int nextId = 0;
    static int getId() {
        return nextId++;
    }
    private int id;

    /** x and y bounds to keep circles in the playAreas */
    private final int xMINRANGE = 60;
    private final int xMAXRANGE = 740;
    private final int yMINRANGE = 160;
    private final int yMAXRANGE = 740;

    private Vector2D velocity;
    private Vector2D acceleration;
    private double maxSpeed;

    /** Fixed size */
    private int radius = 30;
    
    /** Color specified in RGB */
    private Color color = new Color(10, 10, 10);

    /** Location of the JPanel in which the circle is drawn */
    private Point xy = new Point(0, 0);

    /** Delta of location at each timestep */
    Point direction = new Point(+1, +1);

    /** Circels have many random components */
    private Random random = new Random();

    /** Drawn in window when visible */
    private boolean visible = false;

    public int circleCounter = 0;

    private Vector2D vector;

    /** Reassigns member variables to the circle. */
    public void reset() {
        randomXY();
        randomDirection();
        randomColor();
        setLocation(xy.x, xy.y);
        showCircle();
    }

    /** Circle is visible */
    public void showCircle() {
        visible = true;
    }

    /** Circle is not visible */
    public void hideCircle() {
        visible = false;
    }

    /** Default constructor */
    public Circle() {
        id = getId();   // for debugging

        this.setSize(radius * 2, radius * 2);

        // Make the box/panel on which the circle is drawn transparent
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));

        // Randomly assign values
        randomXY();
        randomDirection();
        randomColor();

        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, 0);
        maxSpeed = 10.0; // default max speed
    }

    boolean overlaps(Circle other){
        
        double xPos = other.xy.x - xy.x;
        double yPos = xy.y - other.xy.y;

        double avgDirX = Math.sqrt(Math.pow(((xy.x + other.xy.x) / 2), 2));
        double avgDirY = Math.sqrt(Math.pow(((xy.x + other.xy.y) / 2), 2));

        double hypot;

        hypot = Math.sqrt(Math.pow(xPos, 2) + Math.pow(yPos, 2));

        if(hypot < (radius + other.radius)){
            xy.x = (int) avgDirX;
            xy.y = (int) avgDirY;
            other.xy.x = (int) avgDirX;
            other.xy.y = (int) avgDirY;
            return true;
        }else{
            return false;
        }
    }



    /** Randomly assign its location based on the fixed ranges. */
    public void randomXY() {
        // place at random location
        xy.x = random.nextInt(xMAXRANGE - xMINRANGE) + xMINRANGE;
        xy.y = random.nextInt(yMAXRANGE - yMINRANGE) + yMINRANGE;
    }

    /** Randomly point it in a direction with random "speed" */
    public void randomDirection() {
        // set in a random direction
        direction.x = (random.nextInt(3) - 1) * 5;
        direction.y = (random.nextInt(3) - 1) * 5;
    }

    /** Randomly assign the RGB components */
    public void randomColor() {
        // color randomly
        color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /** Move the robot the "delta" for 1 timestep */
    public void step() {
        xy.x += direction.x;
        xy.y += direction.y;

        //xy.x += 5;
        //xy.y += 5;

        if(xy.x < xMINRANGE || xy.x > xMAXRANGE){
            direction.x *= -1;
            randomColor();
        }
        if(xy.y < yMINRANGE || xy.y > yMAXRANGE){
            direction.y *= -1;
            randomColor();
        }
    }

    public void reverse(){
        direction.x *= -1;
        direction.y *= -1;
    }

    public Point getXY() {
        return xy;
    }

    @Override
    public void paintComponent(Graphics g) {
        // This is called every time the circle location is reset in the CircleModel
        // System.out.print(" P"+id);
        super.paintComponent(g);
        if (visible) {
            g.setColor(color);
            g.fillOval(0, 0, radius * 2, radius * 2);
        }
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void applyForce(Vector2D force) {
        acceleration = acceleration.add(force);
    }

    public Vector2D subtract(Circle other) {
        double dx = this.xy.x - other.xy.x;
        double dy = this.xy.y - other.xy.y;
        return new Vector2D(dx, dy);
    }

    public double distance(Circle v) {
        double dx = this.xy.x - v.xy.x;
        double dy = this.xy.y - v.xy.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void normalize() {
        double magnitude = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
        if (magnitude != 0) {
            velocity.x /= magnitude;
            velocity.y /= magnitude;
        }
    }
}