package ch.epfl.moocprog;

import java.util.List;

import ch.epfl.moocprog.utils.Time;
public interface AnimalEnvironmentView {
    void selectSpecificBehaviorDispatch(AntWorker antWorker, Time dt);
    void selectSpecificBehaviorDispatch(AntSoldier antSoldier, Time dt);
    void selectSpecificBehaviorDispatch(Termite termite, Time dt);
    RotationProbability selectComputeRotationProbsDispatch(Ant ant);
    RotationProbability selectComputeRotationProbsDispatch(Termite termite);
    void selectAfterMoveDispatch(Ant ant, Time dt);
    void selectAfterMoveDispatch(Termite termite, Time dt);
    List<Animal> getVisibleEnemiesForAnimal(Animal from);
	public boolean isVisibleFromEnemies(Animal from);
}
