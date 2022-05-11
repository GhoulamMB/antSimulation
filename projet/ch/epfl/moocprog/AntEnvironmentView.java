package ch.epfl.moocprog;

public interface AntEnvironmentView extends AnimalEnvironmentView{
    public void addPheromone(Pheromone pheromone);
    public double[] getPheromoneQuantitiesPerIntervalForAnt(ToricPosition position,double directionAngleRad, double[] angles);
}
