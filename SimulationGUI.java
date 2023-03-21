import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * The "VIEW" of Model-View-Controller
 * An instance of this gui contains a reference to the Controller and the Model.
 * @author Amy Larson (with Erik Steinmetz)
 */
public class SimulationGUI extends JFrame {

    // Controller GUI Components
    private final JLabel countLabel = new JLabel("Circles (2-20): ");
    protected final JSlider count = new JSlider(2, 20, 10);
    //private final JLabel speedLabel = new JLabel("Speed (1-5): ");
    //protected final JTextField speed = new JTextField(10);
    private final JLabel separationLabel = new JLabel("Separation: ");
    protected final JSlider separation = new JSlider();
    private final JLabel cohesionLabel = new JLabel("Coherence: ");
    protected final JSlider cohesion = new JSlider();
    private final JLabel alignmentLabel = new JLabel("Alignment: ");
    protected final JSlider alignment = new JSlider();
    private final JButton stop = new JButton("Stop");
    private final JButton play = new JButton("Play");
    private final JButton restart = new JButton("Set Up");
    private final JButton reverse = new JButton("Reverse");

    private ArrayList<Circle> circles;

    /**
     * Creates a Simulation GUI application.
     * Sets the components and their positions in the gui.
     * Sets the Controller as the buttons' action listener.
     */
    public SimulationGUI(Controller control, FlockModel model) {

        // Initialize the graphics window
        super("Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,800);
        // You control the layout
        this.getContentPane().setLayout(null);

        // Play Area
        BoundingBox playArea = new BoundingBox(700,600);
        playArea.setLocation(50,150);
        getContentPane().add(playArea);

        // The Circles
        circles = model.getCircles();
        for (Circle circle: circles) {
            circle.setLocation(circle.getXY().x,circle.getXY().y);
            getContentPane().add(circle);
        }
        
        // Controller Display

        // Place the circle count label and text box
        this.countLabel.setBounds(20,20,100,30);
        this.getContentPane().add(this.countLabel);

        this.count.setBounds(115, 20, 80, 30);
        this.getContentPane().add(count);
        
        // place the sim speed label and text box
        //this.speedLabel.setBounds( 20, 50, 100, 30);
        //this.getContentPane().add(this.speedLabel);
        
        //this.speed.setBounds(115, 50, 80, 30);
        //this.getContentPane().add(this.speed);

        // place the separation label and text box
        this.separationLabel.setBounds( 20, 50, 100, 30);
        this.getContentPane().add(this.separationLabel);

        this.separation.setBounds(115, 50, 80, 30);
        this.getContentPane().add(this.separation);

        // place the cohesion label and text box
        this.cohesionLabel.setBounds( 220, 50, 100, 30);
        this.getContentPane().add(this.cohesionLabel);

        this.cohesion.setBounds(300, 50, 80, 30);
        this.getContentPane().add(this.cohesion);

        // place the alignment label and text box
        this.alignmentLabel.setBounds( 400, 50, 100, 30);
        this.getContentPane().add(this.alignmentLabel);

        this.alignment.setBounds(485, 50, 80, 30);
        this.getContentPane().add(this.alignment);

        // place the restart button 
        this.restart.setBounds(200, 20, 120, 30);
        this.restart.addActionListener(control);
        this.getContentPane().add(this.restart);
        
        // place the play and stop buttons
        this.play.setBounds(40, 100, 120, 30);
        this.play.addActionListener(control);
        this.getContentPane().add(this.play);
        
        this.stop.setBounds(150, 100, 120, 30);
        this.stop.addActionListener(control);
        this.getContentPane().add(this.stop);

        // place the reverse button
        this.reverse.setBounds(280, 100, 120, 30);
        this.reverse.addActionListener(control);
        this.getContentPane().add(this.reverse);
    }
    
}
