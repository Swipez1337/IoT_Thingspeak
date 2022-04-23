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

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // Declaring public variables
    int readTemp1, wantedTemp1, tempCounter = 0;
    boolean initTemp = true;
    String field2, field3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        getCurrentTemp(); //get current temp via api call
        getCurrentFanSpeed(); //get current fan speed via api call

    }

    /**
     * Initialize temperatures one time when opening app
     *
     * @param view is fragment 1 view
     */
    public void tempInit(View view) {
        if (initTemp) {
            wantedTemp1 = readTemp1;
            initTemp = false;

            //update view of wanted temp
            ((TextView) findViewById(R.id.textview6)).setText(String.valueOf(wantedTemp1));
        }
    }

    /**
     * Function to send API calls to ThingSpeak as well as call a view update of values
     *
     * @param view is fragment 1 view
     */
    @SuppressLint("SetTextI18n")
    public void updateData(View view) {

        updateView(view); //update views testing

        tempCounter++;

        if (tempCounter > 1) { //CURRENT ISSUE: It only updates the first time after it has been pressed twice. This counter is therefore to be removed at a later time.

            //BE AWARE THE BELOW 2 LINES CURRENTLY CRASHES THE CODE IF IT IS NULL
            if (!Objects.equals(getField2(), "null")) {
                ((TextView) findViewById(R.id.textview5)).setText(Integer.toString(field2Convert(getField2())));  //view current rounded temp
                readTemp1 = field2Convert(getField2());
            }

            //update fan setting //BE AWARE THE BELOW LINE CURRENTLY CAN CRASH THE CODE IF IT IS NULL
            ((TextView) findViewById(R.id.textview2)).setText(getField3());


            tempCounter = 0; //reset temporary counter

            //Initialize temperature variables etc.
            if (initTemp) {
                tempInit(view);
            } else {
                if (wantedTemp1 != readTemp1) {
                    setWantedTemp(wantedTemp1); // set wanted temp via api call
                }
            }
        }
    }


    /**
     * Function to update view of the different values
     *
     * @param view is fragment 1 view
     */
    public void updateView(View view) {

        //current temp val
        ((TextView) findViewById(R.id.textview5)).setText(getField2());

    }

    /**
     * Method to set the fan speed to highest setting
     *
     * @param view is fragment 1 view
     */
    public void clickButton0(View view) {
        setFanSpeed(2); //2 is high setting

        ((TextView) findViewById(R.id.textview2)).setText(getString(R.string.fan_high));
    }


    /**
     * Method to set the fan speed to medium
     *
     * @param view is fragment 1 view
     */
    public void clickButton2(View view) {
        setFanSpeed(1); //1 is medium setting

        ((TextView) findViewById(R.id.textview2)).setText(getString(R.string.fan_med));

    }

    /**
     * Method to decrement the wanted temperature and update the view
     *
     * @param view is fragment 1 view
     */
    public void clickButton3(View view) {
        if (initTemp) {
            updateData(view);
            initTemp = false;
        } //in case values have not been read yet

        wantedTemp1--;

        ((TextView) findViewById(R.id.textview6)).setText(String.valueOf(wantedTemp1));
    }


    /**
     * Method to increment the wanted temperature and update the view
     *
     * @param view is fragment 1 view
     */
    public void clickButton4(View view) {

        if (initTemp) {
            updateData(view);
            initTemp = false;
        } //in case values have not been read yet

        wantedTemp1++;

        ((TextView) findViewById(R.id.textview6)).setText(String.valueOf(wantedTemp1));
    }


    /**
     * Method to turn off the fan
     *
     * @param view is fragment 1 view
     */
    public void clickButton5(View view) {
        setFanSpeed(0); //0 is low/off setting

        ((TextView) findViewById(R.id.textview2)).setText(getString(R.string.fan_low));

    }


    /**
     * A method to get the current temperature via an API call to ThingSpeak
     * This method has inspiration from https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
     */
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


    /**
     * A method to get the current fan speed via API call to ThingSpeak
     * This method has inspiration from https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
     */
    public void getCurrentFanSpeed() {
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


    /**
     * A method to set the wanted temperature via. API call to ThingSpeak
     *
     * @param wantedTemp This method has inspiration from https://google.github.io/volley/simple.html
     */
    //Inspiration for API write: https://google.github.io/volley/simple.html
    public void setWantedTemp(int wantedTemp) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.thingspeak.com/update?api_key=SOR94XAST8J94V4W&field1=" + wantedTemp;

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
                            Toast.makeText(MainActivity.this, getString(R.string.toofast), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setWantedTemp_error", "An error has occurred");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Method for API call to set the fan speed 0,1,2 to ThingSpeak
     *
     * @param wantedFanSpeed This method has inspiration from https://google.github.io/volley/simple.html
     */
    public void setFanSpeed(int wantedFanSpeed) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.thingspeak.com/update?api_key=SOR94XAST8J94V4W&field3=" + wantedFanSpeed;

// Request a string response from the provided API URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response" + response.substring(0,500));
                        // Display the first 500 characters of the response string.
                        Log.d("Wanted fan speed: ", wantedFanSpeed + " . Response id: " + response + " .");

                        if (response == null || response.equals("0")) { //if user is too quick and response is therefore 0
                            Log.d("Error", "User refreshed too soon");
                            Toast.makeText(MainActivity.this, getString(R.string.toofast), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setWantedFan_error", "An error has occurred");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
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

    /**
     * Set method for field2
     *
     * @param field2 a string for the read temperature
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }

    /**
     * Get method for field2
     *
     * @return returns a string for the read temperature
     */
    public String getField2() {
        return field2;
    }

    /**
     * Convert field2 from string to double to an int
     *
     * @param field2 is the read temp in double type formatted as a string
     * @return return current temp as a rounded integer
     */
    public int field2Convert(String field2) {
        //string to double
        double d = Double.parseDouble(field2);

        //round double to int
        int currTemp = (int) Math.round(d);

        Log.d("a", Integer.toString(currTemp));
        return currTemp;
    }

    /**
     * set method for field3
     *
     * @param field3 field3 as a string which is the fan speed
     */
    public void setField3(String field3) {
        this.field3 = field3;
    }

    /**
     * get method for field3
     *
     * @return field3 as a string which is the fan speed
     */
    public String getField3() {
        return field3;
    }

}