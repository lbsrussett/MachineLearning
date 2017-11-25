import java.util.Random;
 
public class KMeansClustering extends WilsonClusteringAlgorithm{
	private double[][] centers;
	private int[] centerForInput;
	
	public double[][] getCenters(){
		return this.centers;
	}
	
	public int[] getCenterForInputs(){
		return this.centerForInput;
	}
	
	public void createClusterCenters(int k, double inputs[][]) throws Exception{
		int dimensionOfInputs = inputs[0].length;//Assumes every input has the same dimension
 	
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
 			for(int dimension = 0; dimension < dimensionOfInputs; dimension++){
 				if(inputs[input][dimension]<minimum[dimension]){
 					minimum[dimension]=inputs[input][dimension];
 				}
 				if(inputs[input][dimension]>maximum[dimension]){
 					maximum[dimension]=inputs[input][dimension];
 				}
 			}
 		}
 		
 		//The center that an input is currently assigned to
 		int[] centerForInput = new int[inputs.length];
 		
 		//Randomly assign centers for the k clusters
 		double[][] centers = new double[k][dimensionOfInputs];
 		Random rand = new Random();
 		for(int input = 0; input < k; input++){
 			for(int dimension = 0; dimension < dimensionOfInputs; dimension++){
 				centers[input][dimension] = (maximum[dimension] - minimum[dimension])*rand.nextDouble() + minimum[dimension];
 			}
 		}
 	
 		//Repeat until centers converge
 		boolean changeHappened = true;
 		while(changeHappened){
 			changeHappened = false;
 			
 			//Assign points to centers using Euclidean distance
			for(int input = 0; input < inputs.length; input++){//For each input
				double[] distances = new double[k];
 				for(int center = 0; center < k; center++){//For each center
 					double sumOfDistancesSquared = 0;
 					for(int dimension = 0; dimension<dimensionOfInputs; dimension++){//For each dimension
 						//Get the distance squared
 						sumOfDistancesSquared += Math.pow((centers[center][dimension]-inputs[input][dimension]),2);
 					}
 					//We would typically take the square root for true Euclidean distance, but it will not affect results not to squareroot
 					distances[center] = sumOfDistancesSquared;
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
 				if(centerForInput[input] != closestCenter){
					changeHappened = true;
 					centerForInput[input] = closestCenter;
 				}
 			}
 			
 			//Update center values by averaging all inputs pointing to that center
 			if(changeHappened){
 				//Number of inputs assigned to a specific cluster
 				int[] numberOfInputs = new int[k];
 				
 				//Set centers to 0
 				for(int centerLoop = 0; centerLoop < k; centerLoop++){
 					for(int dimensionLoop = 0; dimensionLoop<dimensionOfInputs; dimensionLoop++){
 						centers[centerLoop][dimensionLoop] = 0;
 					}
 				}
 				
 				//Adds all the input values assigned to a centers
 				for(int input = 0; input < inputs.length; input++){
 					numberOfInputs[centerForInput[input]]++;//Adds up number of inputs that have this center
 					for(int dimension = 0; dimension<dimensionOfInputs; dimension++){
 						//Adds up all the input values for the center the input is assigned to
 						centers[centerForInput[input]][dimension]+=inputs[input][dimension];
 					}
 				}
 				
 				//Completes averaging for inputs at their centers
 				for(int center = 0; center < k; center++){
 					for(int dimensionLoop = 0; dimensionLoop<dimensionOfInputs; dimensionLoop++){
 						if(numberOfInputs[center] == 0){
 							throw new Exception();
 						}
 						//Divides the sum of the input values at a center by the number of inputs assigned to that center
 						centers[center][dimensionLoop] = centers[center][dimensionLoop]/numberOfInputs[center];
 					}
 				}
 				
 			}
 		}
 		this.centers = centers;
 		this.centerForInput = centerForInput;
 	}
}