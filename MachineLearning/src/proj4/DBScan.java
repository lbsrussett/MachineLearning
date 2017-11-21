package proj4;

import java.util.ArrayList;

public class DBScan {
	private Point[] points = null;
	private int clusterNum = 0;
	private ArrayList<Point[]> clusters = new ArrayList<Point[]>();
	private final double EPSILON = 0.5;
	private final int MINPOINTS = 2;
	
	public DBScan(double[][] inputs) {
		points = new Point[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			this.points[i] = new Point(inputs[i]);
		}
	}
	public void updateClusters() {
		for(int i = 0; i < points.length; i++) {
			if(points[i].unclassified()) {
				calcDistance(points[i]);
			}
			
		}
		for(int j = 0; j < clusters.size(); j++) {
			System.out.println(clusters.get(j));
		}
	}
	public void calcDistance(Point p) {
			clusterNum++;
			Point point1 = p;
			p.isCorePoint();
			p.updateCluster(clusterNum);
			for(int i = 0; i < points.length; i++) {
				if(points[i] != p) {
					double distance = euclideanDistance(point1.getValues(), points[i].getValues());
					//System.out.println(distance);
					if(distance < EPSILON) {
						points[i].updateCluster(p.getCluster());
						p.addNeighbor(points[i]);
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
	public Point[] returnPoints() {
		return points;
	}
}
