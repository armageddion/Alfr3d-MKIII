package littl31.alfr3d;

import littl31.alfr3d.util.SystemUiHider;

import android.annotation.TargetApi;
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
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                    //Intent i = new Intent(SplashActivity.this,MainAlfr3d.class);
                    //startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

}
