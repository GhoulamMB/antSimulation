package ch.epfl.moocprog;
import ch.epfl.moocprog.utils.Time;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.*;
import ch.epfl.moocprog.random.NormalDistribution;
import ch.epfl.moocprog.random.UniformDistribution;

public final class FoodGenerator {
    private Time time;
    private static final double w = getConfig().getInt(WORLD_WIDTH);
    private static final double h = getConfig().getInt(WORLD_HEIGHT);

    public FoodGenerator(Time dt) {
        this.time = time.plus(dt);
    }
    public FoodGenerator() {
        this.time = Time.ZERO;
    }
    public void update(FoodGeneratorEnvironmentView env, Time dt){
        this.time = this.time.plus(dt);
        double x = NormalDistribution.getValue(w/2.0, w*w/16.0);
        double y = NormalDistribution.getValue(h/2.0, h*h/16.0);
        double max = getConfig().getDouble(NEW_FOOD_QUANTITY_MAX);
        double min = getConfig().getDouble(NEW_FOOD_QUANTITY_MIN);
        double quantite = UniformDistribution.getValue(min, max);
        final Time delay = getConfig().getTime(FOOD_GENERATOR_DELAY);
        while(this.time.compareTo(delay) >= 0){
            this.time = this.time.minus(delay);
            Food food = new Food(new ToricPosition(x,y), quantite);
            env.addFood(food);
        }
    }
}
