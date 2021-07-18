package shared.model.troops.timerTasks;

import shared.model.troops.Troop;

import java.util.ArrayList;
import java.util.TimerTask;

public class RageTask extends TimerTask {
    private ArrayList<Troop> changedTroops;
    private double coefficientForMultiply;

    public RageTask(ArrayList<Troop> changedTroops , double coefficientForMultiply){
        this.changedTroops = changedTroops;
        this.coefficientForMultiply = coefficientForMultiply;
    }


    @Override
    public void run() {
        for (Troop troop:changedTroops)
        {
            troop.setMovingSpeed(troop.getMovingSpeed().getValue() * coefficientForMultiply);
            troop.setDamage((int)(troop.getDamage() * coefficientForMultiply));
            troop.setHitSpeed(troop.getHitSpeed() * coefficientForMultiply);
        }
    }
}
