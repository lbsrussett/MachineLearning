package proj4;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<Point> points;
	private int minSize;
	private int clusterID;
	
	public Cluster(int minSize, int cluster) {
		this.clusterID = cluster;
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
	}
	
	public Point getCenter(){
		for(Point p: points){
			if(p.isCorePoint()){
				return p;
			}
		}
		return null;
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
	
	public int getClusterID(Cluster c) {
		return c.clusterID;
	}
	
	public ArrayList<Point> getClusterPoints(Cluster c) {
		return c.points;
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
}
