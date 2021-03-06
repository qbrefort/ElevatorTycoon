package com.pgm.qbr.elevatortycoon;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by qbr on 20/01/16.
 */

public class Simulation {

    private int iteration;
    private Elevator[] elevators;
    private List<Folk> folks;
    private MainActivity mainActivity;

    private int cash;
    private int[] up_capacity_price;
    private int[] repair_elevator_price;
    private List<String> algoList;
    private List<String> musicList;
    private int algo_research_price;
    private int music_research_price;
    private int music_played;
    private int add_elevator;
    private boolean new_elevator_added;

    private int cash_earned;

    public Simulation(MainActivity ma, List<Folk> waiting_folk, Elevator[] elevators) {
        this.folks = waiting_folk;
        this.elevators = elevators;
        this.iteration = 0;
        this.mainActivity = ma;
        this.cash = 100;
        this.algoList = new ArrayList<>();
        this.musicList = new ArrayList<>();
        this.up_capacity_price = new int[2];up_capacity_price[0]=up_capacity_price[1]=50;
        this.repair_elevator_price = new int[2];repair_elevator_price[0]=repair_elevator_price[1]=500;
        this.algo_research_price = 200;
        this.music_research_price = 150;
        this.music_played = 0;
        this.new_elevator_added = false;
        this.cash_earned = 50;
        this.add_elevator = 0;
    }

    public List<Folk> getFolks() {
        return folks;
    }

    public int getFloor_elevator(int i) {
        return this.elevators[i].getFloor();
    }

    public void generate_chaos(){

        //Update price of repair
        for(int i=0;i<elevators.length;i++){

            if(elevators[i].isWorking()){

                double mf1 = elevators[i].getMaintenance()*5.0;
                repair_elevator_price[i] = (int) mf1;

                Random rand = new Random();
                int test_broke = rand.nextInt(98)+2;

                Log.i("Cauchy",i+"; maint: "+elevators[i].getMaintenance()+";test broke:"+test_broke+"; prob cauchy: "+this.probCauchy(elevators[i].getMaintenance()));

                if(test_broke < probCauchy(elevators[i].getMaintenance())) {
                    elevators[i].setWorking(false);
                    if(i==0){
                        Button buttonB1 = (Button) mainActivity.findViewById(R.id.buttonME1);
                        buttonB1.setText("BROKEN ($" + getRepair_elevator_price(0) + ")");
                        Log.i("Broke","0");
                    }
                    if(i==1){
                        Button buttonB2 = (Button) mainActivity.findViewById(R.id.buttonME2);
                        buttonB2.setText("BROKEN ($" + getRepair_elevator_price(1) + ")");
                        Log.i("Broke", "1");
                    }
                    Toast toast = Toast.makeText(mainActivity,i+1+ " is BROKEN!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        Random rand = new Random();
        int test_broke = rand.nextInt(98)+2;
        add_elevator++;

        if(test_broke < probCauchy(add_elevator,80,2)) {
            add_elevator = 0;
            elevators[0].addMax_level();
            Toast toast = Toast.makeText(mainActivity,"Firm is in expansion ! New floor added", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void iterate(int coup) {
        switch (coup) {
            case 0:
                Log.i("Iteration", "" + iteration);
                this.elevators[0].where_to_move();
                this.elevators[1].where_to_move();
                this.iteration = this.iteration + 1;
                generate_chaos();
                break;
            case 1:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goOutElevator();
                    if (folks.get(i).isTreated()) {
                        Log.i("OUT", folks.get(i).getName());
                        operateCash_by_time_waiting(folks.get(i));
                        this.mainActivity.update_cashflow_and_repaint_button();
                        this.mainActivity.repaint();
                        //folks.remove(i);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goInElevator(elevators);
                    folks.get(i).add_waiting_time();
                    if(folks.get(i).isWaiting() && folks.get(i).getTime_waiting()>10){
                        Toast toast = Toast.makeText(mainActivity, "Fuck this, I am taking the stairs", Toast.LENGTH_SHORT);
                        //toast.show();
                        folks.remove(i);
                    }
                }
                break;
        }
    }

    public List<String> getAlgoList() {
        return algoList;
    }
    public List<String> getMusicList() {
        return musicList;
    }

    public void addAlgoList(String item) {
        this.algoList.add(item);
    }

    public void addMusicList(String item) {
        this.musicList.add(item);
    }

    public int getCash() {
        return this.cash;
    }

    public int getRepair_elevator_price(int i){
        return this.repair_elevator_price[i];
    }


    public int getAlgo_research_price() {
        return algo_research_price;
    }

    public int getMusic_research_price() {
        return music_research_price;
    }

    public void setMusic_played(int music_played) {
        this.music_played = music_played;
    }

    public boolean isNew_elevator_added() {
        return new_elevator_added;
    }

    public void setNew_elevator_added(boolean new_elevator_added) {
        this.new_elevator_added = new_elevator_added;
    }

    public boolean canAffordRepair(int i){
        if(this.cash>= this.repair_elevator_price[i]) {
            this.cash = this.cash - this.repair_elevator_price[i];
            return true;
        }
        else
            return false;
    }

    public boolean canAffordUpdateCapacity(int i){
        if(cash>= up_capacity_price[i])
            return true;
        else
            return false;

    }

    public boolean canAffordResearchAlgo(){
        if(this.cash>= this.algo_research_price){
            this.cash -= this.algo_research_price;
            return true;
        }

        else
            return false;

    }

    public boolean canAffordResearchMusic(){
        if(this.cash>= this.music_research_price){
            this.cash -= this.music_research_price;
            return true;
        }

        else
            return false;

    }

    public boolean canAffordMaintainReset(int i){
        if(this.cash>= elevators[i].getMaintenance()) {
            this.cash -= elevators[i].getMaintenance();
            return true;
        }
        else
            return false;
    }

    public int getUp_capacity_price(int i){
            return this.up_capacity_price[i];
    }

    public void updateCapacityPrice(int i){
        this.cash -= this.up_capacity_price[i];
        double res = this.up_capacity_price[i]*1.2;
        this.up_capacity_price[i] = (int) res;

    }

    public void operateCash_by_time_waiting(Folk folk) {
        double multiply_music = 1;
        if(music_played ==0)    multiply_music = 1.06;
        if(music_played ==1)    multiply_music = 1.20;
        if(music_played ==2)    multiply_music = 1.36;
        if(music_played ==3)    multiply_music = 1.77;
        double time = (double) folk.getTime_waiting();
        int cash_to_add = (int) (cash_earned/time*multiply_music);
        this.cash += cash_to_add;
    }

    public int probCauchy(double x){
        double x0 = 40;
        double a = 2;
        double res = 1.0/Math.PI*Math.atan((x-x0)/a) + 0.5;
        res*=100;
        return (int) res;
    }
    public int probCauchy(double x, double x0, double a){
        double res = 1.0/Math.PI*Math.atan((x-x0)/a) + 0.5;
        res*=100;
        return (int) res;
    }
}
