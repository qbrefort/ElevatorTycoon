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
    private int algo_research_music_price;

    public Simulation(MainActivity ma, List<Folk> waiting_folk, Elevator[] elevators) {
        this.folks = waiting_folk;
        this.elevators = elevators;
        this.iteration = 0;
        this.mainActivity = ma;
        this.cash = 0;
        this.algoList = new ArrayList<>();
        this.musicList = new ArrayList<>();
        this.up_capacity_price = new int[2];up_capacity_price[0]=up_capacity_price[1]=50;
        this.repair_elevator_price = new int[2];repair_elevator_price[0]=repair_elevator_price[1]=50;
        this.algo_research_price = 50;
        this.algo_research_music_price = 10;
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

    public void generate_chaos(){

        //Update price of repair

        double mf1 = elevators[0].getMaintenance()*5.0;
        double mf2 = elevators[1].getMaintenance()*5.0;

        repair_elevator_price[0] = elevators[0].getMaintenance();
        repair_elevator_price[1] = elevators[1].getMaintenance();

        Random rand = new Random();
        int test_broke = rand.nextInt(100);
        if (test_broke > 95){
            int eleb= rand.nextInt(2);
            elevators[eleb].setWorking(false);
            if(eleb==0){
                Button buttonUpCapacity = (Button) mainActivity.findViewById(R.id.buttonUpCapacity);
                buttonUpCapacity.setText("E1 BROKEN ($" + getRepair_elevator_price(0) + ")");
            }
            if(eleb==1){
                Button buttonElevator2 = (Button) mainActivity.findViewById(R.id.buttonAddElevator);
                buttonElevator2.setText("E2 BROKEN ($" + getRepair_elevator_price(1) + ")");
            }
            Toast toast = Toast.makeText(mainActivity,eleb+1+ " is BROKEN!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void iterate(int iter) {
        switch (iter) {
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
                        this.mainActivity.update_data();
                        this.mainActivity.repaint();
                        //folks.remove(i);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < folks.size(); i++) {
                    folks.get(i).goInElevator(elevators);
                    folks.get(i).add_waiting_time();
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

    public boolean canAffordRepair(int i){
        if(this.cash>= this.repair_elevator_price[i]) {
            this.cash = this.cash - this.repair_elevator_price[i];
            return true;
        }
        else
            return false;
    }

    public boolean canAffordUpdateCapacity(int i){
        if(this.cash>= this.up_capacity_price[i])
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
        if(this.cash>= this.algo_research_music_price){
            this.cash -= this.algo_research_music_price;
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

    public void operateCash(int cash) {
        this.cash += cash;
    }

    public void operateCash_by_time_waiting(Folk folk) {
        double time = (double) folk.getTime_waiting();
        int cash_to_add = (int) (50/time);
        this.cash += cash_to_add;
    }
}
