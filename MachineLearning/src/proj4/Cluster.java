import java.util.ArrayList;


public class Cluster {
	private ArrayList<Point> points;
	private int minSize;
	private int id;
	
	public int getId(){
		return this.id;
	}
	
	public Cluster(int minSize, int id) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
		this.id = id;
	}
	
	public Cluster(int minSize) {
		this.points = new ArrayList<Point>();
		this.minSize = minSize;
	}
        
	/* @overload constructor. */
	public Cluster(){
		this.minSize = 0;
		this.points = new ArrayList<Point>();
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
		double[] centerValues = getAveragePosition();
		double sumDistance = 0;
		int combinations = 0;
		/*for(int i = 0; i < centerValues.length; i++){
			System.out.print(centerValues[i]+ " ");
		}
		System.out.println();*/
		for(Point p: points){
			if(p.getValues()[0] != 0){
			//if(p.getValues().equals(centerValues)){
				combinations++;
				/*for(int i = 0; i < centerValues.length; i++){
					System.out.print(p.getValues()[i]+ " ");
				}
				System.out.println();*/
				if(!Double.isNaN(getDistance(p.getValues(),centerValues)) && !Double.isInfinite(getDistance(p.getValues(),centerValues))){
					sumDistance+=getDistance(p.getValues(),centerValues);
				}else{
					//System.out.println(p.getValues()[0] + " " + centerValues[0]);
				}
			}
		}
		//System.out.println("Sum: " + sumDistance);
		return sumDistance/combinations;
	}
	
	public int getNumberOfDimensions(){
		return points.get(0).getValues().length;
	}
	
	public double[] getAveragePosition(){
		double[] values = new double[this.getNumberOfDimensions()];
		for(Point p: points){
			//if(!p.isCorePoint()){
				double[] pointValues = p.getValues();
				for(int dimension = 0; dimension < values.length; dimension++){
					values[dimension]+=pointValues[dimension];
				}
			//}
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
	
	public void createCenter() {
		double[] values = getAveragePosition();
		Point center = new Point(values);
		center.setCorePoint();
		addPoint(center);
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
				//removePoint(p);
				p.updateUnclassified(true);
			}
	}
	public int clusterSize() {
		return points.size();
	}
}
