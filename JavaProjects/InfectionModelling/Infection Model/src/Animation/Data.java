package Animation;

import java.util.ArrayList;

/*
 * This is the Data class. The data class keeps track of how many people are infected, recovered, and healthy and the corresponding percentages based on the population size. These are
 * stored in ArrayLists which will then be used to display the Data on the Graph.
 */
public class Data {
	
	/*
	 * These are all global variables that keep track of things like the number of infected, healthy, and recovered people as well as the percentages associated. There is also 
	 * a Person[] which will be initialized based on the Person[] created in the AnimationWindow class.
	 */
	private int numInfected;
	private int numHealthy;
	private int numRecovered;
	double infectedPercent;
	double healthyPercent;
	double recoveredPercent;
	private ArrayList<Person> people;
	
	/*
	 * This are the storage containers for the data.
	 */
	private ArrayList<Integer> infected = new ArrayList<Integer>();
	private ArrayList<Integer> healthy = new ArrayList<Integer>();
	private ArrayList<Integer> recovered = new ArrayList<Integer>();
	private ArrayList<Double> infectedPercentage = new ArrayList<Double>();
	private ArrayList<Double> healthyPercentage = new ArrayList<Double>();
	private ArrayList<Double> recoveredPercentage = new ArrayList<Double>();
	
	public ArrayList<Double> getInfectedPercentage() {
		return infectedPercentage;
	}

	public void setInfectedPercentage(ArrayList<Double> infectedPercentage) {
		this.infectedPercentage = infectedPercentage;
	}

	public ArrayList<Double> getHealthyPercentage() {
		return healthyPercentage;
	}

	public void setHealthyPercentage(ArrayList<Double> healthyPercentage) {
		this.healthyPercentage = healthyPercentage;
	}

	public ArrayList<Double> getRecoveredPercentage() {
		return recoveredPercentage;
	}

	public void setRecoveredPercentage(ArrayList<Double> recoveredPercentage) {
		this.recoveredPercentage = recoveredPercentage;
	}

	public ArrayList<Integer> getHealthy() {
		return healthy;
	}

	public ArrayList<Integer> getRecovered() {
		return recovered;
	}
	
	/*
	 * This is the Data constructor which sets all data values to zero for the start of the animation. Then the Person[] is initialized.
	 * @param p = Person[] created in the AnimationWindow class
	 */
	public Data(ArrayList<Person> p) {
		numInfected = 0;
		numHealthy = 0;
		numRecovered = 0;
		infectedPercent = 0;
		healthyPercent = 0;
		recoveredPercent = 0;
		people = p;
	}
	
	/*
	 * This is a separate constructor used to load graphs based on saved data from a previous animation run. The saved data only includes the raw numbers of people infected, healthy, and
	 * recovered at a specific point in time. This means that the percentage holders must be filled since this is how we display the graph. 
	 * @param loadedInfected = ArrayList<Integer> filled by different numbers of people infected at given points in time, and was previously saved to a .csv file
	 * @param loadedHealthy = ArrayList<Integer> filled by different numbers of people healthy at given points in time, and was previously saved to a .csv file
	 * @param loadedRecovered = ArrayList<Integer> filled by different numbers of people recovered at given points in time, and was previously saved to a .csv file
	 */
	public Data(ArrayList<Integer> loadedInfected, ArrayList<Integer> loadedHealthy, ArrayList<Integer> loadedRecovered) {
		infected = loadedInfected;
		healthy = loadedHealthy;
		recovered = loadedRecovered;
		
		for (int i = 0; i < infected.size(); i++) {
			infectedPercent = (double)infected.get(i)/(infected.get(i) + healthy.get(i) + recovered.get(i));
			infectedPercentage.add(infectedPercent);
			healthyPercent = (double)healthy.get(i)/(infected.get(i) + healthy.get(i) + recovered.get(i));
			healthyPercentage.add(healthyPercent);
			recoveredPercent = (double)recovered.get(i)/(infected.get(i) + healthy.get(i) + recovered.get(i));
			recoveredPercentage.add(recoveredPercent);
		}
	}
	
	public double getInfectedPercent() {
		return infectedPercent;
	}

	public double getHealthyPercent() {
		return healthyPercent;
	}

	public double getRecoveredPercent() {
		return recoveredPercent;
	}

	public int getNumInfected() {
		return numInfected;
	}
	public void setNumInfected(int numInfected) {
		this.numInfected = numInfected;
	}
	public int getNumHealthy() {
		return numHealthy;
	}
	public void setNumHealthy(int numHealthy) {
		this.numHealthy = numHealthy;
	}
	public int getNumRecovered() {
		return numRecovered;
	}
	public void setNumRecovered(int numRecovered) {
		this.numRecovered = numRecovered;
	}
	
	public ArrayList<Integer> getInfected(){
		return infected;
	}
	
	/*
	 * this method will update the data every time the Graph timer triggers the actionPerformed() method. It loops through the Person population and finds the number
	 * of healthy, infected, and recovered people. Those are then added to their respective storage holders. Then the respective percentages are calculated and put in their
	 * corresponding storage holders.
	 */
	public void updateData() {
		numInfected = 0;
		numHealthy = 0;
		numRecovered = 0;
		for (int i = 0; i < people.size(); i++) {
			if (people.get(i).isHealthy()) {
				numHealthy++;
			}else if (people.get(i).isInfected()) {
				numInfected++;
			}else {
				numRecovered++;
			}
		}
		infected.add(numInfected);
		infectedPercent = (double)numInfected/people.size();
		infectedPercentage.add(infectedPercent);
		healthy.add(numHealthy);
		healthyPercent = (double)numHealthy/people.size();
		healthyPercentage.add(healthyPercent);
		recovered.add(numRecovered);
		recoveredPercent = (double)numRecovered/people.size();
		recoveredPercentage.add(recoveredPercent);
		//System.out.println(report());
	}
	
	/*
	 * This method is called whenever the graph reaches the end of its JFrame. This will remove every other data point to scale down the graph to fit into the Graph window.
	 * Note: some data will be lost and may be very limited if the animation runs for an extraordinary amount of time (relatively speaking).
	 */
	public void resize() {
		for (int i = 1; i < infected.size(); i = i + 2) {
			infected.remove(i);
			infectedPercentage.remove(i);
			healthy.remove(i);
			healthyPercentage.remove(i);
			recovered.remove(i);
			recoveredPercentage.remove(i);
			
		}
	}
	
	
	/*
	 * This method is no longer used but can be called nonetheless to print out the data at a point in time.
	 */
	public String report() {
		return "Healthy: " + numHealthy + ", Infected: " + numInfected + ", Recovered: " + numRecovered;
	}

}
