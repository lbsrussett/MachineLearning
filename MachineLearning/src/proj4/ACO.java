package proj4;

import java.util.ArrayList;
import java.util.Random;

public class ACO extends ClusteringAlgorithm {
	private Point[] allPoints;
	private Ant[] ants;
	private Point[][] grid;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private final int NUM_ANTS = 20;
	
	public ACO(double[][] inputs) {
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		ants = new Ant[NUM_ANTS];
		for(int i = 0; i < NUM_ANTS; i++) {
			ants[i] = new Ant(i);
		}
		grid = new Point[allPoints.length][allPoints.length];
		createGrid();
	}
	private void createGrid() {
		Random rand = new Random();
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = null;
			}
		}
		for(int i = 0; i < allPoints.length; i++) {
			int row = rand.nextInt(allPoints.length);
			int col= rand.nextInt(allPoints.length);
			
			while(grid[row][col] != null) {
				row = rand.nextInt(allPoints.length);
				col = rand.nextInt(allPoints.length);
			}
			grid[row][col] = allPoints[i];
		}
	}
	public void startAnts(Point[][] grid) {
		for(Ant a : ants) {
			a.antSearch(grid);
		}
	}
	public ArrayList<Cluster> findBestSolution() {
		ArrayList<Cluster> solutions = new ArrayList<Cluster>();
		
		return solutions;
	}
	@Override
	public ArrayList<Cluster> returnClusters() {
		clusters = findBestSolution();
		return clusters;
	}
	@Override
	public void updateClusters() {
		// TODO Auto-generated method stub
		
	}
	
}
