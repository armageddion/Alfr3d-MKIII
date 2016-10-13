package com.littl31.alfr3d.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.littl31.alfr3d.MainActivity;

/**
 * Created by armageddion on 9/28/14.
 */
public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static boolean isConnectedToHome(Context context) {
        String ssid = "";

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String homeSSID = mySharedPreferences.getString("home_ssid_preference","ssid not set");
        Log.d("NetworkUtil", "Home SSID:"+homeSSID);

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (activeNetwork.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
                ssid = ssid.substring(1,ssid.length()-1);
                Log.d("NetworkUtil", "SSID:"+ssid);
            }
        }

        return (ssid.equalsIgnoreCase(homeSSID));

    }

    public static boolean isConnectedToOffice(Context context) {
        String ssid = null;

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String officeSSID = mySharedPreferences.getString("office_ssid_preference","ssid not set");
        Log.d("NetworkUtil", "Office SSID:"+officeSSID);

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (activeNetwork.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
                ssid = ssid.substring(1,ssid.length()-1);
                Log.d("NetworkUtil", "SSID:"+ssid);
            }
        }

        return (ssid.equalsIgnoreCase(officeSSID));

    }
}
