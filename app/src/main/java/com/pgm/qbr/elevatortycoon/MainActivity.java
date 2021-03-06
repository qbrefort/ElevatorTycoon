package com.pgm.qbr.elevatortycoon;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {



    private List<ImageView> IV_folks;
    private List<TextView> TV_folks;
    private List<Folk> folks;
    private Simulation simu;
    private Elevator[] elevators;
    private String[] folks_name;
    private MediaPlayer[] mp_tab= new MediaPlayer[4];
    private int coup;

    private Handler mHandler = new Handler();
    private Handler mHandler2 = new Handler();


    public void repaint(){
        set_image_elevator();
        set_image_Folk();
    }

    public void update_cashflow_and_repaint_button(){
        TextView textViewCash = (TextView) findViewById(R.id.textViewCash);
        textViewCash.setText(Integer.toString(simu.getCash()));

        Button buttonME1 = (Button) findViewById(R.id.buttonME1);
        Button buttonME2 = (Button) findViewById(R.id.buttonME2);
        if(elevators[0].isWorking()){
            buttonME1.setText("Maintain ($"+elevators[0].getMaintenance()+")");
        }
        else
            buttonME1.setText("BROKEN ($"+simu.getRepair_elevator_price(0)+")");

        if(elevators[1].isWorking()){
            buttonME2.setText("Maintain ($"+elevators[1].getMaintenance()+")");
        }
        else
            buttonME2.setText("BROKEN ($"+simu.getRepair_elevator_price(1)+")");

        if(!simu.isNew_elevator_added())
            buttonME2.setText("ADD new elevator ($"+simu.getRepair_elevator_price(1)+")");

        Animator animSet = AnimatorInflater.loadAnimator(this, R.animator.cahspop);
        animSet.setTarget(textViewCash);
        animSet.start();

    }

    public void set_image_elevator(){
        ImageView imageElevator = (ImageView) findViewById(R.id.imageElevator);
        ImageView imageElevator2 = (ImageView) findViewById(R.id.imageElevator2);
        float cf = simu.getFloor_elevator(0);
        float cf2 = simu.getFloor_elevator(1);
        int got_to = (int) ((10-cf)*1175/10+5);
        int got_to2 = (int) ((10-cf2)*1175/10+5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(150, 150);
        //params.topMargin = 0;
        params.leftMargin = 5;
        params2.leftMargin = 5;
        imageElevator.setLayoutParams(params);
        imageElevator2.setLayoutParams(params2);

        ObjectAnimator anim = ObjectAnimator.ofFloat(imageElevator, "translationY", imageElevator.getY(), got_to);
        anim.setDuration(150);
        anim.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageElevator2, "translationY", imageElevator2.getY(), got_to2);
        anim2.setDuration(150);
        anim2.start();


        TextView textViewFolkIn = (TextView) findViewById(R.id.textViewFolkIn);
        textViewFolkIn.setText(Integer.toString(elevators[0].getNb_person_in()));

        TextView textViewFolkIn2 = (TextView) findViewById(R.id.textViewFolkIn2);
        textViewFolkIn2.setText(Integer.toString(elevators[1].getNb_person_in()));
    }

    public void set_image_Folk(){

        int off_set_X = 400;

        for(int i=0;i<=10;i++){
            TV_folks.get(i).setText("");
        }
        for(float i=0;i<IV_folks.size();i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
            params.topMargin = (int) ((10 - i) * 1175 / 10 + 5);
            ImageView IV_to_move = IV_folks.get((int) i);
            IV_to_move.setX(off_set_X);
            IV_to_move.setLayoutParams(params);
            IV_to_move.setVisibility(View.INVISIBLE);
        }
        int nb_of_folk_waiting[] = new int[11];
        for (int i=0;i<nb_of_folk_waiting.length;i++){
            nb_of_folk_waiting[i] = 0;
        }
        for(int i=0;i<folks.size();i++){
            if (folks.get(i).isTreated()){
                ImageView IV_to_move = IV_folks.get(folks.get(i).getWanted_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(IV_to_move, "translationX", off_set_X, off_set_X + 50);
                anim.setDuration(300);
                anim.start();
            }

            if(folks.get(i).isWaiting()){
                //Log.i("Waiting", folks.get(i).getName() + " ON " + folks.get(i).getCurrent_floor());
                ImageView IV_to_move = IV_folks.get(folks.get(i).getCurrent_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                nb_of_folk_waiting[folks.get(i).getCurrent_floor()]++;

            }

        }
        for (int i=0;i<nb_of_folk_waiting.length;i++){
            if(nb_of_folk_waiting[i] != 0){
                TV_folks.get(i).setText("x"+nb_of_folk_waiting[i]);
            }
        }
    }



    public void setOnClickListenerReset(){
        FloatingActionButton resb = (FloatingActionButton) findViewById(R.id.reset);
        resb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_init_simu();
            }
        });
    }

    public void reset_init_simu(){
        MainActivity ma = MainActivity.this;

        elevators = new Elevator[2];
        elevators[0] = new Elevator(ma);elevators[0].setId("E1");
        elevators[1] = new Elevator(ma);elevators[1].setId("E2");
        elevators[1].setFloor(5);
        folks = new ArrayList<>();
        for(int i=0;i<10;i++){
            Random rand = new Random();
            int rr = rand.nextInt(30);
            folks.add(new Folk(MainActivity.this, elevators, folks_name[rr]));
        }

        elevators[0].setCapacity(3);
        elevators[1].setCapacity(1);
        elevators[0].setWorking(true);
        elevators[1].setWorking(false);

        coup = 0;
        simu = new Simulation(ma, folks, elevators);

        setOnClickListenerReset();
        setOnClickListenerRun();
        setOnClickListenerCapacityE1();
        setOnClickListenerCapacityE2();
        addListenerSpinnerAlgo();
        setOnClickListenerAlgoButton();
        addListenerSpinnerMusic();
        setOnClickListenerMusicButton();
        setOnClickListenerMaintainButton();

        repaint();
        update_cashflow_and_repaint_button();
    }

    public void setOnClickListenerRun(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread myT = new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (folks.size()>0) {
                                Thread.sleep(200);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        simu.iterate(coup);
                                        coup++;
                                        coup = coup % 3;
                                        repaint();
                                        for (int i = 0; i < folks.size(); i++) {
                                            if(folks.get(i).isTreated()){
                                                folks.remove(i);
                                            }
                                        }
                                        onNotifyListenerPB();
                                        if(coup==0){
                                            Random rand= new Random();
                                            int ran = rand.nextInt(25);
                                            int test = rand.nextInt(100);
                                            if(test<elevators[0].getMax_level()*12)
                                                folks.add(new Folk(MainActivity.this,elevators,folks_name[ran]));
                                            int fw=0;
                                            for(Folk f:folks){
                                                if(f.isWaiting())   fw++;
                                            }
                                            if(fw<3)
                                                folks.add(new Folk(MainActivity.this,elevators,folks_name[ran]));
                                            if(fw<2)
                                                folks.add(new Folk(MainActivity.this,elevators,folks_name[ran]));
                                            if(fw<1)
                                                folks.add(new Folk(MainActivity.this,elevators,folks_name[ran]));
                                        }


                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                });
            myT.start();
            }
        });
    }

    public void setOnClickListenerCapacityE1(){
        final Button buttonUpCapacity = (Button) findViewById(R.id.buttonUpCapacity);
        buttonUpCapacity.setText("CAP+1 ($"+simu.getUp_capacity_price(0)+")");
        buttonUpCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simu.canAffordUpdateCapacity(0)) {
                    elevators[0].addCapacity();
                    simu.updateCapacityPrice(0);
                    buttonUpCapacity.setText("CAP+1 ($" + simu.getUp_capacity_price(0) + ")");
                    update_cashflow_and_repaint_button();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
    }

    public void setOnClickListenerCapacityE2(){
        final Button buttonElevator2 = (Button) findViewById(R.id.buttonElevator2);
        buttonElevator2.setText("CAP+1 ($"+simu.getUp_capacity_price(1)+")");
        buttonElevator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simu.canAffordUpdateCapacity(1)) {
                    elevators[1].addCapacity();
                    simu.updateCapacityPrice(1);
                    buttonElevator2.setText("CAP+1 ($" + simu.getUp_capacity_price(1) + ")");
                    update_cashflow_and_repaint_button();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void onNotifyListenerPB(){

        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBarM1);

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(elevators[0].getMaintenance()*2);
                        }
                    });
                }
        }).start();

        final ProgressBar mProgress2 = (ProgressBar) findViewById(R.id.progressBarM2);

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                // Update the progress bar
                mHandler2.post(new Runnable() {
                    public void run() {
                        mProgress2.setProgress(elevators[1].getMaintenance()*2);
                    }
                });
            }
        }).start();

    }

    public void refreshSpinnerAlgoItem(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerAlgo);

        List<String> categories = simu.getAlgoList();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void refreshSpinnerMusicItem(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerMusic);

        List<String> categories = simu.getMusicList();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerSpinnerAlgo() {

        Spinner spinner = (Spinner) findViewById(R.id.spinnerAlgo);

        refreshSpinnerAlgoItem();

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<elevators.length;i++){
                    for(Elevator el:elevators) {
                        el.setAlgo(position + 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addListenerSpinnerMusic() {

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMusic);

        refreshSpinnerMusicItem();

        for(int i=0;i<mp_tab.length;i++){
            if(mp_tab[i]!=null){
                mp_tab[i].pause();
            }
        }

        mp_tab[0] = MediaPlayer.create(MainActivity.this,R.raw.jazzy);
        mp_tab[1] = MediaPlayer.create(MainActivity.this,R.raw.refreshing);
        mp_tab[2] = MediaPlayer.create(MainActivity.this,R.raw.bestm);
        mp_tab[3] = MediaPlayer.create(MainActivity.this,R.raw.hey);


        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < 4; i++) {
                    if (mp_tab[i].isPlaying())
                        mp_tab[i].pause();
                }
                mp_tab[position].start();
                simu.setMusic_played(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setOnClickListenerAlgoButton(){

        final String[] algo_tab = new String[3];
        algo_tab[0] = "Closest Dist.";
        algo_tab[1] = "Weighted Dist.";
        algo_tab[2] = "up/down";

        simu.addAlgoList(algo_tab[0]);
        refreshSpinnerAlgoItem();

        Button buttonAlgo = (Button) findViewById(R.id.buttonAlgo);
        buttonAlgo.setText("Research Algo ($"+simu.getAlgo_research_price()+")");
        buttonAlgo.setOnClickListener(new View.OnClickListener() {
            int i=1;
            @Override
            public void onClick(View v) {
                if (simu.canAffordResearchAlgo() && i<3) {
                    simu.addAlgoList(algo_tab[i]);
                    refreshSpinnerAlgoItem();
                    i++;
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void setOnClickListenerMusicButton(){

        final String[] music_tab = new String[4];

        music_tab[0] = "Jazz";
        music_tab[1] = "Refresh";
        music_tab[2] = "Best";
        music_tab[3] = "Hey";

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerMusic);

        Button buttonMusic = (Button) findViewById(R.id.buttonMusic);

        buttonMusic.setText("New Music ($"+simu.getMusic_research_price()+")");

        buttonMusic.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View v) {
                if(simu.canAffordResearchMusic()){
                    if(i<4) {
                        simu.addMusicList(music_tab[i]);
                    }
                    refreshSpinnerMusicItem();
                    i++;
                }
            }
        });
    }

    public void setOnClickListenerMaintainButton(){


        final Button buttonME1 = (Button) findViewById(R.id.buttonME1);
        update_cashflow_and_repaint_button();
        buttonME1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(elevators[0].isWorking()){
                    if(simu.canAffordMaintainReset(0)){
                        elevators[0].resetMaintenance();
                        update_cashflow_and_repaint_button();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    if (simu.canAffordRepair(0)) {
                        elevators[0].setWorking(true);
                        elevators[0].setMaintenance((int) (elevators[0].getMaintenance() * 0.8));
                        update_cashflow_and_repaint_button();
                    } else {
                        buttonME1.setText("BROKEN ($" + simu.getRepair_elevator_price(1) + ")");
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        final Button buttonME2 = (Button) findViewById(R.id.buttonME2);
        buttonME2.setText("BROKEN ($" + simu.getRepair_elevator_price(1) + ")");
        buttonME2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(elevators[1].isWorking()){
                    if(simu.canAffordMaintainReset(1)){
                        elevators[1].resetMaintenance();
                        update_cashflow_and_repaint_button();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    if (simu.canAffordRepair(1)) {
                        elevators[1].setWorking(true);
                        elevators[1].setMaintenance((int) (elevators[1].getMaintenance() * 0.8));
                        update_cashflow_and_repaint_button();
                        simu.setNew_elevator_added(true);
                    } else {
                        String toDisplay = "BROKEN ($" + simu.getRepair_elevator_price(1) + ")";
                        buttonME2.setText(toDisplay);
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Resources res = getResources();
        folks_name = res.getStringArray(R.array.names);

        ImageView imageFW1 = (ImageView) findViewById(R.id.imageFolkW1);
        ImageView imageFW2 = (ImageView) findViewById(R.id.imageFolkW2);
        ImageView imageFW3 = (ImageView) findViewById(R.id.imageFolkW3);
        ImageView imageFW4 = (ImageView) findViewById(R.id.imageFolkW4);
        ImageView imageFW5 = (ImageView) findViewById(R.id.imageFolkW5);
        ImageView imageFW6 = (ImageView) findViewById(R.id.imageFolkW6);
        ImageView imageFW7 = (ImageView) findViewById(R.id.imageFolkW7);
        ImageView imageFW8 = (ImageView) findViewById(R.id.imageFolkW8);
        ImageView imageFW9 = (ImageView) findViewById(R.id.imageFolkW9);
        ImageView imageFW10 = (ImageView) findViewById(R.id.imageFolkW10);
        ImageView imageFW11 = (ImageView) findViewById(R.id.imageFolkW11);

        TextView textViewl10 = (TextView) findViewById(R.id.textViewl10);
        TextView textViewl9 = (TextView) findViewById(R.id.textViewl9);
        TextView textViewl8 = (TextView) findViewById(R.id.textViewl8);
        TextView textViewl7 = (TextView) findViewById(R.id.textViewl7);
        TextView textViewl6 = (TextView) findViewById(R.id.textViewl6);
        TextView textViewl5 = (TextView) findViewById(R.id.textViewl5);
        TextView textViewl4 = (TextView) findViewById(R.id.textViewl4);
        TextView textViewl3 = (TextView) findViewById(R.id.textViewl3);
        TextView textViewl2 = (TextView) findViewById(R.id.textViewl2);
        TextView textViewl1 = (TextView) findViewById(R.id.textViewl1);
        TextView textViewl0 = (TextView) findViewById(R.id.textViewl0);


        IV_folks = new ArrayList<>();
        IV_folks.add(imageFW1);IV_folks.add(imageFW2);IV_folks.add(imageFW3);IV_folks.add(imageFW4);IV_folks.add(imageFW5);IV_folks.add(imageFW6);IV_folks.add(imageFW7);
        IV_folks.add(imageFW8);
        IV_folks.add(imageFW9);
        IV_folks.add(imageFW10);IV_folks.add(imageFW11);

        TV_folks = new ArrayList<>();
        TV_folks.add(textViewl0);TV_folks.add(textViewl1);TV_folks.add(textViewl2);TV_folks.add(textViewl3);TV_folks.add(textViewl4);TV_folks.add(textViewl5);TV_folks.add(textViewl6);TV_folks.add(textViewl7);TV_folks.add(textViewl8);TV_folks.add(textViewl9);
        TV_folks.add(textViewl10);

        reset_init_simu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
