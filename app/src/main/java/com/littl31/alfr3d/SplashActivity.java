package com.littl31.alfr3d;

import com.littl31.alfr3d.R;
import com.littl31.alfr3d.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Quick, short splash screen
 */
public class SplashActivity extends Activity {

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
            Thread welcomeThread = new Thread(){

                @Override
                public void run(){
                    try{
                        super.run();
                        sleep(3000);
                    } catch (Exception e){
                        //TODO
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
