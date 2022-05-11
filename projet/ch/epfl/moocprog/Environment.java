package ch.epfl.moocprog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_MAX_PERCEPTION_DISTANCE;
import static ch.epfl.moocprog.config.Config.ANT_SMELL_MAX_DISTANCE;
import static ch.epfl.moocprog.config.Config.ANIMAL_SIGHT_DISTANCE;
import static ch.epfl.moocprog.config.Config.*;
import ch.epfl.moocprog.gfx.EnvironmentRenderer;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;
import ch.epfl.moocprog.utils.Vec2d;


public final class Environment implements FoodGeneratorEnvironmentView, AnimalEnvironmentView, AnthillEnvironmentView,
AntEnvironmentView, AntWorkerEnvironmentView , TermiteEnvironmentView{
    private FoodGenerator fgen;
    private LinkedList<Food> foods;
    private List<Animal> animalsList = new LinkedList<Animal>();
    private List<Anthill> anthillList = new LinkedList<Anthill>();
    private List<Pheromone> pheromoneList = new LinkedList<Pheromone>();
    public Environment(){
        this.fgen = new FoodGenerator();
        this.foods = new LinkedList<Food>();
    }
    
    public void addFood(Food food) {
        if(food == null){
            throw new IllegalArgumentException();
        }else{
            this.foods.add(food);
        }
    }
    public List<Double> getFoodQuantities(){
        ArrayList<Double> tmp = new ArrayList<Double>();
        for(Food i:this.foods){
            tmp.add(i.getQuantity());
        }
        return tmp;
    }
    public void update(Time dt){
        fgen.update(this, dt);
        Iterator<Animal> iter = this.animalsList.iterator();
        Iterator<Pheromone> iterP = this.pheromoneList.iterator();
        while(iterP.hasNext()){
            Pheromone Phero = iterP.next();
            if(Phero.isNegligible()){
                iterP.remove();
            }else{
                Phero.update(dt);
            }
        }
		while (iter.hasNext()) {
			Animal a = iter.next();
			if (a.isDead()) {
                iter.remove();
            }
			else {
                a.update(this, dt);
            }
		}
        for(Anthill a : anthillList){
            a.update(this, dt);
        }
        
        foods.removeIf(food -> food.getQuantity() <= 0);
    }
    public List<ToricPosition> getAnimalsPosition(){
        List<ToricPosition> postions = new ArrayList<ToricPosition>();
        for(Animal a : this.animalsList){
            postions.add(a.getPosition());
        }
        return postions;
    }
    public void renderEntities(EnvironmentRenderer environmentRenderer){
        foods.forEach(environmentRenderer::renderFood);
        animalsList.forEach(environmentRenderer::renderAnimal);
        anthillList.forEach(environmentRenderer::renderAnthill);
        pheromoneList.forEach(environmentRenderer::renderPheromone);
    }
    public int getWidth(){
        return getConfig().getInt(WORLD_WIDTH);
    }
    public int getHeight(){
        return getConfig().getInt(WORLD_HEIGHT);
    }
    public void addAnimal(Animal animal){
        Utils.requireNonNull(animal);
        animalsList.add(animal);
    }
    public void addAnthill(Anthill anthill){
        Utils.requireNonNull(anthill);
        anthillList.add(anthill);
    }
    
    public void addAnt(Ant ant){
        Utils.requireNonNull(ant);
        this.addAnimal(ant);
    }
    
    public Food getClosestFoodForAnt(AntWorker antWorker) {
        Utils.requireNonNull(antWorker);
        Food closestFood = Utils.closestFromPoint(antWorker, this.foods);
        if(closestFood == null || (closestFood.getPosition().toricDistance(antWorker.getPosition()) > getConfig().getDouble(ANT_MAX_PERCEPTION_DISTANCE))){
            return null;
        }
        return closestFood;
    }
    
    public boolean dropFood(AntWorker antWorker) {
        Utils.requireNonNull(antWorker);
        Anthill anthil = this.getAnthillByID(antWorker.getAnthillId());
        if(anthil == null ||(anthil.getPosition().toricDistance(antWorker.getPosition())> getConfig().getDouble(ANT_MAX_PERCEPTION_DISTANCE))){
            return false;
        }
        anthil.dropFood(antWorker.getFoodQuantity());
        return true;
    }
    protected Anthill getAnthillByID(Uid id){
        Utils.requireNonNull(id);
        for(Anthill a : this.anthillList){
            if(a.getAnthillId().equals(id)){
                return a;
            }
        }
        return null;
    }
    
    public void selectSpecificBehaviorDispatch(AntWorker antWorker, Time dt) {
        antWorker.seekForFood(this, dt);
    }
    
    public void selectSpecificBehaviorDispatch(AntSoldier antSoldier, Time dt) {
        antSoldier.seekForEnemies(this, dt);
    }
    
    public void selectSpecificBehaviorDispatch(Termite termite, Time dt) {
        termite.seekForEnemies(this, dt);
    }
    
    public void addPheromone(Pheromone pheromone) {
        Utils.requireNonNull(pheromone);
        this.pheromoneList.add(pheromone);
    }
    public List<Double> getPheromonesQuantities(){
        List<Double> PheromonesQuantities = new LinkedList<Double>();
        for(Pheromone p : pheromoneList){
            PheromonesQuantities.add(p.getQuantity());
        }
        return PheromonesQuantities;
    }
    private static double normalizedAngle(double angle){
        double newangle = angle;
        while(newangle<0.0){
            newangle += (2*Math.PI);
        }
        while(newangle> 2*Math.PI){
            newangle -= (2*Math.PI);
        }
        return newangle;
    }
    private static double closestAngleFrom(double angle, double target){
        double diff = angle - target;
        diff = Environment.normalizedAngle(diff);
        double otherdiff = (2*Math.PI)-diff;
        if(diff>otherdiff){
            return otherdiff;
        }else{
            return diff;
        }
    }
    
    public double[] getPheromoneQuantitiesPerIntervalForAnt(ToricPosition position, double directionAngleRad,
            double[] angles) {
        if(angles == null || position == null){
            throw new IllegalArgumentException();
        }
        double[] T = new double[angles.length];
        for(Pheromone P : pheromoneList){
            if(!P.isNegligible() && P.getPosition().toricDistance(position)<=getConfig().getDouble(ANT_SMELL_MAX_DISTANCE)){
                Vec2d v = position.toricVector(P.getPosition());
                double beta = v.angle() - directionAngleRad;

                int closestPos = 0;
                for(int i=0;i<angles.length;++i){
                    if(closestAngleFrom(angles[i], beta) < closestAngleFrom(angles[closestPos], beta)){
                        closestPos = i;
                    }
                }
                T[closestPos] += P.getQuantity();
            }
        }
        return T;
    }
    
    public RotationProbability selectComputeRotationProbsDispatch(Ant ant) {
        return ant.computeRotationProbs(this);
    }
    
    public void selectAfterMoveDispatch(Ant ant, Time dt) {
        ant.afterMoveAnt(this, dt);
    }
    
    public void selectAfterMoveDispatch(Termite termite, Time dt) {
        termite.afterMoveTermite(this, dt);
    }
    
    public RotationProbability selectComputeRotationProbsDispatch(Termite termite) {
        return termite.computeRotationProbs(this);
    }
    
    public List<Animal> getVisibleEnemiesForAnimal(Animal from) {
        Utils.requireNonNull(from);
        double sight = getConfig().getDouble(ANIMAL_SIGHT_DISTANCE);
        return this.animalsList.stream().filter(a -> (a.getPosition().toricDistance(from.getPosition())<=sight) && a.isEnemy(from))
                                                    .collect(Collectors.toList());
    }
    public boolean isVisibleFromEnemies(Animal from){
        Utils.requireNonNull(from);
        for(Animal a : animalsList){
            if(this.getVisibleEnemiesForAnimal(a).contains(from)){
                return true;
            }
        }
        return false;
    }
}
