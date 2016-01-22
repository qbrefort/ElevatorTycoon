package com.pgm.qbr.elevatortycoon;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private ImageView imageElevator;
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
    private List<ImageView> IV_folks;
    private List<Folk> folks;
    private Simulation simu;
    private Elevator elevator_1;
    private String[] folks_name;

    public void update_image(){
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
        }
        for(int i=0;i<folks.size();i++){
            if (folks.get(i).isTreated()){
                ImageView IV_to_move = IV_folks.get(folks.get(i).getWanted_floor());
                IV_to_move.setVisibility(View.VISIBLE);
                IV_to_move.setBackgroundColor(Color.rgb(255, 0, 0));
                folks.remove(i);
            }
            else if(folks.get(i).isWaiting()){
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

        elevator_1 = new Elevator();
        Folk gerard = new Folk(elevator_1,"Gerard");
        Folk jeanmich = new Folk(elevator_1,"JeanMich");
        Folk ray = new Folk(elevator_1,"Raymond");

        folks = new ArrayList<>();

        folks.add(gerard);
        folks.add(jeanmich);
        folks.add(ray);

        simu = new Simulation(folks,elevator_1);

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



        update_image();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_image();
                simu.iterate();
                update_image();


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
