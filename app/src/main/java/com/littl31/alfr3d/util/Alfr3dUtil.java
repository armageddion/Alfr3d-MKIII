package com.littl31.alfr3d.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.littl31.alfr3d.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by armageddion on 9/28/14.
 */
public class Alfr3dUtil {

    public static void sendButtonCommand(final Context context, String Command) {
        // Do something in response to button
        String message = Command;

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String alfr3d_url= mySharedPreferences.getString("alfr3d_url_preference","url not set");
        String full_alfr3d_call = alfr3d_url;
        Log.d("Main", "Alfr3d URL:" + alfr3d_url);

        //TEMP DEBUGING STUFF>>>
        String homeSSID = mySharedPreferences.getString("home_ssid_preference","ssid not set");
        Log.d("Main", "Home SSID:"+homeSSID);

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

        // curl: "http://alfr3d.no-ip.org/cgi-bin/test2.py?command=Blink"
        if (!full_alfr3d_call.substring(0,7).equalsIgnoreCase("http://"))
        {
            full_alfr3d_call = "http://"+full_alfr3d_call;
        }

        final String finalCall = full_alfr3d_call;
        Toast.makeText(context, full_alfr3d_call, Toast.LENGTH_LONG).show();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, "Response is: "+ response.substring(0,500), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "That didn't work!", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
