package proj4;

import java.util.ArrayList;
import java.util.Random;

public class Ant {
	
	/*private int antID;
	private boolean isCarrying = false;
	private Point point;*/
	private int[] solution;
	private double[][] pheromones;
	private int clustNum;
	private final double Q_0 = 0.98;
	
	public Ant(int clustNum, double[][] pheromones) {
		solution = new int[pheromones.length];
		this.clustNum = clustNum;
		initSolutions();
	}
	private void initSolutions() {
		for(int i = 0; i < solution.length; i++) {
			Random rand = new Random();
			solution[i] = rand.nextInt(clustNum) + 1;
		}
		
	}
	public void antSearch(double[][] pheromones) {
		solution = new int[pheromones.length];
		this.pheromones = pheromones;
		createSolution();
	}
	private void createSolution() {
		Random rand = new Random();
		for(int i = 0; i < solution.length; i++) {
			double highest = 0;
			double prob = rand.nextDouble();
			if(prob < Q_0) {
				for(int j = 0; j < clustNum; j++) {
					if(pheromones[i][j] > highest) {
						highest = pheromones[i][j];
						solution[i] = j+1;
					}
				}
			}
			else {
				int clust = rand.nextInt(clustNum);
				solution[i] = clust+1;
			}
		}
		
			/*if(clustNum == 2) {
				if(prob[i] > 0.50) {
					solution[i] = 1;
				}
				else
					solution[i] = 2;
			}
			else if(clustNum == 3) {
				if(prob[i] < 0.33) {
					solution[i] = 1;
				}
				else if (0.33 < prob[i] && prob[i] < 0.66) {
					solution[i] = 2;
				}
				else
					solution[i] = 3;
			}*/
		
	}
	public int[] returnSolution() {
		return solution;
	}
	/*public boolean carrying() {
		return isCarrying;
	}
	public void pickUp(Point p) {
		isCarrying = true;
		point = p;
	}
	public void dropOff(Point p) {
		isCarrying = false;
		point = null;
	}
	public int getID() {
		return antID;
	}
	private double euclideanDistance(double[] point1, double[] point2) {
		double distance = 0;
		for(int i = 0; i < point1.length; i++) {
			double element = (point2[i] - point1[i]);
			element = Math.pow(element, 2);
			distance += element;
		}
		distance = Math.sqrt(distance);
		return distance;
	}
	private boolean checkCluster(Point p) {
		if(p.unclassified()) {
			return false;
		}
		else
			return true;
	}*/
}
