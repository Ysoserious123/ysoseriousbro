package Animation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/*
 * This is the AnimationWindow class. This class maintains the responsibility of drawing the animation as it plays out. It implements ActionListener which is run using a Swing Timer class to
 * update the images of where each person is.
 */
public class AnimationWindow extends JPanel implements ActionListener {

	// an array of people in the simulation
	private ArrayList<Person> people;

	// the timer that calls actionPerformed and its delay in milliseconds
	private Timer timer;
	private int delay = 1;

	// the bounds of the window
	private int xBound;
	private int yBound;

	// The animation window will collect data from the people and pass it to a graph
	// to display
	private Data data;
	private Graph graph;

	// this is the play/pause button in the menu bar of the graph.
	JMenuItem playPause;

	// these are the images displayed on the play/pause button based on the state of
	// the animation
	private ImageIcon playImg;
	private ImageIcon pauseImg;

	// this is an object which will allow for pausing using multithreading
	Object lock = new Object();
	// this is a boolean variable used between both threads to pause and unpause the
	// animation
	private volatile boolean paused = false;

	/*
	 * 
	 * 
	 * QUAD TREE INPLEMENTATION
	 * 
	 * 
	 */

	private Rectangle r;
	private Quadtree qt;
	private boolean quadtree;

	/*
	 * This is the constructor for the Animation Window. It initializes the global
	 * variables including the Person[]. It does so by using the java.util.Random
	 * class to give people random positions on the board, as well as determine how
	 * many people are social distance which is a parameter passed from the Menu
	 * class.
	 * 
	 * @param numPeople = number of people in the simulation
	 * 
	 * @param diam = the visual diameter of a person
	 * 
	 * @param maxX = the far x axis bound of the window size
	 * 
	 * @param maxY = the far y axis bound of the window size
	 * 
	 * @param rnd = a Random class passed into the constructor to help initialize
	 * the Person[]
	 * 
	 * @param speed = the coefficient of a Person's speed vectors (all people will
	 * have the same speed)
	 * 
	 * @param percentDistanced = the percentage of people that will be socially
	 * distanced in the simulation
	 * 
	 * @param graphName = a String passed from the Menu class that contains all of
	 * the parameters for the simulation which will be passed to the Graph class as
	 * a title
	 */
	public AnimationWindow(int numPeople, int diam, int maxX, int maxY, Random rnd, int speed, int percentDistanced,
			String graphName, boolean quadtree) {

		playImg = new ImageIcon("play-button.png");
		pauseImg = new ImageIcon("pause-button.png");
		xBound = maxX;
		yBound = maxY;
		this.quadtree = quadtree;

		// QUADTREE
		// make a rectangle the size of our window for our quadtree
		r = new Rectangle(0, 0, xBound, yBound);

		// This loop will create new Persons to populate the animation and used the
		// random class to set the upper bound of a random number
		// based on the percent of people socially distanced
		people = new ArrayList<Person>();
		;
		for (int i = 0; i < numPeople; i++) {
			int percent = rnd.nextInt(100) + 1;
			if (percent < percentDistanced) {
				people.add(new Person(diam, maxX - 10, maxY - 50, 0, true));
			} else {
				people.add(new Person(diam, maxX - 10, maxY - 50, speed, false));
			}
		}

		// The Person[] is passed into the Data class to get the state of each
		// individual as the animation runs
		data = new Data(people);

		// This loop ensures that the first person infected is not socially distanced to
		// allow for the infection to actually spread
		for (int i = 0; i < people.size(); i++) {
			if (!people.get(i).isStationary()) {
				people.get(i).setInfected(true);
				break;
			}
		}

		setFocusable(false);

		// Initialize timer
		timer = new Timer(delay, this);
		timer.start();

		// These are the bounds of the JFrame that will hold the Graph
		int yBound1 = 250;
		int xBound1 = 600;

		// initialize graph
		graph = new Graph(data, xBound1, yBound1, false);
		graph.initializeGraph(graphName, maxX, 0, xBound1, yBound1, pauseResume);
	}

	/*
	 * This is the paintComponent method which overrides that contained in teh
	 * JPanel class. This merely draws "people" at their specified and constantly
	 * updated coordinates with their respective color.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, xBound, yBound);

		g.setColor(Color.CYAN);
		g.drawString("Healthy: " + data.getNumHealthy(), 0, 10);

		g.setColor(Color.MAGENTA);
		g.drawString("Infected: " + data.getNumInfected(), 0, 20);

		g.setColor(Color.GREEN);
		g.drawString("Recovered: " + data.getNumRecovered(), 0, 30);

		for (int i = 0; i < people.size(); i++) {

			g.setColor(people.get(i).getColor());
			g.fillArc((int) people.get(i).getxCoord(), (int) people.get(i).getyCoord(),
					(int) people.get(i).getdiameter(), (int) people.get(i).getdiameter(), 0, 360);

		}

	}

	/*
	 * This class overrides that in the ActionListener interface. It is activated
	 * whenever the timer restarts and is activated. I chose not to call
	 * timer.restart() here for more readable code when it comes to running the
	 * other thread. This method loops through the Person[] and tells each person to
	 * move so long as they are not socially distanced. It also will run an inner
	 * loop to check for collisions between people. Then it will check if a person
	 * is infected. If they are, that person will begin the recovery process. The
	 * timer will stop when there are no more infected people. Lastly it will
	 * repaint the screen so paintComponent can update the AnimationWindow.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {

		/*
		 * 
		 * QUADTREE IMPLEMENTATION We need to create our quadtree in here so it can be
		 * updated as the particles move
		 * 
		 */
		qt = new Quadtree(r, people, 4);

		// TODO Auto-generated method stub
		// timer.restart();
		if (!paused) {
			// everyone who can move must move
			for (int i = 0; i < people.size(); i++) {
				if (!people.get(i).isStationary()) {
					people.get(i).move();
				}
				

				if (!quadtree) {
					// check for collisions
					for (int j = 0; j < people.size(); j++) {
						if (people.get(i) != people.get(j)) {
							if (people.get(i).isTouching(people.get(j))) {
								people.get(i).move();
							}
						}
					}
				}else {
					/*
					 * QUADTREE!!!!!! we want to create a rectangle to surround our person to see
					 * the closest neighbors
					 * 
					 */
					Rectangle check = new Rectangle(people.get(i).getxCoord() - 5, people.get(i).getyCoord() - 5, 10, 10);
					ArrayList<Person> nearest = qt.query(check);
					// check for collisions
					for (Person j : nearest) {
						if (people.get(i) != j) {
							if (people.get(i).isTouching(j)) {
								people.get(i).move();
							}
						}
					}
				}
				// if infected, then begin recovering
				if (people.get(i).isInfected()) {

					people.get(i).setRecovering(true);
					people.get(i).recover();

				}
			}
			repaint();
			if (data.getNumHealthy() + data.getNumRecovered() == people.size() && graph.x == graph.maxX - 20) {
				timer.stop();
			}
		}
	}

	/*
	 * This determines if the animation can still run.
	 * 
	 * @return true if the animation can run
	 * 
	 * @return false if the animation is done
	 */
	public boolean canRun() {
		return !(data.getNumHealthy() + data.getNumRecovered() == people.size() && graph.x == graph.maxX - 20);
	}

	/*
	 * Returns the graph for this specific animation
	 * 
	 * @return graph associated with the animation
	 */
	public Graph getGraph() {
		return graph;
	}

	/*
	 * PLEASE NOTE: THE REST OF THIS CLASS IS NOT ENTIRELY MY OWN CODE. IT WAS
	 * MODIFIED FROM A PROGRAM I FOUND ONLINE USED TO PAUSE PROGRAMS.
	 */

	/*
	 * This creates a new thread for the program so the animation can be paused. If
	 * the thread is running, it will do work.
	 */
	public Thread counter = new Thread(new Runnable() {
		@Override
		public void run() {
			if (true) {
				work();
			}
		}
	});

	/*
	 * This is the object that is associated with both running threads.
	 * 
	 * @return lock object used by multiple threads
	 */
	public Object getLock() {
		return lock;
	}

	/*
	 * This returns the newly created thread.
	 * 
	 * @return counter - Thread used for pausing and playing
	 */
	public Thread getCounter() {
		return counter;
	}

	/*
	 * This method will update both the animation timer and the graph timer so long
	 * as both can continue to run. As long as they can we will allow for the
	 * program to be paused.
	 */
	private void work() {
		if (canRun() && graph.canRun()) {
			allowPause();
			timer.restart();
			graph.timer.restart();
		}
		done();
	}

	/*
	 * This is where the pausing magic really takes place. We say that the lock
	 * object is a part of both running threads and we check the value of paused. If
	 * the animation is paused both timers are stopped for as long as the user
	 * wishes while the lock object calls wait() to halt the program indefinitely.
	 */
	private void allowPause() {
		synchronized (lock) {
			try {
				if (paused) {
					timer.stop();
					graph.getTimer().stop();
				}
				lock.wait();
			} catch (InterruptedException e) {
				// nothing
				System.out.println("could not stop");
			}
		}
	}

	/*
	 * This creates a new ActionListener which is applied to the play/pause button
	 * for the program.
	 */
	private java.awt.event.ActionListener pauseResume = new java.awt.event.ActionListener() {
		/*
		 * When the play/pause button is clicked this will trigger setting the paused
		 * state to whatever it was not before the button is pressed. We then pause the
		 * graph as well and change the displayed image on the play/pause button. The
		 * lock object then notifies all which ends the lock.wait() method that was
		 * called. This whole process allows the user to pause the entire program
		 * indefinitely.
		 */
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			paused = !paused;

			graph.setPaused(paused);

			if (paused) {
				graph.getPlayPause().setText("Play");
				graph.getPlayPause().setIcon(pauseImg);
			} else {
				graph.getPlayPause().setText("Pause");
				graph.getPlayPause().setIcon(playImg);
			}

			synchronized (lock) {
				lock.notifyAll();
			}
		}
	};

	/*
	 * This just pauses everything when the program is done running.
	 */
	private void done() {
		paused = true;
	}

	public Timer getTimer() {
		// TODO Auto-generated method stub
		return timer;
	}
}
