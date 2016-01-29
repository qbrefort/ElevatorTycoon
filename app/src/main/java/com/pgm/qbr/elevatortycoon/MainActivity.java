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
import android.util.Log;
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
    private int iter;

    private Handler mHandler = new Handler();
    private Handler mHandler2 = new Handler();


    public void repaint(){
        set_image_elevator();
        set_image_Folk();
    }

    public void update_data(){
        TextView textViewCash = (TextView) findViewById(R.id.textViewCash);
        textViewCash.setText(Integer.toString(simu.getCash()));
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
                Log.i("Waiting", folks.get(i).getName() + " ON " + folks.get(i).getCurrent_floor());
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


        iter = 0;
        simu = new Simulation(ma, folks, elevators);
        repaint();
        update_data();
    }

    public void setOnClickListenerRun(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (folks.size()>0) {
                                Thread.sleep(200);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        simu.iterate(iter);
                                        iter++;
                                        iter = iter % 3;
                                        repaint();
                                        for (int i = 0; i < folks.size(); i++) {
                                            if(folks.get(i).isTreated()){
                                                folks.remove(i);
                                            }
                                        }
                                        onNotifyListenerPB();
                                        Random rand= new Random();
                                        int ran = rand.nextInt(25);
                                        int test = rand.nextInt(100);
                                        if(test>80)
                                            folks.add(new Folk(MainActivity.this,elevators,folks_name[ran]));

                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }).start();

            }
        });
    }

    public void setOnClickListenerAddFolk(){
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int rr = rand.nextInt(30);
                simu.getFolks().add(new Folk(MainActivity.this,elevators,folks_name[rr]));
                repaint();
            }
        });
    }


    public void setOnClickListenerUpdate(){
        final Button buttonUpCapacity = (Button) findViewById(R.id.buttonUpCapacity);
        buttonUpCapacity.setText("E1 CAP+1 ($"+simu.getUp_capacity_price(1)+")");

        buttonUpCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elevators[0].isWorking()) {
                    if(simu.canAffordUpdateCapacity(1)){
                        elevators[0].addCapacity();
                        simu.updateCapacityPrice(1);
                        buttonUpCapacity.setText("E1 CAP+1 ($"+simu.getUp_capacity_price(1)+")");
                        update_data();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    if (simu.canAffordRepair(0)) {
                        elevators[0].setWorking(true);
                        elevators[0].resetMaintenance();
                        buttonUpCapacity.setText("E1 CAP+1 ($" + simu.getUp_capacity_price(0) + ")");
                        update_data();
                    }
                    else {
                        buttonUpCapacity.setText("E1 BROKEN ($" + simu.getRepair_elevator_price(0) + ")");
                    }
                }


            }
        });
    }

    public void setOnClickListenerElevator2(){
        final Button buttonElevator2 = (Button) findViewById(R.id.buttonAddElevator);
        buttonElevator2.setText("E2 BROKEN ($" + simu.getRepair_elevator_price(1) + ")");
        buttonElevator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elevators[1].isWorking()) {
                    if (simu.canAffordUpdateCapacity(1)) {
                        elevators[1].addCapacity();
                        simu.updateCapacityPrice(1);
                        buttonElevator2.setText("E2 CAP+1 ($" + simu.getUp_capacity_price(1) + ")");
                        update_data();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    if (simu.canAffordRepair(1)) {
                        elevators[1].setWorking(true);
                        elevators[1].resetMaintenance();
                        buttonElevator2.setText("E2 CAP+1 ($" + simu.getUp_capacity_price(1) + ")");
                        update_data();
                    } else {
                        buttonElevator2.setText("E2 BROKEN ($" + simu.getRepair_elevator_price(1) + ")");
                    }
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
                String sort_carac = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addListenerSpinnerMusic() {

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMusic);

        refreshSpinnerMusicItem();
        final MediaPlayer mp = new MediaPlayer();

        final MediaPlayer[] mp_tab= new MediaPlayer[4];
        mp_tab[0] = MediaPlayer.create(MainActivity.this,R.raw.jazzy);
        mp_tab[1] = MediaPlayer.create(MainActivity.this,R.raw.refreshing);
        mp_tab[2] = MediaPlayer.create(MainActivity.this,R.raw.bestm);
        mp_tab[3] = MediaPlayer.create(MainActivity.this,R.raw.hey);

        for(int i=0;i<mp_tab.length;i++){
            mp_tab[i].setLooping(true);
        }

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<4;i++){
                    if(mp_tab[i].isPlaying())
                        mp_tab[i].pause();
                }
                mp_tab[position].start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setOnClickListenerAlgoButton(){

        final String[] algo_tab = new String[3];
        algo_tab[0] = "top bottom";
        algo_tab[1] = "distance";
        algo_tab[2] = "classic";

        Button buttonAlgo = (Button) findViewById(R.id.buttonAlgo);
        buttonAlgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simu.canAffordResearchAlgo()) {
                    simu.addAlgoList(algo_tab[0]);
                    refreshSpinnerAlgoItem();
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



        Button buttonMusic = (Button) findViewById(R.id.buttonMusic);
        buttonMusic.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View v) {
                if(simu.canAffordResearchMusic()){
                    simu.addMusicList(music_tab[i]);
                    refreshSpinnerMusicItem();
                    i++;
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
        IV_folks.add(imageFW1);IV_folks.add(imageFW2);IV_folks.add(imageFW3);IV_folks.add(imageFW4);IV_folks.add(imageFW5);IV_folks.add(imageFW6);IV_folks.add(imageFW7);IV_folks.add(imageFW8);IV_folks.add(imageFW9);IV_folks.add(imageFW10);IV_folks.add(imageFW11);

        TV_folks = new ArrayList<>();
        TV_folks.add(textViewl0);TV_folks.add(textViewl1);TV_folks.add(textViewl2);TV_folks.add(textViewl3);TV_folks.add(textViewl4);TV_folks.add(textViewl5);TV_folks.add(textViewl6);TV_folks.add(textViewl7);TV_folks.add(textViewl8);TV_folks.add(textViewl9);TV_folks.add(textViewl10);

        reset_init_simu();

        iter = 0;

        repaint();

        setOnClickListenerReset();
        setOnClickListenerRun();
        setOnClickListenerAddFolk();
        setOnClickListenerUpdate();
        setOnClickListenerElevator2();
        addListenerSpinnerAlgo();
        setOnClickListenerAlgoButton();
        addListenerSpinnerMusic();
        setOnClickListenerMusicButton();


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
