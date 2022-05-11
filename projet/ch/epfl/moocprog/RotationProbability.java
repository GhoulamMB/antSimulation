package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Utils;

public class RotationProbability {
    private double[] Angles;
    private double[] Probabilities;
    public RotationProbability(double[] Angles,double[] Probabilities){
        Utils.requireNonNull(Angles);
        Utils.requireNonNull(Probabilities);
        if(Angles.length != Probabilities.length){
            throw new IllegalArgumentException();
        }else{
            this.Angles = Angles.clone();
            this.Probabilities = Probabilities.clone();
        }
    }
    public double[] getAngles(){
        return this.Angles.clone();
    }
    public double[] getProbabilities(){
        return this.Probabilities.clone();
    }
}
