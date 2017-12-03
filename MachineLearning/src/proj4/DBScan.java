package proj4;

import java.util.ArrayList;

public class DBScan extends ClusteringAlgorithm {
	private Point[] allPoints = null;
	private int clusterNum = 0;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private final double EPSILON = 2;
	private final int MINPOINTS = 5;
	ArrayList<Point> cp = new ArrayList<Point>();
	
	public DBScan(double[][] inputs) {
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		initCores();
	}
	private void initCores() {
		int cores = 0;
		for(Point a : allPoints) {
			Point point1 = a;
			int neighbors = 0;
			for(int j = 0; j < allPoints.length; j++) {
				Point point2 = allPoints[j];
				if(!point1.equals(point2) && point2.unclassified()) {
					if(withinEpsilon(point1, point2)) {
						neighbors++;
					}
				}
			}
			if(neighbors >= MINPOINTS) {
				point1.setCorePoint();
				cores++;
			}
		}
		initClusters();
		System.out.println(cores);
	}
	private void initClusters() {
		for(Point a : allPoints) {
			if(a.isCorePoint()) {
				cp.add(a);
			}
		}
		for(Point c: cp) {
			if(c.getCluster() == null) {
				for(int i = 0; i < cp.size(); i++) {
					if(cp.get(i).getCluster() == null) {
						if(!c.equals(cp.get(i)) && withinEpsilon(c, cp.get(i))) {
							Cluster clust = new Cluster(MINPOINTS);
							clusters.add(clust);
							clust.addPoint(c);
							clust.addPoint(cp.get(i));
							c.updateCluster(clust);
							cp.get(i).updateCluster(clust);
						}
					}
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
			if(allPoints[i].unclassified() && allPoints[i].isNoise() == false) {
				if(isBorder(allPoints[i])) {
					Point p = allPoints[i].getNeighbor();
					allPoints[i].updateCluster(p.getCluster());
					Cluster c = p.getCluster();
					c.addPoint(allPoints[i]);
				}
			}
			else {
				allPoints[i].updateNoise(true);
			}
		}
		evaluateClusters();
	}
	private void evaluateClusters() {
		for(Cluster c : clusters) {
			if(c.clusterSize() < MINPOINTS) {
				double distance;
				double shortest;
				ArrayList<Point> points = c.getClusterPoints();
				c.removeCluster();
				for(Point p : points) {
					shortest = euclideanDistance(p.getValues(), cp.get(0).getValues());
					Cluster newCluster = cp.get(0).getCluster();
					for(int core = 1; core < cp.size(); core++) {
						distance = euclideanDistance(p.getValues(), cp.get(core).getValues());
						Cluster temp = cp.get(core).getCluster();
						if(distance < shortest) {
							shortest = distance;
							newCluster = temp;
						}
					}
					p.updateCluster(newCluster);
				}
			}
		}
	}
	private boolean isBorder(Point p) {
		int neighbors = 0;
		for(int i = 0; i < allPoints.length; i ++) {
			if(!allPoints[i].equals(p)){
				double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
				if(distance < EPSILON) {
					neighbors++;
					p.addNeighbor(allPoints[i]);
					if(p.getCluster() == null) {
						p.updateCluster(allPoints[i].getCluster());
					}
					allPoints[i].updateCluster(p.getCluster());
					Cluster c = p.getCluster();
					c.addPoint(allPoints[i]);
				}
			}
		}
		if(neighbors < MINPOINTS) {
			return true;
		}
		else
			return false;
	}
	private void calcDistance(Point p) {
			for(int i = 0; i < allPoints.length; i++) {
				if(!allPoints[i].equals(p)) {
					double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
					//System.out.println(distance);
					if(distance < EPSILON) {
						p.addNeighbor(allPoints[i]);
						Cluster c = p.getCluster();
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
	public void printClusters() {
		System.out.println("There are " + clusters.size() + " clusters.");
		/*for(int i = 0; i < clusters.size(); i++) {
			System.out.println("Cluster " + i + " has " + clusters.get(i).clusterSize());
		}*/
		
	}
	@Override
	public ArrayList<Cluster> returnClusters() {
		// TODO Auto-generated method stub
		return null;
	}
}
