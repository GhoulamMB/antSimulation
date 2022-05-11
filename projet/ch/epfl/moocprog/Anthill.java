package ch.epfl.moocprog;
import ch.epfl.moocprog.random.UniformDistribution;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANTHILL_WORKER_PROB_DEFAULT;
import static ch.epfl.moocprog.config.Config.ANTHILL_SPAWN_DELAY;

import ch.epfl.moocprog.utils.Time;
public final class Anthill extends Positionable{
    private final Time DELAY = getConfig().getTime(ANTHILL_SPAWN_DELAY);
    private Time time = Time.ZERO;
    private double foodStock = 0.0;
    private Uid anthillID;
    private double probabilitytoSeeAntWorker;
    public Anthill(ToricPosition toric,double probabilitytoSeeAntWorker){
        super(toric);
        this.probabilitytoSeeAntWorker = probabilitytoSeeAntWorker;
        this.anthillID = Uid.createUid();
    }
    public Anthill(ToricPosition toric){
        super(toric);
        this.probabilitytoSeeAntWorker = getConfig().getDouble(ANTHILL_WORKER_PROB_DEFAULT);
        this.anthillID = Uid.createUid();
    }
    public void dropFood(double toDrop){
        if(toDrop<0){
            throw new IllegalArgumentException();
        }else{
            this.foodStock += toDrop;
        }
    }
    public void update(AnthillEnvironmentView env, Time dt){
        this.time = this.time.plus(dt);
        while(this.time.compareTo(DELAY)>= 0.0){
            double random = UniformDistribution.getValue(0.0, 1.0);
            if(random<= this.probabilitytoSeeAntWorker){
                AntWorker antWorker = new AntWorker(this.getPosition(), this.anthillID);
                env.addAnt(antWorker);
            }else{
                AntSoldier antSoldier = new AntSoldier(this.getPosition(), this.anthillID);
                env.addAnt(antSoldier);
            }
            this.time = this.time.minus(DELAY);
        }
    }
    public double getQuantity(){
        return 0.0;
    }
    public double getFoodQuantity(){
        return this.foodStock;
    }
    public Uid getAnthillId(){
        return this.anthillID;
    }
    public String toString(){
        double X = this.getPosition().toVec2d().getX();
        double Y = this.getPosition().toVec2d().getY();
        return "Position : "+X+", "+Y+"\nQuantity : "+this.foodStock;
    }
}
