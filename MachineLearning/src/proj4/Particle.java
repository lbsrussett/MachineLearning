
package proj4;

import java.lang.Math.*;
import java.util.ArrayList;

/**
 * @author John R. Blixt
 */

public class Particle {
    /* Instance Variables. */
    private double[]    lbest;              /* Individual best position. */
    private double[]    curPos;
    private double[]    nextPos;
    private double[]    iDistanceVector;    /* R^n => R by distance for all points in data set. */
    private Cluster     cluster;            /* Prospective cluster for the individual. */
    private double[]    iNextVelocityV;
    private double      gPhi;               /* ~U[0, 1] */
    private double      iPhi;
    private double      learnFactor_1;      /* C1 and C2, positive acceleration constants.  */
    private double      learnFactor_2;
    private double      omega;              /* inertia weight. */
    private double      lbestFitness;
    private int         clusterNum;
    
    
    /* PSO particle constructor. 
     * PRE:  ---
     * POST: Create the PSO particle with instance varialbes passed into the
     *       the constructor.                  ***TESTED****
     */
    public Particle(double learnFactor_1, double learnFactor_2, int dimension, 
                     double omega, double[] position){
        /* Initailize variables. */
        this.curPos                 = position;
        this.lbest                  = new double[dimension];
        this.nextPos                = new double[dimension];
        this.iNextVelocityV         = new double[dimension];
        this.gPhi                   = Math.random();
        this.iPhi                   = Math.random();
        /* The cluster for this particle. */
        this.cluster                = new Cluster();
        
        /* Tunable parameters. Should be set in class PSO. */
        this.learnFactor_1          = learnFactor_1;
        this.learnFactor_2          = learnFactor_2;
        this.omega                  = omega;
        /* END Tunable parameters. */
        this.clusterNum             = 0;
        this.lbestFitness           = Double.MAX_VALUE;
        this.iDistanceVector        = null;
        
    } /* END Particle constructor. */
    
    
    /*
     * Add a point to the cluster. Access the cluster class.
     *      *****TESTED******
     */
    public boolean addPoint(double[] point){
        
        cluster.addPoint(new Point(point));
        clusterNum ++;
            
        return true;
    }
    
    
    /* Calculate the next velocity vector v(t+1) 
     * The calculation of the next time step velocity used to calculate the movement
     * of the next position of the particle.
     * PRE: v_ij(t + 1) = omega*v_i(t) + phi_1*learnRate_1(lbest - x_i(t)) + phi_2*
     *       learnRate_2(gbest - x_i(t))
     * POST: velocity vector to the next position, x_i(t + 1), from position x_i(t).
     *       ****Side effect**** channge of iNextVelocity value. 
     *                                 ******TESTED**********
     */
    public double[] calculateNextVelocity(double[] gbest){
        /* Next velocity vector. */
        double[] nextVelocity       = new double[curPos.length];
        double[] lbestV             = new double[curPos.length];
        double[] gbestV             = new double[curPos.length];
        
        /* Difference */
        lbestV = vectorAddition(gbest,curPos, 0);
        gbestV = vectorAddition(lbest, curPos, 0);
        //lbestV = vectorAddition(curPos, gbest, 0);
        //gbestV = vectorAddition(curPos, lbest, 0);
        /* Scalar calculations. */
        nextVelocity = vectorScalarMult(omega, curPos);
        lbestV = vectorScalarMult(iPhi * learnFactor_1, lbestV);
        gbestV = vectorScalarMult(gPhi * learnFactor_2, gbestV);
        /* Sum the parts. */
        nextVelocity = vectorAddition(nextVelocity, lbestV, 1);
        nextVelocity = vectorAddition(nextVelocity, gbestV, 1);
        
        /* Side effect. Make sure that the velocity returned is the same value
         * that the node has stored. Side effect needed.  */
        iNextVelocityV = nextVelocity;
        return iNextVelocityV;
        
    } /* END nextVelocity() */
    
    
    /* 
     *  Scalar multiplication on a vector
     *  PRE: a vector and scalar as inputs.
     *  POST: a vector with the computation
     *             **********TESTED*************
     */
    public double[] vectorScalarMult(double scalar, double[] vector){
        /* Scalar multiplication on a vector. */
        double[] multVector     = new double[vector.length];
        
        for (int i = 0; i < multVector.length; i++)
            multVector[i] = vector[i] * scalar;
        
        return multVector;
    }
    
    /* Differences in current position vs. lbest and gbest positions. 
     * PRE: two vectors in n dimensional space.
     * POST: Difference vector.
     *     *********TESTED**********
     */
    public double[] vectorAddition(double[] vector_1, double[] vector_2, int sign){
        /* Local variables. */
        double[] sum            = new double[vector_1.length];
        
        /* Error check the sign value. */
        if (sign != 1 && sign != 0) {
            throw new RuntimeException("Error with the sign argument in: "
                    + "Particle.vectorDifference()");
        }
        
        /* Make sure vectors are same dimension. */
        if (vector_1.length != vector_2.length) { 
            throw new RuntimeException("Error with Particle.vectorDifference();"
                    + " vectors are not the same dimension.");
        }
        
        /* Component by component addition. */
        if (sign == 1){
            for (int i = 0; i < vector_1.length; i++)
                sum[i] = vector_1[i] + vector_2[i];
        }
        /* Component by component subtraction. */
        if (sign == 0){
            for (int i = 0; i < vector_1.length; i++)
                sum[i] = vector_1[i] - vector_2[i];
        }
            
        return sum;
    } /* END vectorDifference() */
    
    
    /* Calculate the next particle position. 
     * PRE: Uses individual memory to calculate the next position.
     * POST: Returns the next position vector for the particle.
     * ***Side effect. *** Changes the value of 
     *         ***********TESTED*************
     */
    public double[] calculateNextPosition(){
        nextPos = vectorAddition(curPos, iNextVelocityV, 1);

        return nextPos;
        
    } /* END nextPos() */
    
    
    /* Calculate the distance from particle to each point in search space. 
     * This algorithm uses the Euclidean distance to map from R^n to R. 
     * PRE: Two vectors of same dimensionality.
     * POST: Euclid Dist. between them in n dimensions.
     *               ******TESTED*********
     */
    public double distance(double[] vector_1, double[] vector_2){
        /* Local variables. */
        double distance         = 0;
        double[] innerProduct   = new double[vector_1.length];
        
        /* Verify dimensionality. */
        if (vector_1.length != vector_2.length)
            throw new RuntimeException("Error in Particle.distanceV.");
        
        innerProduct = vectorAddition(vector_1, vector_2, 0); /* Subtract. */
        
        /* Square components. Ensures positive. */
        for (int i = 0; i < innerProduct.length; i++)
            innerProduct[i] *= innerProduct[i];
        
        /* Add them */
        for(int i = 0; i < innerProduct.length; i++)
            distance += innerProduct[i];
        
        /* Sqrt. */
        distance = Math.sqrt(distance);
            
        return distance;
    } /* END distanceV() */
    
    /* Sets the threshhold for the distance vectors and collects points wihtin
     * that bound. */
    public void collectPoints(double alpha, ArrayList<Point> allPoints){
        /* Start with alpha at 2, meaning if a point is within double the distance
         * from the centroid it is in. As alpha decreases the clusters will become
         * tighter but, this could cause more outliers on the fringes.
         */
        /* Local variables. */
         double min      = Double.MAX_VALUE;
        
        
        if (iDistanceVector == null){
            iDistanceVector = new double[allPoints.size()];
        } else {
            for(int i = 0; i < iDistanceVector.length; i++){
                iDistanceVector[i] = 0;
            }
        }
        
        /* Calculate distance for all points in the data set. */
        for(int i = 0; i < iDistanceVector.length; i++){
            iDistanceVector[i] = distance(curPos, allPoints.get(i).getValues());
        }
        /* Verify that nothing went wrong. */
        if (iDistanceVector.length - allPoints.size() != 0){
            throw new RuntimeException("Problem in Particle.collectPoints()");
        }
        
        /* Find min. dist. */
        for(int i = 0; i < iDistanceVector.length; i++){
            if (iDistanceVector[i] < min && iDistanceVector[i] != 0 && 
                    iDistanceVector[i] > alpha){ /* We don't want it too close. */
                min = iDistanceVector[i];
            }
        }
        /* Find those within the alpha range and put them in the cluster bucket. */
        min = min * alpha;
        for(int i = 0; i < iDistanceVector.length; i++){
            if(iDistanceVector[i] <= min){
                /* Add it to the cluster. */
               if (addPoint(allPoints.get(i).getValues()) != true){
                   throw new RuntimeException("Error from Particle.collectPoints()");
               }
               /* Take the point out of the distance vector. */
               iDistanceVector[i] = Double.MAX_VALUE;
            }
        }
        
        /* Points are collected given original alpha and data set. */
        /* TODO: for all points in cluster. iterate over cluster and avarage the
         *         distance to curPos. call it rho.
         */
        double rho = 0;
        int numInClust = 0;
        for(int i = 0; i < cluster.clusterSize(); i++){
            double dist = distance(cluster.getPoint(i).getValues(), curPos);
            rho += dist;
            if (dist != 0){
                numInClust++;
            }
        }
        
        /* Average */
        rho = rho * 1/numInClust;                                         
        
        /* TODO: For all points in the base cluster. Find all points in the data
         * set within rho from each point in the cluster. When you find a point
         * append it to the iterative container. When you get to the end, they've
         * all been found.    This is the final cluster.  */
        double distToCurPoint = 0;
        for(int i = 0; i < cluster.clusterSize(); i++){
            for (int j = 0; j < allPoints.size(); j++){
                if (iDistanceVector[j] - Double.MAX_VALUE != 0){
                    distToCurPoint = distance(cluster.getPoint(i).getValues(), 
                                            allPoints.get(j).getValues());
                    if (distToCurPoint != 0 && distToCurPoint <= rho){
                        /* Append the point to the cluster. */
                        cluster.addPoint(allPoints.get(j));
                        iDistanceVector[j] = Double.MAX_VALUE;
                    }
                }
            }
        }
    } /* END collectPoints() */
    
    
    /* Calculate value for evaluation for the current cluster.
     *  PRE: A cluster of points.
     *  POST: Normalized fitness value. Normalize means scale wrt itself.
     *  This value when compared to another set should be scaled wrt the other set.
     *  ****Side Effect****  if the calculation to the fitness of a set is strictly
     *  less than the best fitness. It is the best fitness so far so, replace.
     *  ******Side Effect****  update lbest when an improvement to lbestFitness
     *   is found.
     */
    public double clusterFitness(){
        /*  */
        double distanceTotal             = 0;
        double fitness                   = 0;
        ArrayList<Point> pointsInCluster = cluster.getClusterPoints();
        
        for (int i = 0; i < cluster.clusterSize(); i++){
            distanceTotal += distance(curPos, pointsInCluster.get(i).getValues());
        }
        /* Calculate and return the fitness value. */
        /* Site effect. Make sure the two values are equal at time of computation. */
        fitness = distanceTotal * 1/cluster.clusterSize();
        /* If it's the best fitness so far. Grab the fitness value, and points. */
        if (fitness - lbestFitness < 0) { 
            lbestFitness = fitness; 
            /* Update lbest position when we found a new best fitness. */
            lbest = curPos;
        }
        return fitness;
        
    } /* END clusterDistSum() */
    
//    public double getBestIndividualFitness(){
//        return lbestFitness;
//    }
    
    public double[] getPosition(){
        return curPos;
    }
    
    public Cluster getCluster(){
        return cluster;
    }
    
    public void clearClusterNumber(){
        this.clusterNum = 0;
    }
    
    public void clearCluster(){
        this.cluster = null;
        this.cluster = new Cluster();
    }
    
    
    public void moveParticle(){
        curPos = nextPos;
    }
    
    
} /* END Class Particle. */
