package com.littl31.alfr3d.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.littl31.alfr3d.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by armageddion on 9/28/14.
 */
public class Alfr3dUtil {

    public static void sendButtonCommand(Context context, String Command) {
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

        new Thread() {
            public void run() {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response;
                    String responseString = null;
                    try {
                        response = httpclient.execute(new HttpGet(finalCall));
                        StatusLine statusLine = response.getStatusLine();
                        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);
                            out.close();
                            responseString = out.toString();
                        } else{
                            //Closes the connection.
                            response.getEntity().getContent().close();
                            throw new IOException(statusLine.getReasonPhrase());
                        }
                    } catch (ClientProtocolException e) {
                        //TODO Handle problems..
                    } catch (IOException e) {
                        //TODO Handle problems..
                    }
                }
                catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }.start();
    }
}
