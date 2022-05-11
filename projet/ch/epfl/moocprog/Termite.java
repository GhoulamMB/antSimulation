package ch.epfl.moocprog;
import ch.epfl.moocprog.utils.Time;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.TERMITE_SPEED;
import static ch.epfl.moocprog.config.Config.TERMITE_HP;
import static ch.epfl.moocprog.config.Config.TERMITE_LIFESPAN;
import static ch.epfl.moocprog.config.Config.TERMITE_MIN_STRENGTH;
import static ch.epfl.moocprog.config.Config.TERMITE_MAX_STRENGTH;
import static ch.epfl.moocprog.config.Config.TERMITE_ATTACK_DURATION;
public final class Termite extends Animal{

    public Termite(ToricPosition position) {
        super(position, getConfig().getInt(TERMITE_HP), getConfig().getTime(TERMITE_LIFESPAN));
    }
    
    protected void seekForEnemies(AnimalEnvironmentView env , Time dt){
        this.move(env , dt);
        this.fight(env, dt);
    }
    
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }

    
    public double getSpeed() {
        return getConfig().getDouble(TERMITE_SPEED);
    }

    
	public void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
		env.selectSpecificBehaviorDispatch(this, dt);
	}
    
    protected RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env) {
        return env.selectComputeRotationProbsDispatch(this);
    }

    
    protected void afterMoveDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectAfterMoveDispatch(this, dt);
    }
    public RotationProbability computeRotationProbs(TermiteEnvironmentView env){
        return this.computeDefaultRotationProbs();
    }
    public void afterMoveTermite(TermiteEnvironmentView env,Time dt){
        
    }

    
    public
    boolean isEnemy(Animal entity) {
        return !this.isDead() && !entity.isDead() && entity.isEnemyDispatch(this) ;
    }

    
    boolean isEnemyDispatch(Termite other) {
        return false;
    }

    
    boolean isEnemyDispatch(Ant other) {
        return true;
    }
    
    public int getMinAttackStrength() {
        return getConfig().getInt(TERMITE_MIN_STRENGTH);
    }
    
    public int getMaxAttackStrength() {
        return getConfig().getInt(TERMITE_MAX_STRENGTH);
    }
    
    public Time getMaxAttackDuration() {
        return getConfig().getTime(TERMITE_ATTACK_DURATION);
    }
}
