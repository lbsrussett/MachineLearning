package proj4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Project4Application {
	private static int dimensions = 22; //How many inputs are passed in
	private static int inputVectors = 7195; //How many arrays of inputs are tested
	private static int possibleClassifications = 60; //If the problem is multiClass, the number of classifcations
	private static String filename = "Frogs_MFCCs.csv";
	private static double[][] inputs = null;
	
	public static void main(String[] args) {
		inputs = loadInputs(); 
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
			for(int i = 0; i < input.length; i++) {
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

