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
import android.widget.TextView;

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

    public List<ImageView> IV_folks;
    public List<TextView> TV_folks;
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
        for(int i=0;i<=10;i++){
            TV_folks.get(i).setText("");
        }
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
            int nb_of_folk_wainting = 0;
            if(folks.get(i).isWaiting()){
                Log.i("Waiting", folks.get(i).getName() + " ON " + folks.get(i).getCurrent_floor());
                ImageView IV_to_move = IV_folks.get(folks.get(i).getCurrent_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 255, 255));
                nb_of_folk_wainting++;
            }
            if(nb_of_folk_wainting > 0)
                TV_folks.get(folks.get(i).getCurrent_floor()).setText("x"+elevator_1.getFloor_queue()[folks.get(i).getCurrent_floor()]);
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

        repaint();

        FloatingActionButton resb = (FloatingActionButton) findViewById(R.id.reset);
        resb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = MainActivity.this;
                elevator_1 = new Elevator(ma);
                Folk gerard = new Folk(ma, elevator_1, "Gerard");
                Folk jeanmich = new Folk(ma, elevator_1, "JeanMich");
                Folk ray = new Folk(ma, elevator_1, "Raymond");

                folks = new ArrayList<>();

                folks.add(gerard);
                folks.add(jeanmich);
                folks.add(ray);

                iter = 0;

                simu = new Simulation(ma, folks, elevator_1);
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

            }
        });

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int rr = rand.nextInt(30);
                simu.getFolks().add(new Folk(MainActivity.this,elevator_1,folks_name[rr]));
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
