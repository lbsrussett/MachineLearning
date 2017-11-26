package proj4;

import java.util.ArrayList;

public class Point {
	private Cluster cluster;
	private boolean noise, corePoint, unclassified = true;
	private double[] values;
	private ArrayList<Point> neighbors = new ArrayList<Point>();
	
	public Point(double[] values) {
		this.values = values;
		this.cluster = null;
		this.corePoint = false;
	}
	public void updateCluster(Cluster c) {
		this.cluster = c;
		unclassified = false;
	}
	
	public void updateNoise(boolean update) {
		this.noise = update;
	}
	public void setCorePoint() {
		this.corePoint = true;
		unclassified = false;
	}
	public boolean isCorePoint() {
		return this.corePoint;
	}
	public boolean isNoise() {
		return this.noise;
	}
	public double[] getValues() {
		return this.values;
	}
	public void updateUnclassified(boolean update){
		this.unclassified = update;
	}
	public boolean unclassified() {
		return this.unclassified;
	}
	public Cluster getCluster() {
		return cluster;
	}
	public void addNeighbor(Point p) {
		neighbors.add(p);
		if(!p.neighbors.contains(this)) {
			p.addNeighbor(this);
		}
	}
	public Point getNeighbor() {
		return neighbors.get(0);
	}
 }
