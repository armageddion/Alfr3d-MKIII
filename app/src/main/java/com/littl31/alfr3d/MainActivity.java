package com.littl31.alfr3d;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.littl31.alfr3d.R;
import com.littl31.alfr3d.util.NetwrokChangeReceiver;

/**
 *
 */
public class MainActivity extends Activity {


    // detect changes in wifi connectivity so we can know when we are in Alfr3d's home
    NetwrokChangeReceiver wifi1 = new NetwrokChangeReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //registerReceiver(wifi1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /*
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                float curSpeed = location.getSpeed();
                if (curSpeed > 0.0 ){
                    TextView Alfr3dURLView = (TextView) findViewById(R.id.fullscreen_content);
                    Alfr3dURLView.setText("Current Speed: " + curSpeed);
                }
                else {
                    TextView Alfr3dURLView = (TextView) findViewById(R.id.fullscreen_content);
                    Alfr3dURLView.setText("Alfr3d\n-_-");
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fs__play, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. Use NavUtils to allow users
//            // to navigate up one level in the application structure. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            // TODO: If Settings has multiple levels, Up should navigate up
//            // that hierarchy.
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();

        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    protected void onPause() {
        super.onStop();

        try {
            unregisterReceiver(wifi1);
        } catch (Exception e) {
            Log.e("onPause", getClass()+" Releasing receivers-"+e.getMessage());
        }
    }


    protected void onResume() {
        registerReceiver(wifi1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(wifi1);
        } catch (Exception e) {
            Log.e("onDestroy", getClass()+" Releasing receivers-"+e.getMessage());
        }

    }

    public void home(View view){
        Intent home = new Intent(this, HomeActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(home);
    }

    // pop-up dialog to send a custom command
    public void custom(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Custom");
        alert.setMessage("Command");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Send custom command to Alfr3d
                sendButtonCommand(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    // a rather useless help menu
    public void help(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Help");
        alert.setMessage("Available custom commands are:\n" +
                "Hello\n" +
                "Blink\n" +
                "welcomehome\n" +
                "reboot");

        alert.show();
    }

    // settings menu
    public void settings(View view){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    // send commands to Alfr3d
    public void sendButtonCommand(String Command) {
        // Do something in response to button
        String message = Command;

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String alfr3d_url = mySharedPreferences.getString("alfr3d_url_preference", "url not set");
        String full_alfr3d_call = alfr3d_url;
        Log.d("Main", "Alfr3d URL:" + alfr3d_url);

        //TEMP DEBUGING STUFF>>>
        String homeSSID = mySharedPreferences.getString("home_ssid_preference", "ssid not set");
        Log.d("Main", "Home SSID:" + homeSSID);

        String method = mySharedPreferences.getString("method", "-1");

        if (method.equals("CGI")) {
            full_alfr3d_call = alfr3d_url + "/cgi-bin/alfr3d.cgi?command=" + message;
        } else if (method.equals("Node.js")) {
            // TODO: implement Node properly
            full_alfr3d_call = alfr3d_url + ":1337/" + message;
        } else if (method.equals("BottleRPC")) {
            // TODO: complete bottle implementation
            full_alfr3d_call = alfr3d_url + ":8080/" + message;
        }


        boolean node_enabled = mySharedPreferences.getBoolean("node_enabled", false);
        if (node_enabled == true) {
            //TODO Finish this bit
            full_alfr3d_call = alfr3d_url + "/complete this bit when you have it working in node";
        }

        // curl: "http://alfr3d.no-ip.org/cgi-bin/test2.py?command=Blink"
        if (!full_alfr3d_call.substring(0, 7).equalsIgnoreCase("http://")) {
            full_alfr3d_call = "http://" + full_alfr3d_call;
        }

        final String finalCall = full_alfr3d_call;
        TextView Alfr3dURLView = (TextView) findViewById(R.id.alfr3d_call);
        Alfr3dURLView.setText(full_alfr3d_call);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView mTextView = (TextView) findViewById(R.id.alfr3d_response);

        // log requested message
        final TextView Alfr3dLog = (TextView) findViewById(R.id.alfr3d_log);
        Alfr3dLog.append("\n" + message);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.append("\nResponse is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.append("\nThat didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}

