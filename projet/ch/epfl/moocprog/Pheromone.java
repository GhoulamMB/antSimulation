package ch.epfl.moocprog;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.PHEROMONE_THRESHOLD;
import static ch.epfl.moocprog.config.Config.PHEROMONE_EVAPORATION_RATE;

import ch.epfl.moocprog.utils.Time;
public final class Pheromone extends Positionable{
    private double Quantity;
    public Pheromone(ToricPosition toric , double Quantity){
        super(toric);
        this.Quantity = Quantity;
    }
    public double getQuantity(){
        return this.Quantity;
    }
    public boolean isNegligible(){
        if(this.Quantity < getConfig().getDouble(PHEROMONE_THRESHOLD)){
            return true;
        }
        return false;
    }
    public void update(Time dt){
        if(!isNegligible()){
            this.Quantity -= dt.toSeconds() * getConfig().getDouble(PHEROMONE_EVAPORATION_RATE);
            if(this.Quantity< 0.0){
                this.Quantity = 0.0;
            }
        }
    }
    public double getFoodQuantity(){
        return 0.0;
    }
}
