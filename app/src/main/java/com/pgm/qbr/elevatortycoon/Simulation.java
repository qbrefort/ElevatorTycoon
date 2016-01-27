package com.pgm.qbr.elevatortycoon;

import android.util.Log;
import java.util.List;

/**
 * Created by qbr on 20/01/16.
 */

public class Simulation {

    private int iteration;
    private Elevator[] elevators;
    private List<Folk> folks;
    private MainActivity mainActivity;

    private int cash;
    private int up_capacity_price;

    public Simulation(MainActivity ma, List<Folk> waiting_folk, Elevator[] elevators) {
        this.folks = waiting_folk;
        this.elevators = elevators;
        this.iteration = 0;
        this.mainActivity = ma;
        this.cash = 0;
        this.up_capacity_price = 50;
    }

    public List<Folk> getFolks() {
        return folks;
    }
    public void setFolks(List<Folk> folks) {
        this.folks = folks;
    }

    public int getFloor_elevator(int i) {
        return this.elevators[i].getFloor();
    }

    public void iterate(int iter) {
        switch (iter) {
            case 0:
                Log.i("Iteration", "" + iteration);
                this.elevators[0].where_to_move();
                this.elevators[1].where_to_move();
                this.iteration = this.iteration + 1;
                break;
            case 1:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goOutElevator();
                    if (folks.get(i).isTreated()) {
                        Log.i("OUT", folks.get(i).getName());
                        operateCash(2);
                        this.mainActivity.update_data();
                        this.mainActivity.repaint();
                        //folks.remove(i);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goInElevator(elevators);
                }
                break;
        }
    }

    public int getCash() {
        return cash;
    }

    public boolean canAffordUpdateCapacity(){
        if(this.cash>= this.up_capacity_price)
            return true;
        else
            return false;
    }

    public int getUp_capacity_price(){
        return this.up_capacity_price;
    }

    public void updateCapacityPrice(){
        this.cash -= this.up_capacity_price;
        double res = this.up_capacity_price*1.2;
        this.up_capacity_price = (int) res;
    }

    public void operateCash(int cash) {
        this.cash += cash;
    }
}
