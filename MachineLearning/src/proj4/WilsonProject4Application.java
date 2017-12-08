import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class WilsonProject4Application {
	public static final String fileName = "network.csv";
	//frogs 22 working
	//seeds 7 working
	//customers 6
	//htru 8
	//network 4
	public static final int dimensions = 4;
	public static final int maxClusterSize = 10;
	public static final int numberOfTests = 100;
	public static double maxStrength = 0;
	public static int strongestClusterSize = 0;
	public static int stongestAntSize = 0;
	public static double strongestQValue = 0;
	
	//public static double[] epsilonValues = {1,3,5};
	//public static int[] minpointValues = {3,5,10};
	
	public static int[] antValues = {5, 10, 20};
	public static double[] qValues = {0.25, 0.5, 0.75};
	
	public static void main(String[] args){
		double[][] inputs = loadInputs(fileName);//{{1},{2},{3},{5},{8},{9},{10}};//
		final long startTime = System.currentTimeMillis();
		for(int test = 0; test < numberOfTests; test++){
			System.out.println("Test: "+(test+1));
			runTests(inputs);
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("Max Strength: " + maxStrength);
		//System.out.println("Strongest Min Point Value: " + stongestminpoint);
		//System.out.println("Strongest Epsilon Value: " + strongestEpsilonValue);
		System.out.println("Strongest Number of Ants: " + stongestAntSize);
		System.out.println("Strongest Q Value: " + strongestQValue);
		//System.out.println("Max Strength happened with " + strongestClusterSize + " clusters");
		System.out.println("Total execution time: " + (endTime - startTime)/1000.0 + " seconds" );
	}
	
	public static double runTests(double[][] inputs){
		double maxStrength = 0;
		/*for(int k = 2; k <= 10; k++){
			KMeansClustering kmc = new KMeansClustering();
			kmc.setInputs(inputs);
			kmc.setNumberOfClusters(k);
			kmc.updateClusters(inputs);
			evaluateCluster(kmc, true);
		}*/
		/*for(int epsilonIndex = 0; epsilonIndex < epsilonValues.length; epsilonIndex++){
			for(int minpointIndex = 0; minpointIndex < minpointValues.length; minpointIndex++){
			//Run the cluster algorithm
				DBScan dbs = new DBScan(inputs,epsilonValues[epsilonIndex],minpointValues[minpointIndex]);
				dbs.updateClusters(inputs);
				evaluateCluster(dbs, true);
			}
		}*/
		
		for(int antIndex = 0; antIndex < antValues.length; antIndex++){
			for(int qIndex = 0; qIndex < qValues.length; qIndex++){
			//Run the cluster algorithm
				ACO aco = new ACO(inputs,2,antValues[antIndex],qValues[qIndex]);
				aco.updateClusters(inputs);
			}
		}
		return maxStrength;
	}
	
	public static boolean evaluateCluster(ClusteringAlgorithm clusterCreater, boolean finalTest){
		boolean illegitimateAnswer = false;
		int k = clusterCreater.returnClusters().size();
		//Find the average distance between an input and its center
		double sumDistancesSquared = 0;
		for(Cluster cluster: clusterCreater.returnClusters()){
			sumDistancesSquared+=cluster.getAverageDistanceToCenter();
		}
		double averageDistanceToCenter = Math.sqrt(sumDistancesSquared)/k;
		//if(averageDistanceToCenter <= 0){
		//	illegitimateAnswer = true;
		//}
		
		//Find average distance between centers
		int combinations = 0;
		
		double sumOfDistancesSquared = 0;
		for(int center1 = 0; center1<(k-1);center1++){
			for(int center2 = center1+1; center2<k;center2++){
				combinations++;
				double[] c1 = clusterCreater.returnClusters().get(center1).getAveragePosition();
				double[] c2 = clusterCreater.returnClusters().get(center2).getAveragePosition();
				if(Double.isNaN(c1[0]) || Double.isNaN(c2[0]) || Double.isInfinite(c1[0]) || Double.isInfinite(c2[0])){
					//illegitimateAnswer = true;
				}else{
					sumOfDistancesSquared += Cluster.getDistance(c1, c2);
				}
			}
		}
		double averageDistanceBetweenCenters = Math.sqrt(sumOfDistancesSquared)/combinations;
		if(!Double.isNaN(averageDistanceBetweenCenters) && !Double.isNaN(averageDistanceToCenter)){
			
			System.out.println("Distance Between Centers: " + averageDistanceBetweenCenters + " \t Distance To Centers: " + averageDistanceToCenter);
			double strength = averageDistanceBetweenCenters/averageDistanceToCenter;
			if(illegitimateAnswer){
				strength = 0;
			}
			if(strength>maxStrength && finalTest){
				maxStrength = strength;
				ACO aco = (ACO) clusterCreater;
				stongestAntSize = aco.NUM_ANTS;
				strongestQValue = aco.Q;
				strongestClusterSize = clusterCreater.returnClusters().size();
			}
			return true;
		}else{
			System.out.println("Invalid results for this cluster size");
			return false;
		}
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
