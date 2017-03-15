package com.example.tinku.googlemapsdemotest1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import static android.location.Location.distanceBetween;

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback,
                                                                OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private ArrayList<placeData> places;
    private ArrayList<placeData> placesPro;
   // private int listLength;
    private LatLng questionLatLng;
    private double Score;
    int spree;
    int skipsLeft;

    private double distance;
    private String currentCity;
    private int index = 0;
    double timeLeft;
    int placesCount;

    int hintNumber;

    boolean addPlacesProComplete;

    double currentScore;

   // Intent mainActivityIntent = new Intent(this,MainActivity.class);

    boolean isGameOver;

    CountDownTimer currentCountDownTimer;

    long startTime = 0L;
    long timeInMilliSeconds = 0L;
    long updatedTime = 0L;

    Handler handler = new Handler();

    @Override
    public void onMapClick(LatLng latLng) {

    }

    static class placeData
    {
        String cityName;
        String countryName;
        double latitude;
        double longitude;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Score = 0;
        spree = 0;
        skipsLeft = 3;
        index=0;
        isGameOver = false;
        addPlacesProComplete = false;
        addPlacesPro();





    }
/*
    @Override
    protected void onResume(){
        super.onResume();

        isGameOver = false;
    }
*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // CALL SET QUESTION
        isGameOver = false;

        TextView updatesTextView = (TextView)findViewById(R.id.currentScoreTextView);
        updatesTextView.setText("");

        TextView locateTextView = (TextView)findViewById(R.id.locateTextView);
        locateTextView.setText("Locate: ???");

        TextView scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        scoreTextView.setText("Total Score: 0" );

        Button hintButton = (Button)findViewById(R.id.hintButton);
        hintButton.setText("Click Here for Hint");

        Button skipButton = (Button)findViewById(R.id.skipButton);
        skipButton.setText("Skips Left: 3");



        TextView timerTextView = (TextView)findViewById(R.id.timerTextView);
        timerTextView.setText("Time Left: ???");

       // addPlacesPro();
        if(!isGameOver) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3000ms
                    mMap.clear();
                    // currentCountDownTimer.cancel();
                    while(!addPlacesProComplete)
                    {

                    }
                    setQuestion();
                    // handler.postDelayed(updateTimerThread,0);



                    runTimer();
                }
            }, 3000);
        }


        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onMapLongClick(LatLng point) {
        mMap.clear();


       // int timeLeft = 20;
        Marker answerMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Your Answer"));

        answerMarker.showInfoWindow();

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        calculateDistance(point);

        //SCORE


        //CALL SET QUESTION
         if(!isGameOver) {
             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     //Do something after 3000ms
                     mMap.clear();
                     spree++;
                     // currentCountDownTimer.cancel();


                     setQuestion();
                     // handler.postDelayed(updateTimerThread,0);

                    currentCountDownTimer.cancel();

                     runTimer();
                 }
             }, 3000);
         }


    }

    public void runTimer()
    {
        currentCountDownTimer = new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                TextView timerTextView = (TextView)findViewById(R.id.timerTextView);
                timeLeft = (double)(millisUntilFinished/1000);
                timerTextView.setText("Time Left: " + Double.toString(timeLeft));


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //call game over method
                if(!isGameOver) {
                    gameOver(1);
                }
            }

        }.start();
    }
/*
    private Runnable updateTimerThread = new Runnable(){
        public void run() {
            timeInMilliSeconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliSeconds / 1000);

            handler.postDelayed(this,0);
        }

    };
*/

    public void calculateDistance(LatLng point) {
        float[] result = new float[3];
        distanceBetween(questionLatLng.latitude, questionLatLng.longitude, point.latitude, point.longitude, result);
        distance = result[0] / 1000;

       Marker questionMarker = mMap.addMarker(new MarkerOptions()
        .position(questionLatLng)
        .title(currentCity)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );

        questionMarker.showInfoWindow();


        Context context = getApplicationContext();
        String text = "You were off from " + currentCity + " by "+ Integer.toString((int) distance) + "km";
        int duration = Toast.LENGTH_SHORT;

        /*
        TextView updatesTextView = (TextView)findViewById(R.id.updatesTextView);
        updatesTextView.setText(text);
        */

       // mMap.addMarker(new MarkerOptions().position(questionLatLng).title("Question"));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(questionLatLng.latitude, questionLatLng.longitude), point)
                .width(5)
                .color(Color.BLUE));

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if(distance > 100)
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3000ms
                   mMap.clear();

                    gameOver(2);
                }
            }, 3000);
        }
        else {
            currentScore = (int)((((double)100 - distance) + (300-timeLeft))/4);
            if(hintNumber == 1)
                currentScore /= 2;
            if(hintNumber == 0)
                currentScore /= 3;

            Score += (int)currentScore ;
            updateScore();
        }
    }

    public void gameOver(int reason)
    {
        isGameOver = true;
        SharedPreferences sharedPref = getSharedPreferences("highScore",Context.MODE_PRIVATE);


        Context context = getApplicationContext();
        CharSequence text = "";

        currentCountDownTimer.cancel();

        if(reason == 1)
        {
            text = "You were too slow!\n\tGAME OVER";
        }
        else if(reason == 2)
        {
            text = "You were too far(>100 km)!\n\tGAME OVER";
        }
        else
        {
            text = "No more skips left!\nGAME OVER";
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


            /*
            SharedPreferences.Editor editor = sharedPref.edit();
            long highScore = sharedPref.getLong("highScore",0);
            if((long)Score > highScore)
            {
                highScore = (long)Score;
                text ="New High Score! " + Double.toString((double)highScore);
                toast = Toast.makeText(context,text,duration);
                toast.show();

            }
            */

        SharedPreferences.Editor editor = sharedPref.edit();
        long highScore = sharedPref.getLong("highScore",0);
        int longestSpree = sharedPref.getInt("longestSpree",0);
        if((long)Score > highScore || spree > longestSpree)
        {

            if((long)Score > highScore && spree < longestSpree)
            {
                text ="New High Score! " + Long.toString((long)Score);
                editor.putLong("highScore",(long)Score);
                editor.commit();
            }
            if((long)Score <= highScore && spree > longestSpree)
            {

                text = "New Longest Spree! " + Integer.toString(spree);
                editor.putInt("longestSpree",spree);
                editor.commit();
            }
           if((long)Score > highScore && spree > longestSpree)
            {
                text = "New High Score! " + Long.toString((long)Score) + "\n" +"New Longest Spree! " + Integer.toString(spree);
                editor.putInt("longestSpree",spree);
                editor.putLong("highScore",(long)Score);
                editor.commit();
            }





            final Toast toast1 = Toast.makeText(context,text,duration);
            toast.show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast1.show();
                }
            }, Toast.LENGTH_SHORT);


        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                MapsActivity.this.finish();
                // startActivity(mainActivityIntent);

            }
        }, Toast.LENGTH_SHORT);
    }


    // SET QUESTION METHOD
    public void setQuestion() {
       // Random rand = new Random();
        if(currentCountDownTimer != null)
            currentCountDownTimer.cancel();
        //int index = rand.nextInt(places.size() - 1);
        if(!isGameOver) {

            Button hintButton = (Button)findViewById(R.id.hintButton);
            hintButton.setText("Click Here for Hint");
            hintNumber = 2;

            TextView currentScoreTextView = (TextView)findViewById(R.id.currentScoreTextView);
            currentScoreTextView.setText("Hint 1:???");

            index = 1;
            Random rand = new Random();
            index = rand.nextInt(placesPro.size()-1) + 1;

            currentCity = placesPro.get(index).cityName;
            //question toast
            Context context = getApplicationContext();
            CharSequence text = "Find " + currentCity;
            int duration = Toast.LENGTH_SHORT;

            //  TextView updatesTextView = (TextView)findViewById(R.id.updatesTextView);
            // updatesTextView.setText("Find " + places.get(index).cityName);


            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            TextView locateTextView = (TextView) findViewById(R.id.locateTextView);
            locateTextView.setText("Locate: " + currentCity);

            //set a global variable for question latlng
            questionLatLng = new LatLng(placesPro.get(index).latitude, placesPro.get(index).longitude);


            //index = (index + rand.nextInt(placesCount)) % (placesCount-1);
        }

    }
/*
    public void addPlaces(){
        places = new ArrayList<placeData>();

        placeData temp;

        temp = new placeData();

        temp.cityName = "Delhi";
        temp.countryName = "India";
        temp.latitude = 28.6699929;
        temp.longitude = 77.23000403;

        places.add(0,temp);

        temp = new placeData();

        temp.cityName = "Moscow";
        temp.countryName = "Russia";
        temp.latitude = 55.7521;
        temp.longitude = 37.6155;

        places.add(1,temp);

        temp = new placeData();

        temp.cityName = "London";
        temp.countryName = "Britain";
        temp.latitude = 51.4999;
        temp.longitude = -0.1167;

        places.add(2,temp);

        temp = new placeData();

        temp.cityName = "Tokyo";
        temp.countryName = "Japan";
        temp.latitude = 35.6850;
        temp.longitude = 139.7514;

        places.add(3,temp);

        temp = new placeData();

        temp.cityName = "Cairo";
        temp.countryName = "Egypt";
        temp.latitude = 30.0499;
        temp.longitude = 31.2499;

        places.add(4,temp);

        temp = new placeData();

        temp.cityName = "Sydney";
        temp.countryName = "Australia";
        temp.latitude = -33.9200;
        temp.longitude = 151.1851;

        places.add(5,temp);



    }

    */

    public void addPlacesPro(){

        placesPro = new ArrayList<placeData>();
        placeData temp;


        InputStream inputStream;

        String[] ids;
       // int count =0;

        inputStream = getResources().openRawResource(R.raw.worldcities);

        placesCount = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {


                String csvLine;
                while ((csvLine = reader.readLine()) != null) {


                    ids = csvLine.split(",");
                    try {
                        temp = new placeData();

                        temp.cityName = "" + ids[0];
                        temp.countryName = "" + ids[5];
                        temp.latitude = Double.parseDouble(ids[2]);
                        temp.longitude = Double.parseDouble(ids[3]);

                        placesPro.add(placesCount, temp);
                        placesCount++;
                        /*
                        Log.d("placesPro", ids[1] + " " + ids[5] + " " + ids[2] + " " + ids[3]);
                        Log.d("placesPro", placesPro.get(placesCount - 1).cityName + " " + placesPro.get(placesCount - 1).countryName +
                                " " + Double.toString(placesPro.get(placesCount - 1).latitude) + " " + Double.toString(placesPro.get(placesCount - 1).longitude));
                        Log.d("placesPro", Integer.toString(placesCount));
                         */
                    } catch (Exception e) {
                        Log.e("Unknown fuck", e.toString());
                    }
                }



        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        Log.d("placesPro","exiting with placesProComplete = true");
        addPlacesProComplete = true;

    }

    public void updateScore(){
        TextView scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        scoreTextView.setText("Total Score: " +Integer.toString((int)Score));

       // TextView currentScoreTextView = (TextView)findViewById(R.id.currentScoreTextView);
      //  currentScoreTextView.setText("Score for previous city: " + Integer.toString((int)currentScore) + "/10");
    }




    public void onHintButtonClick(View view){

        if(hintNumber == 2)
        {
            TextView currentScoreTextView = (TextView)findViewById(R.id.currentScoreTextView);
            currentScoreTextView.setText("Country: " + placesPro.get(index).countryName);
            Button hintButton = (Button)findViewById(R.id.hintButton);
            hintButton.setText("Hint 2");
            hintNumber--;
        }
        else if(hintNumber == 1) {
            Random rand = new Random();
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(questionLatLng.latitude + rand.nextFloat(), questionLatLng.longitude + rand.nextFloat()))
                    .radius(300000)
                    .strokeColor(Color.BLUE)

            );
            Button hintButton = (Button)findViewById(R.id.hintButton);
            hintButton.setText("No hints left");

            hintNumber--;
        }
        else {

        }
    }

    public void onSkipButtonClick(View view){
        Button skipButton = (Button)findViewById(R.id.skipButton);
        if(skipsLeft > 0) {
            skipButton.setText("Skips Left: " + --skipsLeft);
            Marker questionMarker = mMap.addMarker(new MarkerOptions()
                    .position(questionLatLng)
                    .title(currentCity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );

            questionMarker.showInfoWindow();

            if (currentCountDownTimer != null)
                currentCountDownTimer.cancel();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 3000ms
                    mMap.clear();
                    setQuestion();


                    runTimer();
                }
            }, 10000);


        }
        else
        {
            skipButton.setText("No more skips left!");
            gameOver(3);
        }
    }
}
