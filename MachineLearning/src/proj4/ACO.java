package proj4;

import java.util.ArrayList;

public class ACO extends ClusteringAlgorithm {
	private Point[] allPoints;
	private Ant[] ants;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private final int NUMANTS = 20;
	
	public ACO(double[][] inputs) {
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		ants = new Ant[NUMANTS];
		for(int i = 0; i < NUMANTS; i++) {
			ants[i] = new Ant(i);
		}
	}
	public void updateClusters() {
		
	}
	@Override
	public ArrayList<Cluster> returnClusters() {
		// TODO Auto-generated method stub
		return null;
	}
}
