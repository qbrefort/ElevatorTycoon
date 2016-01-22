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

    public Simulation(List<Folk> waiting_folk,Elevator elevator) {
        this.folks = waiting_folk;
        this.elevator = elevator;
        this.iteration = 0;
    }

    public int getFloor_elevator(){
        return this.elevator.getFloor();
    }

    public void iterate(){
        Log.i("Iteration", "" + iteration);
        this.elevator.print_Floor_queue();
        this.elevator.where_to_move();



        for(int i=0;i<folks.size();i++){
            Log.i("Folk treated:", "" + folks.get(i).getName());
            folks.get(i).goInElevator(this.elevator);
            folks.get(i).goOutElevator(this.elevator);
            if(folks.get(i).isTreated()){
                Log.i("OUT", folks.get(i).getName());
            }
        }
        //SystemClock.sleep(1000);




        this.iteration = this.iteration + 1;
    }
}
