package proj4;

import java.util.ArrayList;

public class Point {
	private int cluster;
	private boolean noise, corePoint, unclassified = true;
	private double[] values;
	
	public Point(double[] values) {
		this.values = values;
		this.cluster = -1;
		this.noise = true;
		this.corePoint = false;
	}
	public void updateCluster(int c) {
		this.cluster = c;
		unclassified = false;
		updateNoise(false);
	}
	public void updateNoise(boolean b) {
		this.noise = b;
	}
	public void isCorePoint() {
		this.corePoint = true;
	}
	public boolean getCorePoint() {
		return this.corePoint;
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
	public int getCluster() {
		return cluster;
	}
 }
