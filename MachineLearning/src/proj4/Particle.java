
package proj4;

import java.lang.Math.*;

/**
 * @author John R. Blixt
 */

public class Particle {
    /* Instance Variables. */
    private double[] lbest;       /* Individual best position. */
    private double[] curPos;
    private double[] idistanceV;  /* Holds fitness result. */
    private Cluster cluster;      /* Prospective cluster for the individual. */
    private double[] iLastVelocityV;    /* TODO: Possibly remove see constructor. */
    private double[] iNextVelocityV;
    private double gPhi;
    private double iPhi;
    private double learnFactor_1;
    private double learnFactor_2;
    private double omega;
    private double aggregateDist;
    private int clusterNum;
    
    
    /* PSO particle constructor. 
     * PRE:  ---
     * POST: Create the PSO particle with instance varialbes passed into the
     *       the constructor.
     */
    public Particle(double learnFactor_1, double learnFactor_2, int dimension, 
                           double gPhi, double iPhi, double omega, double[] position){
        /* Initailize variables. */
        this.curPos = position;
        this.lbest = new double[dimension];
        this.idistanceV = new double[dimension];
        this.iLastVelocityV = new double[dimension];     /* TODO: REMOVE COMMENT IF NEEDED otherwise DELETE STATEMENT. */
        this.iNextVelocityV = new double[dimension];
        /* The cluster for this particle. */
        this.cluster = new Cluster();
        
        /* Tunable parameters. Should be set in class PSO. */
        this.learnFactor_1 = learnFactor_1;
        this.learnFactor_2 = learnFactor_2;
        this.gPhi = gPhi;
        this.iPhi = iPhi;
        this.omega = omega;
        /* END Tunable parameters. */
        this.clusterNum = 0;
        
    } /* END Particle constructor. */
    
    
    /*
     * Add a point to the cluster. Access the cluster class.
     */
    public boolean addPoint(double[] point){
        
        cluster.addPoint(new Point(point));
        clusterNum ++;
        
        /* Cross check the dimensionality. */
        if (point.length != curPos.length )
            throw new RuntimeException("Error in: Particle.addPoint()");
            
        return true;
    }
    
    
    /* Calculate the next velocity vector v(t+1) 
     * The calculation of the next time step velocity used to calculate the movement
     * of the next position of the particle.
     * PRE: v_ij(t + 1) = omega*v_i(t) + phi_1*learnRate_1(lbest - x_i(t)) + phi_2*
     *       learnRate_2(gbest - x_i(t))
     * POST: velocity vector to the next position, x_i(t + 1), from position x_i(t).
     *       ****Side effect**** channge of iNextVelocity value. 
     */
    public double[] calculateNextVelocity(double[] gbest){
        /* Next velocity vector. */
        double[] nextVelocity = new double[curPos.length];
        double[] lbestV = new double[curPos.length];
        double[] gbestV = new double[curPos.length];
        
        /* Difference */
        lbestV = vectorAddition(gbest,curPos, 0);
        gbestV = vectorAddition(lbest, curPos, 0);
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
     */
    public double[] vectorScalarMult(double scalar, double[] vector){
        /* Scalar multiplication on a vector. */
        double[] multVector = new double[vector.length];
        for (int i = 0; i < multVector.length; i++)
            multVector[i] = vector[i] * scalar;
        
        return multVector;
    }
    
    /* Differences in current position vs. lbest and gbest positions. 
    * PRE: two vectors in n dimensional space.
    * POST: Difference vector.
    */
    public double[] vectorAddition(double[] vector_1, double[] vector_2, int sign){
        /* Local variables. */
        double[] sum = new double[vector_1.length];
        
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
     * PRE: 
     * POST: 
     */
    public void nextPos(){
        
        
    } /* END nextPos() */
    
    
    /* Calculate the distance from particle to each point in search space. 
     * This algorithm uses the Euclidean distance to map from R^n to R. 
     * PRE: Two vectors of same dimensionality.
     * POST: Euclid Dist. between them in n dimensions.
     */
    public double distance(double[] vector_1, double[] vector_2){
        /* Local variables. */
        double distance = 0;
        double[] innerProduct = new double[vector_1.length];
        
        /* Verify dimensionality. */
        if (vector_1.length != vector_2.length)
            throw new RuntimeException("Error in Particle.distanceV.");
        
        innerProduct = vectorAddition(vector_1, vector_2, 0); /* Subtract. */
        
        /* Square components. */
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
    public void setAlphaCollectPoints(){
        
        
    } /* END setAlphaCollectPoints() */
    
    
    /* Calculate the cluster sum of distances for evaluation. */
    public void clusterDistSum(){
        
        
    
    } /* END clusterDistSum() */
    
    
    
    
}
