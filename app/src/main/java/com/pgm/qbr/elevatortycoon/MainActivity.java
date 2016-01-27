package com.pgm.qbr.elevatortycoon;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView imageElevator;
    private ImageView imageElevator2;
    private ImageView imageFW1;
    private ImageView imageFW2;
    private ImageView imageFW3;
    private ImageView imageFW4;
    private ImageView imageFW5;
    private ImageView imageFW6;
    private ImageView imageFW7;
    private ImageView imageFW8;
    private ImageView imageFW9;
    private ImageView imageFW10;
    private ImageView imageFW11;

    private TextView textViewl10;
    private TextView textViewl9;
    private TextView textViewl8;
    private TextView textViewl7;
    private TextView textViewl6;
    private TextView textViewl5;
    private TextView textViewl4;
    private TextView textViewl3;
    private TextView textViewl2;
    private TextView textViewl1;
    private TextView textViewl0;
    private TextView textViewFolkIn;
    private TextView textViewFolkIn2;
    private TextView textViewCash;

    private List<ImageView> IV_folks;
    private List<TextView> TV_folks;
    private List<Folk> folks;
    private Simulation simu;
    private Elevator[] elevators;
    private String[] folks_name;
    private int iter;

    public void repaint(){
        set_image_elevator();
        set_image_Folk();
    }

    public void update_data(){
        textViewCash = (TextView) findViewById(R.id.textViewCash);
        textViewCash.setText(Integer.toString(simu.getCash()));
        Animator animSet = AnimatorInflater.loadAnimator(this, R.animator.cahspop);
        animSet.setTarget(textViewCash);
        animSet.start();

    }

    public void set_image_elevator(){
        imageElevator = (ImageView) findViewById(R.id.imageElevator);
        imageElevator2 = (ImageView) findViewById(R.id.imageElevator2);
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


        textViewFolkIn = (TextView) findViewById(R.id.textViewFolkIn);
        textViewFolkIn.setText(Integer.toString(elevators[0].getNb_person_in()));

        textViewFolkIn2 = (TextView) findViewById(R.id.textViewFolkIn2);
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
            IV_to_move.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        int nb_of_folk_waiting[] = new int[11];
        for (int i=0;i<nb_of_folk_waiting.length;i++){
            nb_of_folk_waiting[i] = 0;
        }
        for(int i=0;i<folks.size();i++){
            if (folks.get(i).isTreated()){
                ImageView IV_to_move = IV_folks.get(folks.get(i).getWanted_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 0, 0));
                ObjectAnimator anim = ObjectAnimator.ofFloat(IV_to_move, "translationX", off_set_X, off_set_X + 50);
                anim.setDuration(300);
                anim.start();
            }

            if(folks.get(i).isWaiting()){
                Log.i("Waiting", folks.get(i).getName() + " ON " + folks.get(i).getCurrent_floor());
                ImageView IV_to_move = IV_folks.get(folks.get(i).getCurrent_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 255, 255));
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

        iter = 0;
        simu = new Simulation(ma, folks, elevators);
        repaint();
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
                                        Random rand = new Random();
                                        int ran = rand.nextInt(25);
                                        int test = rand.nextInt(100);
                                        if(test>70)
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
        buttonUpCapacity.setText("CAP+1 ($"+simu.getUp_capacity_price()+")");

        buttonUpCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simu.canAffordUpdateCapacity()){
                    elevators[0].addCapacity();
                    simu.updateCapacityPrice();
                    buttonUpCapacity.setText("CAP+1 ($"+simu.getUp_capacity_price()+")");
                    update_data();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not enough minerals", Toast.LENGTH_SHORT);
                    toast.show();
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





        imageFW1 = (ImageView) findViewById(R.id.imageFolkW1);
        imageFW2 = (ImageView) findViewById(R.id.imageFolkW2);
        imageFW3 = (ImageView) findViewById(R.id.imageFolkW3);
        imageFW4 = (ImageView) findViewById(R.id.imageFolkW4);
        imageFW5 = (ImageView) findViewById(R.id.imageFolkW5);
        imageFW6 = (ImageView) findViewById(R.id.imageFolkW6);
        imageFW7 = (ImageView) findViewById(R.id.imageFolkW7);
        imageFW8 = (ImageView) findViewById(R.id.imageFolkW8);
        imageFW9 = (ImageView) findViewById(R.id.imageFolkW9);
        imageFW10 = (ImageView) findViewById(R.id.imageFolkW10);
        imageFW11 = (ImageView) findViewById(R.id.imageFolkW11);

        textViewl10 = (TextView) findViewById(R.id.textViewl10);
        textViewl9 = (TextView) findViewById(R.id.textViewl9);
        textViewl8 = (TextView) findViewById(R.id.textViewl8);
        textViewl7 = (TextView) findViewById(R.id.textViewl7);
        textViewl6 = (TextView) findViewById(R.id.textViewl6);
        textViewl5 = (TextView) findViewById(R.id.textViewl5);
        textViewl4 = (TextView) findViewById(R.id.textViewl4);
        textViewl3 = (TextView) findViewById(R.id.textViewl3);
        textViewl2 = (TextView) findViewById(R.id.textViewl2);
        textViewl1 = (TextView) findViewById(R.id.textViewl1);
        textViewl0 = (TextView) findViewById(R.id.textViewl0);


        IV_folks = new ArrayList<>();
        IV_folks.add(imageFW1);IV_folks.add(imageFW2);IV_folks.add(imageFW3);IV_folks.add(imageFW4);IV_folks.add(imageFW5);IV_folks.add(imageFW6);IV_folks.add(imageFW7);IV_folks.add(imageFW8);IV_folks.add(imageFW9);IV_folks.add(imageFW10);IV_folks.add(imageFW11);

        TV_folks = new ArrayList<>();
        TV_folks.add(textViewl0);TV_folks.add(textViewl1);TV_folks.add(textViewl2);TV_folks.add(textViewl3);TV_folks.add(textViewl4);TV_folks.add(textViewl5);TV_folks.add(textViewl6);TV_folks.add(textViewl7);TV_folks.add(textViewl8);TV_folks.add(textViewl9);TV_folks.add(textViewl10);

        reset_init_simu();

        elevators[1].setCapacity(0);

        iter = 0;

        repaint();

        setOnClickListenerReset();
        setOnClickListenerRun();
        setOnClickListenerAddFolk();
        setOnClickListenerUpdate();

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
