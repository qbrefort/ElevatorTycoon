package com.pgm.qbr.elevatortycoon;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbr on 20/01/16.
 */
public class Simulation {

    private int iteration;
    private Elevator elevator;
    private List<Folk> folks;
    private MainActivity mainActivity;

    public Simulation(MainActivity ma, List<Folk> waiting_folk, Elevator elevator) {
        this.folks = waiting_folk;
        this.elevator = elevator;
        this.iteration = 0;
        this.mainActivity = ma;
    }

    public List<Folk> getFolks() {
        return folks;
    }

    public int getFloor_elevator() {
        return this.elevator.getFloor();
    }

    public void iterate(int iter) {
        switch (iter) {
            case 0:
                Log.i("Iteration", "" + iteration);
                this.elevator.where_to_move();
                this.iteration = this.iteration + 1;
                break;
            case 1:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goInElevator(this.elevator);
                }
                break;
            case 2:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goOutElevator(this.elevator);
                    if (folks.get(i).isTreated()) {
                        Log.i("OUT", folks.get(i).getName());
                        this.mainActivity.repaint();
                        //folks.remove(i);
                    }
                }
                break;
        }
    }
}
