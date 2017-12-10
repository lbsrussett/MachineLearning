import java.util.ArrayList;

/**
 * @author laura sullivan-russett
 * @version December 11, 2017
 * 
 * Point class to take input vectors and assign them as Point values
 *
 */
public class Point {
	private Cluster cluster;
	private boolean noise, corePoint, unclassified = true;
	private double[] values;
	private ArrayList<Point> neighbors = new ArrayList<Point>();
	
	/**
	 * constructor to initialize an instance of Point with input vectors as Point values
	 * @param values
	 */
	public Point(double[] values) {
		this.values = values;
		this.cluster = null;
		this.corePoint = false;
	}
	/**
	 * updateCluster method to assign Point instance to a cluster and mark it as classified
	 * @param c
	 */
	public void updateCluster(Cluster c) {
		this.cluster = c;
		unclassified = false;
	}
	/**
	 * setValues method to update Point values
	 * 
	 * @param values
	 */
	public void setValues(double[] values) {
		this.values = values;
	}
	/**
	 * updateNoise method to mark a Point as noise
	 * 
	 * @param update
	 */
	public void updateNoise(boolean update) {
		this.noise = update;
	}
	/**
	 * setCorePoint method to set a Point as a core point
	 */
	public void setCorePoint() {
		this.corePoint = true;
		unclassified = false;
	}
	/**
	 * isCorePoint method to return true if the point has been classified
	 * as a core point
	 * 
	 * @return
	 */
	public boolean isCorePoint() {
		return this.corePoint;
	}
	/**
	 * isNoise method to return true if the point has been classified as noise
	 * 
	 * @return
	 */
	public boolean isNoise() {
		return this.noise;
	}
	/**
	 * getValues method to return the value array of the Point
	 * 
	 * @return
	 */
	public double[] getValues() {
		return this.values;
	}
	/**
	 * updateUnclassified method to reset a Point as unclassified if its 
	 * cluster is removed
	 * 
	 * @param update
	 */
	public void updateUnclassified(boolean update){
		this.unclassified = update;
	}
	/**
	 * unclassified method to return true if a Point has not been classified
	 * as belonging to a cluster
	 * 
	 * @return
	 */
	public boolean unclassified() {
		return this.unclassified;
	}
	/**
	 * getCluster method to return the current cluster a Point is assigned to
	 * 
	 * @return
	 */
	public Cluster getCluster() {
		return cluster;
	}
	/**
	 * addNeighbor method to add the input point as a neighbor of the current Point
	 * 
	 * @param p
	 */
	public void addNeighbor(Point p) {
		neighbors.add(p);
		//If the input point does not contain the current point in its neighbors array, add it
		if(!p.neighbors.contains(this)) {
			p.addNeighbor(this);
		}
	}
	/**
	 * getNeighbor method to return a neighbor of the current point
	 * 
	 * @return
	 */
	public Point getNeighbor() {
		return neighbors.get(0);
	}
 }
