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




    public Elevator() {
        this.capacity = 10;
        this.max_level = 10;
        this.nb_person_in = 0;
        this.floor = 0;
        this.floor_queue = new boolean[this.max_level];
        for (int i = 0 ;i<this.floor_queue.length; i++){
            this.floor_queue[i] = false;
        }
    }

    public int getFloor(){
        return this.floor;
    }

    public void add_person(){
        Log.i("Elevator","Folk added");
        this.nb_person_in = this.nb_person_in + 1;

    }

    public void remove_person(){
        Log.i("Elevator","Folk removed");
        this.nb_person_in = this.nb_person_in - 1;

    }

    public void add_requested_floor(int floor){
        Log.i("Elevator","Floor " + floor + " requested");
        this.floor_queue[floor] = true;

    }

    public void remove_requested_floor(int floor){
        Log.i("Elevator","Floor " + floor + " reached and removed");
        this.floor_queue[floor] = false;

    }

    public void move_to(int floor){
        Log.i("Elevator", "decided to move to " + floor);
        this.floor = floor;
        remove_requested_floor(floor);
    }

    public void where_to_move(){
        int go = 20;
        List<Integer> tmp_list = new ArrayList<>();
        for(int i=0;i<this.floor_queue.length;i++){
            if(this.floor_queue[i]){
                tmp_list.add(i);
            }
        }
        for(int i=0; i <tmp_list.size(); i++){
            int temp = Math.abs(this.floor - tmp_list.get(i));
            if(temp  < go ){
                go = tmp_list.get(i);
            }
        }
        if(go!=20)
            move_to(go);
    }

}
