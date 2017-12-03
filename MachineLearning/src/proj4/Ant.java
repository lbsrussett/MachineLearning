package proj4;

import java.util.ArrayList;

public class Ant {
	
	private int antID;
	private boolean isCarrying = false;
	private Point point;
	private ArrayList<Cluster> solution = new ArrayList<Cluster>();
	
	public Ant(int antID) {
		this.antID = antID;
	}
	public void antSearch(Point[][] grid) {
		
	}
	public ArrayList<Cluster> returnSolution() {
		return solution;
	}
	public boolean carrying() {
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
}
