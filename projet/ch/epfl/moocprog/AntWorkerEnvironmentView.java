package ch.epfl.moocprog;

public interface AntWorkerEnvironmentView extends AntEnvironmentView{
    public Food getClosestFoodForAnt(AntWorker antWorker);
    public boolean dropFood(AntWorker antWorker);
}
