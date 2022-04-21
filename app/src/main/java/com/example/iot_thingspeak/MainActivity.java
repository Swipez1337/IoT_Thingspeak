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
    String test;
    String field2;


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


    /**
     * Function to send API calls to ThingSpeak as well as call a view update of values
     * @param view
     */
    public void updateData(View view) {
        setWantedTemp(wantedTemp1); // set wanted temp via api call


        fetchData(view); //update visual representation??
    }


    public void fetchData(View view){
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


    //Inspiration for API read: https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
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
                        Log.d("e1", field1.toString());//responseObj.toString());
                        String field1_2 = field1.get("field2").toString();
                        Log.d("e2", field1_2.toString());//responseObj.toString());
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


    //decrement temp
    public void clickButton3(View view) {

        getCurrentTemp(/*null, */ view);
        //Toast.makeText(MainActivity.this, test, Toast.LENGTH_SHORT).show();

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


public void printme(String string){
    Log.d("ThisAString", string);
    //test = string;
}

/*
    public interface VolleyCallback{
        void onSuccess(String result);
    }


    public void onSuccess(String result){
        test = result;
    }
*/

    //Inspiration for API read: https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/
    public int getCurrentTemp(/*final VolleyCallback callback,*/ View view) {

        final String[] something = {"1.1"};
        final String[] currentString = new String[1];
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
                    //currentString[0] = field2;

                    if (field2local == "null" || field2local == null || field2local == "0") {
                        field2local = "29.20315";
                    }
                    something[0] = field2local;
                    test = field2local;
                    Log.d("Field2 contents: ", field2local);
                    Log.d("new", test);
                    Log.d("newarray", something[0]);

                    //printme(field2local);

                    //callback.onSuccess(field2);

                    setField2(field2local);

                    //TextView textView = (TextView) findViewById(R.id.textview2);
                    //textView.setText(String.valueOf(field2));


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
        //Log.d("newarray2", something[0]);
        //Log.d("new2", test);

        //convert temp string to float
        /*
        float f = Float.parseFloat(currentString[0]);


        //round float to int
        int currTemp = Math.round(f);

        return currTemp;


         */
        return 1;
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
}