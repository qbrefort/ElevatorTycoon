package com.pgm.qbr.elevatortycoon;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by qbr on 20/01/16.
 */
public class Elevator {

    private String id;
    private int capacity;
    private int max_level;
    private int floor;
    private int nb_person_in;
    private int[] floor_queue;
    private int algo;
    private int maintenance;
    private int maintenance_prob;
    private MainActivity mainActivity;
    private boolean going_up;
    private boolean working;

    public int[] getFloor_queue() {
        return floor_queue;
    }
    public int getNb_person_in(){ return nb_person_in; }
    public int getCapacity() { return capacity;}
    public void print_Floor_queue(){
        String queue="";
        for(int i=0;i<this.getFloor_queue().length;i++){
            if(this.floor_queue[i]>0){
                queue=queue+i+" ";
            }
        }
        Log.i("Queue",this.id+" "+queue);
    }

    public Elevator(MainActivity ma) {
        this.capacity = 3;
        this.max_level = 10;
        this.nb_person_in = 0;
        this.floor = 0;
        this.going_up = true;
        this.working = false;
        this.maintenance = 0;
        this.maintenance_prob = 0;
        this.floor_queue = new int[this.max_level+1];
        for (int i = 0 ;i<this.floor_queue.length; i++){
            this.floor_queue[i] = 0;
        }
        this.algo = 3;
        this.id = "Ex";
        this.mainActivity = ma;
    }

    public void setId(String id){
        this.id = id;
    }
    public void addCapacity(){
        this.capacity += 1;
    }
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
    public void setWorking(boolean working){
        this.working = working;
    }
    public boolean isWorking(){return this.working;}

    public String getId(){
        return this.id;
    }

    public void setAlgo(int alg){
        this.algo = alg;
    }

    public int getFloor(){
        return this.floor;
    }
    public void setFloor(int floor){
        this.floor = floor;
    }

    public int getMaintenance() {
        return this.maintenance;
    }
    public void resetMaintenance(){this.maintenance =0;}

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
        //Log.i("Elevator",this.id+ " decided to move from" +this.floor+" to "+ floor);
        if(this.working) {
            this.floor = floor;
            Random ran1 = new Random();
            int maintenance_worse = ran1.nextInt(100);
            if(maintenance_worse < probCauchy(maintenance_prob)){
                this.maintenance = this.maintenance+1;
                maintenance_prob = 0;
            }
            else
                maintenance_prob++;
        }
    }

    public void where_to_move(){
        switch (algo){
            case 1:
                //Distance algo
                print_Floor_queue();
                double go = 20;
                List<Integer> tmp_list = new ArrayList<>();
                tmp_list.clear();
                for(int i=0;i<this.floor_queue.length;i++){
                    if(this.floor_queue[i] > 0){
                        tmp_list.add(i);
                    }
                }
                double best_dist = go;
                for(int i=0; i <tmp_list.size(); i++){

                    double temp = Math.abs(- this.floor + tmp_list.get(i));
                    int pond_floor = this.floor_queue[tmp_list.get(i)];

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
                break;

            case 2:
                print_Floor_queue();
                double go2 = 20;
                List<Integer> tmp_list2 = new ArrayList<>();
                tmp_list2.clear();
                for(int i=0;i<this.floor_queue.length;i++){
                    if(this.floor_queue[i] > 0){
                        tmp_list2.add(i);
                    }
                }
                List<Double> distance2 = new ArrayList<>();
                double best_dist2 = go2;
                for(int i=0; i <tmp_list2.size(); i++){

                    double temp2 = Math.abs(- this.floor + tmp_list2.get(i));

                    //TODO
                    //Ponderer la distance en fonction de nombre de personne en attente a un etage
                    double pond_floor = this.floor_queue[tmp_list2.get(i)];

                    temp2 = temp2/pond_floor;

                    distance2.add(temp2);
                    Log.i("Distance",Double.toString(temp2));
                    if(temp2  < best_dist2 ){
                        best_dist2 = temp2;
                        go2 = tmp_list2.get(i);
                        Log.i("Distance chosen",Double.toString(temp2));
                    }
                }
                if(go2!=20) {
                    move_to((int) go2);
                }
                break;
            case 3:
                print_Floor_queue();
                List<Integer> tmp_list3 = new ArrayList<>();
                tmp_list3.clear();
                for(int i=0;i<this.floor_queue.length;i++){
                    if(this.floor_queue[i] > 0){
                        tmp_list3.add(i);
                    }
                }
                int got_to = this.floor;

                if(going_up){
                    for(int i=tmp_list3.size() - 1; i >= 0; i--){
                        if(tmp_list3.get(i)>this.floor){
                            got_to = tmp_list3.get(i);
                        }
                    }
                    if(got_to == this.floor)
                        this.going_up = !going_up;
                }
                else {
                    for(int i=0; i < tmp_list3.size(); i++){
                        if(tmp_list3.get(i) < this.floor){
                            got_to = tmp_list3.get(i);
                        }
                    }
                    if(got_to == this.floor)
                        this.going_up = !going_up;
                }
                move_to(got_to);
                break;

            case 4:
                print_Floor_queue();
                List<Integer> tmp_list4 = new ArrayList<>();
                tmp_list4.clear();
                for(int i=0;i<this.floor_queue.length;i++){
                    if(this.floor_queue[i] > 0){
                        tmp_list4.add(i);
                    }
                }
                got_to = this.floor;

                if(going_up){
                    for(int i=tmp_list4.size() - 1; i >= 0; i--){
                        if(tmp_list4.get(i)>this.floor){
                            got_to = tmp_list4.get(i);
                        }
                    }
                    if(got_to == this.floor)
                        this.going_up = !going_up;
                }
                else {
                    for(int i=0; i < tmp_list4.size(); i++){
                        if(tmp_list4.get(i) < this.floor){
                            got_to = tmp_list4.get(i);
                        }
                    }
                    if(got_to == this.floor)
                        this.going_up = !going_up;
                }

                if(got_to!=this.floor){
                    if(this.going_up)
                        move_to(this.floor++);
                    else
                        move_to(this.floor--);
                }
                break;


        }


    }

    public int probCauchy(double x){
        double x0 = 5;
        double a = 5;
        double res = 1/Math.PI*Math.atan((x-x0)/a) + 0.5;
        res*=100;
        return (int) res;
    }

    public boolean isFull(){
        if(this.nb_person_in >= this.capacity){
            Log.i("Elevator",this.id+" is full");
            return true;
        }
        else
            return false;
    }

}
