package ConfigClasses;

import java.util.ArrayList;
import java.util.List;

public final class DifficultyLevel {

    private List<Integer> healthPoint = new ArrayList<>();
    private  List<Float> gravityForce = new ArrayList<>();
    private  List<Long> amountOfFuel = new ArrayList<>();
    private  List<Long>  basicPointsForLevel = new ArrayList<>() ;

    public List<Integer> getHealthPoint() {
        return healthPoint;
    }

    public List<Float> getGravityForce() {
        return gravityForce;
    }

    public List<Long> getAmountOfFuel() {
        return amountOfFuel;
    }

    public List<Long> getBasicPointsForLevel() {
        return basicPointsForLevel;
    }
}
