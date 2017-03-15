package com.example.tinku.googlemapsdemotest1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button startButton;
    TextView highScoreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setContentView(R.layout.activity_maps);


        getSupportActionBar().setTitle("Map Quiz");

        TextView controlsTextView = (TextView)findViewById(R.id.controlsTextView);

        controlsTextView.setText("\t\t                 INSTRUCTIONS           " +
                                "\nSelect answer on Map with \"Long Click\" \n" +
                                 "             Hint 1: Country Name            " +
                                "\n Hint 2: Blue Circle indicating the area of city " +
                                 "\nSkip: Shows answer and skips the current city  ");

        highScoreTextView = (TextView)findViewById(R.id.highScoreTextView);
        highScoreTextView.setText("");






        //delete this
        /*
        SharedPreferences sharedPref = getSharedPreferences("highScore",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("highScore",0);
        editor.commit();
        */

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SharedPreferences sharedPref = getSharedPreferences("highScore", Context.MODE_PRIVATE);
        highScoreTextView = (TextView)findViewById(R.id.highScoreTextView);

        long highScore = sharedPref.getLong("highScore",0);
        int longestSpree = sharedPref.getInt("longestSpree",0);
        highScoreTextView.setText("High Score: " + Long.toString(highScore) + "\nLongest Spree: " +Integer.toString(longestSpree));



    }



    public void onStartButtonClick(View view){
        startButton = (Button)view;
        Intent myIntent = new Intent(this, MapsActivity.class);
        startActivity(myIntent);
    }

}
