package com.ispl.voice.recorder;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;


public class EchoApplication extends Application {
    @Override 
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override 
    public void onCreate() {
        super.onCreate();
    }
}
