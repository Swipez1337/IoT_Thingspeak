package com.example.iot_thingspeak;

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
import com.android.volley.toolbox.JsonArrayRequest;
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
    boolean ledStatus, fanStatus, initTemp = true, testStatus = false;


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
            readTemp1 = 25;
            initTemp = false;
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

    public void updateData(View view) {
        getLedStatus(view);
        getFanStatus(view);
        getTempStatus(view); //currently sets a fixed value!!
    }


    public void clickButton0(View view) {

        //Testing with turning on/off LED
        if (ledStatus) { //turned on, turn off
            ledStatus = false;
            turnOffLED();
        } else { //turned off, turn on
            ledStatus = true;
            turnOnLED();
        }

        //Update view of LED status
        TextView textView = (TextView) findViewById(R.id.textview2);
        if (ledStatus) {
            textView.setText(getString(R.string.thingspeakAction_LEDon));

        } else {
            textView.setText(getString(R.string.thingspeakAction_LEDoff));
        }

        /*
        //Update view of temperature
        TextView textView = (TextView) findViewById(R.id.textview2);
        textView.setText(String.valueOf(temperatureVal1));
*/

        if (testStatus) {
            Toast.makeText(getApplicationContext(), "TEST1", Toast.LENGTH_SHORT).show();
        }

    }


    public void clickButton2(View view) {
        //request current temp

        String url = "https://api.thingspeak.com/channels/1710056/fields/2.json?api_key=D5UZ9WBG9IXRLLTD&results=1";


        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); //(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressBar.setVisibility(View.GONE);
                //courseRV.setVisibility(View.VISIBLE);
                    // creating a new json object and
                    // getting each object from our json array.
                Log.d("a", response.toString());
                    try {
                        // we are getting each json object.
                        JSONArray responseObj = response.getJSONArray("feeds");

                        Log.d("b", responseObj.toString());
                        // now we get our response from API in json object format.
                        // in below line we are extracting a string with
                        // its key value from our json object.
                        // similarly we are extracting all the strings from our json object.
                        JSONObject field1 = responseObj.getJSONObject(0);
                        Log.d("e", field1.toString());//responseObj.toString());
                        String field1_2 = field1.get("field2").toString();
                        Log.d("e", field1_2.toString());//responseObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("catch block", response.toString());
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
                Log.d("error response", error.toString());
            }
        });
        queue.add(jsonArrayRequest);


/*
        String url = "https://api.thingspeak.com/channels/1710056/fields/1.json?api_key=D5UZ9WBG9IXRLLTD&results=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        */

        /*
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.thingspeak.com/channels/1710056/fields/1.json?api_key=D5UZ9WBG9IXRLLTD&results=1";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response" + response.substring(0,500));
                        // Display the first 500 characters of the response string.


                        Log.d("Req", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error");
                //System.out.println("Error");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


*/



        /*
        //Testing with turning on/off LED
        if (ledStatus) { //turned on, turn off
            ledStatus = false;
            turnOffLED();
        } else { //turned off, turn on
            ledStatus = true;
            turnOnLED();
        }

        //Update view of LED status
        TextView textView = (TextView) findViewById(R.id.textview5);
        if (ledStatus) {
            textView.setText(getString(R.string.thingspeakAction_LEDon));

        } else {
            textView.setText(getString(R.string.thingspeakAction_LEDoff));
        }


         */
        /*
        //Update view of temperature
        TextView textView = (TextView) findViewById(R.id.textview2);
        textView.setText(String.valueOf(temperatureVal1));
*/

        /*
        if (testStatus) {
            Toast.makeText(getApplicationContext(), "TEST1", Toast.LENGTH_SHORT).show();
        }
       */


    }

    private void setRepoListText(String error_while_calling_rest_api) {
        Log.d("a","a");
    }

    //decrement temp
    public void clickButton3(View view) {
        if (initTemp) {
            updateData(view);
        } //in case values have not been read yet

        if (wantedTemp1 > 0) { //no negative values
            wantedTemp1--;

            TextView textView = (TextView) findViewById(R.id.textview6);
            textView.setText(String.valueOf(wantedTemp1));

        }
    }


    //increment temp
    public void clickButton4(View view) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.thingspeak.com/update?api_key=SOR94XAST8J94V4W&field1=" + 30 ;//wantedTemp1;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response" + response.substring(0,500));
                        // Display the first 500 characters of the response string.
                        Log.d("Req", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error");
                //System.out.println("Error");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);










        /*
        if (initTemp) {
            updateData(view);
        } //in case values have not been read yet

        wantedTemp1++;

        TextView textView = (TextView) findViewById(R.id.textview6);
        textView.setText(String.valueOf(wantedTemp1));

*/
    }

    /**
     * TODO: Implement turn on LED function
     */
    public void turnOffLED() {

    }

    /**
     * TODO: Implement turn off LED function
     */
    public void turnOnLED() {

    }

    /**
     * TODO: Adjust temperature function
     */
    public void adjustTemp() {

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
}