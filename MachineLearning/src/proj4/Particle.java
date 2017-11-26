
package proj4;

/**
 * @author jrb
 */

public class Particle {
    /* Instance Variables. */
    private double[] ibest;
    private double[] curPos;
    private double[] idistanceV;
    private Cluster cluster;
    private double[] iLastVelocityV;
    private double[] iNextVelocityV;
    private double gPhi;
    private double iPhi;
    private double learnFactor_1;
    private double learnFactor_2;
    private double omega;
    private double aggregateDist;
    
    
    /* Particle constructor. */
    public Particle(){
        
        
    } /* END Particle constructor. */
    
    
    /* Calculate the next velocity vector v(t+1) */
    public void nextVelocity(){
        
        
        
    } /* END nextVelocity() */
    
    
    /* Calculate the next particle position. */
    public void nextPos(){
        
        
    } /* END nextPos() */
    
    
    /* Calculate the distance from particle to each point in search space. */
    public void distanceV(){
        
        
    } /* END distanceV() */
    
    /* Sets the threshhold for the distance vectors and collects points wihtin
     * that bound. */
    public void setAlphaCollectPoints(){
        
        
    } /* END setAlphaCollectPoints() */
    
    
    /* Calculate the cluster sum of distances for evaluation. */
    public void clusterDistSum(){
        
        
    
    } /* END clusterDistSum() */
    
    
    
    
    
    
}
