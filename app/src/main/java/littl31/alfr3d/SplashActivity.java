package littl31.alfr3d;

import littl31.alfr3d.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


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
        ImageView AnimationView = (ImageView) findViewById(R.id.AnimView);
        AnimationView.setBackgroundResource(R.drawable.animation);
        AnimationDrawable SplashAnimation = (AnimationDrawable) AnimationView.getBackground();

        SplashAnimation.start();

        Thread welcomeThread = new Thread(){

            @Override
            public void run(){
                try{
                    super.run();
                    sleep(700);
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
