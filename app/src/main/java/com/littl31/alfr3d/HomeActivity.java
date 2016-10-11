package com.littl31.alfr3d;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.littl31.alfr3d.util.Alfr3dUtil;
import com.littl31.alfr3d.util.NetworkUtil;
import com.littl31.alfr3d.util.NetwrokChangeReceiver;
import com.littl31.alfr3d.util.TypeWriter;

import java.util.concurrent.ThreadLocalRandom;

public class HomeActivity extends Activity {

    // intiate type writer for animated responses
    private TypeWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        writer = (TypeWriter) findViewById(R.id.alfr3d_response_text);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        window_response_anim(findViewById(R.id.alfr3d_response_bg));
        window_log_anim(findViewById(R.id.alfr3d_log_bg));

        // long winded way to fade in some text...
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.log_win_cont_anim);
        TextView alfr3d_log_heading = (TextView) findViewById(R.id.alfr3d_log_heading);
        TextView alfr3d_log_text = (TextView) findViewById(R.id.alfr3d_log_text);
        TextView alfr3d_response_heading = (TextView) findViewById(R.id.alfr3d_response_heading);
        TextView alfr3d_response_text = (TextView) findViewById(R.id.alfr3d_response_text);
        alfr3d_log_heading.startAnimation(animation);
        alfr3d_log_text.startAnimation(animation);
        alfr3d_response_heading.startAnimation(animation);
        alfr3d_response_text.startAnimation(animation);
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
            // this looks like a good place to put my animation in
            window_response_anim(findViewById(R.id.alfr3d_response_bg));
            window_log_anim(findViewById(R.id.alfr3d_log_bg));
            logo_anim((TextView) findViewById(R.id.logo3));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        window_response_anim(findViewById(R.id.alfr3d_response_bg));
        window_log_anim(findViewById(R.id.alfr3d_log_bg));

        // long winded way to fade in some text...
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.log_win_cont_anim);
        TextView alfr3d_log_heading = (TextView) findViewById(R.id.alfr3d_log_heading);
        TextView alfr3d_log_text = (TextView) findViewById(R.id.alfr3d_log_text);
        TextView alfr3d_response_heading = (TextView) findViewById(R.id.alfr3d_response_heading);
        TextView alfr3d_response_text = (TextView) findViewById(R.id.alfr3d_response_text);
        alfr3d_log_heading.startAnimation(animation);
        alfr3d_log_text.startAnimation(animation);
        alfr3d_response_heading.startAnimation(animation);
        alfr3d_response_text.startAnimation(animation);
    }

    public void mainActivity(View view){
        Intent main = new Intent(this, MainActivity.class);
        //main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(main);
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

    public void Lights_toggle(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            sendButtonCommand("arduino/LightsOn");
        } else {
            sendButtonCommand("arduino/LightsOff");
        }
    }

    public void RF1_toggle(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            sendButtonCommand("arduino/RF1ON");
        } else {
            sendButtonCommand("arduino/RF1OFF");
        }
    }

    public void RF2_toggle(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            sendButtonCommand("arduino/RF2ON");
        } else {
            sendButtonCommand("arduino/RF2OFF");
        }
    }

    public void RF3_toggle(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            sendButtonCommand("arduino/RF3ON");
        } else {
            sendButtonCommand("arduino/RF3OFF");
        }
    }

    public void reboot(View view){
        sendButtonCommand("reboot");
    }

    // send commands to Alfr3d
    public void sendButtonCommand(String Command) {
        // Do something in response to button
        String message = Command;

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String alfr3d_url= mySharedPreferences.getString("alfr3d_url_preference_home","url not set");
        if (NetworkUtil.isConnectedToHome(this)) {
            Log.d("Home","Connected to home network...");
            alfr3d_url= mySharedPreferences.getString("home_ip_preference","ip not set");
        }
        String full_alfr3d_call = alfr3d_url;
        Log.d("Home", "Alfr3d URL:"+alfr3d_url);


        //TEMP DEBUGING STUFF>>>
        String homeSSID = mySharedPreferences.getString("home_ssid_preference","ssid not set");
        Log.d("Home", "Home SSID:"+homeSSID);

        String method = mySharedPreferences.getString("method","-1");

        if (method.equals("CGI"))
        {
            full_alfr3d_call = alfr3d_url + "/cgi-bin/alfr3d.cgi?command=" + message;
        }
        else if (method.equals("Node.js"))
        {
            // TODO: implement Node properly
            full_alfr3d_call = alfr3d_url + ":1337/" + message;
        }
        else if (method.equals("BottleRPC"))
        {
            // TODO: complete bottle implementation
            full_alfr3d_call = alfr3d_url + ":8080/" + message;
        }


        boolean node_enabled = mySharedPreferences.getBoolean("node_enabled",false);
        if (node_enabled == true)
        {
            //TODO Finish this bit
            full_alfr3d_call = alfr3d_url+"/complete this bit when you have it working in node";
        }

        // "http://alfr3d.no-ip.org/cgi-bin/test2.py?command=Blink"
        if (!full_alfr3d_call.substring(0,7).equalsIgnoreCase("http://"))
        {
            full_alfr3d_call = "http://"+full_alfr3d_call;
        }

        final String finalCall = full_alfr3d_call;
        TextView Alfr3dURLView = (TextView) findViewById(R.id.alfr3d_call);
        Alfr3dURLView.setText(full_alfr3d_call);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // log requested message
        TypeWriter log_writer = (TypeWriter) findViewById(R.id.alfr3d_log_text);
        log_writer.animateText(message);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        // display response
                        writer.animateText("Response is :"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response","That didn't work!");
                writer.animateText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // cool response window creation animation
    public void window_response_anim(View view) {
        Log.d("Home","Starting window_response_anim");
        TextView text_anim = (TextView) findViewById(R.id.alfr3d_response_bg);

        text_anim.setVisibility(View.VISIBLE);
        for (Drawable drawable : text_anim.getCompoundDrawables()) {

            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    // cool log window creation animation
    public void window_log_anim(View view) {
        Log.d("Home","Log window_log_anim");
        TextView text_anim = (TextView) findViewById(R.id.alfr3d_log_bg);

        text_anim.setVisibility(View.VISIBLE);
        for (Drawable drawable : text_anim.getCompoundDrawables()) {

            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    // cool logo creation animation
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
}
