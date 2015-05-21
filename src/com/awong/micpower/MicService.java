package com.awong.micpower;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MicService extends Service {
    private static final String TAG = "MicService";
    private static final Boolean DEBUG = false;
    /* constants */
    private static final int POLL_INTERVAL = 300;
    private int mTickCount = 0;
    private int mHitCount = 0;
    
    /** config state **/
    private int mThreshold = 5;
    private int mPollDelay;
    
    
    private Handler mHandler = new Handler();
    
    /* data source */
    private SoundMeter mSensor;
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    stop();
                } else if (intent.getIntExtra("state", 0) == 1) {
                    start();
                }
            }
        }
    };
    
    private Runnable mSleepTask = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };
    private Runnable mPollTask = new Runnable() {
        @Override
        public void run() {
            double amp = mSensor.getAmplitude();
            
            if (DEBUG)
                Log.d(TAG, "amp:" + amp);
            
            if ((amp > mThreshold)) {
                mHitCount++;
                if (mHitCount > 5) {
                }
            }
            
            mTickCount++;
            
            if ((mPollDelay > 0) && mTickCount > 100) {
                sleep();
            } else {
                mHandler.postDelayed(mPollTask, POLL_INTERVAL);
            }
        }
    };
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG)
            Toast.makeText(this, "open service", Toast.LENGTH_SHORT).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(receiver, filter);
        mSensor = new SoundMeter();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG)
            Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
        stop();
        unregisterReceiver(receiver);
    }
    
    private void start() {
        mTickCount = 0;
        mHitCount = 0;
        try {
            mSensor.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }
    
    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
    }
    
    private void sleep() {
        // mSensor.stop();
        mHandler.postDelayed(mSleepTask, 1000 * mPollDelay);
    }
}
