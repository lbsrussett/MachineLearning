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

}
