package Animation;

import java.util.ArrayList;

public class Data {
	private int numInfected;
	private int numHealthy;
	private int numRecovered;
	double infectedPercent;
	double healthyPercent;
	double recoveredPercent;
	private int[] data;
	private Person[] people;
	private int numPoints;
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

	public Data(Person[] p) {
		numInfected = 0;
		numHealthy = 0;
		numRecovered = 0;
		infectedPercent = 0;
		healthyPercent = 0;
		recoveredPercent = 0;
		data = new int[] {numHealthy, numInfected, numRecovered};
		people = p;
	}
	
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
	
	public void updateData() {
		numInfected = 0;
		numHealthy = 0;
		numRecovered = 0;
		for (int i = 0; i < people.length; i++) {
			if (people[i].isHealthy()) {
				numHealthy++;
			}else if (people[i].isInfected()) {
				numInfected++;
			}else {
				numRecovered++;
			}
		}
		infected.add(numInfected);
		infectedPercent = (double)numInfected/people.length;
		infectedPercentage.add(infectedPercent);
		healthy.add(numHealthy);
		healthyPercent = (double)numHealthy/people.length;
		healthyPercentage.add(healthyPercent);
		recovered.add(numRecovered);
		recoveredPercent = (double)numRecovered/people.length;
		recoveredPercentage.add(recoveredPercent);
		//System.out.println(report());
	}
	
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
	
	public String report() {
		return "Healthy: " + numHealthy + ", Infected: " + numInfected + ", Recovered: " + numRecovered;
	}

}
