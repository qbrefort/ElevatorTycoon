package com.pgm.qbr.elevatortycoon;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ImageView imageElevator;
    public ImageView imageFW1;
    public ImageView imageFW2;
    public ImageView imageFW3;
    public ImageView imageFW4;
    public ImageView imageFW5;
    public ImageView imageFW6;
    public ImageView imageFW7;
    public ImageView imageFW8;
    public ImageView imageFW9;
    public ImageView imageFW10;
    public ImageView imageFW11;
    public List<ImageView> IV_folks;
    public List<Folk> folks;
    public Simulation simu;
    public Elevator elevator_1;
    public String[] folks_name;
    private int iter;

    public void repaint(){
        set_image_elevator();
        set_image_Folk();
    }

    public void set_image_elevator(){
        imageElevator = (ImageView) findViewById(R.id.imageElevator);
        float cf = simu.getFloor_elevator();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
        params.topMargin = (int) ((10-cf)*1175/10+5);
        params.leftMargin = 5;
        imageElevator.setLayoutParams(params);
    }



    public void set_image_Folk(){
        for(float i=0;i<IV_folks.size();i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
            params.leftMargin = 250;
            params.topMargin = (int) ((10 - i) * 1175 / 10 + 5);
            IV_folks.get((int) i).setLayoutParams(params);
            IV_folks.get((int) i).setVisibility(View.INVISIBLE);
            IV_folks.get((int) i).setBackgroundColor(Color.rgb(255, 255, 255));
        }
        for(int i=0;i<folks.size();i++){
            if (folks.get(i).isTreated()){
                ImageView IV_to_move = IV_folks.get(folks.get(i).getWanted_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 0, 0));
            }
            if(folks.get(i).isWaiting()){
                Log.i("Waiting",folks.get(i).getName()+"ON "+folks.get(i).getCurrent_floor());
                ImageView IV_to_move = IV_folks.get(folks.get(i).getCurrent_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 255, 255));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        elevator_1 = new Elevator(this);
        Folk gerard = new Folk(this,elevator_1,"Gerard");
        Folk jeanmich = new Folk(this,elevator_1,"JeanMich");
        Folk ray = new Folk(this,elevator_1,"Raymond");

        folks = new ArrayList<>();

        folks.add(gerard);
        folks.add(jeanmich);
        folks.add(ray);

        simu = new Simulation(this,folks,elevator_1);

        iter = 0;

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

        IV_folks = new ArrayList<>();
        IV_folks.add(imageFW1);IV_folks.add(imageFW2);IV_folks.add(imageFW3);IV_folks.add(imageFW4);IV_folks.add(imageFW5);IV_folks.add(imageFW6);IV_folks.add(imageFW7);IV_folks.add(imageFW8);IV_folks.add(imageFW9);IV_folks.add(imageFW10);IV_folks.add(imageFW11);

        repaint();

        FloatingActionButton resb = (FloatingActionButton) findViewById(R.id.reset);
        resb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = MainActivity.this;
                elevator_1 = new Elevator(ma);
                Folk gerard = new Folk(ma,elevator_1,"Gerard");
                Folk jeanmich = new Folk(ma,elevator_1,"JeanMich");
                Folk ray = new Folk(ma,elevator_1,"Raymond");

                folks = new ArrayList<>();

                folks.add(gerard);
                folks.add(jeanmich);
                folks.add(ray);

                iter = 0;

                simu = new Simulation(ma,folks,elevator_1);
                repaint();
            }
        });

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
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                        }
                        }
                }).start();




//                Random rand = new Random();
//                int test = rand.nextInt(100);
//                if(test<5){
//                    int int_name = rand.nextInt(folks_name.length);
//                    Folk new_folk = new Folk(elevator_1,folks_name[int_name]);
//                    folks.add(new_folk);
//                }

            }
        });
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
