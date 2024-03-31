package com.ispl.voice.recorder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.ispl.voice.recorder.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    public RelativeLayout a;
    public RelativeLayout b;
    public RelativeLayout c;
    public RelativeLayout d;
    public TextView e;
    public TextView f;
    public TextView g;
    public TextView h;
    public SwitchCompat i;
    public Preference j;
    public RelativeLayout k;
    public SeekBar m;
    public SeekBar n;
    public SeekBar o;
    public SeekBar p;

    LinearLayout mfeedback, mrate, mshare, mpp, mmore;

    private void findViews() {
        this.a = (RelativeLayout) findViewById(R.id.ingain_rel);
        this.b = (RelativeLayout) findViewById(R.id.outgain_rel);
        this.c = (RelativeLayout) findViewById(R.id.delay_rel);
        this.d = (RelativeLayout) findViewById(R.id.decay_rel);
        this.e = (TextView) findViewById(R.id.ingain_tv);
        this.f = (TextView) findViewById(R.id.outgain_tv);
        this.g = (TextView) findViewById(R.id.delay_tv);
        this.h = (TextView) findViewById(R.id.decay_tv);
        this.i = (SwitchCompat) findViewById(R.id.echoswitch);
        this.m = (SeekBar) findViewById(R.id.inGainSB);
        this.n = (SeekBar) findViewById(R.id.outGainSB);
        this.o = (SeekBar) findViewById(R.id.delaysSB);
        this.p = (SeekBar) findViewById(R.id.decaysSB);

        mfeedback = findViewById(R.id.setting_feedback);
        mrate = findViewById(R.id.setting_rate);
        mshare = findViewById(R.id.setting_share);
        mpp = findViewById(R.id.setting_pp);
        mmore = findViewById(R.id.setting_more);

        this.e.setText(this.j.getString("inGain", "0.6"));
        this.f.setText(this.j.getString("outGain", "0.6"));
        this.g.setText(this.j.getString("delays", "110"));
        this.h.setText(this.j.getString("decays", "0.5"));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back);
        this.k = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                SettingsActivity.this.setResult(101);
                SettingsActivity.this.finish();
            }
        });
    }

    @Override 
    public void onBackPressed() {
        setResult(101);
        super.onBackPressed();
    }

    @Override 
    public void onClick(View view) {
        view.getId();
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.act_setting_demo);



        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);


        this.j = new Preference(this);
        findViews();
        this.i.setChecked(this.j.getBoolean("isEchoEnabled", Boolean.FALSE).booleanValue());
        this.i.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { 
            @Override 
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingsActivity.this.j.setBoolean("isEchoEnabled", Boolean.valueOf(z));
            }
        });
        this.m.setMax(90);
        this.m.setProgress((int) (Float.parseFloat(this.e.getText().toString()) * 100.0f));
        this.m.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                SettingsActivity.this.e.setText(String.valueOf(i / 100.0f));
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.j.setString("inGain", settingsActivity.e.getText().toString());
            }

            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.n.setMax(90);
        this.n.setProgress((int) (Float.parseFloat(this.f.getText().toString()) * 100.0f));
        this.n.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                SettingsActivity.this.f.setText(String.valueOf(i / 100.0f));
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.j.setString("outGain", settingsActivity.f.getText().toString());
            }

            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.o.setMax(20);
        this.o.setProgress((int) (Integer.parseInt(this.g.getText().toString()) / 100.0f));
        this.o.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                SettingsActivity.this.g.setText(String.valueOf(i * 100));
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.j.setString("delays", settingsActivity.g.getText().toString());
            }

            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.p.setMax(90);
        this.p.setProgress((int) (Float.parseFloat(this.h.getText().toString()) * 100.0f));
        this.p.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { 
            @Override 
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                SettingsActivity.this.h.setText(String.valueOf(i / 100.0f));
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.j.setString("decays", settingsActivity.h.getText().toString());
            }

            @Override 
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] recipients = {getString(R.string.Email)};
                composeEmail(recipients, "Feedback");
            }
        });
        mrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUs();
            }
        });
        mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });
        mpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PolicyActivity.class));
            }
        });
        mmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreApp();
            }
        });

    }

    @Override 
    public void onResume() {
        this.e.setText(this.j.getString("inGain", "0.6"));
        this.f.setText(this.j.getString("outGain", "0.6"));
        this.g.setText(this.j.getString("delays", "110"));
        this.h.setText(this.j.getString("decays", "0.5"));
        super.onResume();
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);

    }

    public void shareApp() {
        Intent myapp = new Intent(Intent.ACTION_SEND);
        myapp.setType("text/plain");
        myapp.putExtra(Intent.EXTRA_TEXT, "Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
        startActivity(myapp);
    }

    public void rateUs() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void moreApp() {
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=Inventics+Software+Pvt+Ltd")));

    }
}
