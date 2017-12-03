package proj4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class WilsonProject4Application {
	public static final String fileName = "frogs.txt";
	public static final int dimensions = 22;
	public static final int maxClusterSize = 10;
	public static final int numberOfTests = 10;
	
	public static void main(String[] args){
		WilsonProject4Application application = new WilsonProject4Application();
		WilsonClusteringAlgorithm kmc = new KMeansClustering();
		double[][] inputs = loadInputs(fileName);//{{1},{2},{3},{5},{8},{9},{10}};//
		double maxStrength = 0;
		for(int test = 0; test < numberOfTests; test++){
			double strength = application.evaluateClusters(kmc, inputs);
			if(strength > maxStrength){
				maxStrength = strength;
			}
		}
		System.out.println("Max Strength: " + maxStrength);
	}
	
	public double evaluateClusters(WilsonClusteringAlgorithm clusterCreater, double[][] inputs){
		double maxStrength = 0;
		for(int k = 2; k <= maxClusterSize; k++){
			//Run the cluster algorithm
			try {
				clusterCreater.createClusterCenters(k, inputs);
			} catch (Exception e) {
				break;
			}
			double[][] centers = clusterCreater.getCenters();
			int[] centerForInput = clusterCreater.getCenterForInputs();
			
			int numberOfInputs = inputs.length;
			int dimensionOfInputs = inputs[0].length;
			
			
			//Find the average distance between an input and its center
			double sumOfAllDistancesSquared = 0;
			for(int input = 0; input < numberOfInputs; input++){//For each center
				double sumOfDistancesSquared = 0;
				for(int dimension = 0; dimension<dimensionOfInputs; dimension++){//For each dimension
					//Get the distance squared
					sumOfDistancesSquared += Math.pow((centers[centerForInput[input]][dimension]-inputs[input][dimension]),2);
				}
				sumOfAllDistancesSquared += sumOfDistancesSquared;
			}
			double averageDistanceToCenter = Math.sqrt(sumOfAllDistancesSquared)/k;
			
			//Find average distance between centers
			int combinations = 0;
			for(int center1 = 0; center1<(k-1);center1++){
				double sumOfDistancesSquared = 0;
				for(int center2 = center1+1; center2<k;center2++){
					combinations++;
					for(int dimension = 0; dimension<centers[0].length;dimension++){
						sumOfDistancesSquared += Math.pow(centers[center1][dimension]-centers[center2][dimension], 2);
					}
				}
				sumOfAllDistancesSquared += sumOfDistancesSquared;
			}
			double averageDistanceBetweenCenters = Math.sqrt(sumOfAllDistancesSquared)/combinations;
			double strength = averageDistanceBetweenCenters/averageDistanceToCenter;
			if(strength > maxStrength){
				maxStrength = strength;
			}
		}
		return maxStrength;
	}
	
	/**
	 *Loads a text file from the file at the variable fileName
	 *@return	the randomly generated inputs
	 */
	private static double[][] loadInputs(String fileName){
		ArrayList<double[]> inputsList = new ArrayList<double[]>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/"+fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Arrrg, there was an error loading the file, matey.");
			System.exit(0);
		}
		Scanner scanner = new Scanner(br);
		while(scanner.hasNext()){
			double[] fileInput = new double[dimensions];
			for(int index = 0; index < fileInput.length;index++){
				fileInput[index] = scanner.nextDouble();
			}
			inputsList.add(fileInput);
		}
		scanner.close();
		double[][] inputs = new double[inputsList.size()][dimensions];
		int index = 0;
		for(double[] input: inputsList){
			inputs[index] = input;
			index++;
		}
		return inputs;
	}
}
