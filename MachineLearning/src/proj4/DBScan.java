package proj4;

import java.util.ArrayList;

public class DBScan extends ClusteringAlgorithm {
	private Point[] allPoints = null;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private double EPSILON;
	private int MINPOINTS;
	ArrayList<Point> cp = new ArrayList<Point>();
	
	public DBScan(double[][] inputs, double epsilon, int minpoints) {
		this.EPSILON = epsilon;
		this.MINPOINTS = minpoints;
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
				Cluster clust = new Cluster(MINPOINTS);
				clusters.add(clust);
				clust.addPoint(c);
				c.updateCluster(clust);
				for(int i = 0; i < cp.size(); i++) {
					if(cp.get(i).getCluster() == null) {
						if(!c.equals(cp.get(i)) && withinEpsilon(c, cp.get(i))) {
							clust.addPoint(cp.get(i));
							c.updateCluster(clust);
							cp.get(i).updateCluster(clust);
						}
					}
				}
			}
		}
	}
	public void updateClusters(double input[][]) {
		System.out.println("here");
		for(int i = 0; i < allPoints.length; i++) {
			if(allPoints[i].isCorePoint()) {
				calcDistance(allPoints[i]);
			}
		}
		System.out.println("HERE");
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
		mergeClusters();
	}
	private void mergeClusters() {
		for(Cluster c : clusters) {
			if(c.clusterSize() < MINPOINTS) {
				double distance;
				double shortest;
				Cluster newCluster = null;
				ArrayList<Point> points = c.getClusterPoints();
				c.removeCluster();
				for(Point p : points) {
					int core = 0;
					do {
						core++;
					}
					while(p.equals(cp.get(core)));
					shortest = euclideanDistance(p.getValues(), cp.get(core).getValues());
					newCluster = cp.get(core).getCluster();
						
					for(int i = core; i < cp.size(); i++) {
						if(!p.equals(cp.get(i))) {
							distance = euclideanDistance(p.getValues(), cp.get(i).getValues());
							Cluster temp = cp.get(i).getCluster();
							if(distance < shortest) {
								shortest = distance;
								newCluster = temp;
							}
						}
					}
					p.updateCluster(newCluster);
				}
			}
		}
		System.out.print("DBScan with " + this.EPSILON + " epsilon and " + this.MINPOINTS + " min points: ");
		//WilsonProject4Application.evaluateCluster(this, false);
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
						Cluster c = new Cluster(MINPOINTS);
						c.addPoint(p);
						p.updateCluster(c);
					}
					allPoints[i].updateCluster(p.getCluster());
					Cluster c = p.getCluster();
					c.addPoint(allPoints[i]);
				}
			}
		}
		if(neighbors <= MINPOINTS && neighbors > 0) {
			return true;
		}
		else {
			p.updateNoise(true);
			return false;
		}
			
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
		return clusters;
	}
}