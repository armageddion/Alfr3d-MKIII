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
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;
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

    // intiate type writer for animated responses
    private TypeWriter writer;

    // set up text to speech
    TextToSpeech ttsobj;
    boolean isTtsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        writer = (TypeWriter) findViewById(R.id.alfr3d_response_text);

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

        // set up text to speech
        ttsobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("TTS", "TTS onInit() called with status "+Integer.toString(status));
                if (status == TextToSpeech.SUCCESS) {
                    // try tts for speak
                    Log.d("TTS", "Trying to speak: Initializing");
                    ttsobj.speak("Initializing all Alfred services",TextToSpeech.QUEUE_FLUSH, null);
                    isTtsReady = true;
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create some windows just for fun
        window_anim((TextView) findViewById(R.id.alfr3d_response_bg));
        logo_anim((TextView) findViewById(R.id.logo3));
        // after window is created.
        writer.animateText("System initializing...");

        buttonAnim();

        // need to get MAC address for later
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String MAC = manager.getConnectionInfo().getMacAddress();
        Log.d("Main", "MAC: "+MAC);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        writer.animateText("Initiating location services");

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                float curSpeed = location.getSpeed();
                Log.d("Main", "Speed: "+curSpeed);
                TextView mGeoSpeedView = (TextView) findViewById(R.id.alfr3d_win2_text);
                mGeoSpeedView.setText(String.valueOf(curSpeed));
//                if (curSpeed > 0.0 ){
//                    Log.d("Main", "Speed: "+curSpeed);
//                    TextView mGeoSpeedView = (TextView) findViewById(R.id.alfr3d_win2_text);
//                    mGeoSpeedView.setText("Current Speed: " + curSpeed);
//                    show_win3(findViewById(R.id.alfr3d_win2));
//                }
//                else {
//                    Log.d("Main","Speed: 0");
//                    TextView mGeoSpeedView = (TextView) findViewById(R.id.alfr3d_win2_text);
//                    mGeoSpeedView.setText("-_-");
//                }

                writer.animateText("Location update: \nLat:"+String.valueOf(location.getLatitude()) +
                                                    "\nLong:"+String.valueOf(location.getLongitude()));
                TextView geoText = (TextView) findViewById(R.id.alfr3d_win1_text);
                geoText.setText("Lat:"+String.valueOf(String.format("%.5g", location.getLatitude())) +
                                "\nLong:"+String.valueOf(String.format("%.5g", location.getLongitude())));

                TypeWriter bearingWriter = (TypeWriter) findViewById(R.id.alfr3d_win3_text);
                bearingWriter.animateText("Bearing:"+String.valueOf(location.getBearing()));

                // temp call
                // String geocall = "http://maps.google.com/maps/api/geocode/json?latlng="+String.valueOf(location.getLatitude())+
                // "String.valueOf(location.getLatitude())+"&sensor=false";

                // Check in with the Littl3.1 Database
//                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                final String finalCall = "http://www.littl31.com:8080/device/set?MAC=" +
//                        "60:36:dd:6a:24:d5&location="+
//                        String.valueOf(location.getLatitude())+","+
//                        String.valueOf(location.getLongitude());
//                Log.d("Main", "Check-in call: "+finalCall);
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, finalCall,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("Response",response);
//                                // display response
//                                writer.animateText("Littl31 check-in successful. Response: "+response);
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        writer.animateText("Littl31 check-in failed");
//                    }
//                });
//                // Add the request to the RequestQueue.
//                queue.add(stringRequest);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // Register wifi listener
        IntentFilter wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
        wifiIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        //wifiIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifi1, wifiIntentFilter);
    }

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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // release the network receiver
        try {
            unregisterReceiver(wifi1);
        } catch (Exception e) {
            Log.e("onPause", getClass()+" Releasing receivers-"+e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(wifi1, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        window_anim((TextView) findViewById(R.id.alfr3d_response_bg));
        // write out a quip
        writer.animateText("Resuming systems on main thread");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(wifi1);
        } catch (Exception e) {
            Log.e("onDestroy", getClass()+" Releasing receivers-"+e.getMessage());
        }

        // release the text to speech stuff back
        if (ttsobj != null) {
            ttsobj.stop();
            ttsobj.shutdown();
            Log.d("onDestroy", "Releasing TTS hogs");
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
        TextView win1_text = (TextView) findViewById(R.id.alfr3d_win1_text);
        if (win1_text.getAlpha() == 0) {
            window_anim((TextView) findViewById(R.id.alfr3d_win1));
            win1_text.animate()
                    .alpha(1f)
                    .setDuration(1000);
        }
        else {
            TextView win1 = (TextView) findViewById(R.id.alfr3d_win1);
            // fade out, then re-set visibility/alpha settings
            win1.animate()
                    .alpha(0f)
                    .setDuration(1000);
            win1.setVisibility(View.INVISIBLE);

            win1_text.animate()
                    .alpha(0f)
                    .setDuration(1000);
        }
    }

    public void show_win2(View view) {
        TextView win2_text = (TextView) findViewById(R.id.alfr3d_win2_text);
        if (win2_text.getAlpha() == 0) {
            window_anim((TextView) findViewById(R.id.alfr3d_win2));
            win2_text.animate()
                    .alpha(1f)
                    .setDuration(1000);
        }
        else {
            TextView win2 = (TextView) findViewById(R.id.alfr3d_win2);
            // fade out, then re-set visibility/alpha settings
            win2.animate()
                    .alpha(0f)
                    .setDuration(1000);
            win2.setVisibility(View.INVISIBLE);

            win2_text.animate()
                    .alpha(0f)
                    .setDuration(1000);
        }
    }

    public void show_win3(View view) {
        TextView win3_text = (TextView) findViewById(R.id.alfr3d_win3_text);
        if (win3_text.getAlpha() == 0) {
            window_anim((TextView) findViewById(R.id.alfr3d_win3));
            win3_text.animate()
                    .alpha(1f)
                    .setDuration(1000);
        }
        else {
            TextView win3 = (TextView) findViewById(R.id.alfr3d_win3);
            // fade out, then re-set visibility/alpha settings
            win3.animate()
                    .alpha(0f)
                    .setDuration(1000);
            win3.setVisibility(View.INVISIBLE);

            win3_text.animate()
                    .alpha(0f)
                    .setDuration(1000);
        }
    }

    public void show_win4(View view) {
        TextView win4_text = (TextView) findViewById(R.id.alfr3d_win4_text);
        if (win4_text.getAlpha() == 0) {
            window_anim((TextView) findViewById(R.id.alfr3d_win4));
            win4_text.animate()
                    .alpha(1f)
                    .setDuration(1000);
        }
        else {
            TextView win4 = (TextView) findViewById(R.id.alfr3d_win4);
            // fade out, then re-set visibility/alpha settings
            win4.animate()
                    .alpha(0f)
                    .setDuration(1000);
            win4.setVisibility(View.INVISIBLE);

            win4_text.animate()
                    .alpha(0f)
                    .setDuration(1000);
        }
    }

    public void show_win5(View view) {
        TextView win5_text = (TextView) findViewById(R.id.alfr3d_win5_text);
        if (win5_text.getAlpha() == 0) {
            window_anim((TextView) findViewById(R.id.alfr3d_win5));
            win5_text.animate()
                    .alpha(1f)
                    .setDuration(1000);
        }
        else {
            TextView win5 = (TextView) findViewById(R.id.alfr3d_win5);
            // fade out, then re-set visibility/alpha settings
            win5.animate()
                    .alpha(0f)
                    .setDuration(1000);
            win5.setVisibility(View.INVISIBLE);

            win5_text.animate()
                    .alpha(0f)
                    .setDuration(1000);
        }
    }

    public void write(View view){

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
        if (isTtsReady){
            ttsobj.speak("sorry, can't help you", TextToSpeech.QUEUE_FLUSH, null);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        writer.animateText("Opening help menu");

        alert.setTitle("Help");
        alert.setMessage("Sorry.... \n"+
        "can't <help> you yet..\n"+
        "we have only just met");

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

        String alfr3d_url = "http://www.littl31.com:8080/"+message;
        String full_alfr3d_call = alfr3d_url;
        Log.d("Main", "LitTl3.1 URL:" + alfr3d_url);

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
                        writer.animateText(response);
                        //mTextView.append("\nResponse is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Response", Integer.toString(error.networkResponse.statusCode));
                writer.animateText("That didn't work!");
                //mTextView.append("\nThat didn't work!");
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
        view.setAlpha(1f);
        for (Drawable drawable : view.getCompoundDrawables()) {

            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    // cool response window creation animation
    public void logo_anim(TextView view) {
        Log.d("Home","Starting littl31 logo anim");

        view.setVisibility(View.VISIBLE);
        view.setAlpha(1f);
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