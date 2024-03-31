package com.ispl.voice.recorder;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;


public class AdActivity extends AppCompatActivity implements View.OnClickListener {
    public RelativeLayout a;
    public Preference d;
    public Button e;
    public SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd hh:mm:ss", Locale.getDefault());
    public ImageView g;
    Dialog dialog;


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void dialogBox() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.rate_d);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ((Button) dialog.findViewById(R.id.okay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdActivity.isNetworkAvailable(AdActivity.this.getApplicationContext())) {
                    AdActivity.this.d.setBoolean("ratingGiven", Boolean.TRUE);
                }
                try {
                    AdActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.iApp.echo.voicerecorder")));
                } catch (ActivityNotFoundException unused) {
                    AdActivity adActivity = AdActivity.this;
                    adActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdActivity.this.getResources().getString(R.string.menu_share_link) + getPackageName())));
                }
                dialog.dismiss();
                AdActivity.this.finish();
            }
        });
        ((Button) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AdActivity.this.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        dialog.dismiss();
        ratingDialog();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_start) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.policyBtn) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(getString(R.string.policy_url)));
            startActivity(intent);
        }
    }

    @Override

    public void onCreate(Bundle bundle) {
        JSONObject jSONObject;
        super.onCreate(bundle);
        setContentView(R.layout.activity_ad);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);


        this.d = new Preference(this);
        Button button = (Button) findViewById(R.id.btn_start);
        this.e = button;
        button.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.policyBtn);
        this.g = imageView;
        imageView.setOnClickListener(this);
        this.a = (RelativeLayout) findViewById(R.id.adLayout);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void ratingDialog() {
        if (this.d.getBoolean("ratingGiven").booleanValue()) {
            finish();
        } else if (this.d.getInteger("ratingCnt").intValue() == 3) {
            this.d.setInteger("ratingCnt", 1);
            dialogBox();
        } else {
            Preference preference = this.d;
            preference.setInteger("ratingCnt", Integer.valueOf(preference.getInteger("ratingCnt").intValue() + 1));
            finish();
        }
    }
}
