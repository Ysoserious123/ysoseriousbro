package Animation;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
//import java.awt.event.KeyEvent;
import java.util.Random;

//import javax.swing.ButtonGroup;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
//import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * This is the Menu class. It is a GUI that enables the user to set the parameters of the animation and then run said animation with their own custom specifications.
 */
public class Menu {
	// The frame that will be seen
	private JFrame frm;
	
	AnimationWindow win = null;

	/*
	 * This is the constructor for Menu. Its only job is to add all the components to a JFrame.
	 */
	public Menu() {
		initialize();
	}

	/*
	 * This method initializes and adds all components to a JFrame to be displayed.
	 */
	public void initialize() {

		//sets specific font specifications so I don't have to do it over and over
		Font smallLabelFont = new Font("Tahoma", Font.BOLD, 20);
		Font descriptionFont = new Font("Tahoma", Font.BOLD, 15);

		// creates a JFrame that all of the following components will be stored in
		frm = new JFrame("Infection Modeling");
		frm.setBounds(200, 200, 940, 580);
		frm.getContentPane().setBackground(Color.BLACK);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(null);

		// creates a title label for the window and Name of the program
		JLabel title = new JLabel("Infection Spread Simulation");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Tahoma", Font.BOLD, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(190, 30, 500, 35);
		frm.add(title);

		// This creates a text field where the user can input any number of people to be in the animation
		JTextField numPersons = new JTextField("300");
		numPersons.setBounds(500, 100, 70, 20);
		frm.add(numPersons);

		// creates a label next to the corresponding text field
		JLabel people = new JLabel("Number of People:");
		people.setForeground(Color.WHITE);
		people.setFont(smallLabelFont);
		people.setBounds(300, 98, 300, 20);
		frm.add(people);
		
		// creates a text area where a description of the input can be displayed
		JTextArea descrip1 = new JTextArea("My system seems to lag more around 2500 people\n                       so continue as you wish.");
		descrip1.setBackground(Color.BLACK);
		descrip1.setForeground(Color.GREEN);
		descrip1.setFont(descriptionFont);
		descrip1.setBounds(250, 138, 500, 40);
		frm.add(descrip1);

		// creates a text field  where the user can input a percent of people who are socially distanced
		JTextField percent = new JTextField("0");
		percent.setBounds(500, 190, 70, 20);
		frm.add(percent);

		// creates a label next to the corresponding text field
		JLabel distanced = new JLabel("Percent Distanced:");
		distanced.setForeground(Color.WHITE);
		distanced.setFont(smallLabelFont);
		distanced.setBounds(300, 188, 300, 20);
		frm.add(distanced);

		// creates a text field that allows the user to enter a size for each person
		JTextField peopleSize = new JTextField("2");
		peopleSize.setBounds(500, 230, 70, 20);
		frm.add(peopleSize);

		// creates a label next to the corresponding text field
		JLabel radius = new JLabel("Person Size:");
		radius.setForeground(Color.WHITE);
		radius.setFont(smallLabelFont);
		radius.setBounds(300, 228, 300, 25);
		frm.add(radius);
		
		// creates a text area that displays a description of the input
		JTextArea descrip2 = new JTextArea("Collisions become strange when this is greater than about 8.");
		descrip2.setBackground(Color.BLACK);
		descrip2.setForeground(Color.MAGENTA);
		descrip2.setFont(descriptionFont);
		descrip2.setBounds(215, 268, 500, 15);
		frm.add(descrip2);

		// creates a text field that allows the user to enter the speed of each person
		JTextField speed = new JTextField("1");
		speed.setBounds(500, 300, 70, 20);
		frm.add(speed);

		// creates a label next to the corresponding text field
		JLabel personSpeed = new JLabel("Person Speed:");
		personSpeed.setForeground(Color.WHITE);
		personSpeed.setFont(smallLabelFont);
		personSpeed.setBounds(300, 298, 300, 25);
		frm.add(personSpeed);
		
		// creates a text area that displays a description of the input
		JTextArea descrip3 = new JTextArea("As speed increses the colision detection might not work properly");
		descrip3.setBackground(Color.BLACK);
		descrip3.setForeground(Color.RED);
		descrip3.setFont(descriptionFont);
		descrip3.setBounds(210, 338, 500, 40);
		frm.add(descrip3);

		// creates a text field that allows the user to enter the width of the animation window
		JTextField width = new JTextField("700");
		width.setBounds(500, 380, 70, 20);
		frm.add(width);

		// creates a label next to the corresponding input
		JLabel windowWidth = new JLabel("Window Width:");
		windowWidth.setForeground(Color.WHITE);
		windowWidth.setFont(smallLabelFont);
		windowWidth.setBounds(300, 378, 300, 20);
		frm.add(windowWidth);

		// creates a text field that allows the user to enter the height of the animation window
		JTextField height = new JTextField("500");
		height.setBounds(500, 420, 70, 20);
		frm.add(height);

		// creates a label next to the corresponding text field
		JLabel windowHeight = new JLabel("Window Width:");
		windowHeight.setForeground(Color.WHITE);
		windowHeight.setFont(smallLabelFont);
		windowHeight.setBounds(300, 418, 300, 20);
		frm.add(windowHeight);
		
		
		JCheckBox quadtree = new JCheckBox();
		quadtree.setBounds(75, 475, 20, 20);
		frm.add(quadtree);
		
		JLabel useQuad = new JLabel("Use Quadtree");
		useQuad.setForeground(Color.WHITE);
		useQuad.setFont(smallLabelFont);
		useQuad.setBounds(100, 475, 150, 20);
		frm.add(useQuad);

		// creates a JButton that allows the user to run their specific animation
		JButton run = new JButton("Run");
		run.setBounds(380, 460, 100, 50);
		frm.add(run);
		// This allows the user to press enter to press the button
		frm.getRootPane().setDefaultButton(run);

		// this adds a new ActionListener to the run button that will take all the strings from the text fields and turn them into integers which can be passed into the AnimationWindow constructor
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numPeople = Integer.parseInt(numPersons.getText());
				int radius = Integer.parseInt(peopleSize.getText());
				int movementSpeed = Integer.parseInt(speed.getText());
				int windowWidth = Integer.parseInt(width.getText());
				int windowHeight = Integer.parseInt(height.getText());
				int percentDistanced = Integer.parseInt(percent.getText());
				// the AnimationWindow requires a Random class
				Random rnd = new Random();
				// this will be the title of the Menu JFrame
				String name = "Infection Spread Simulation";
				
				// try to create a new animation in its own JFrame. If it can't then an error message is displayed
				try {
					// minimizes the Menu for later use
					frm.setState(JFrame.ICONIFIED);
					
					// creates a new AnimationWindow class
					win = new AnimationWindow(numPeople, radius, windowWidth, windowHeight, rnd, movementSpeed,
							percentDistanced, "(" + numPeople + " people, " + radius + " person size, " + percentDistanced + " % social distancing)", quadtree.isSelected());
					
					// this block of code creates a JFrame to store the AnimationWindow which spawns at the top left corner of the user's screen
					JFrame frm1 = new JFrame(name);
					frm1.setBounds(0, 0, windowWidth, windowHeight);
					frm1.setFocusable(false);
					frm1.setResizable(false);
					frm1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frm1.addWindowListener(new WindowListener() {

						@Override
						public void windowOpened(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowClosing(WindowEvent e) {
							// TODO Auto-generated method stub
							win.getTimer().stop();
							win.getGraph().getTimer().stop();
							win.getGraph().getFrame().dispose();
							win = null;
						}

						@Override
						public void windowClosed(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowIconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowDeiconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowActivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowDeactivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						
					});
					frm1.add(win);
					frm1.setVisible(true);
					
				} catch (Exception r) {
					JOptionPane.showMessageDialog(null, "Invalid Inputs", "Run Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		});

	}
	
	

	/*
	 * THIS IS THE MAIN METHOD. This method runs the entire program.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// create a new Menu and the program can begin to be used
					Menu menu = new Menu();
					menu.frm.setFocusable(false);
					menu.frm.setVisible(true);
					//Create Save Folder
					if(!InstantiateSave.doesFolderExist()) {
						InstantiateSave.CanAddFolder();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
