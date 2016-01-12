package com.littl31.alfr3d;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
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
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.littl31.alfr3d.R;
import com.littl31.alfr3d.util.NetwrokChangeReceiver;
import com.littl31.alfr3d.util.TypeWriter;

/**
 *
 */
public class MainActivity extends Activity {

    // detect changes in wifi connectivity so we can know when we are in Alfr3d's home
    NetwrokChangeReceiver wifi1 = new NetwrokChangeReceiver();

    // button views.. these will be used by a few methods below
    private TextView [] buttons = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // setup button view array
        buttons[0] = (TextView) findViewById(R.id.test_button1);
        buttons[1] = (TextView) findViewById(R.id.test_button2);
        buttons[2] = (TextView) findViewById(R.id.test_button3);
        buttons[3] = (TextView) findViewById(R.id.test_button4);
        buttons[4] = (TextView) findViewById(R.id.test_button5);
        buttons[5] = (TextView) findViewById(R.id.test_button6);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.GONE);
        }

        //registerReceiver(wifi1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        //writeStatusLine("initializing...");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create some windows just for fun
        window_anim((TextView) findViewById(R.id.alfr3d_response_bg));
        //window_anim((TextView) findViewById(R.id.alfr3d_win1));
        //window_anim((TextView) findViewById(R.id.alfr3d_win2));

        buttonAnim();

        //writeStatusLine("Done");
//        // Acquire a reference to the system Location Manager
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        // Define a listener that responds to location updates
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//
//                float curSpeed = location.getSpeed();
//                if (curSpeed > 0.0 ){
//                    Log.d("Main", "Speed: "+curSpeed);
//                    TextView mGeoSpeedView = (TextView) findViewById(R.id.geoSpeed);
//                    mGeoSpeedView.setText("Current Speed: " + curSpeed);
//                }
//                else {
//                    Log.d("Main","Speed: Alfr3d-_-");
//                    TextView mGeoSpeedView = (TextView) findViewById(R.id.geoSpeed);
//                    mGeoSpeedView.setText("Alfr3d\n-_-");
//                }
//
//                Log.d("Main", "Latitude:"+String.valueOf(location.getLatitude()));
//                TextView mLatitue = (TextView) findViewById(R.id.geoLatitude);
//                mLatitue.setText("Lat:"+String.valueOf(location.getLatitude()));
//                Log.d("Main", "Longitude:"+String.valueOf(location.getLongitude()));
//                TextView mLongitude = (TextView) findViewById(R.id.geoLongitude);
//                mLongitude.setText("Long:"+String.valueOf(location.getLongitude()));
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            public void onProviderEnabled(String provider) {}
//
//            public void onProviderDisabled(String provider) {}
//        };
//
//        // Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
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
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            window_anim((TextView) findViewById(R.id.alfr3d_response_bg));
            //window_anim((TextView) findViewById(R.id.alfr3d_win1));
            //window_anim((TextView) findViewById(R.id.alfr3d_win2));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(wifi1);
        } catch (Exception e) {
            Log.e("onPause", getClass()+" Releasing receivers-"+e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifi1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        window_anim((TextView) findViewById(R.id.alfr3d_response_bg));
        //window_anim((TextView) findViewById(R.id.alfr3d_win1));
        //window_anim((TextView) findViewById(R.id.alfr3d_win2));
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

    public void office(View view){
        Intent office = new Intent(this, OfficeActivity.class);
        office.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(office);
    }

    public void show_win1(View view) {
        window_anim((TextView) findViewById(R.id.alfr3d_win1));
    }

    public void show_win2(View view) {
        window_anim((TextView) findViewById(R.id.alfr3d_win2));
    }


    public void write(View view){
        TypeWriter writer = (TypeWriter) findViewById(R.id.alfr3d_response_text);
        //setContentView(writer);
        writer.animateText("initializing...");
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
                TypeWriter writer = (TypeWriter) findViewById(R.id.alfr3d_response_text);
                writer.animateText("Sending custom command: "+value);
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

        TypeWriter writer = (TypeWriter) findViewById(R.id.alfr3d_response_text);
        writer.animateText("Opening help menu");

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
    // TODO finish this
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
        final TextView mTextView = (TextView) findViewById(R.id.alfr3d_response_text);

        // log requested message
        // TODO final TextView Alfr3dLog = (TextView) findViewById(R.id.alfr3d_log);
        // TODO Alfr3dLog.append("\n" + message);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        // display response
                        mTextView.append("\nResponse is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Response", Integer.toString(error.networkResponse.statusCode));
                mTextView.append("\nThat didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // cool response window creation animation
    public void window_anim(TextView view) {
        Log.d("Home","Starting window_response_anim");

        view.setVisibility(View.VISIBLE);
        for (Drawable drawable : view.getCompoundDrawables()) {

            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    // button animation when activity is started
    public void buttonAnim() {
        // get window dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y;

        // find our px from dp
        int px = dpToPx(48);

        // set up animation
        Log.d("Main", "setting up button animation");
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setAlpha(0f);
            buttons[i].setVisibility(View.VISIBLE);

            //buttons[i].startAnimation(buttonAnims[i]);
            ObjectAnimator preAnimTranslateX = ObjectAnimator.ofFloat(buttons[i],"x",width/2,width);
            preAnimTranslateX.setDuration(0);
            preAnimTranslateX.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator preAnimTranslateY = ObjectAnimator.ofFloat(buttons[i],"y",height/2,0f+height/2*(i+1));
            preAnimTranslateY.setDuration(0);
            preAnimTranslateY.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator alpha = ObjectAnimator.ofFloat(buttons[i],"alpha",0f,1f);
            alpha.setDuration(1000);
            alpha.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(buttons[i],"scaleX",50f,1f);
            scaleX.setDuration(1000);
            scaleX.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(buttons[i],"scaleY",50f,1f);
            scaleY.setDuration(1000);
            scaleY.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator scalingTranslateX = ObjectAnimator.ofFloat(buttons[i],"x",width,width/2);
            scalingTranslateX.setDuration(1000);
            scalingTranslateX.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator scalingTranslateY = ObjectAnimator.ofFloat(buttons[i],"y",0f+height/2*(i+1),height/2);
            scalingTranslateY.setDuration(1000);
            scalingTranslateY.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator postScaleTranslateX = ObjectAnimator.ofFloat(buttons[i],"x",width/2,0f);
            postScaleTranslateX.setDuration(400);
            postScaleTranslateX.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator postScaleTranslateY = ObjectAnimator.ofFloat(buttons[i],"y",height/2,0f+px*(i+1));
            postScaleTranslateY.setDuration(400);
            postScaleTranslateY.setInterpolator(new AccelerateInterpolator());

            //set up animation set
            AnimatorSet buttonAnims = new AnimatorSet();

            //run animations
            buttonAnims.play(preAnimTranslateX);
            buttonAnims.play(preAnimTranslateY);
            buttonAnims.play(scaleX).after(200*(i+1));
            buttonAnims.play(scaleY).after(200*(i+1));
            buttonAnims.play(alpha).after(200*(i+1));
            buttonAnims.play(scalingTranslateX).after(200*(i+1));
            buttonAnims.play(scalingTranslateY).after(200*(i+1));
            buttonAnims.play(postScaleTranslateX).after(3000+(200*i));
            buttonAnims.play(postScaleTranslateY).after(3000+(200*i));

            buttonAnims.start();
        }
    }
}