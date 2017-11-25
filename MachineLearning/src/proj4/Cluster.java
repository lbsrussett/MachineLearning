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
