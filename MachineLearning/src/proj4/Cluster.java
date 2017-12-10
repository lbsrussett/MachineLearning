import java.util.ArrayList;


/**
 * @author laura sullivan-russett
 * @version December 11, 2017
 * 
 * Cluster class to create a Cluster instance and hold the points that belong
 * to the cluster
 *
 */
public class Cluster {
	private ArrayList<Point> points;
	private int minSize;
	private int id;
	
	/**
	 * constructor to initialize a Cluster instance with a minimum number of points
	 * and a cluster ID
	 * 
	 * @param minSize
	 * @param id
	 */
	public Cluster(int minSize, int id) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
		this.id = id;
	}
	/**
	 * constructor to initialize a Cluster instance with a minimum number of points
	 * 
	 * @param minSize
	 */
	public Cluster(int minSize) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
	}
	/**
	 * constructor to initialize a Cluster instance with no mimimum number of points
	 */
	/* @overload constructor. */
	public Cluster(){
		this.minSize = 0;
		this.points = new ArrayList<Point>();
	}
	/**
	 * getId method to return the ID of the current Cluster
	 * 
	 * @return
	 */
	public int getId(){
		return this.id;
	}
	/**
	 * getCenter method to return the core point of a Cluster or null if no core point is set
	 * @return
	 */
	public Point getCenter(){
		for(Point p: points){
			if(p.isCorePoint()){
				return p;
			}
		}
		return null;
	}
	
	/**
	 * getAverageDistanceToCenter method to calculate the distance between each point
	 * in the Cluster and the core point
	 * 
	 * @return
	 */
	public double getAverageDistanceToCenter(){
		double[] centerValues = getAveragePosition();
		double sumDistance = 0;
		int combinations = 0;
		for(Point p: points){
			if(p.getValues()[0] != 0){
				combinations++;
				if(!Double.isNaN(getDistance(p.getValues(),centerValues)) && !Double.isInfinite(getDistance(p.getValues(),centerValues))){
					sumDistance+=getDistance(p.getValues(),centerValues);
				}
			}
		}
		return sumDistance/combinations;
	}
	
	/**
	 * getNumberOfDimensions method to return the dimension of the points in the Cluster
	 * 
	 * @return
	 */
	public int getNumberOfDimensions(){
		return points.get(0).getValues().length;
	}
	
	/**
	 * getAveragePosition method to return the average position of the points in the Cluster
	 * @return
	 */
	public double[] getAveragePosition(){
		double[] values = new double[this.getNumberOfDimensions()];
		for(Point p: points){
			double[] pointValues = p.getValues();
			for(int dimension = 0; dimension < values.length; dimension++){
				values[dimension]+=pointValues[dimension];
			}
		}
		for(int dimension = 0; dimension < values.length; dimension++){
			values[dimension]/=(this.clusterSize()-1);
		}
		return values;
	}
	
	/**
	 * getDistance method to return the Euclidean distance between two points
	 * 
	 * @param p1Values
	 * @param p2Values
	 * @return
	 */
	public static double getDistance(double[] p1Values, double[] p2Values){
		double sumDistance = 0;
		for(int dimension = 0; dimension<p1Values.length; dimension++){//For each dimension
			sumDistance += Math.pow(p1Values[dimension]-p2Values[dimension],2);
		}
		return Math.sqrt(sumDistance);
	}
	
	/**
	 * createCenter method to create a center point for ACO clusters for use 
	 * in measuring fitness
	 */
	public void createCenter() {
		//average the values in the cluster and create a Point with those values
		double[] values = getAveragePosition();
		Point center = new Point(values);
		center.setCorePoint();
		addPoint(center);
	}
    
	/**
	 * addPoint method to add a Point to the current Cluster
	 * 
	 * @param p
	 */
	public void addPoint(Point p) {
		points.add(p);
	}
	
	/**
	 * removePoint method to remove a point from the current Cluster
	 * 
	 * @param p
	 */
	public void removePoint(Point p) {
		points.remove(points.indexOf(p));
	}
	
	/**
	 * getClusterPoints method to return the points in the current Cluster
	 * 
	 * @return
	 */
	public ArrayList<Point> getClusterPoints() {
		return points;
	}

	/**
	 * removeCluster method to mark all points in the current Cluster as unclassified
	 * so that they can be assigned to another Cluster
	 */
	public void removeCluster() {
			for(Point p : points) {
				p.updateUnclassified(true);
			}
	}
	/**
	 * clusterSize method to return the current number of points in the Cluster
	 * 
	 * @return
	 */
	public int clusterSize() {
		return points.size();
	}
}
