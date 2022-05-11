package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_PHEROMONE_DENSITY;
import static ch.epfl.moocprog.config.Config.ANT_PHEROMONE_ENERGY;
public abstract class Ant extends Animal{
    private Uid AnthillId;
    private ToricPosition lastPos;
    private AntRotationProbabilityModel probModel;
    public Ant(ToricPosition position,int hitpoints,Time lifespan,Uid AnthillId) {
        super(position,hitpoints,lifespan);
        this.AnthillId = AnthillId;
        this.lastPos = new ToricPosition(position.toVec2d().getX(), position.toVec2d().getY());
        this.probModel = new PheromoneRotationProbabilityModel();
    }
    public Ant(ToricPosition position,int hitpoints,Time lifespan,Uid AnthillId, AntRotationProbabilityModel probModel) {
        super(position,hitpoints,lifespan);
        this.AnthillId = AnthillId;
        this.lastPos = new ToricPosition(position.toVec2d().getX(), position.toVec2d().getY());
        this.probModel = probModel;
    }
    public final Uid getAnthillId(){
        return this.AnthillId;
    }
    private final void spreadPheromones(AntEnvironmentView env) {
		double densite = getConfig().getDouble(ANT_PHEROMONE_DENSITY);
		ToricPosition currentPos = this.getPosition();
		double d = lastPos.toricDistance(currentPos);
		int numPheros = (int) (d * densite);
		double step = d / numPheros;
		Vec2d direction = this.lastPos.toricVector(currentPos).normalized();
		for (int i = 0; i < numPheros; ++i) {
			this.lastPos = this.lastPos.add(direction.scalarProduct(step));
			env.addPheromone(new Pheromone(this.lastPos, getConfig().getDouble(ANT_PHEROMONE_ENERGY))); 

		}
	}
    
    protected final RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env) {
        return env.selectComputeRotationProbsDispatch(this);
    }
    
    protected final void afterMoveDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectAfterMoveDispatch(this, dt);
    }
    protected final RotationProbability computeRotationProbs(AntEnvironmentView env){
        return probModel.computeRotationProbs(computeDefaultRotationProbs(), getPosition(), getDirection(), env);
    }
    protected final void afterMoveAnt(AntEnvironmentView env, Time dt){
        this.spreadPheromones(env);
    }
    
    final boolean isEnemy(Animal entity) {
        return !this.isDead() && !entity.isDead() && entity.isEnemyDispatch(this);
    }
    
    final boolean isEnemyDispatch(Ant other) {
        return !other.AnthillId.equals(this.AnthillId);
    }
    
    final boolean isEnemyDispatch(Termite other) {
        return true;
    }
    public String toString(){
        return super.toString();
    }
}
