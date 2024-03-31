package com.ispl.voice.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ispl.voice.recorder.R;


public class SplashScreen extends Activity {


    public class LoadThread extends Thread {


        public class SubThread implements Runnable {
            public SubThread() {
            }

            @Override
            public void run() {

                SplashScreen.this.finish();
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, AdActivity.class));

            }
        }

        public LoadThread() {
        }

        @Override
        public void run() {
            SubThread subThread;
            SplashScreen splashScreen;
            try {
                try {

                    Thread.sleep(1000L);

                    splashScreen = SplashScreen.this;
                    subThread = new SubThread();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    splashScreen = SplashScreen.this;
                    subThread = new SubThread();
                }
                splashScreen.runOnUiThread(subThread);
            } catch (Throwable th) {
                SplashScreen.this.runOnUiThread(new SubThread());
                throw th;
            }
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setFormat(1);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main1);




        new LoadThread().start();
    }
}
