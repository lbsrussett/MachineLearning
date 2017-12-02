package proj4;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<Point> points;
	private int minSize;
	
	public Cluster(int minSize) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
	}
        
        /* Overload constructor. */
        public Cluster(){
            this.minSize = 0;
            this.points = new ArrayList<Point>();
        }
	
	public double getAverageDistanceToCenter(){
		Point center = this.getCenter();
		double[] centerValues = center.getValues();
		double sumDistance = 0;
		int combinations = 0;
		for(Point p: points){
			if(p!=center){
				combinations++;
				sumDistance+=getDistance(p.getValues(),centerValues);
			}
		}
		return sumDistance/combinations;
	}
	
	public int getNumberOfDimensions(){
		return points.get(0).getValues().length;
	}
	
	public double[] getAveragePosition(){
		double[] values = new double[this.getNumberOfDimensions()];
		for(Point p: points){
			if(!p.isCorePoint()){
				double[] pointValues = p.getValues();
				for(int dimension = 0; dimension < values.length; dimension++){
					values[dimension]+=pointValues[0];
				}
			}
		}
		for(int dimension = 0; dimension < values.length; dimension++){
			values[dimension]/=(this.clusterSize()-1);
		}
		return values;
	}
	
	public static double getDistance(double[] p1Values, double[] p2Values){
		double sumDistance = 0;
		for(int dimension = 0; dimension<p1Values.length; dimension++){//For each dimension
			sumDistance += Math.pow(p1Values[dimension]-p2Values[dimension],2);
		}
		return Math.sqrt(sumDistance);
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
        
        public Point getPoint(int i){
            return points.get(i);
        }

	public void removeCluster(Cluster c) {
		if(c.minSize > c.points.size()) {
			for(Point p : points) {
				p.updateUnclassified(true);
			}
		}
	}
	public int clusterSize() {
		return points.size();
	}
	public double fitness() {
		Point center = this.getCenter();
		double fitness = 0;
		for(Point p : points) {
			if(!p.equals(center)) {
				fitness += euclideanDistance(center.getValues(), p.getValues());
			}
		}
		return fitness;
	}
 	public void createCenter() {
		Point center = null;
		double[] values = getAveragePosition();
		center = new Point(values);
		center.setCorePoint();
		addPoint(center);
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
        
        private Point getCenter(){
            return points.get(0);
        }
}
