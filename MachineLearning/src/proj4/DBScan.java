import java.util.ArrayList;

/**
 * @author laura sullivan-russett
 * @version December 11, 2017
 * 
 * DBScan class to implement density based clustering algorithm
 *
 */
public class DBScan extends ClusteringAlgorithm {
	private Point[] allPoints = null;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	//Tunable parameters to set the distance points need to be from a core point and 
	//to set the minimum number of points a Cluster can contain
	public double EPSILON = 1.5;
	public  int MINPOINTS = 5;
	ArrayList<Point> cp = new ArrayList<Point>();
	
	/**
	 * constructor to initialize an instance of DBScan and set the points to be clustered 
	 * 
	 * @param inputs
	 * @param epsilon
	 * @param minpoints
	 */
	public DBScan(double[][] inputs, double epsilon, int minpoints) {
		this.EPSILON = epsilon;
		this.MINPOINTS = minpoints;
		allPoints = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.allPoints[i] = new Point(inputs[i]);
		}
		initCores();
	}
	/**
	 * initCores method to find all points that can be classified as core points and set
	 * them as such.  Points are core points if they have more than MINPOINTS neighbors.
	 */
	private void initCores() {
		int cores = 0;
		//for each point, count the number of neighbors
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
			//if the number of neighbors greater than or equal to MINPOINTS, classify as core point
			if(neighbors >= MINPOINTS) {
				point1.setCorePoint();
				cores++;
			}
		}
		//initialize clusters using the classified core points
		initClusters();
	}
	/**
	 * initClusters method to create clusters using core points 
	 */
	private void initClusters() {
		//add all core points to an array list
		for(Point a : allPoints) {
			if(a.isCorePoint()) {
				cp.add(a);
			}
		}
		//for each core point, if it is not assigned to a cluster, create a cluster for it and add the point
		for(Point c: cp) {
			if(c.getCluster() == null) {
				Cluster clust = new Cluster(MINPOINTS);
				clusters.add(clust);
				clust.addPoint(c);
				c.updateCluster(clust);
				//if two core points are within EPSILON of each other, assign them to the same cluster
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
	/**
	 * updateClusters method add all non-core points to clusters, unless they are not within
	 * EPSILON of another point, then classify those points as noise
	 */
	public void updateClusters(double input[][]) {
		for(int i = 0; i < allPoints.length; i++) {
			if(allPoints[i].isCorePoint()) {
				calcDistance(allPoints[i]);
			}
		}
		//for each unclassified points, add them to the appropriate cluster 
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
		//merge any clusters that are within EPSILON of each other
		mergeClusters();
	}
	/**
	 * mergeClusters method to combine any clusters that are within EPSILON of each other or
	 * remove points from any cluster smaller than MINPOINTS and add them to different clusters
	 */
	private void mergeClusters() {
		for(Cluster c : clusters) {
			//remove any cluster that is smaller than MINPOINTS
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
					//find the core point for the cluster to be created by merging
					while(p.equals(cp.get(core)));
					shortest = euclideanDistance(p.getValues(), cp.get(core).getValues());
					newCluster = cp.get(core).getCluster();
					//add points to cluster with shortest distance between points	
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
					//add the point to the new cluster
					p.updateCluster(newCluster);
				}
			}
		}
		System.out.print("DBScan with " + this.EPSILON + " epsilon and " + this.MINPOINTS + " min points: " + this.clusters.size() + " Clusters ");
	}
	/**
	 * isBorder method to set any point that is within EPSILON of a core point, but is not a core point, 
	 * as a border point
	 * 
	 * @param p
	 * @return
	 */
	private boolean isBorder(Point p) {
		int neighbors = 0;
		//for each point, calculate the number of neighbors
		for(int i = 0; i < allPoints.length; i ++) {
			if(!allPoints[i].equals(p)){
				double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
				if(distance < EPSILON) {
					neighbors++;
					p.addNeighbor(allPoints[i]);
					//if a point hasn't been added to a cluster, create a cluster for it
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
		//if a point does have neighbors, but not enough to be a core point, label it as border
		if(neighbors <= MINPOINTS && neighbors > 0) {
			return true;
		}
		//if a point does not have neighbors, label it as noise
		else{
			p.updateNoise(true);
			return false;
		}
	}
	/**
	 * calcDistance method to calculate the distance between the input point 
	 * and all other points
	 * 
	 * @param p
	 */
	private void calcDistance(Point p) {
			for(int i = 0; i < allPoints.length; i++) {
				if(!allPoints[i].equals(p)) {
					double distance = euclideanDistance(p.getValues(), allPoints[i].getValues());
					//if the point is a neighbor add it to the input point's neighbor array
					if(distance < EPSILON) {
						p.addNeighbor(allPoints[i]);
						Cluster c = p.getCluster();
						c.addPoint(allPoints[i]);
						allPoints[i].updateCluster(p.getCluster());
					}
				}
			}
	}
	/**
	 * euclideanDistance method to calculate the Euclidean distance between two points
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
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
	/**
	 * withinEpsilon helper method to return true if two points are less than EPSILON apart
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private boolean withinEpsilon(Point p1, Point p2) {
		double distance = euclideanDistance(p1.getValues(), p2.getValues());
		if(distance < EPSILON) {
			return true;
		}
		return false;
	}
	/**
	 * returnPoints method to return all data points
	 * 
	 * @return
	 */
	public Point[] returnPoints() {
		return allPoints;
	}
	/**
	 * printClusters method to return the number of clusters for help in debugging
	 */
	public void printClusters() {
		System.out.println("There are " + clusters.size() + " clusters.");
	}
	/**
	 * returnClusters method to return the clusters created by DBScan
	 */
	@Override
	public ArrayList<Cluster> returnClusters() {
		return clusters;
	}
}