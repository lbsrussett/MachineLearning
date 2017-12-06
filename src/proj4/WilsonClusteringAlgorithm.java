package proj4;

public abstract class WilsonClusteringAlgorithm {
	public abstract double[][] getCenters();
	public abstract int[] getCenterForInputs();
	public abstract void createClusterCenters(int k, double inputs[][]) throws Exception;
}
