import java.util.ArrayList;
import java.util.Random;
 
public class KMeansClustering extends ClusteringAlgorithm{
	private int k;
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private ArrayList<Point> points = new ArrayList<Point>();
	private double[][] inputs;
	private boolean valid;
	
	public void setInputs(double[][] inputs){
		this.inputs = inputs;
	}
	
	public ArrayList<Cluster> returnClusters(){
		return this.clusters;
	}
	
	public void setNumberOfClusters(int k){
		this.k = k;
	}
	
	private void printEvaluation(int iteration){
		System.out.print("K-Means on iteration " + iteration + " with " + this.k + " clusters strength: ");
		this.valid = WilsonProject4Application.evaluateCluster(this, false);
	}
	
	public void updateClusters(double inputs[][]){
		int dimensionOfInputs = inputs[0].length;//Assumes every input has the same dimension
		this.clusters = new ArrayList<Cluster>();
		this.valid = true;
 	
 		//Find min and max of each dimension
 		double[] minimum = new double[dimensionOfInputs];
 		double[] maximum = new double[dimensionOfInputs];
 		//Set all the inital values for the min and max arrays
 		for(int dimension = 0; dimension < minimum.length;dimension++){
 			minimum[dimension] = Double.MAX_VALUE;//Set to max value so anything will be less than when compared
 			maximum[dimension] = Double.MIN_VALUE;//Set to min value so anything will be greater than when compared
 		}
 		//Set the min and max arrays to their actual values
 		for(int input = 0; input < inputs.length; input++){
 			Point p = new Point(inputs[input]);
 			points.add(p);
 			for(int dimension = 0; dimension < dimensionOfInputs; dimension++){
 				if(inputs[input][dimension]<minimum[dimension]){
 					minimum[dimension]=inputs[input][dimension];
 				}
 				if(inputs[input][dimension]>maximum[dimension]){
 					maximum[dimension]=inputs[input][dimension];
 				}
 			}
 		}
 		
 		//Randomly assign centers for the k clusters
 		Random rand = new Random();
 		for(int center = 0; center < k; center++){
 			double[] centerValues = new double[dimensionOfInputs];
 			for(int dimension = 0; dimension < dimensionOfInputs; dimension++){
 				centerValues[dimension] = (maximum[dimension] - minimum[dimension])*rand.nextDouble() + minimum[dimension];
 			}
 			Point centerPoint = new Point(centerValues);
 			centerPoint.setCorePoint();
 			Cluster cluster = new Cluster(1,center);
 			cluster.addPoint(centerPoint);
 			this.clusters.add(cluster);
 		}
 	
 		//Repeat until centers converge
 		boolean changeHappened = true;
 		int iteration = 1;
 		while(changeHappened){
 			changeHappened = false;
 			
 			//Assign points to centers using Euclidean distance
			for(Point p: points){//For each point
				double[] distances = new double[k];
				int index = 0;
 				for(Cluster cluster: clusters){//For each center
 					Point center = cluster.getCenter();
 					distances[index] = Cluster.getDistance(p.getValues(), center.getValues());
 					index++;
 				}
 				
 				//Find minimum distance
 				double minimumDistance = Double.MAX_VALUE;
 				int closestCenter = -1;
 				for(int center = 0; center < k; center++){
 					if(distances[center]<minimumDistance){
 						minimumDistance = distances[center];
 						closestCenter = center;
 					}
 				}
 				
 				//Update which input a center belongs to
 				if(p.getCluster() == null){
 					changeHappened = true;
 					clusters.get(closestCenter).addPoint(p);
 					p.updateCluster(clusters.get(closestCenter));
 				}else if(p.getCluster().getId() != closestCenter){
					changeHappened = true;
 					clusters.get(closestCenter).addPoint(p);
 					p.updateCluster(clusters.get(closestCenter));
 				}
 			}
 			
 			//Update center values by averaging all inputs pointing to that center
			printEvaluation(iteration++);
			
 			if(changeHappened){
 				for(Cluster cluster: clusters){
 					cluster.getCenter().setValues(cluster.getAveragePosition());
 				}
 			}
 			
 			if(!this.valid){
 				break;
 			}
 		}
 	}
}