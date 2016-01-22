package com.pgm.qbr.elevatortycoon;

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

    public Folk(Elevator elevator,String name) {
        Random rand = new Random();
        this.name = name;
        this.wanted_floor =  rand.nextInt(10);
        this.current_floor =  rand.nextInt(10);
        while (this.current_floor == this.wanted_floor){
            this.wanted_floor = rand.nextInt(10);
        }
        this.waiting = true;
        this.isIn = false;
        this.treated = false;
        request_elevator(elevator);
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

    public void request_elevator(Elevator elevator){
        elevator.add_requested_floor(this.current_floor);
    }

    public void goInElevator(Elevator elevator){
        if(this.waiting && this.current_floor == elevator.getFloor()){
            Log.i("IN: ",this.name+" AND request"+this.wanted_floor);
            elevator.add_person();
            elevator.add_requested_floor(this.wanted_floor);
            this.waiting = false;
            this.isIn = true;
        }else{
            this.waiting = true;
        }
    }

    public void goOutElevator(Elevator elevator){
        if(this.isIn && this.wanted_floor == elevator.getFloor()){
            elevator.remove_person();
            this.isIn = false;
            this.treated = true;
        }else{
            this.isIn = true;
        }
    }

    public boolean isTreated(){
        return this.treated;
    }
    public boolean isWaiting(){
        return this.waiting;
    }

}
