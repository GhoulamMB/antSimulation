package ch.epfl.moocprog;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ALPHA;
import static ch.epfl.moocprog.config.Config.BETA_D;
import static ch.epfl.moocprog.config.Config.Q_ZERO;

public class PheromoneRotationProbabilityModel implements AntRotationProbabilityModel{
    static final double BETA = getConfig().getDouble(BETA_D);
    static final double Q0 = getConfig().getDouble(Q_ZERO);
    static final Integer alpha = getConfig().getInt(ALPHA);
    private double detection(double x){
        return 1.0 / ( 1.0 + Math.exp( -BETA * (x - Q0)));
    }
    
    public RotationProbability computeRotationProbs(RotationProbability movementMatrix, ToricPosition position,
            double directionAngle, AntEnvironmentView env) {
        double[] I = movementMatrix.getAngles();
        double[] Q = env.getPheromoneQuantitiesPerIntervalForAnt(position, directionAngle, I);
        double[] P = movementMatrix.getProbabilities();
        double[] numerateur = new double[I.length];
        double[] pprime = new double[I.length];
        double somme = 0.0;
        for(int i=0;i<I.length;i++){
            numerateur[i] = P[i] * Math.pow(detection(Q[i]), alpha );
            somme += numerateur[i];
        }
        for(int i=0;i<I.length;i++){
            pprime[i] = (P[i] * Math.pow(detection(Q[i]), alpha ))/somme;
        }
        return new RotationProbability(I, pprime);
    }
    
}
