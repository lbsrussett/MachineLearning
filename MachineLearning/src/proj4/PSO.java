
package proj4;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jrb
 */
public class PSO extends ClusteringAlgorithm {
    
    /* Instance variables. */
    private ArrayList<Point>        allPoints;
    //private ArrayList<Particle>     allParticles;
    private double[]                gbest;              /* TODO: get rid of this if not needed. */
    //private Particle                gbCluster;          /* Handle on the cluster with gbest. */
    //private double[]                gbestDistanceV;     /* Distance vector of gbest. */
    private double                  gbestFitness;
    private ArrayList<Cluster>      globalClusters;     /* Final clusters. */
    private int                     numParticles;       /* Initialize with how many? */
    //private double                  upperBound;
  
    /* Tunable parameters */
    private double                  learnFactor_1;
    private double                  learnFactor_2;
    private double                  omega;
    private int                     dimension;
    private double                  alpha;              /* Start at 2, drop slowly. Increases cluster grouping.  */
    private int                     numClusters;
    
    
    
    
    /* Constructor for the PSO class. */
    public PSO(double[][] inputs, int numClusters, int dimension,
                                      double learnFactor_1, double learnFactor_2,
                                                    double alpha, double omega){
        /* local variables */
        int dataSize        = inputs.length;
        this.allPoints      = new ArrayList(dataSize);
        /* Fill it. */
        for (int i = 0; i < dataSize; i++){
            allPoints.add(new Point(inputs[i]));
        }
        
        this.numClusters    = numClusters;
        this.gbest          = new double[dimension];
        for (int i = 0; i < gbest.length; i++)
            gbest[i] = 0;   /* First move migrates toward the origin anyways so, shouldn't throw off. */
        //this.gbCluster      = null;
        //this.gbestDistanceV = new double[dimension];
        this.learnFactor_1 = learnFactor_1;
        this.learnFactor_2  = learnFactor_2;
        this.gbestFitness   = Double.MAX_VALUE;
        this.globalClusters = new ArrayList(numClusters);
        this.alpha          = alpha;
        //this.allParticles   = null;
        this.omega          = omega;
        this.dimension      = dimension;

        
        if ((int)(.3 * dataSize) > 200){
            this.numParticles = 200;
        } else {
            this.numParticles   = (int) Math.ceil((.3 * dataSize));
        }
        
        
        
    } /* END PSO constructor. */

    
    /* Update Clusters for the PSO global clusters. */
    @Override
    public void updateClusters(){
        /* Will make a new cluster until the  global cluster container is full.*/
        
        
        
    }  /* END updateCluster() */
    
    /* Forms the particles that will be used for the swarm. */
    private ArrayList<Particle> formParticles(){
        /* Randomly choose a point for the particle starting position. This will
         * ensure that the particles start in the search space and not in
         * outerspace.
         */
        ArrayList<Particle> allParticlestmp = new ArrayList(numParticles);
       
        
        for (int i = 0; i < numParticles; i++){
             int rndIdx = (int) Math.ceil(Math.random() * (allPoints.size() - 1));
            allParticlestmp.add(new Particle(learnFactor_1, learnFactor_2, 
                             dimension, omega, allPoints.get(rndIdx).getValues()));
        }
        
        return allParticlestmp;
    }  /* END formParticles() */
    
    /* Evaluate the particles per the PSO algorithms and during that evaluation
     * find a best final cluster. */
    public Cluster evaluateParticles(){
        /* Forms particles for each cluster and updates global cluster before termination. Removes the points from 
         * the array of all points. */
        ArrayList<Particle>  allParticles;
        Cluster     tmpCluster = null;
        int                  deltaGbestIter = 0;     /* How many iterations since gbest update. */
        allParticles = formParticles();  /* Forms the particles for this iteration. */
        
        
        /* Evaluate fitness of each particle. Put the best in gbest. Do this 
         * until gbest doesn't change for x iterations. increment deltaGbestIter.
         */
        do {
       
            /* Calculate next velocity and position */            
            
            
            for (int i = 0; i < allParticles.size(); i++){
                allParticles.get(i).calculateNextVelocity(gbest);
                allParticles.get(i).calculateNextPosition();                        /* TODO: Get rid of return. */
            }

            /* For each particle collect points via findClusterPoints(). */
            for (int i = 0; i < allParticles.size(); i++){   System.out.println("ENTER Do/while for loop2..");
                findClusterPoints(allParticles.get(i));
            }

            for (int i = 0; i < allParticles.size(); i++){  System.out.println("ENTER Do/while for loop3..");
                double fitness = allParticles.get(i).clusterFitness();
                if (fitness < gbestFitness && fitness > 0){   /* If it's zero, it's a single point. */
                    /* Save the fitness as the best. */
                    gbestFitness = fitness;
                    gbest = allParticles.get(i).getPosition();
                    tmpCluster = null;
                    tmpCluster = allParticles.get(i).getCluster();
                    allParticles.get(i).clearCluster();
                    allParticles.get(i).clearClusterNumber();
                    /* gbest update. reset. */
                    deltaGbestIter = 0;
                    /* Get the position. */
                } else {
                    allParticles.get(i).clearCluster();
                    allParticles.get(i).clearClusterNumber();
                }
            }
            
            for (int i = 0; i < allParticles.size(); i++){
                allParticles.get(i).moveParticle();
            }
            
            deltaGbestIter++;
            
            ////*************************////
            System.out.println(gbestFitness);
            System.out.println(tmpCluster);
//        if (tmpCluster.clusterSize() == 0){
//            return tmpCluster;
//        }
        } while (deltaGbestIter < 6);
        
        //globalClusters.add(tmpCluster);
        return tmpCluster;
    }  /* END evaluateParticles() */

    
    public void findClusterPoints(Particle particle){
        /* I give you the set of points and an alpha. You find the cluster 
         * within that alpha and hold on to the points. */
        particle.collectPoints(alpha, allPoints);
    }

    @Override
    public ArrayList<Cluster> returnClusters() {
        /* This is my organizing method. This coordinates everything and returns
         * the final cluster array list.  */
        /* Calls evaluate particles */
        int zeroIteration = 0;   /* How many times do we have a fitness of zero? i.e. only one outlier point. */
        Cluster tmpCluster = null;
        
        
        
      
        for (int i = 0; i < numClusters; i++) {            System.out.println("returnCluster  TOP..");
            tmpCluster = evaluateParticles();   /* Do this as many times as there are total clusters. Each time will get a cluster. */
            
            if (tmpCluster == null){
                return globalClusters;
            }
            if (tmpCluster.clusterSize() == 0){
                zeroIteration++;
            }

            /* Before we do it again, we need to remove the points from the population. The global points array should
             * get smaller as we go.   */
            //if (tmpCluster != null) {
            for (int k = 0; k < tmpCluster.clusterSize(); k++){      System.out.println("returnCluster  Find a Cluster.");
                double[] tmp = tmpCluster.getPoint(k).getValues();
                /* Now remove it from the set of total points. */
                for (int j = 0; j < allPoints.size(); j++){           System.out.println("returnCluster  Remove Points.");
                    if (Arrays.equals(allPoints.get(j).getValues(), tmp)) {
                        /* Remove the thing. */
                        allPoints.remove(j);
                        break;
                    }
                }
            }
            globalClusters.add(tmpCluster);
            tmpCluster = null;
            
            
            
            if (zeroIteration == 5 || allPoints.size() == 0) {
            return globalClusters;
        }  
        }
        
        return globalClusters;
    }  /* END returnClusters() */
    
}
