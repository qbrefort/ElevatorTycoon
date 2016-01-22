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
    private int[] floor_queue;
    private MainActivity mainActivity;

    public int[] getFloor_queue() {
        return floor_queue;
    }
    public void print_Floor_queue(){
        String queue="";
        for(int i=0;i<this.getFloor_queue().length;i++){
            if(this.floor_queue[i]>0){
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
        this.floor_queue = new int[this.max_level];
        for (int i = 0 ;i<this.floor_queue.length; i++){
            this.floor_queue[i] = 0;
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
        this.floor_queue[floor] += 1;

    }

    public void remove_requested_floor(int floor){
        this.floor_queue[floor] -= 1;

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
        tmp_list.clear();
        for(int i=0;i<this.floor_queue.length;i++){
            if(this.floor_queue[i] > 0){
                tmp_list.add(i);
            }
        }
        List<Double> distance = new ArrayList<>();
        double best_dist = go;
        for(int i=0; i <tmp_list.size(); i++){

            double temp = Math.abs(- this.floor + tmp_list.get(i));

            //TODO
            //Ponderer la distance en fonction de nombre de personne en attente a un etage
            int pond_floor = this.floor_queue[tmp_list.get(i)];

            distance.add(temp);
            Log.i("Distance",Double.toString(temp));
            if(temp  < best_dist ){
                best_dist = temp;
                go = tmp_list.get(i);
                Log.i("Distance chosen",Double.toString(temp));
            }
        }
        if(go!=20) {
            move_to((int) go);
        }
    }

}
