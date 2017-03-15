package com.example.tinku.googlemapsdemotest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getSupportActionBar().setTitle("Score");

        int Score;
        int distance;



            Score = getIntent().getIntExtra("Score",0);
            distance = getIntent().getIntExtra("distance",0);


        TextView textView1 = (TextView)findViewById(R.id.textView1);
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        TextView textView3 = (TextView)findViewById(R.id.textView3);

        textView1.setText("You were off by " + Integer.toString(distance) + "kms");
        textView2.setText("You scored: " + Integer.toString(20000-distance));
        textView3.setText("Total Score: " + Integer.toString(Score + 20000-distance));



    }
    Button myButton;
    public void onScoreNextButtonClick(View view){
        myButton = (Button)view;
        finish();
    }

}
