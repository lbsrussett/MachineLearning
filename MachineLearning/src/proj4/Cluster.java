package proj4;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<Point> points;
	private int minSize;
	
	public Cluster(int minSize) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
	}
        
        /* @overload constructor. */
        public Cluster(){
            this.minSize = 0;
            this.points = new ArrayList<Point>();
        }
	
	public void addPoint(Point p) {
		points.add(p);
	}
	public void removePoint(Point p) {
		points.remove(points.indexOf(p));
	}
	
	public ArrayList<Point> getClusterPoints() {
		return points;
	}

	public void removeCluster() {
			for(Point p : points) {
				p.updateUnclassified(true);
			}
	}
	public int clusterSize() {
		return points.size();
	}
	public double fitness() {
		Point center = getCenter();
		double fitness = 0;
		for(Point p : points) {
			if(!p.equals(center)) {
				fitness += euclideanDistance(center.getValues(), p.getValues());
			}
		}
		return fitness;
	}
 	private Point getCenter() {
		Point center = null;
		for(int i = 0; i < points.size(); i++) {
			Point temp = points.get(i);
			double tempAvg = Double.MAX_VALUE;
			double average = 0;
			for(int j = 0; j < points.size(); j++) {
				if(!temp.equals(points.get(j))) {
					average += euclideanDistance(temp.getValues(), points.get(j).getValues());
				}
			}
			if(average < tempAvg) {
				tempAvg = average;
				center = temp;
			}
		}
		return center;
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
}
