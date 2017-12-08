package proj4;

import java.util.ArrayList;

public abstract class ClusteringAlgorithm {
	public abstract void updateClusters(double[][] inputs);
	public abstract ArrayList<Cluster> returnClusters();
}
