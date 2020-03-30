package Animation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;

//import javax.swing.Timer;

import java.util.Timer;

public class Person {//implements ActionListener {

	private double xVect;
	private double yVect;
	private double radius;
	private double xCoord;
	private double yCoord;
	private boolean healthy;
	private boolean infected;
	private boolean recovered;
	private boolean wasInfected;
	private Color indicator = Color.BLACK;
	private double mass;
	private double xCenter;
	private double yCenter;
	private Timer timer;
	private double angle;
	private final int STEPS_TO_RECOVERY = 2500;

	private int recoverTime;

	private boolean wasFrozen = false;

	private boolean isStationary;
	
	private boolean isPaused = false;
	private boolean recovering = false;

	public Person(int rad, double maxX, double maxY, int speed, boolean unmoving, ActionListener pausePlay) {
		Random rnd = new Random();
		if (rnd.nextInt(100) + 1 <= 13) {
			recoverTime = 2500;
		} else {
			recoverTime = 1250;
		}

		// timer = new Timer(recoverTime, pausePlay);

		angle = rnd.nextDouble() * (2 * Math.PI);
		xVect = Math.cos(angle) * speed;
		yVect = Math.sin(angle) * speed;
		timer = new Timer();

		if (unmoving) {
			mass = 100000;
		} else {
			mass = 5;
		}

		xCoord = rnd.nextDouble() * maxX - radius;
		while (xCoord < 10) {
			xCoord = rnd.nextDouble() * maxX - radius;
		}
		yCoord = rnd.nextDouble() * maxY - radius;
		while (yCoord < 10) {
			yCoord = rnd.nextDouble() * maxY - radius;
		}
		yCenter = yCoord + .5 * radius;
		xCenter = xCoord + .5 * radius;

		radius = rad;
		healthy = true;
		infected = false;
		recovered = false;
		wasInfected = false;
		this.isStationary = unmoving;
	}
	
	public boolean getRecovering() {
		return recovering;
	}
	
	public void setRecovering(boolean rec) {
		recovering = rec;
	}

	public boolean isStationary() {
		return isStationary;
	}

	public double getxVect() {
		return xVect;
	}

	public void setxVect(double xVect) {
		this.xVect = xVect;
	}

	public double getyVect() {
		return yVect;
	}

	public void setyVect(double yVect) {
		this.yVect = yVect;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getxCoord() {
		return xCoord;
	}

	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
		xCenter = this.xCoord + .5 * radius;
	}

	public double getyCoord() {
		return yCoord;
	}

	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
		yCenter = this.yCoord + .5 * radius;
	}

	public boolean isHealthy() {
		return healthy;
	}

	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
		infected = false;
		recovered = false;
	}

	public boolean isInfected() {
		return infected;
	}

	public void setInfected(boolean infected) {
		this.infected = infected;
		healthy = false;
		recovered = false;
		wasInfected = true;
	}

	public boolean isRecovered() {
		return recovered;
	}

	public void setRecovered(boolean recovered) {
		this.recovered = recovered;
		healthy = false;
		infected = false;
	}

	public double getMass() {
		return mass;
	}

	public double getxCenter() {
		return xCenter;
	}

	public double getyCenter() {
		return yCenter;
	}

	public boolean getWasInfected() {
		return wasInfected;
	}

	public void move() {
		/*
		 * if (critical && isInfected()) { freeze(); } else if (isRecovered() &&
		 * wasFrozen){ restart(); }
		 */

		yCoord = (yCoord + yVect);
		xCoord = (xCoord + xVect);
		yCenter = (yCenter + yVect);
		xCenter = (xCenter + xVect);
	}

	public Color getColor() {
		if (infected) {
			indicator = Color.MAGENTA;
		} else if (recovered) {
			indicator = Color.green;
		} else {
			indicator = Color.CYAN;
		}

		return indicator;
	}
	
	private class Task extends TimerTask {
		private int step = 0;

		@Override
		public void run() {
			if (isPaused()) {
				// do nothing
			} else {
				increaseStep();
				if (step == STEPS_TO_RECOVERY) {
					setRecovered(true);
					timer.cancel();
				}
			}
		}

		public void increaseStep() {
			step++;
		}
	}
	;

	public void recover() {
		Task task = new Task();
		timer.schedule(task, 0, (STEPS_TO_RECOVERY / recoverTime));
	}

	public boolean isPaused() {
		return isPaused;
	}
	
	public void pause(boolean isPau) {
		isPaused = isPau;
	}

	public boolean isTouching(Person p) {
		// dx and dy are the vertical and horizontal distances
		double dx = xCenter - p.getxCenter();
		double dy = yCenter - p.getyCenter();

		// Determine the straight-line distance between centers.
		double d = Math.sqrt((dy * dy) + (dx * dx));

		// Check doubleersections
		if (d > (radius + p.getRadius())) {
			// No Solution. Circles do not doubleersect
			return false;
		} /*
			 * else if (d < Math.abs(radius - p.getRadius())) { // No Solution. one circle
			 * is contained in the other return xCoord = xCoord - radius;
			 * p.setxCoord(p.getxCoord() + p.getRadius()); return false;
			 * 
			 * }
			 */
		else {

			double y2Vect = p.getyVect();
			double x2Vect = p.getxVect();
			p.setxVect((x2Vect * (p.getMass() - mass) + (2 * mass * xVect)) / (mass + p.getMass()));
			xVect = (xVect * (mass - p.getMass()) + (2 * p.getMass() * x2Vect)) / (mass + p.getMass());
			p.setyVect((y2Vect * (p.getMass() - mass) + (2 * mass * yVect)) / (mass + p.getMass()));
			yVect = (yVect * (mass - p.getMass()) + (2 * p.getMass() * y2Vect)) / (mass + p.getMass());
		}

		if (isInfected() && !p.getWasInfected()) {
			p.setInfected(true);
		}
		if (p.isInfected() && !wasInfected) {
			setInfected(true);
		}

		return true;
	}

	/*@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		setRecovered(true);
	}*/

}
