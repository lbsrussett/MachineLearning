import java.util.ArrayList;
import java.util.Random;

/**
 * @author laura sullivan-russett
 * @version December 11, 2017
 *
 * Ant class to create an instance of an ant and create an array of cluster
 * numbers which data points will be assigned to.  Solutions are initialized
 * randomly and updated each iteration using the input pheromone matrix
 */
public class Ant {
	private int[] solution;
	private double[][] pheromones;
	private int clustNum;
	//Q_variable to control solution updates based on pheromone matrix
	private final double Q_0 = 0.98;
	
	/**
	 * constructor to create an instance of an Ant and randomly initialize its solution 
	 * array to random values 
	 * 
	 * @param clustNum
	 * @param pheromones
	 */
	public Ant(int clustNum, double[][] pheromones) {
		solution = new int[pheromones.length];
		this.clustNum = clustNum;
		initSolutions();
	}
	/**
	 * initSolutions method to initialize solution matrix with random cluster
	 * numbers
	 */
	private void initSolutions() {
		for(int i = 0; i < solution.length; i++) {
			Random rand = new Random();
			solution[i] = rand.nextInt(clustNum) + 1;
		}
		
	}
	/**
	 * antSearch method to take in the updated pheromone matrix to create a solution
	 * @param pheromones
	 */
	public void antSearch(double[][] pheromones) {
		solution = new int[pheromones.length];
		this.pheromones = pheromones;
		createSolution();
	}
	/**
	 * createSolution method to update the solution array using the pheromone matrix
	 * to determine which cluster number to put into the array
	 */
	private void createSolution() {
		//Random double [0,1] to compare to variable Q_0
		Random rand = new Random();
		for(int i = 0; i < solution.length; i++) {
			double highest = 0;
			double prob = rand.nextDouble();
			//If the random double is less than Q_0, update the solution using the pheromone matrix
			if(prob < Q_0) {
				for(int j = 0; j < clustNum; j++) {
					if(pheromones[i][j] > highest) {
						//If the pheromone value at this index is high, use that index as
						// the cluster number
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
	}
	/**
	 * returnSolution method to return the ant's current solution
	 * 
	 * @return current solution
	 */
	public int[] returnSolution() {
		return solution;
	}
}