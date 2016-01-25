package com.pgm.qbr.elevatortycoon;

import android.app.Activity;
import android.util.Log;

import java.util.Random;

/**
 * Created by qbr on 20/01/16.
 */
public class Folk {

    private String name;
    private int wanted_floor;
    private int current_floor;
    private boolean waiting;
    private boolean isIn;
    private boolean treated;
    private MainActivity mainActivity;

    public Folk(MainActivity mA, Elevator[] elevators,String name) {
        Random rand = new Random();
        this.name = name;
        this.wanted_floor =  rand.nextInt(11);
        this.current_floor =  rand.nextInt(11);
        while (this.current_floor == this.wanted_floor){
            this.wanted_floor = rand.nextInt(11);
        }
        this.waiting = true;
        this.isIn = false;
        this.treated = false;
        this.mainActivity = mA;
        this.request_elevator(elevators);
    }

    public String getName(){
        return this.name;
    }

    public int getCurrent_floor() {
        return current_floor;
    }
    public int getWanted_floor() {
        return wanted_floor;
    }

    public void request_elevator(Elevator[] elevators){
        elevators[0].add_requested_floor(this.current_floor);
        elevators[1].add_requested_floor(this.current_floor);
    }

    public void goInElevator(Elevator elevator){
        if(this.waiting && !elevator.isFull() && this.current_floor == elevator.getFloor()){
            Log.i("IN: ", this.name + " AND requested " + this.wanted_floor);
            elevator.add_person();
            elevator.add_requested_floor(this.wanted_floor);
            this.waiting = false;
            this.isIn = true;
        }
    }

    public void goOutElevator(Elevator elevator){
        if(this.isIn && this.wanted_floor == elevator.getFloor()){
            elevator.remove_person();
            this.isIn = false;
            this.treated = true;
        }
    }

    public boolean isTreated(){
        return this.treated;
    }
    public boolean isWaiting(){
        return (this.waiting);
    }

}
