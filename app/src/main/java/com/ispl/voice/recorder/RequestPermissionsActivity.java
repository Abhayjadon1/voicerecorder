package com.ispl.voice.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ispl.voice.recorder.utils.PermissionUtils;

public class RequestPermissionsActivity extends Activity {
    public static final int PERMISSIONS_REQUEST_ALL_PERMISSIONS = 999;
    public static final String PREVIOUS_ACTIVITY_INTENT = "previous_intent";
    public static String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.WAKE_LOCK", "android.permission.INTERNET"};
    public Intent mPreviousActivityIntent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPreviousActivityIntent = (Intent) getIntent().getExtras().get(PREVIOUS_ACTIVITY_INTENT);
        requestPermissionsIfRequired();
    }

    public void requestPermissionsIfRequired() {
        if (PermissionUtils.hasPermissions(this, permissions)) {
            proceedToPreviousActivity();
        } else {
            requestPermissions(permissions, PERMISSIONS_REQUEST_ALL_PERMISSIONS);
        }
    }

    private void proceedToPreviousActivity() {
        mPreviousActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mPreviousActivityIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ALL_PERMISSIONS && PermissionUtils.verifyPermissions(grantResults)) {
            proceedToPreviousActivity();
        } else {
            requestPermissionsIfRequired();
        }
    }
}
