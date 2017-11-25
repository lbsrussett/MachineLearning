package proj4;

import java.util.ArrayList;

public class DBScan extends ClusteringAlgorithm {
	private Point[] allPoints = null;
	private int clusterNum = 0;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private final double EPSILON = 1;
	private final int MINPOINTS = 5;
	
	public DBScan(double[][] inputs) {
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		initCores();
	}
	private void initCores() {
		for(int i = 0; i < allPoints.length; i++) {
			Point point1 = allPoints[i];
			int neighbors = 0;
			for(int j = i+1; j < allPoints.length; j++) {
				Point point2 = allPoints[j];
				if(withinEpsilon(point1, point2)) {
					neighbors++;
				}
				if(neighbors >= MINPOINTS) {
					point1.setCorePoint();
					clusterNum++;
					Cluster c = new Cluster(MINPOINTS, clusterNum);
					clusters.add(c);
					c.addPoint(point1);
					point1.updateCluster(c.getClusterID(c));
				}
			}
		}
	}
	public void updateClusters() {
		for(int i = 0; i < allPoints.length; i++) {
			if(allPoints[i].isCorePoint()) {
				calcDistance(allPoints[i]);
			}
		}
		for(int i = 0; i < allPoints.length; i++) {
			if(allPoints[i].unclassified()) {
				if(isBorder(allPoints[i])) {
					Point p = allPoints[i].getNeighbor();
					allPoints[i].updateCluster(p.getCluster());
					Cluster c = clusters.get(p.getCluster());
					c.addPoint(allPoints[i]);
				}
			}
			else {
				allPoints[i].updateNoise(true);
			}
		}
	}
	private boolean isBorder(Point p) {
		int neighbors = 0;
		for(int i = 0; i < allPoints.length; i ++) {
			if(allPoints[i] != p){
				double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
				if(distance < EPSILON) {
					neighbors++;
					p.addNeighbor(allPoints[i]);
					allPoints[i].updateCluster(p.getCluster());
					Cluster c = clusters.get(p.getCluster());
					c.addPoint(allPoints[i]);
				}
			}
		}
		if(neighbors == 1) {
			return true;
		}
		else
			return false;
	}
	private void calcDistance(Point p) {
			for(int i = 0; i < allPoints.length; i++) {
				if(allPoints[i] != p) {
					double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
					//System.out.println(distance);
					if(distance < EPSILON) {
						p.addNeighbor(allPoints[i]);
						Cluster c = clusters.get(p.getCluster());
						c.addPoint(allPoints[i]);
						allPoints[i].updateCluster(p.getCluster());
					}
				}
			}
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
	private boolean withinEpsilon(Point p1, Point p2) {
		double distance = euclideanDistance(p1.getValues(), p2.getValues());
		if(distance < EPSILON) {
			return true;
		}
		return false;
	}
	public Point[] returnPoints() {
		return allPoints;
	}
	public void returnClusters() {
		System.out.println("There are " + clusters.size() + "clusters.");
		for(int i = 0; i < clusters.size(); i++) {
			System.out.println("Cluster " + i + "has " + clusters.get(i).clusterSize());
		}
		
	}
}
