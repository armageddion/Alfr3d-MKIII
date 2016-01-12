package com.littl31.alfr3d.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class TypeWriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = ThreadLocalRandom.current().nextInt(50, 150); //Default 150ms delay


    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
        //Log.d("TypeWritier", "Writing: "+mText.subSequence(mIndex, mIndex+1));
        append(mText.subSequence(mIndex, mIndex+1));
        mIndex++;
        if(mIndex < mText.length()) {
            mDelay = ThreadLocalRandom.current().nextInt(50, 150);
            mHandler.postDelayed(characterAdder, mDelay);
        }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        append("\n");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}