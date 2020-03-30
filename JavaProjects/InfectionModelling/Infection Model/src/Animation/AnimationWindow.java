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
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
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

public class AnimationWindow extends JPanel implements ActionListener {

	private Person[] people;
	private Timer timer;
	private int delay = 1;
	private int xBound;
	private int yBound;
	private Data data;
	private Graph graph;
	JMenuItem playPause;
	private ImageIcon playImg = new ImageIcon("play-button.png");
	private ImageIcon pauseImg = new ImageIcon("pause-button.png");

	Object lock = new Object();
	private volatile boolean paused = false;

	public AnimationWindow(int numPeople, int radius, int maxX, int maxY, Random rnd, int speed, int percentDistanced,
			String graphName) {
		// counter.start();
		xBound = maxX;
		yBound = maxY;
		people = new Person[numPeople];
		for (int i = 0; i < people.length; i++) {
			int percent = rnd.nextInt(100) + 1;
			if (percent < percentDistanced) {
				people[i] = new Person(radius, maxX - 10, maxY - 50, 0, true, pauseResume);
			} else {
				people[i] = new Person(radius, maxX - 10, maxY - 50, speed, false, pauseResume);
			}
		}

		data = new Data(people);
		for (int i = 0; i < people.length; i++) {
			if (!people[i].isStationary()) {
				people[i].setInfected(true);
				break;
			}
		}
		setFocusable(false);
		timer = new Timer(delay, this);
		timer.start();
		
		int yBound1 = 250;
		int xBound1 = 600;

		graph = new Graph(data, xBound1, yBound1, false);
		graph.initializeGraph(graphName, maxX, 0, xBound1, yBound1, pauseResume);
	}

	public void pause() {
		timer.stop();
		// isPaused = true;
	}

	public void play() {
		timer.restart();
		// isPaused = false;
	}

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

		for (int i = 0; i < people.length; i++) {

			g.setColor(people[i].getColor());
			g.fillArc((int) people[i].getxCoord(), (int) people[i].getyCoord(), (int) people[i].getRadius(),
					(int) people[i].getRadius(), 0, 360);

			// System.out.println(people[i].getxCoord() + ", " + people[i].getyCoord());
			// g.setColor(Color.black);
			// g.fillArc((int)people[i].getxCenter(), (int)people[i].getyCenter(), 5, 5, 0,
			// 360);

			if (people[i].getxCoord() - people[i].getRadius() <= 0) {

				people[i].setxVect(-(people[i].getxVect()));
				people[i].setxCoord(people[i].getRadius() + 1);

			} else if (people[i].getxCoord() + people[i].getRadius() >= xBound - 10) {

				people[i].setxVect(-(people[i].getxVect()));
				people[i].setxCoord(xBound - 10 - people[i].getRadius() - 1);

			}

			if (people[i].getyCoord() - people[i].getRadius() <= 0) {

				people[i].setyVect(-(people[i].getyVect()));
				people[i].setyCoord(people[i].getRadius() + 1);

			} else if (people[i].getyCoord() + people[i].getRadius() >= yBound - 40) {

				people[i].setyVect(-(people[i].getyVect()));
				people[i].setyCoord(yBound - 40 - people[i].getRadius() - 1);
			}

			people[i].move();
			for (int j = 0; j < people.length; j++) {
				if (people[i] != people[j]) {
					if (people[i].isTouching(people[j])) {
						people[i].move();
					}
				}
			}

		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// timer.restart();
		if (!paused) {
			for (int i = 0; i < people.length; i++) {
				if (people[i].isInfected() && !people[i].getRecovering()) {

					people[i].setRecovering(true);
					people[i].recover();

				}
			}
			repaint();
			if (data.getNumHealthy() + data.getNumRecovered() == people.length && graph.x == graph.maxX - 20) {
				timer.stop();
			}
		}
	}

	public boolean canRun() {
		return !(data.getNumHealthy() + data.getNumRecovered() == people.length && graph.x == graph.maxX - 20);
	}

	public Graph getGraph() {
		return graph;
	}

	public Thread counter = new Thread(new Runnable() {
		@Override
		public void run() {
			if (true) {
				work();
			}
		}
	});

	public Object getLock() {
		return lock;
	}

	public Thread getCounter() {
		return counter;
	}

	private void work() {
		if (canRun() && graph.canRun()) {
			allowPause();
			timer.restart();
			graph.timer.restart();
			for (int i = 0; i < people.length; i++) {
				people[i].pause(false);
			}
		}
		done();
	}

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

	private java.awt.event.ActionListener pauseResume = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			paused = !paused;

			graph.setPaused(paused);

			if (paused) {
				graph.getPlayPause().setText("Play");
				graph.getPlayPause().setIcon(pauseImg);
				for (int i = 0; i < people.length; i++) {
					people[i].pause(true);
				}
			} else {
				graph.getPlayPause().setText("Pause");
				graph.getPlayPause().setIcon(playImg);
				for (int i = 0; i < people.length; i++) {
					people[i].pause(false);
				}
			}

			synchronized (lock) {
				lock.notifyAll();
			}
		}
	};

	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// nothing
			System.out.println("WHOOPS");
		}
	}

	private void done() {
		// button.setText("Start");
		paused = true;
	}

}
