package com.example.iot_thingspeak;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.iot_thingspeak.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // Declaring public variables
    int readTemp1;
    int wantedTemp1;
    int wantedFanSpeed;
    int tempCounter = 0;
    boolean ledStatus, fanStatus, initTemp = true, testStatus = false;
    String test;
    String field2, field3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        this.getSupportActionBar().hide();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }





    public String getFanStatus(String string){

        return "";
    }




    /**
     * Get LED status via ThingSpeak API
     */
    public void getLedStatus(View view) {
        if (ledStatus) {
            TextView textView = (TextView) findViewById(R.id.textview2);
            textView.setText(getString(R.string.thingspeakAction_LEDon));
        } else {
            TextView textView = (TextView) findViewById(R.id.textview2);
            textView.setText(getString(R.string.thingspeakAction_LEDoff));
        }
    }


    public void tempInit(View view) {
        if (initTemp) {
            wantedTemp1 = readTemp1 ;
            initTemp = false;

            //update view of wanted temp
            TextView textView = (TextView) findViewById(R.id.textview6);
            textView.setText(String.valueOf(wantedTemp1));
        }
    }


    /**
     * Get temperature status via ThingSpeak API
     */
    public void getTempStatus(View view) {
        tempInit(view);
        wantedTemp1 = readTemp1;

        TextView textView = (TextView) findViewById(R.id.textview6);
        textView.setText(String.valueOf(wantedTemp1));

    }


    /**
     * Set wanted temperature
     */
    public void setTemp() {
        wantedTemp1 = 20;

    }


    /**
     * Get fan status via ThingSpeak API
     */
    public void getFanStatus(View view) {
        //fanStatus = true || false;
        if (ledStatus) {
            TextView textView = (TextView) findViewById(R.id.textview5);
            textView.setText(getString(R.string.thingspeakAction_LEDon));
        } else {
            TextView textView = (TextView) findViewById(R.id.textview5);
            textView.setText(getString(R.string.thingspeakAction_LEDoff));
        }
    }


    /**
     * Set fan status
     */
    public void setFanStatus(View view) {
        fanStatus = !fanStatus;
    }


    /**
     * Function to send API calls to ThingSpeak as well as call a view update of values
     * @param view
     */
    @SuppressLint("SetTextI18n")
    public void updateData(View view) {



        getCurrentTemp(); //get current temp via api call
        getCurrentFanspeed(); //get current fan speed via api call
        updateView(view); //update views testing
        tempCounter ++;

        if (tempCounter > 1) { //CURRENT ISSUE: It only updates the first time after it has been pressed twice. This counter is therefore to be removed at a later time.
            TextView textView = (TextView) findViewById(R.id.textview5); //view current rounded temp

            //BE AWARE THE BELOW 2 LINES CURRENTLY CRASHES THE CODE IF IT IS NULL
            textView.setText(Integer.toString(field2Convert(getField2())));
            readTemp1 = field2Convert(getField2());

            //update fan setting
            TextView textViewfan = (TextView) findViewById(R.id.textview2); //view current fan speed
            textViewfan.setText(getField3());

            tempCounter = 0; //reset temporary counter


            if (initTemp) {
                tempInit(view);
            }
            else {
                setWantedTemp(wantedTemp1); // set wanted temp via api call
            }


        }

    }


    /**
     * Function to update view of the different values
     * @param view
     */
    public void updateView(View view){

        //current temp val
        TextView textView = (TextView) findViewById(R.id.textview5);
        textView.setText(getField2());

    }

    public void fetchData(View view){
        //getFanStatus(view);
        getTempStatus(view); //currently sets a fixed value!!
    }

    //high fan speed button
    public void clickButton0(View view) {
        setFanSpeed(2); //2 is high setting

        TextView textView = (TextView) findViewById(R.id.textview2); //update view
        textView.setText("High");

        //Log.d("a", Integer.toString(field2Convert("21.95032")));
        //Log.d("a", Integer.toString(field2Convert(getField2())));
    }


    //medium fan speed button
    public void clickButton2(View view) {
        setFanSpeed(1); //1 is medium setting

        TextView textView = (TextView) findViewById(R.id.textview2); //update view
        textView.setText("Medium");

    }

    //Off(low) fan speed button
    public void clickButton5(View view) {
        setFanSpeed(0); //0 is low/off setting

        TextView textView = (TextView) findViewById(R.id.textview2); //update view
        textView.setText("Off");

    }

    //decrement temp
    public void clickButton3(View view) {

        getCurrentTemp();

        //Toast.makeText(MainActivity.this, test, Toast.LENGTH_SHORT).show();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("something", getField2());

        /*
        TextView textView = (TextView) findViewById(R.id.textview2);
        test = textView.getText().toString();
        Toast.makeText(MainActivity.this, test , Toast.LENGTH_SHORT).show();


         */

        //wantedTemp1 = getCurrentTemp();
        //TextView textView = (TextView) findViewById(R.id.textview6);
        //textView.setText(String.valueOf(wantedTemp1));
        /*
        if (initTemp) {
            updateData(view);
        } //in case values have not been read yet

        if (wantedTemp1 > 0) { //no negative values
            wantedTemp1--;

            TextView textView = (TextView) findViewById(R.id.textview6);
            textView.setText(String.valueOf(wantedTemp1));

        }

         */
    }




    //Inspiration for API read: https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
    public void getCurrentTemp(/* View view*/) {
        String url = "https://api.thingspeak.com/channels/1710056/fields/2.json?api_key=D5UZ9WBG9IXRLLTD&results=1";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); //(MainActivity.this);

        //get json array contents
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Select JSON array
                    JSONArray responseObj = response.getJSONArray("feeds");
                    //Select JSON object //field2JsonObject.toString())
                    JSONObject field2JsonObject = responseObj.getJSONObject(0);

                    String field2local = field2JsonObject.get("field2").toString();

                    Log.d("Field2 contents: ", field2local);

                    setField2(field2local);
                    Log.d("this", getField2());

                } catch (JSONException e) {
                    Log.d("GetCurrentTemp catch: ", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Data unavailable. Please try again in 10 seconds", Toast.LENGTH_SHORT).show();
                Log.d("GetCurrentTemp response", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }



    //Inspiration for API read: https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
    public void getCurrentFanspeed() {
        String url = "https://api.thingspeak.com/channels/1710056/fields/3.json?api_key=D5UZ9WBG9IXRLLTD&results=1";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); //(MainActivity.this);

        //get json array contents
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Select JSON array
                    JSONArray responseObj = response.getJSONArray("feeds");
                    //Select JSON object //field2JsonObject.toString())
                    JSONObject field3JsonObject = responseObj.getJSONObject(0);

                    String field3local = field3JsonObject.get("field3").toString();

                    Log.d("Field3 contents: ", field3local);

                    setField3(field3local);
                    Log.d("this", getField3());

                } catch (JSONException e) {
                    Log.d("GetCurrentTemp catch: ", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Data unavailable. Please try again in 10 seconds", Toast.LENGTH_SHORT).show();
                Log.d("GetCurrentFan response", error.toString());
            }
        });
        queue.add(jsonArrayRequest);
    }



    //Inspiration for API write: https://google.github.io/volley/simple.html
    public void setWantedTemp(int wantedTemp){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.thingspeak.com/update?api_key=SOR94XAST8J94V4W&field1=" + wantedTemp ;

// Request a string response from the provided API URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response" + response.substring(0,500));
                        // Display the first 500 characters of the response string.
                        Log.d("Wanted temperature: ", wantedTemp + " . Response id: " + response + " .");

                        if (response == null || response.equals("0")) { //if user is too quick and response is therefore 0
                            Log.d("Error", "User refreshed too soon");
                            Toast.makeText(MainActivity.this, "You are too fast for our servers to handle. Please wait a few seconds and try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setWantedTemp_error", "An error has occured");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    //Inspiration for API write: https://google.github.io/volley/simple.html
    public void setFanSpeed(int wantedFanSpeed){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.thingspeak.com/update?api_key=SOR94XAST8J94V4W&field3=" + wantedFanSpeed ;

// Request a string response from the provided API URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response" + response.substring(0,500));
                        // Display the first 500 characters of the response string.
                        Log.d("Wanted fanspeed: ", wantedFanSpeed + " . Response id: " + response + " .");

                        if (response == null || response.equals("0")) { //if user is too quick and response is therefore 0
                            Log.d("Error", "User refreshed too soon");
                            Toast.makeText(MainActivity.this, "You are too fast for our servers to handle. Please wait a few seconds and try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setWantedFan_error", "An error has occured");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    //increment temp button
    public void clickButton4(View view) {

        if (initTemp) {
            fetchData(view);
            initTemp = false;
        } //in case values have not been read yet

        wantedTemp1++;

        TextView textView = (TextView) findViewById(R.id.textview6);
        textView.setText(String.valueOf(wantedTemp1));

        //wantedTemp1 = 22;

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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setField2(String field2){
        this.field2 = field2;
    }

    public String getField2(){
        return field2;
    }

    public int field2Convert(String field2){
        //string to double
        double d = Double.parseDouble(field2);

        //round double to int
        int currTemp = (int) Math.round(d);

        //int to string

        Log.d("a", Integer.toString(currTemp));
        return currTemp;
    }

    public void setField3(String field3){
        this.field3 = field3;
    }

    public String getField3(){
        return field3;
    }
}