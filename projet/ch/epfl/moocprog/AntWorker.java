package ch.epfl.moocprog;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_HP;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_LIFESPAN;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_SPEED;
import static ch.epfl.moocprog.config.Config.ANT_MAX_FOOD;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_MIN_STRENGTH;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_MAX_STRENGTH;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_ATTACK_DURATION;

import ch.epfl.moocprog.utils.Time;

public final class AntWorker extends Ant{
    private double foodQuantity=0.0;
    public AntWorker(ToricPosition position, Uid AnthillId) {
        super(position,getConfig().getInt(ANT_WORKER_HP),getConfig().getTime(ANT_WORKER_LIFESPAN),AnthillId);
    }
    public AntWorker(ToricPosition position, int hitpoints , Time lifespan , Uid AnthillId){
        super(position, hitpoints, lifespan, AnthillId);
    }
    public AntWorker(ToricPosition position, Uid AnthillId,AntRotationProbabilityModel probModel){
        super(position,getConfig().getInt(ANT_WORKER_HP),getConfig().getTime(ANT_WORKER_LIFESPAN),AnthillId,probModel);
    }
    
    
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }
    
    public double getSpeed() {
        return getConfig().getDouble(ANT_WORKER_SPEED);
    }
    public double getFoodQuantity(){
        return this.foodQuantity;
    }
    protected void seekForFood(AntWorkerEnvironmentView env, Time dt){
        this.move(env , dt);
        if(env.getClosestFoodForAnt(this)!= null && this.foodQuantity == 0.0){
            double quantity = env.getClosestFoodForAnt(this).takeQuantity(getConfig().getDouble(ANT_MAX_FOOD));
            this.foodQuantity = quantity;
            this.makeTurn();
        }
        if(env.dropFood(this) && this.foodQuantity != 0.0){
            this.foodQuantity = 0.0;
            this.makeTurn();
        }
    }
    
    protected void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectSpecificBehaviorDispatch(this, dt);
    }
    public String toString(){
        return super.toString()+"\nQuantity : "+this.foodQuantity;
    }
    
    int getMinAttackStrength() {
        return getConfig().getInt(ANT_WORKER_MIN_STRENGTH);
    }
    
    int getMaxAttackStrength() {
        return getConfig().getInt(ANT_WORKER_MAX_STRENGTH);
    }
    
    Time getMaxAttackDuration() {
        return getConfig().getTime(ANT_WORKER_ATTACK_DURATION);
    }

    
}
