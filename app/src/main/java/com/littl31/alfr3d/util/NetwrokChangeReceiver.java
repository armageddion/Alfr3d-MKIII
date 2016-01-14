package com.littl31.alfr3d.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.littl31.alfr3d.AwayActivity;
import com.littl31.alfr3d.HomeActivity;
import com.littl31.alfr3d.MainActivity;
import com.littl31.alfr3d.OfficeActivity;
import com.littl31.alfr3d.R;

/**
 * Created by armageddion on 9/28/14.
 */
public class NetwrokChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("NetworkChangeReceiver","starting");
        String status = NetworkUtil.getConnectivityStatusString(context);
        Log.d("NetworkChangeReceiver","Status: "+status);

        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        if(status.equals("Wifi enabled")) {
            if (NetworkUtil.isConnectedToHome(context)) {
                //Toast.makeText(context, "Welcome Home", Toast.LENGTH_LONG).show();
                Log.d("NetworkChangeReceiver", "Welcome home");
                Intent home = new Intent(context, HomeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(home);
            }
            else{
                Log.d("NetworkChangeReceiver", "Not at home network");
                //Intent main = new Intent(context, MainActivity.class);
                //main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(main);
            }
            if (NetworkUtil.isConnectedToOffice(context)) {
                //Toast.makeText(context, "Welcome Home", Toast.LENGTH_LONG).show();
                Log.d("NetworkChangeReceiver", "Ready to work");
                Intent work = new Intent(context, OfficeActivity.class);
                work.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(work);
            }
            else{
                Log.d("NetworkChangeReceiver", "Not connected to work network");
                //Intent main = new Intent(context, MainActivity.class);
                //main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(main);
            }
        }
        else{
            //Toast.makeText(context, "Not connected to WiFi", Toast.LENGTH_LONG).show();
            Log.d("NetworkChangeReceiver", "Not Connected to WiFi");
            Intent main = new Intent(context, MainActivity.class);
            //main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(main);
        }
    }
}
