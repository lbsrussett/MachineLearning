package proj4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/*Frogs Data Set: dimensions=21, inputVectors=7195, possibleClassifications=60, filename="Frogs_MFCCs.csv"*/
/*3D Spatial: dimensions=4, inputVectors=20000, possibleClassifications=undefined, filename="3D_spatial_network.csv*/
/*HTRU: dimensions=8, inputVectors=17898, possibleClassifications=2, filename="HTRU_2.csv"*/
/*Seeds: dimensions=7, inputVectors=210, possibleClassifications=3, filename="seeds.csv"*/
/*Wholesale: dimensions=6, inputVectors=440, possibleClassifications=undefined, filename="Wholesale_customers_data.csv"*/

public class Project4Application {
	private static int dimensions = 8; //How many inputs are passed in
	private static int inputVectors = 210; //How many arrays of inputs are tested
	private static int possibleClassifications = 3; //If the problem is multiClass, the number of classifcations
	private static String filename = "HTRU_2.csv";
	private static double[][] inputs = null;
	public static double EPSILON = 40;
	public static  int MINPOINTS = 20;
	
	public static void main(String[] args) {
		inputs = loadInputs(); 
		/*DBScan db = new DBScan(inputs, EPSILON, MINPOINTS);
		db.updateClusters(inputs);
		db.printClusters();*/
		ACO aco = new ACO(inputs, 2);
		aco.updateClusters();
                
                
                
//                double[][] inputs = {{9, 9, 9}, {3, 5, 7 }, {4, 5, 8}, {5, 6, 20}, {1, 4, 6}, {5, 5, 5}, {6, 6, 6}, {7, 7, 7}, {8, 8, 8}, {9, 9, 9}, {9, 8, 12}, {15, 15, 15}, {12, 12, 13}, {13, 14, 16}, {13, 15, 12}, {13, 12, 12}};

                /* Use this setting for HTRU. */
                /*PSO pso = new PSO(inputs, 100, 8, .1, .30, 5.5, .3);
                ArrayList<Cluster> clusters = pso.returnClusters();
                System.out.println();*/
                
	}
	
	private static double[][] loadInputs() {
	ArrayList<double[]> inputsList = new ArrayList<double[]>();
	BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader("data/" + filename));
	} catch (FileNotFoundException e) {
		System.out.println("Arrrg, there was an error loading the file, matey.");
		System.exit(0);
	}
	Scanner scanner = new Scanner(br);
	while(scanner.hasNext()){
		double[] fileInput = new double[dimensions+1];
			String[] input = scanner.next().split(",");
			for(int i = 0; i < input.length-1; i++) {
				fileInput[i] = Double.parseDouble(input[i]);
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

