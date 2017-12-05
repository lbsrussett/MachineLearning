package src.proj4;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class WilsonProject4Application {
	public static final String fileName = "frogs.csv";
	public static final int dimensions = 22;
	public static final int maxClusterSize = 3;
	public static final int numberOfTests = 100;
	
	public static void main(String[] args){
		WilsonProject4Application application = new WilsonProject4Application();
		ClusteringAlgorithm kmc = new KMeansClustering();
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
	
	public double evaluateClusters(ClusteringAlgorithm clusterCreater, double[][] inputs){
		double maxStrength = 0;
		for(int k = 2; k <= maxClusterSize; k++){
			//Run the cluster algorithm
			KMeansClustering KMeans = (KMeansClustering)clusterCreater;
			KMeans.setNumberOfClusters(k);
			KMeans.updateClusters(inputs);
			
			boolean illegitimateAnswer = false;
			
			//Find the average distance between an input and its center
			double sumDistancesSquared = 0;
			for(Cluster cluster: clusterCreater.returnClusters()){
				sumDistancesSquared+=cluster.getAverageDistanceToCenter();
			}
			double averageDistanceToCenter = Math.sqrt(sumDistancesSquared)/k;
			if(averageDistanceToCenter == 0){
				illegitimateAnswer = true;
			}
			
			//Find average distance between centers
			int combinations = 0;
			
			double sumOfDistancesSquared = 0;
			for(int center1 = 0; center1<(k-1);center1++){
				for(int center2 = center1+1; center2<k;center2++){
					combinations++;
					Point c1 = clusterCreater.returnClusters().get(center1).getCenter();
					Point c2 = clusterCreater.returnClusters().get(center2).getCenter();
					if(Double.isNaN(c1.getValues()[0]) || Double.isNaN(c2.getValues()[0])){
						illegitimateAnswer = true;
					}
					sumOfDistancesSquared += Cluster.getDistance(c1.getValues(), c2.getValues());
				}
			}
			double averageDistanceBetweenCenters = Math.sqrt(sumOfDistancesSquared)/combinations;
			double strength = averageDistanceBetweenCenters/averageDistanceToCenter;
			if(strength > maxStrength && !illegitimateAnswer){
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
