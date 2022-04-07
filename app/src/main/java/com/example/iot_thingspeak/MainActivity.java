package com.example.iot_thingspeak;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.iot_thingspeak.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // Declaring public variables
    int readTemp1 ;
    int wantedTemp1 ;
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

        /*
        binding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        //getLedStatus();

        //clickButton0();
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
        if (initTemp){
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

    public void updateData(View view){
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
        if (ledStatus){
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
            Toast.makeText(getApplicationContext(),"TEST1",Toast.LENGTH_SHORT).show();
        }

    }


    public void clickButton2(View view) {

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
        if (ledStatus){
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
            Toast.makeText(getApplicationContext(),"TEST1",Toast.LENGTH_SHORT).show();
        }

    }

    //decrement temp
    public void clickButton3(View view) {
        if (initTemp) {updateData(view);} //in case values have not been read yet

        if (wantedTemp1 > 0){ //no negative values
        wantedTemp1 --;

        TextView textView = (TextView) findViewById(R.id.textview6);
        textView.setText(String.valueOf(wantedTemp1));

        }
    }

    //increment temp
    public void clickButton4(View view) {
        if (initTemp) {updateData(view);} //in case values have not been read yet

        wantedTemp1 ++;

        TextView textView = (TextView) findViewById(R.id.textview6);
        textView.setText(String.valueOf(wantedTemp1));




    }

    /**
     TODO: Implement turn on LED function
     */
    public void turnOffLED() {

    }

    /**
     TODO: Implement turn off LED function
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