package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;
import ch.epfl.moocprog.utils.Vec2d;
import ch.epfl.moocprog.random.UniformDistribution;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANIMAL_LIFESPAN_DECREASE_FACTOR;
import static ch.epfl.moocprog.config.Config.ANIMAL_NEXT_ROTATION_DELAY;
abstract public class Animal extends Positionable{
    private double angle;
    private int hitpoints;
    private Time lifespan;
    private Time rotationDelay = Time.ZERO;
    private Time attackDuration;
    abstract public void accept(AnimalVisitor visitor, RenderingMedia s);
    abstract public double getSpeed();
    protected abstract void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt);
    protected abstract RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env);
    protected abstract void afterMoveDispatch(AnimalEnvironmentView env, Time dt);
    abstract boolean isEnemy(Animal entity);
    abstract boolean isEnemyDispatch(Termite other);
    abstract boolean isEnemyDispatch(Ant other);
    abstract int getMinAttackStrength();
    abstract int getMaxAttackStrength();
    abstract Time getMaxAttackDuration();
    public enum State {IDLE, ESCAPING, ATTACK};
    private State state;
    public Animal(ToricPosition position){
        super(position);
    }
    public Animal(ToricPosition Position,int HitPoints,Time lifespan){
        super(Position);
        this.angle=UniformDistribution.getValue(0.0, 2*Math.PI);
        this.hitpoints = HitPoints;
        this.lifespan = lifespan;
        this.attackDuration = Time.ZERO;
        this.state = State.IDLE;
    }
    public Time getattackDuration(){
        return this.attackDuration;
    }
    public final double getDirection(){
        return this.angle;
    }
    public final void setDirection(double angle){
        this.angle = angle;
    }
    public final int getHitpoints(){
        return this.hitpoints;
    }
    public final Time getLifespan(){
        return this.lifespan;
    }
    public final boolean isDead(){
        if(this.hitpoints<=0 || this.lifespan.toMilliseconds()<=0){
            return true;
        }
        return false;
    }
    public final void update(AnimalEnvironmentView env, Time dt){
        this.lifespan = getLifespan().minus(dt.times(getConfig().getDouble(ANIMAL_LIFESPAN_DECREASE_FACTOR)));
        if(this.getState().equals(State.ATTACK)){
            if(this.canAttack()){
                this.fight(env, dt);
            }else{
                this.setState(State.ESCAPING);
                this.attackDuration = Time.ZERO;
            }
        }else if(this.getState().equals(State.ESCAPING)){
            this.escape(env, dt);
        }else if(!isDead()){
            this.specificBehaviorDispatch(env, dt);
        }
    }
    public final boolean canAttack(){
        if(!this.getState().equals(State.ESCAPING) && this.attackDuration.compareTo(getMaxAttackDuration())<=0){
            return true;
        }
        return false;
    }
    private final void escape(AnimalEnvironmentView env, Time dt){
        this.move(env, dt);
        if(!env.isVisibleFromEnemies(this)){
            this.setState(State.IDLE);
        }
    }
    public final void fight(AnimalEnvironmentView env, Time dt){
        Animal ennemy = Utils.closestFromPoint(this, env.getVisibleEnemiesForAnimal(this));
        if(ennemy != null){
            double attackStrength = UniformDistribution.getValue(getMinAttackStrength(), getMaxAttackStrength());
            ennemy.setState(State.ATTACK);
            this.setState(State.ATTACK);
            ennemy.hitpoints -= attackStrength;
            this.attackDuration = this.attackDuration.plus(dt);
        }else{
            this.attackDuration = Time.ZERO;
            if(this.getState().equals(State.ATTACK)){
                this.setState(State.ESCAPING);
            }
        }
    }
    protected final void move(AnimalEnvironmentView env,Time dt){
        this.rotationDelay = this.rotationDelay.plus(dt);
        Time Delay = getConfig().getTime(ANIMAL_NEXT_ROTATION_DELAY);
        if(!this.isDead()){
            while(this.rotationDelay.compareTo(Delay)>=0){
                Rotate(env);
                this.rotationDelay = this.rotationDelay.minus(Delay);
            }
        }
        setPosition(this.getPosition().add(Vec2d.fromAngle(this.angle).scalarProduct(dt.toSeconds()*(getSpeed()))));
        afterMoveDispatch(env, dt);
    }
    private void Rotate(AnimalEnvironmentView env){
        this.angle += Utils.pickValue(this.computeRotationProbsDispatch(env).getAngles(),this.computeRotationProbsDispatch(env).getProbabilities());
    }
    public void makeTurn(){
        double tempangle = getDirection();
        if(tempangle + Math.PI > 2*Math.PI){
            this.setDirection(tempangle-Math.PI);
        }else{
            this.setDirection(tempangle+Math.PI);
        }
    }
    protected final RotationProbability computeDefaultRotationProbs(){
        double[] anglesinDegree = {-180.0,-100.0,-55.0,-25.0,-10.0,0.0,10.0,25.0,55.0,100.0,180.0};
        double[] Probabilities = {0.0000,0.0000,0.0005,0.0010,0.0050,0.9870,0.0050,0.0010,0.0005,0.0000,0.0000};
        double[] AnglesinRadians = new double[anglesinDegree.length];
        for(int i=0;i<anglesinDegree.length;++i){
            AnglesinRadians[i] = Math.toRadians(anglesinDegree[i]);
        }
        //AnglesinRadians = AnglesinRadians.clone();
        return new RotationProbability(AnglesinRadians, Probabilities);
    }
    public final State getState(){
        return this.state;
    }
    public final void setState(State newState){
        this.state = newState;
    }
    public String toString(){
        return "Position : "+this.getPosition().toVec2d().toString()+"\nSpeed : "+this.getSpeed()+"\nHitPoints : "+this.hitpoints+"\nLifeSpan : "+this.lifespan.toMilliseconds()+"\nState : "+this.getState();
    }
}
