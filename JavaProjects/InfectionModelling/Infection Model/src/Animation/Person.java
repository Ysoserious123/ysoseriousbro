package Animation;

import java.awt.Color;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
//import java.util.TimerTask;

//import javax.swing.Timer;

//import java.util.Timer;

/*
 * This is the person class. A person will have randomly generated x and y vectors using a randomly generated angle
 * and taking the sin and cos of that angle to get the x and y components. The vectors are both multiplied by a speed
 * which will be applied to all people in this simulation case. This will simulate random movement in one direction.
 * Each person will also bounce off the invisible walls at boundaries that are passed into the constructor
 * at an angle equal to that of the incident angle. People will become infected as they move about by coming 
 * into contact with Infected People. After this happens a Person will begin to recover. Recovery time is based on 
 * population percentages from the NJ 2010 Census where about 13% of the population is aged 65 or over. If a person is
 * part of this population, they are considered critical and will take twice as long to recover than a Person who is
 * not critical. This Simulation also allows for social distancing in which a person will randomly choose to social distance.
 * If a person is social distanced, they do not move. To achieve this I assign them relatively infinite mass to every other
 * person. This means when a person who is not socially distanced comes into contact with someone who is socially distanced they
 * will simply bounce off of that person and possibly infect them or get infected. 
 *  
 */
public class Person {
	
	//These are the window boundaries for the people to "bounce" off of
	private double xWall;
	private double yWall;
	
	//These are Velocity vectors
	private double xVect;
	private double yVect;
	
	//diameter of each person
	private double diameter;
	
	//These are the coordinates at which the animation draws each person
	private double xCoord;
	private double yCoord;
	
	//These are the coordinates of the center of each person which are used in collision detection.
	private double xCenter;
	private double yCenter;
	
	//These are booleans describing the health state of the Person
	private boolean healthy;
	private boolean infected;
	private boolean recovered;
	private boolean wasInfected;
	
	//This is a color associated with the health state of the individual: Cyan for healthy, Magenta for infected, and Green for recovered
	private Color indicator;
	
	//This is the mass of the person which only serves to calculate new velocity vectors keeping the law of
	// Conservation of Momentum. Again people who are socially distanced do not move so they have relative infinite mass
	private double mass;
	
	//This is the angle that will generate the random velocity vectors
	private double angle;
	
	//These are numbers used to give each Person a recovery time
	private final int STEPS_TO_RECOVERY;
	private int stepToRecovery;

	//This determines if a Person is social distanced
	private boolean isStationary;

	//This states if the person is in the process of recovering
	private boolean recovering = false;

	/*
	 * This is the constructor for a Person. All global variables are populated mostly with the parameters of the constructor except for STEPS_TO_RECOVERY, stepToRecovery, health states, and
	 * the indicating color which is set to a default black color. Uses the Random class to give the Person random x and y coordinates and to assign a random angle for motion. The centers are also adjusted
	 * to be offset by the radius of a person since the drawArc() does not use a coordinate system where (0, 0) is not in the center. 
	 * @param diam = diameter for the person
	 * @param maxX = AnimationWindow x boundary
	 * @param maxY = AnimationWindow y boundary
	 * @param speed = speed to scale the velocity vectors
	 */
	public Person(int diam, double maxX, double maxY, int speed, boolean unmoving) {
		stepToRecovery = 0;
		yWall = maxY;
		xWall = maxX;
		Random rnd = new Random();
		if (rnd.nextInt(100) + 1 <= 13) {
			STEPS_TO_RECOVERY = 700;
		} else {
			STEPS_TO_RECOVERY = 350;
		}

		// timer = new Timer(recoverTime, pausePlay);

		angle = rnd.nextDouble() * (2 * Math.PI);
		xVect = Math.cos(angle) * speed;
		yVect = Math.sin(angle) * speed;
		// timer = new Timer();

		if (unmoving) {
			mass = 100000;
		} else {
			mass = 5;
		}

		xCoord = rnd.nextDouble() * maxX - diameter;
		while (xCoord < 10) {
			xCoord = rnd.nextDouble() * maxX - diameter;
		}
		
		yCoord = rnd.nextDouble() * maxY - diameter;
		while (yCoord < 10) {
			yCoord = rnd.nextDouble() * maxY - diameter;
		}
		
		yCenter = yCoord + .5 * diameter;
		xCenter = xCoord + .5 * diameter;

		diameter = diam;
		healthy = true;
		infected = false;
		recovered = false;
		wasInfected = false;
		this.isStationary = unmoving;
		indicator = Color.BLACK;
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

	public double getdiameter() {
		return diameter;
	}

	public void setdiameter(double diameter) {
		this.diameter = diameter;
	}

	public double getxCoord() {
		return xCoord;
	}

	/*
	 * This updates the x coordinate of the Person as well as its center x coordinate
	 * @param xCoord = new x coordinate for the Person
	 */
	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
		xCenter = this.xCoord + .5 * diameter;
	}

	public double getyCoord() {
		return yCoord;
	}

	/*
	 * This updates the y coordinate of the Person as well as its center y coordinate
	 * @param yCoord = new y coordinate for the Person
	 */
	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
		yCenter = this.yCoord + .5 * diameter;
	}

	public boolean isHealthy() {
		return healthy;
	}

	/*
	 * This ensures that a person is only healthy and not any of the other health states.
	 * @param healthy = boolean variable describing if the Person is healthy or not
	 */
	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
		infected = false;
		recovered = false;
	}

	public boolean isInfected() {
		return infected;
	}

	/*
	 * This ensures that a person is only Infected and not any of the other health states.
	 * @param infected = boolean variable describing if the Person is infected or not
	 */
	public void setInfected(boolean infected) {
		this.infected = infected;
		healthy = false;
		recovered = false;
		wasInfected = true;
	}

	public boolean isRecovered() {
		return recovered;
	}

	/*
	 * This ensures that a person is only recovered and not any of the other health states.
	 * @param recovered = boolean variable describing if the Person is recovered or not
	 */
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

	/*
	 * This is the logic to move a Person. The x, y, and center coordinates are increased (or decreased) by each respective velocity vector. If a person is at the boundaries of the 
	 * AnimationWindow their respective component vector is multiplied by -1 to create the effect of bouncing off at the same angle as the incident angle. The Coordinate is also updated to ensure that 
	 * people don't get "stuck" behind the barriers and keep changing the direction of their velocity vectors which will cause them to get caught in the boundary.
	 */
	public void move() {

		yCoord = (yCoord + yVect);
		xCoord = (xCoord + xVect);
		yCenter = (yCenter + yVect);
		xCenter = (xCenter + xVect);
		
		if (getxCoord() - getdiameter() <= 0) {

			setxVect(-(getxVect()));
			setxCoord(getdiameter() + 1);

		} else if (getxCoord() + getdiameter() >= xWall) {

			setxVect(-(getxVect()));
			setxCoord(xWall - getdiameter() - 1);

		}

		if (getyCoord() - getdiameter() <= 0) {

			setyVect(-(getyVect()));
			setyCoord(getdiameter() + 1);

		} else if (getyCoord() + getdiameter() >= yWall + 20) {

			setyVect(-(getyVect()));
			setyCoord(yWall + 20 - getdiameter() - 1);
		}
	}

	/*
	 * Returns the color associated with the Person's health state.
	 * @return indicator = associated health color
	 */
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

	/*
	 * private class Task extends TimerTask { private int step = 0;
	 * 
	 * @Override public void run() { 
	 * if (isPaused()) { 
	 * // do nothing 
	 * } else {
	 * increaseStep(); 
	 * if (step == STEPS_TO_RECOVERY) 
	 * { setRecovered(true);
	 * timer.cancel(); } 
	 * } 
	 * }
	 * 
	 * public void increaseStep() { step++; } } ;
	 * 
	 * public void recover() { 
	 * Task task = new Task(); 
	 * timer.schedule(task, 0, (STEPS_TO_RECOVERY / recoverTime)); 
	 * }
	 */

	/*
	 * This increases the step to recovery that the Person is currently on. It runs at the rate of the Timer of the AnimationWindow as to ensure it is not instantaneous.
	 */
	public void recover() {
		stepToRecovery++;
		if (stepToRecovery == STEPS_TO_RECOVERY) {
			setRecovered(true);
		}
	}

	/*
	 * Returns if any two people are touching and uses the Conservation of Momentum to calculate the new Velocity vectors. These are all completely elastic collisions.
	 * 
	 * NOTE: THIS CODE IS NOT ENTIRELY MY OWN WAS WAS TAKEN FROM DIFFERENT SCOURCES AND HAS BEEN MODIFIED TO MEET MY PROGRAMS NEEDS.
	 * 
	 * @param p = Person that is part of the population
	 * @return true if this Person and Person p are touching
	 * @return false if this Person and Person p are not touching
	 */
	public boolean isTouching(Person p) {
		// dx and dy are the vertical and horizontal distances
		double dx = xCenter - p.getxCenter();
		double dy = yCenter - p.getyCenter();

		// Determine the straight-line distance between centers.
		double d = Math.sqrt((dy * dy) + (dx * dx));

		// Check intersections
		if (d > (diameter + p.getdiameter())) {
			// No Solution. Circles do not intersect
			return false;
		} else {

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

}
