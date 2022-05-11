package ch.epfl.moocprog;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_HP;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_LIFESPAN;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_SPEED;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_MIN_STRENGTH;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_MAX_STRENGTH;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_ATTACK_DURATION;

import ch.epfl.moocprog.utils.Time;

public final class AntSoldier extends Ant{

    public AntSoldier(ToricPosition position, Uid AnthillId) {
        super(position,getConfig().getInt(ANT_SOLDIER_HP),getConfig().getTime(ANT_SOLDIER_LIFESPAN),AnthillId);
    }
    public AntSoldier(ToricPosition position, int hitpoints , Time lifespan , Uid AnthillId){
        super(position, hitpoints, lifespan, AnthillId);
    }
    public AntSoldier(ToricPosition position, Uid AnthillId,AntRotationProbabilityModel probModel){
        super(position,getConfig().getInt(ANT_SOLDIER_HP),getConfig().getTime(ANT_SOLDIER_LIFESPAN),AnthillId,probModel);
    }
    
    
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
        
    }

    
    public double getSpeed() {
        return getConfig().getDouble(ANT_SOLDIER_SPEED);
    }
    protected void seekForEnemies(AntEnvironmentView env, Time dt){
        this.move(env , dt);
        this.fight(env, dt);
    }

    
    protected void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectSpecificBehaviorDispatch(this, dt);
    }
    
    int getMinAttackStrength() {
        return getConfig().getInt(ANT_SOLDIER_MIN_STRENGTH);
    }
    
    int getMaxAttackStrength() {
        return getConfig().getInt(ANT_SOLDIER_MAX_STRENGTH);
    }
    
    Time getMaxAttackDuration() {
        return getConfig().getTime(ANT_SOLDIER_ATTACK_DURATION);
    }
}
