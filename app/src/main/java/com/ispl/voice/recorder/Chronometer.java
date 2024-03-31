package com.ispl.voice.recorder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;
import java.text.DecimalFormat;


public class Chronometer extends TextView {
    public static final String TAG = "Chronometer";
    public static final int TICK_WHAT = 2;
    public long mBase;
    public Handler mHandler;
    public OnChronometerTickListener mOnChronometerTickListener;
    public boolean mRunning;
    public boolean mStarted;
    public boolean mVisible;
    public long timeElapsed;

    
    public interface OnChronometerTickListener {
        void onChronometerTick(Chronometer chronometer);
    }

    public Chronometer(Context context) {
        this(context, null, 0);
    }

    private void init() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mBase = elapsedRealtime;
        updateText(elapsedRealtime);
    }

    private void updateRunning() {
        boolean z = this.mVisible && this.mStarted;
        if (z != this.mRunning) {
            if (z) {
                updateText(SystemClock.elapsedRealtime());
                c();
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(Message.obtain(handler, 2), 100L);
            } else {
                this.mHandler.removeMessages(2);
            }
            this.mRunning = z;
        }
    }

    
    public synchronized void updateText(long j) {
        this.timeElapsed = j - this.mBase;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        int i = (int) (this.timeElapsed / 3600000);
        int i2 = (int) (this.timeElapsed % 3600000);
        int i3 = i2 / 60000;
        int i4 = i2 % 60000;
        int i5 = i4 / 1000;
        int i6 = i4 % 1000;
        int i7 = (((int) this.timeElapsed) % 1000) / 10;
        String str = "";
        if (i > 0) {
            str = str + decimalFormat.format(i) + ":";
        }
        setText(((str + decimalFormat.format(i3) + ":") + decimalFormat.format(i5) + ":") + decimalFormat.format(i7));
    }

    public void c() {
        OnChronometerTickListener onChronometerTickListener = this.mOnChronometerTickListener;
        if (onChronometerTickListener != null) {
            onChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getBase() {
        return this.mBase;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return this.mOnChronometerTickListener;
    }

    public long getTimeElapsed() {
        return this.timeElapsed;
    }

    @Override 
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        updateRunning();
    }

    @Override 
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        this.mVisible = i == 0;
        updateRunning();
    }

    public void setBase(long j) {
        this.mBase = j;
        c();
        updateText(SystemClock.elapsedRealtime());
    }

    public void setOnChronometerTickListener(OnChronometerTickListener onChronometerTickListener) {
        this.mOnChronometerTickListener = onChronometerTickListener;
    }

    public void setStarted(boolean z) {
        this.mStarted = z;
        updateRunning();
    }

    public void start() {
        this.mStarted = true;
        updateRunning();
    }

    public void stop() {
        this.mStarted = false;
        updateRunning();
    }

    public Chronometer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Chronometer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new Handler() { 
            @Override 
            public void handleMessage(Message message) {
                if (Chronometer.this.mRunning) {
                    Chronometer.this.updateText(SystemClock.elapsedRealtime());
                    Chronometer.this.c();
                    sendMessageDelayed(Message.obtain(this, 2), 100L);
                }
            }
        };
        init();
    }
}
