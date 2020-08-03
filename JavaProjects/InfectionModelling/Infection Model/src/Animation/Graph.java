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
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

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
 * This is the Graph class which will show the data collection from the simulation as it runs. The user can also use their mouse to get the data at any given instance on the graph.
 */
public class Graph extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

	// This is the timer used to run the graph and its delay in milliseconds
	public Timer timer;
	private int delay = 1;

	// this is the play/pause button on the menu bar
	public JMenuItem playPause;
	
	// This is the JFrame that will contain this class
	private JFrame frm;

	// this is the starting x position on the graph (starts from the left of the
	// window)
	public int x = 0;

	// these are the heights of the rectangles which will display the data collected
	private int infectedHeight;
	private int healthyHeight;
	private int recoveredHeight;

	// this is the width of each rectangle painted on the graph
	private int width;

	// this is the data class that the graph will collect its data from
	private Data data;

	// these are the bound of the graph window
	private int maxY;
	public int maxX;

	// these are coordinates for the users mouse position on the graph window
	private int mouseX = 0;
	private int mouseY = 0;

	// this is a boolean variable to check if the mouse is on the graph window
	public boolean mouseOnScreen = false;

	// this is a boolean variable to determine if the graph was loaded or is a new
	// graph
	private boolean isLoaded;

	// this ia another shared boolean across threads to allow for pausing
	private volatile boolean paused = false;

	// this is the initial state of the string that will display the data where ever
	// the users cursor is
	private String showData = "";

	// this is the legend to explain the order of the data shown in showData
	private String legend = "Legend: (Healthy, Infected, Recovered)";

	/*
	 * This is the Graph constructor which initializes the global variables not
	 * initialized already. It will check to see if a graph is an instance of a
	 * loaded graph using a boolean variable, start the timer, and add the mouse
	 * action listeners.
	 * 
	 * @param d = Data class passed into the graph to display data
	 * 
	 * @param maxX = max x coordinate position of the JFrame window
	 * 
	 * @param maxY = max y coordinate position of the JFrame window
	 * 
	 * @param loaded = boolean variable is the graph is a loaded graph
	 */
	public Graph(Data d, int maxX, int maxY, boolean loaded) {
		isLoaded = loaded;
		data = d;
		width = 1;
		infectedHeight = data.getNumInfected();
		this.maxX = maxX;
		this.maxY = maxY;
		timer = new Timer(delay, this);
		timer.start();
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	/*
	 * This method initializes the graph which has a menu bar with a File menu and a
	 * play/pause button. The cursor for this window is also set to be invisible
	 * intentionally.
	 * 
	 * @param name = title of the JFrame window defined by parameters of the
	 * animation set in the Menu class
	 * 
	 * @param startX = the x coordinate on the user's screen the JFrame will be
	 * created at
	 * 
	 * @param startY = the y coordinate on the user's screen the JFrame will be
	 * created at
	 * 
	 * @param xBound = the width of the JFrame window
	 * 
	 * @param yBound = the height of the JFrame window
	 * 
	 * @param pauseResume = ActionListener defined in the AnimationWindow class that
	 * enables pausing
	 */
	public void initializeGraph(String name, int startX, int startY, int xBound, int yBound,
			ActionListener pauseResume) {

		frm = new JFrame(name);
		Cursor blank = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invis");
		setCursor(blank);
		frm.add(this);

		// JMenu items are created with their titles and images
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save Graph Data");
		JMenuItem load = new JMenuItem("Load Graph Data");
		playPause = new JMenuItem("Pause");
		playPause.setIcon(new ImageIcon("pause-button.png"));
		playPause.setBounds(20, 0, 30, 20);

		// this is the keyboard shortcut to activate the play/pause button
		playPause.setMnemonic(KeyEvent.VK_P);
		// adds the pauseResume action listener from the AnimationWindow class to enable
		// pausing to be P
		playPause.addActionListener(pauseResume);
		playPause.setPreferredSize(new Dimension(80, (int) playPause.getPreferredSize().getHeight()));

		// creates an action listener for the Save menu item
		class SaveActionListener implements ActionListener {
			// when the Save menu item is pressed, if the animation is no longer running, a
			// new SaveWindow will be created and displayed.
			public void actionPerformed(ActionEvent e) {

				if (!timer.isRunning()) {

					SaveWindow save = new SaveWindow(name, frm, data);
					save.setVisible(true);

				} else {
					JOptionPane.showMessageDialog(null, "Cannot save while program is running", "Save Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		// creates an action listener for the Load menu item
		class LoadActionListener implements ActionListener {
			/*
			 * When the Load menu item is pressed, if the animation is not running, a new
			 * LoadWindow will be created and displayed.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadWindow load = new LoadWindow(frm, pauseResume);
				load.setVisible(true);
			}
		}

		// sets the keyboard shortcut for the Load menu Item to be Ctrl+L
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		// adds the LoadActionListener to the Load menu item
		load.addActionListener(new LoadActionListener());

		// sets the keyboard shortcut for the Save menu item to be Ctrl+S
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		// adds the SaveActionListener to the Save menu item
		save.addActionListener(new SaveActionListener());

		// adds the Save and Load menu items to the File menu on the menu bar (save only if the file has not been loaded)
		if (!isLoaded) {
			file.add(save);
		}
		file.add(load);

		// this next block of code initializes the JFrame and adds the menu bar
		frm.setBounds(startX, startY, xBound, yBound);
		frm.setFocusable(true);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		menuBar.add(file);
		menuBar.add(playPause);
		frm.setJMenuBar(menuBar);
		// frm.add(graph);
		frm.setVisible(true);
	}

	public JMenuItem getPlayPause() {
		return playPause;
	}

	public void pause() {
		timer.stop();
	}

	public void play() {
		timer.restart();
	}

	public Timer getTimer() {
		return timer;
	}
	
	public JFrame getFrame() {
		return frm;
	}

	/*
	 * This method overrides that in the JPanel class. This will create rectangles
	 * of stacked on top of each other with a height equal to the percentage of the
	 * JPanel defined by the percentage of each health state of the people in the
	 * simulation. The rectangles are created from the bottom up (the need for
	 * subtraction) and the (maxY - 55) term is used due to the actual bounds of the
	 * JPanel laying out of view.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 700, 300);

		// draw rectangles
		for (int i = 0; i < data.getInfectedPercentage().size(); i++) {

			infectedHeight = (int) ((maxY - 55) * data.getInfectedPercentage().get(i));
			healthyHeight = (int) ((maxY - 55) * data.getHealthyPercentage().get(i));
			recoveredHeight = (int) ((maxY - 55) * data.getRecoveredPercentage().get(i));

			g.setColor(Color.MAGENTA);
			g.fillRect(width * i, maxY - 55, width, -infectedHeight);
			g.setColor(Color.CYAN);
			g.fillRect(width * i, (maxY - 55) - infectedHeight, width, -healthyHeight);
			g.setColor(Color.GREEN);
			g.fillRect(width * i, ((maxY - 55) - infectedHeight) - healthyHeight, width, -recoveredHeight);

		}

		g.setColor(Color.BLACK);

		// draw the showData string based on the data displayed at the x value of the
		// user's cursor as well as a crosshair where ever the user's cursor lies within
		// the window
		if (mouseOnScreen) {
			g.drawLine(mouseX - 5, mouseY, mouseX + 5, mouseY);
			g.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 5);

			g.drawString(showData, mouseX + 4, mouseY + 10);

		}

		// draws the legend describing the order of the data displayed
		g.drawString(legend, 0, 10);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		// The program is set up to not restart the timer here to allow for pauses but a
		// loaded file must restart its timer by itself
		if (isLoaded) {
			timer.restart();
			repaint();
		} else {
			// if the animation is not paused, increase the x coordinate to where the next
			// rectangle is to be drawn and update the data
			if (!paused) {
				x = x + width;
				data.updateData();

				// if the graph is at the end of the screen and the animation is over, stop the
				// timer, else resize the graph
				if (x == maxX - 20 && data.getNumInfected() == 0) {
					timer.stop();
				} else if (x == maxX - 20) {
					data.resize();
					// try to show the data where the mouse is, but if it is out of bounds of the
					// data ArrayLists we change the showData string to display that there is no
					// data there
					try {
						showData = "(" + data.getHealthy().get(mouseX - 1) + ", " + data.getInfected().get(mouseX - 1)
								+ ", " + data.getRecovered().get(mouseX - 1) + ")";
					} catch (IndexOutOfBoundsException e) {
						showData = "(No Data)";
					}
					// this is a visually pleasing way to redefine where the graph is resized to
					// while the program is running
					// any other expression would either resize to soon or one the graph had gone
					// off screen
					// to my knowledge is is kind of arbitrary
					x = (maxX / 2) + 80;
				}
				// repaint the JPanel
				repaint();
			}
		}
	}

	/*
	 * Return if the Animation can still run.
	 * 
	 * @return true if the animation can run
	 * 
	 * @return false if the animation can not run
	 */
	public boolean canRun() {
		return !(x == maxX - 20 && data.getNumInfected() == 0);
	}

	/*
	 * Returns the Data class being used by the graph.
	 * 
	 * @return data = the Data class that the graph is displaying the data for
	 */
	public Data getData() {
		return data;
	}

	public void setPaused(boolean pause) {
		paused = pause;
	}

	/*
	 * NOT USED
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * This method simply says if the user's mouse is moved, update the mouse
	 * coordinates and display the data at the x value on the graph. If the data can
	 * not be showed then showData is set to explain that there is not data to be
	 * found at that point.
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		mouseX = arg0.getX();
		mouseY = arg0.getY();

		try {
			showData = "(" + data.getHealthy().get(mouseX - 1) + ", " + data.getInfected().get(mouseX - 1) + ", "
					+ data.getRecovered().get(mouseX - 1) + ")";
		} catch (IndexOutOfBoundsException e) {
			showData = "(No Data)";
		}

		repaint();
	}

	/*
	 * NOT USED
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * This method simply says if the mouse has entered the window's bounds, the
	 * cursor is onScreen and showData must show the data where the cursor is
	 * located.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseOnScreen = true;
		if (mouseX < x) {
			showData = "(" + data.getHealthy().get(mouseX) + ", " + data.getInfected().get(mouseX) + ", "
					+ data.getRecovered().get(mouseX) + ")";
		}

	}

	/*
	 * NOT USED
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseOnScreen = false;

	}

	/*
	 * NOT USED
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * NOT USED
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
