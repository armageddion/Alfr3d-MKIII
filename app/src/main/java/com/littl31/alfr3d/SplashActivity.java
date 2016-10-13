package com.littl31.alfr3d;

import com.littl31.alfr3d.R;
import com.littl31.alfr3d.util.SystemUiHider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Quick, short splash screen
 */
public class SplashActivity extends Activity {

    final private int MY_PERMISSION_REQUEST_FINE_LOCATION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.hide();

        // attempt at animated Splash screen
//        ImageView AnimationView = (ImageView) findViewById(R.id.AnimView);
//        AnimationView.setBackgroundResource(R.drawable.animation);
//        AnimationDrawable SplashAnimation = (AnimationDrawable) AnimationView.getBackground();
//
//        SplashAnimation.start();
//
//        Thread welcomeThread = new Thread(){
//
//            @Override
//            public void run(){
//                try{
//                    super.run();
//                    sleep(1500);
//                } catch (Exception e){
//                    //TODO
//                } finally {
//                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }
//        };
//        welcomeThread.start();
    }

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
            TextView logoView = (TextView) findViewById(R.id.logo3);
            Log.d("Splash","Starting littl31 logo anim");

            logoView.setVisibility(View.VISIBLE);
            logoView.setAlpha(1f);
            for (Drawable drawable : logoView.getCompoundDrawables()) {

                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }

            // check permissions
            Log.d("Splash","Checking permissions");
            int temp = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d("Splash","Permissions check:"+temp);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Show Explanation
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_FINE_LOCATION);
                }
            }
            else {
                Log.d("Splash","Permission granted.. Starting main");

                // wait thread to allow animation to finish before going to main
                Log.d("Splash","Starting welcome thread");
                Thread welcomeThread = new Thread(){

                    @Override
                    public void run(){
                        try{
                            super.run();
                            sleep(3000);
                        } catch (Exception e){
                            Log.e("welcomeThread", getClass()+" Error:"+e.getMessage());
                        } finally {
                            Intent i = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                };
                welcomeThread.start();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("Splash","Permission request response received: "+requestCode);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION: {
                // if request is cancelled, the results arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, YAY! Do the task you need to do...
                    Log.d("Splash", "permission granted");
                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                }
                else {
                    // permission was denied, boo!
                    Log.d("Splash", "permission denied, exiting.");
                    System.exit(0);
                }
                return;
            }
        }
    }
}
