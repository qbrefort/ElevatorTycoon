package com.pgm.qbr.elevatortycoon;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qbr on 20/01/16.
 */
public class Elevator {

    private int capacity;
    private int max_level;
    private int floor;
    private int nb_person_in;
    private boolean[] floor_queue;
    private MainActivity mainActivity;

    public boolean[] getFloor_queue() {
        return floor_queue;
    }
    public void print_Floor_queue(){
        String queue="";
        for(int i=0;i<this.getFloor_queue().length;i++){
            if(this.floor_queue[i]){
                queue=queue+i+" ";
            }
        }
        Log.i("Queue",queue);
    }




    public Elevator(MainActivity ma) {
        this.capacity = 10;
        this.max_level = 10;
        this.nb_person_in = 0;
        this.floor = 0;
        this.floor_queue = new boolean[this.max_level];
        for (int i = 0 ;i<this.floor_queue.length; i++){
            this.floor_queue[i] = false;
        }
        this.mainActivity = ma;
    }

    public int getFloor(){
        return this.floor;
    }

    public void add_person(){
        this.nb_person_in = this.nb_person_in + 1;

    }

    public void remove_person(){
        this.nb_person_in = this.nb_person_in - 1;

    }

    public void add_requested_floor(int floor){
        this.floor_queue[floor] = true;

    }

    public void remove_requested_floor(int floor){
        this.floor_queue[floor] = false;

    }

    public void move_to(int floor){
        Log.i("Elevator", "decided to move from" +this.floor+" to "+ floor);
        this.floor = floor;
        remove_requested_floor(floor);
    }

    public void where_to_move(){
        print_Floor_queue();
        double go = 20;
        List<Integer> tmp_list = new ArrayList<>();
        for(int i=0;i<this.floor_queue.length;i++){
            if(this.floor_queue[i]){
                tmp_list.add(i);
            }
        }

        for(int i=0; i <tmp_list.size(); i++){

            double temp = (double) this.floor - (double) tmp_list.get(i);
            temp = Math.abs( temp);
            Log.i("Distance",Double.toString(temp));
            if(temp  < go ){
                go = tmp_list.get(i);
                Log.i("Distance chosen",Double.toString(temp));
            }
        }
        if(go!=20) {
            move_to((int) go);
        }
    }

}
