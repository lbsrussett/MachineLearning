import java.util.*;
import java.util.Random;

/**
 * @author laura sullivan-russett
 * @version December 11, 2017
 * 
 * ACO class to create an ant colony, cluster data points and return the best solution
 *
 */
public class ACO extends ClusteringAlgorithm {
	private Point[] allPoints;
	private Ant[] ants;
	private double[][] pheromones;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private int clustNum, iterations = 0, termination = 0;
	private double[] prob;
	private double currentFitness = Double.MAX_VALUE;
	private int[] currentSol;
	//Tunable parameters for number of ants and Q value used in pheromone updates
	public int NUM_ANTS = 20;
	public double Q = 0.80;
	//Limit of iterations to run once clusters have converged
	private final int ITERATIONS = 10;
	
	
	/**
	 * Constructor to create ants, initialize the pheromone matrix and create points from
	 * the data set inputs. 
	 * 
	 * @param inputs
	 * @param clustNum
	 * @param antNum
	 * @param q
	 */
	public ACO(double[][] inputs, int clustNum, int antNum, double q) {
		this.clustNum = clustNum;
		this.NUM_ANTS = antNum;
		this.Q = q;
		//Create a Point array with all data points
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		//Initialize the pheromone matrix
		initPheromones();
		//Create an array of ants
		ants = new Ant[NUM_ANTS];
		for(int i = 0; i < NUM_ANTS; i++) {
			ants[i] = new Ant(clustNum, pheromones);
		}
		
	}
	
	/**
	 * startAnts method to initialize each ant for a solution search
	 * and send in the current pheromone matrix
	 */
	public void startAnts() {
		iterations++;
		for(int i = 0; i < ants.length; i++) {
			ants[i].antSearch(pheromones);
		}
	}
	/**
	 * startAnts method to initialize each ant for a solution search
	 * and send in the current pheromone matrix
	 */
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
	/**
	 * returnClusters method to return the best cluster solution to the Project4Application
	 */
	@Override
	public ArrayList<Cluster> returnClusters() {
		return clusters;
	}
	/**
	 * findBestSolution method to take the solutions returned by each ant, test their
	 * fitness and return the clusters created by the most fit solution
	 */
	public ArrayList<Cluster> findBestSolution() {
		int[][] sol = new int [ants.length][allPoints.length];
		ArrayList<Cluster> solutions = new ArrayList<Cluster>();
		Ant temp = null;
		double fitness = Double.MAX_VALUE;
		//For each ant, get its solution, create the clusters and test their fitness
		for(int i = 0; i < ants.length; i++) {
			sol[i] = ants[i].returnSolution();
			double tempFit = 0;
			ArrayList<Cluster> c = createClusters(sol[i]);
			//Use average distance from the center point to determine solution fitness
			for(int j = 0; j < c.size(); j++) {
				tempFit += c.get(j).getAverageDistanceToCenter();
			}
			//Select the fitness with the lowest distance value
			if(tempFit < fitness) {
				fitness = tempFit;
				temp = ants[i];
				currentSol = ants[i].returnSolution();
				solutions = c;
			}
		}
		
		if(currentFitness > fitness) {
			currentFitness = fitness;
		}
		else {
			termination++;
		}
		//Return the clusters created by the best solution
		return solutions;
	}
	/**
	 * updatePheromones method to recalculate pheromone matrix based on the 
	 * fitness of the most recent best solution returned by the ant colony.
	 * Updates are calculated using the Ant-Quantity Ant System method where 
	 * the change in pheromone concentration is = Q/distance from center point.
	 */
	private void updatePheromones() {
		//get best solution from current iteration
		ArrayList<Cluster> c = findBestSolution();
		for(int i = 0; i < c.size(); i++) {
			double fitness = c.get(i).getAverageDistanceToCenter();
			//use average distance from center point to calculate pheromone change
			for(int j = 0; j < pheromones.length; j++) {
				if(currentSol[j] == i+1) {
					pheromones[j][i] += Q/fitness;
				}
			}
		}
	}
	/**
	 * createClusters method to take an ant's solution and form clusters from the
	 * data points. Cluster number was determined using K-Means and input into ACO.
	 * 
	 * @param sol
	 * @return clusters created by solution
	 */
	private ArrayList<Cluster> createClusters(int[] sol) {
		ArrayList<Cluster> soln = new ArrayList<Cluster>();
		//Create clusters and add each point that is assigned to each cluster by the solution
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
			//Create center points for each cluster to use for fitness measurement
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
		//Return the clusters created by the solution
		this.clusters = soln;
		return soln;
	}
	
	/**
	 * initPheromones method to initialize a pheromone matrix of N x M dimensions
	 * where N = number of data points and M = number of clusters.  Matrix is 
	 * initialized with value of 0.1 at each element.
	 */
	private void initPheromones() {
		pheromones = new double[allPoints.length][clustNum];
		for(int i = 0; i < allPoints.length; i++) {
			for(int j = 0; j < clustNum; j++) {
				pheromones[i][j] = 0.01;
			}
		}
	}
}