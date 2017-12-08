import java.util.*;
import java.util.Random;
//double check the pheromone calculation - all values are ending up the same.
public class ACO extends ClusteringAlgorithm {
	private Point[] allPoints;
	private Ant[] ants;
	private double[][] pheromones;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private int clustNum, iterations = 0, termination = 0;
	private double[] prob;
	private double currentFitness = Double.MAX_VALUE;
	private int[] currentSol;
	public int NUM_ANTS = 20;
	public double Q = 0.80;
	private final int ITERATIONS = 10;
	
	
	public ACO(double[][] inputs, int clustNum, int antNum, double q) {
		this.clustNum = clustNum;
		this.NUM_ANTS = antNum;
		this.Q = q;
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		initPheromones();
		ants = new Ant[NUM_ANTS];
		for(int i = 0; i < NUM_ANTS; i++) {
			ants[i] = new Ant(clustNum, pheromones);
		}
		
	}
	
	public void startAnts() {
		iterations++;
		for(int i = 0; i < ants.length; i++) {
			ants[i].antSearch(pheromones);
		}
	}
	public void updateClusters(double[][] inputs) {
		int counter = 1;
		while(termination < ITERATIONS) {
			updatePheromones();
			startAnts();
			System.out.print("ACO on iteration " + counter++ + " with " + this.Q + " Q value and " + this.NUM_ANTS + " ants: ");
			WilsonProject4Application.evaluateCluster(this, false);
		}
		WilsonProject4Application.evaluateCluster(this, true);
	}
	@Override
	public ArrayList<Cluster> returnClusters() {
		//System.out.println(iterations);
		return clusters;
	}
	public ArrayList<Cluster> findBestSolution() {
		int[][] sol = new int [ants.length][allPoints.length];
		ArrayList<Cluster> solutions = new ArrayList<Cluster>();
		Ant temp = null;
		double fitness = Double.MAX_VALUE;
		for(int i = 0; i < ants.length; i++) {
			sol[i] = ants[i].returnSolution();
			double tempFit = 0;
			ArrayList<Cluster> c = createClusters(sol[i]);
			for(int j = 0; j < c.size(); j++) {
				tempFit += c.get(j).getAverageDistanceToCenter();
			}
			if(tempFit < fitness) {
				fitness = tempFit;
				temp = ants[i];
				currentSol = ants[i].returnSolution();
				solutions = c;
			}
		}
		//System.out.println(fitness);
		//System.out.println(currentFitness/allPoints.length);
		
		if(currentFitness > fitness) {
			currentFitness = fitness;
		}
		else {
			termination++;
		}
		return solutions;
	}
	private void updatePheromones() {
		ArrayList<Cluster> c = findBestSolution();
		for(int i = 0; i < c.size(); i++) {
			double fitness = c.get(i).getAverageDistanceToCenter();
			for(int j = 0; j < pheromones.length; j++) {
				if(currentSol[j] == i+1) {
					pheromones[j][i] += Q/fitness;
				}
			}
		}
	}
	private ArrayList<Cluster> createClusters(int[] sol) {
		ArrayList<Cluster> soln = new ArrayList<Cluster>();
		if(clustNum == 2) {
			Cluster c1 = new Cluster();
			Cluster c2 = new Cluster();
			c1.addPoint(allPoints[0]);
			for(int i = 1; i < sol.length; i++) {
				if(sol[i] == sol[0]) {
					c1.addPoint(allPoints[i]);
				}
				else {
					c2.addPoint(allPoints[i]);
				}
			}
			c1.createCenter();
			c2.createCenter();
			soln.add(c1);
			soln.add(c2);
		}
		else {
			Cluster c1 = new Cluster();
			Cluster c2 = new Cluster();
			Cluster c3 = new Cluster();
			for(int i = 0; i < sol.length; i++) {
				if(sol[i] == 1) {
					c1.addPoint(allPoints[i]);
				}
				else if(sol[i] == 2) {
					c2.addPoint(allPoints[i]);
				}
				else
					c3.addPoint(allPoints[i]);
			}
			c1.createCenter();
			c2.createCenter();
			c3.createCenter();
			soln.add(c1);
			soln.add(c2);
			soln.add(c3);
		}
		this.clusters = soln;
		return soln;
	}
	
	private void initPheromones() {
		pheromones = new double[allPoints.length][clustNum];
		for(int i = 0; i < allPoints.length; i++) {
			for(int j = 0; j < clustNum; j++) {
				pheromones[i][j] = 0.01;
			}
		}
	}
	/*private void normalize() {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(int i = 0; i < pheromones.length; i++) {
			for(int j = 0; j < clustNum; j++) {
				if(pheromones[i][j] < min) {
					min = pheromones[i][j];
				}
				if(pheromones[i][j] > max) {
					max = pheromones[i][j];
				}
			}
		}
		for(int i = 0; i < pheromones.length; i++) {
			for(int j = 0; j < clustNum; j++) {
				pheromones[i][j] = (pheromones[i][j]-min)/(max-min);
			}
		}
	}*/
}