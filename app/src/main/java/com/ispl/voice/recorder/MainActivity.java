package com.ispl.voice.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.ispl.voice.recorder.R;
import com.ispl.voice.recorder.utils.PermissionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public RelativeLayout a;
    public RelativeLayout b;
    public RelativeLayout c;
    public Chronometer chronometer;
    public MediaRecorder d;
    public MaterialDialog dialog;
    public TextView e;
    public String filePath;
    public String finalPath;
    public File h;
    public File i;
    public File j;
    public File k;
    public String l;
    public ImageView m;
    public Preference o;
    public LinearLayout p;
    public ImageView q;
    public SeekBar r;
    public TextView s;
    public Button t;
    public Button u;
    public int v;
    public Handler w;
    public Runnable x;
    public double y;
    public double z;
    public MediaPlayer f = new MediaPlayer();
    public boolean g = false;
    public SimpleDateFormat n = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());

    private static final int PERMISSION_REQUEST_CODE = 999; // Arbitrary value

    // Define your permissions array
    public static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.INTERNET
    };


    public void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (String str : file.list()) {
                new File(file, str).delete();
            }
        }
    }

    public static boolean directoryCopy(File file, File file2) {
        File[] listFiles;
        try {
            if (!file2.exists()) {
                file2.mkdir();
            }
            boolean z = true;
            for (File file3 : file.listFiles()) {
                if (file3.isDirectory()) {
                    z = directoryCopy(file3, new File(file2, file3.getName()));
                } else {
                    File file4 = new File(file2, file3.getName());
                    FileInputStream fileInputStream = new FileInputStream(file3);
                    FileOutputStream fileOutputStream = new FileOutputStream(file4);
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            }
            file2.exists();
            return z;
        } catch (IOException unused) {
            return false;
        }
    }

    private void discardRecDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage(getString(R.string.discard_rec));
        builder.setPositiveButton(getResources().getString(R.string.discard_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity mainActivity = MainActivity.this;
                if (mainActivity.g) {
                    mainActivity.d.stop();
                    MainActivity.this.d.release();
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.d = null;
                    mainActivity2.chronometer.stop();
                }
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    private void findViews() {
        this.a = (RelativeLayout) findViewById(R.id.record_audio);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        this.chronometer = chronometer;
        chronometer.setBase(SystemClock.elapsedRealtime());
        this.b = (RelativeLayout) findViewById(R.id.settings);
        this.c = (RelativeLayout) findViewById(R.id.rec_list);
        this.p = (LinearLayout) findViewById(R.id.playerLay);
        this.m = (ImageView) findViewById(R.id.mainBtn);
        this.e = (TextView) findViewById(R.id.playerChron);
        this.q = (ImageView) findViewById(R.id.playPause);
        this.r = (SeekBar) findViewById(R.id.playerSB);
        this.m.setTag(getString(R.string.record_audio));
        this.s = (TextView) findViewById(R.id.songNameTV);
        this.u = (Button) findViewById(R.id.btnNew);
        this.t = (Button) findViewById(R.id.btnSave);
        this.a.setOnClickListener(this);
        this.b.setOnClickListener(this);
        this.c.setOnClickListener(this);
        this.q.setOnClickListener(this);
        this.u.setOnClickListener(this);
        this.t.setOnClickListener(this);
        this.w = new Handler();
        this.x = new Runnable() {
            @Override
            public void run() {
                MainActivity mainActivity = MainActivity.this;
                MediaPlayer mediaPlayer = mainActivity.f;
                if (mediaPlayer != null) {
                    mainActivity.r.setProgress(mediaPlayer.getCurrentPosition());
                    MainActivity.this.w.postDelayed(this, 0L);
                }
            }
        };
    }


    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void resetCall() {
        this.w.removeCallbacks(this.x);
        File file = this.j;
        if (file != null && file.exists()) {
            deleteDirectory(this.j);
        }
        File file2 = this.i;
        if (file2 != null && file2.exists()) {
            deleteDirectory(this.i);
        }
        if (this.p.getVisibility() == View.VISIBLE) {
            this.p.setVisibility(View.GONE);
        }
        this.m.setImageResource(R.drawable.record);
        this.m.setTag(getString(R.string.record_audio));
        this.finalPath = null;
        this.filePath = null;
    }


    public void startPlaying() throws IOException {
        this.f = new MediaPlayer();
        this.e.setText(R.string.time);
        if (this.finalPath == null || this.filePath == null) {
            Toast.makeText(this, "Please Record the Audio First..", Toast.LENGTH_SHORT).show();
        } else if (this.o.getBoolean("isEchoEnabled", Boolean.FALSE).booleanValue()) {
            this.f.setDataSource(this.finalPath);
        } else {
            this.f.setDataSource(this.filePath);
        }
        this.f.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                MainActivity.this.f.start();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.r.setMax(mainActivity.f.getDuration());
                MainActivity.this.q.setImageResource(R.drawable.ic_pause);
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.s.setText(mainActivity2.filePath.substring(MainActivity.this.filePath.lastIndexOf("/") + 1));
                MainActivity mainActivity3 = MainActivity.this;
                mainActivity3.w.postDelayed(mainActivity3.x, 0L);
            }
        });
        this.f.prepareAsync();
        this.f.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MainActivity.this.f.seekTo(0);
                MainActivity.this.q.setImageResource(R.drawable.ic_play);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.w.removeCallbacks(mainActivity.x);
                MainActivity.this.f.stop();
                MainActivity.this.f.reset();
                MainActivity.this.f.release();
                MainActivity.this.f = null;
            }
        });
    }

    @SuppressLint({"SimpleDateFormat"})
    private void startRecording() {
        try {
            this.l = "echo-" + this.n.format(new Date());
            this.chronometer.setVisibility(View.VISIBLE);
            this.m.setImageResource(R.drawable.recording);
            this.m.setTag(getString(R.string.stop));
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.d = mediaRecorder;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.d.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.d.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            this.d.setAudioEncodingBitRate(128000);
            this.d.setAudioSamplingRate(44100);
            this.h = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File file = new File(this.h.getAbsolutePath() + "/" + getString(R.string.app_name) + "/temp");
            this.i = file;
            if (!file.exists()) {
                this.i.mkdirs();
            }
            deleteDirectory(this.i);
            this.filePath = this.h.getAbsolutePath() + "/" + getString(R.string.app_name) + "/temp/" + this.l + ".mp3";
            StringBuilder sb = new StringBuilder();
            sb.append(this.h.getAbsolutePath());
            sb.append("/");
            sb.append(getString(R.string.app_name));
            sb.append("/recordings");
            File file2 = new File(sb.toString());
            this.j = file2;
            if (!file2.exists()) {
                this.j.mkdirs();
            }
            this.finalPath = this.h.getAbsolutePath() + "/" + getString(R.string.app_name) + "/recordings/" + this.l + ".mp3";
            this.d.setOutputFile(this.filePath);
            this.finalPath = this.h.getAbsolutePath() + "/" + getString(R.string.app_name) + "/recordings/" + this.l + ".mp3";
            try {
                this.d.prepare();
                this.d.start();
                this.g = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.chronometer.setBase(SystemClock.elapsedRealtime());
            this.chronometer.start();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void stopRecording() {
        try {
            this.d.stop();
            this.d.release();
            this.g = false;
            this.d = null;
            this.chronometer.stop();
            this.v = MediaPlayer.create(this, Uri.fromFile(new File(this.filePath))).getDuration();
            this.dialog = new MaterialDialog.Builder(this).content("Creating Audio with Echo").progress(false, 100, true).cancelable(false).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.addEcho();
                }
            }, 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String updateText(long j) {
        int i = 0;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        int i2 = (int) (j % 3600000);
        int i3 = i2 / 60000;
        int i4 = i2 % 60000;
        int i5 = i4 / 1000;
        int i6 = i4 % 1000;
        int i7 = (((int) j) % 1000) / 10;
        String str = "";
        if (((int) (j / 3600000)) > 0) {
            str = str + decimalFormat.format(i) + ":";
        }
        return ((str + decimalFormat.format(i3) + ":") + decimalFormat.format(i5) + ":") + decimalFormat.format(i7);
    }

    public void addEcho() {
        String[] strArr = {"-i", this.filePath, "-map", "0", "-c:v", "copy", "-af", getEchoString() + ",volume=6.5", this.finalPath};
        Config.enableStatisticsCallback(new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.dialog.setProgress((statistics.getTime() * 100) / mainActivity.v);
            }
        });
        FFmpeg.executeAsync(strArr, new ExecuteCallback() {
            @Override
            public void apply(long j, int i) {
                MainActivity.this.dialog.dismiss();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.g = false;
                mainActivity.m.setImageResource(R.drawable.record);
                MainActivity.this.m.setTag(null);
                MainActivity.this.chronometer.setVisibility(View.GONE);
                MainActivity.this.p.setVisibility(View.VISIBLE);
                try {
                    MainActivity.this.startPlaying();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void exitAct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage(getString(R.string.discard_msg));
        builder.setPositiveButton(getResources().getString(R.string.discard_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.w.removeCallbacks(mainActivity.x);
                MediaPlayer mediaPlayer = MainActivity.this.f;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    MainActivity.this.f.reset();
                    MainActivity.this.f.release();
                    MainActivity.this.f = null;
                }
                MainActivity.this.p.setVisibility(View.GONE);
                MainActivity mainActivity2 = MainActivity.this;
                if (mainActivity2.g) {
                    mainActivity2.d.stop();
                    MainActivity.this.d.release();
                    MainActivity.this.d = null;
                }
                MainActivity.this.resetCall();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public String getEchoString() {
        Log.d("echoString", "aecho=" + this.o.getString("inGain", "0.6") + ":" + this.o.getString("outGain", "0.6") + ":" + this.o.getString("delays", "110") + ":" + this.o.getString("decays", "0.5"));
        return "aecho=" + this.o.getString("inGain", "0.6") + ":" + this.o.getString("outGain", "0.6") + ":" + this.o.getString("delays", "110") + ":" + this.o.getString("decays", "0.5");
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        boolean z = true;
        if (!(i == 96) || !(i2 == 101)) {
            boolean z2 = i == 90;
            if (i2 != 102) {
                z = false;
            }
            if (z2 && z) {
                MediaPlayer mediaPlayer = this.f;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    this.f.release();
                    this.f = null;
                }
                this.r.setProgress(0);
                this.q.setImageResource(R.drawable.ic_play);
            }
        } else {
            if (this.filePath == null || this.finalPath == null) {
                MediaPlayer mediaPlayer2 = this.f;
                if (mediaPlayer2 != null) {
                    mediaPlayer2.stop();
                    this.f.release();
                    this.f = null;
                }
                this.r.setProgress(0);
                this.q.setImageResource(R.drawable.ic_play);
                return;
            }
            this.v = MediaPlayer.create(this, Uri.fromFile(new File(this.filePath))).getDuration();
            this.dialog = new MaterialDialog.Builder(this).content("Creating Audio with Echo").progress(false, 100, true).cancelable(false).show();
            File file = this.j;
            if (file != null && file.exists()) {
                deleteDirectory(this.j);
                this.finalPath = this.h.getAbsolutePath() + "/" + getString(R.string.app_name) + "/recordings/" + this.l + ".mp3";
            }
            addEcho();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.p.getVisibility() == View.VISIBLE) {
            exitAct();
        } else if (this.g) {
            discardRecDilog();
        } else {
            finish();
        }
    }

    @Override
    @SuppressLint({"StaticFieldLeak"})
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnNew:
//                exitAct();
//                return;
//            case R.id.btnSave:
//                if (this.filePath == null || this.finalPath == null) {
//                    Toast.makeText(this, "Please Record the Audio first.", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (!this.g) {
//                    new AsyncTask<Void, Void, Void>() {
//
//                        public Void doInBackground(Void... voidArr) {
//                            if (MainActivity.this.o.getBoolean("isEchoEnabled", Boolean.FALSE).booleanValue()) {
//                                MainActivity mainActivity = MainActivity.this;
//                                MainActivity.directoryCopy(mainActivity.j, mainActivity.k);
//                                MainActivity mainActivity2 = MainActivity.this;
//                                mainActivity2.deleteDirectory(mainActivity2.j);
//                                return null;
//                            }
//                            MainActivity mainActivity3 = MainActivity.this;
//                            MainActivity.directoryCopy(mainActivity3.i, mainActivity3.k);
//                            MainActivity mainActivity4 = MainActivity.this;
//                            mainActivity4.deleteDirectory(mainActivity4.i);
//                            return null;
//                        }
//
//
//                        public void onPostExecute(Void r4) {
//                            super.onPostExecute(r4);
//                            CDialog.hideLoading();
//                            MainActivity.this.m.setImageResource(R.drawable.record);
//                            MainActivity mainActivity = MainActivity.this;
//                            mainActivity.m.setTag(mainActivity.getString(R.string.record_audio));
//                            MainActivity mainActivity2 = MainActivity.this;
//                            mainActivity2.w.removeCallbacks(mainActivity2.x);
//                            MediaPlayer mediaPlayer = MainActivity.this.f;
//                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                                MainActivity.this.f.stop();
//                                MainActivity.this.f.release();
//                                MainActivity mainActivity3 = MainActivity.this;
//                                mainActivity3.f = null;
//                                mainActivity3.chronometer.setVisibility(View.GONE);
//                            }
//                            MainActivity.this.filePath = null;
//                            MainActivity.this.finalPath = null;
//
//                            MainActivity.this.startActivityForResult(new Intent(MainActivity.this.getApplicationContext(), RecordingListActivity.class), 90);
//                            if (MainActivity.this.p.getVisibility() == View.VISIBLE) {
//                                MainActivity.this.p.setVisibility(View.GONE);
//                            }
//                            Toast.makeText(MainActivity.this, "Recording is saved.", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onPreExecute() {
//                            super.onPreExecute();
//                            CDialog.showLoading(MainActivity.this);
//                        }
//                    }.
//
//                            execute(new Void[0]);
//                    return;
//                } else {
//                    return;
//                }
//            case R.id.playPause:
//                MediaPlayer mediaPlayer = this.f;
//                if (mediaPlayer == null) {
//                    try {
//                        startPlaying();
//                        return;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                } else if (mediaPlayer.isPlaying()) {
//                    this.f.pause();
//                    this.q.setImageResource(R.drawable.ic_play);
//                    return;
//                } else {
//                    this.f.start();
//                    this.q.setImageResource(R.drawable.ic_pause);
//                    this.y = this.f.getDuration();
//                    Log.d("durationPlayer", ">>finalTime: " + this.y);
//                    this.z = (double) this.f.getCurrentPosition();
//                    Log.d("durationPlayer", ">>startTime: " + this.z);
//                    return;
//                }
//            case R.id.rec_list:
//                if (!this.g) {
//                    startActivityForResult(new Intent(getApplicationContext(), RecordingListActivity.class), 90);
//                    return;
//                }
//                return;
//            case R.id.record_audio:
//                if (!this.g && this.m.getTag() != null) {
//                    File file = this.j;
//                    if (file != null && file.exists()) {
//                        deleteDirectory(this.j);
//                    }
//                    startRecording();
//                    return;
//                } else if (this.m.getTag() != null) {
//                    stopRecording();
//                    return;
//                } else {
//                    return;
//                }
//            case R.id.settings:
//                if (!this.g) {
//                    startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), 96);
//                    return;
//                }
//                return;
//            default:
//                return;
//        }


            if (view.getId() == R.id.btnNew) {
                exitAct();
            } else if (view.getId() == R.id.btnSave) {
                if (this.filePath == null || this.finalPath == null) {
                    Toast.makeText(this, "Please Record the Audio first.", Toast.LENGTH_SHORT).show();
                } else if (!this.g) {
                    new AsyncTask<Void, Void, Void>() {
                        public Void doInBackground(Void... voidArr) {
                            if (MainActivity.this.o.getBoolean("isEchoEnabled", Boolean.FALSE).booleanValue()) {
                                MainActivity mainActivity = MainActivity.this;
                                MainActivity.directoryCopy(mainActivity.j, mainActivity.k);
                                MainActivity mainActivity2 = MainActivity.this;
                                mainActivity2.deleteDirectory(mainActivity2.j);
                            } else {
                                MainActivity mainActivity3 = MainActivity.this;
                                MainActivity.directoryCopy(mainActivity3.i, mainActivity3.k);
                                MainActivity mainActivity4 = MainActivity.this;
                                mainActivity4.deleteDirectory(mainActivity4.i);
                            }
                            return null;
                        }

                        public void onPostExecute(Void r4) {
                            super.onPostExecute(r4);
                            CDialog.hideLoading();
                            MainActivity.this.m.setImageResource(R.drawable.record);
                            MainActivity mainActivity = MainActivity.this;
                            mainActivity.m.setTag(mainActivity.getString(R.string.record_audio));
                            MainActivity mainActivity2 = MainActivity.this;
                            mainActivity2.w.removeCallbacks(mainActivity2.x);
                            MediaPlayer mediaPlayer = MainActivity.this.f;
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                MainActivity.this.f.stop();
                                MainActivity.this.f.release();
                                MainActivity mainActivity3 = MainActivity.this;
                                mainActivity3.f = null;
                                mainActivity3.chronometer.setVisibility(View.GONE);
                            }
                            MainActivity.this.filePath = null;
                            MainActivity.this.finalPath = null;

                            MainActivity.this.startActivityForResult(new Intent(MainActivity.this.getApplicationContext(), RecordingListActivity.class), 90);
                            if (MainActivity.this.p.getVisibility() == View.VISIBLE) {
                                MainActivity.this.p.setVisibility(View.GONE);
                            }
                            Toast.makeText(MainActivity.this, "Recording is saved.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPreExecute() {
                            super.onPreExecute();
                            CDialog.showLoading(MainActivity.this);
                        }
                    }.execute();
                }
            } else if (view.getId() == R.id.playPause) {
                MediaPlayer mediaPlayer = this.f;
                if (mediaPlayer == null) {
                    try {
                        startPlaying();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mediaPlayer.isPlaying()) {
                    this.f.pause();
                    this.q.setImageResource(R.drawable.ic_play);
                } else {
                    this.f.start();
                    this.q.setImageResource(R.drawable.ic_pause);
                    this.y = this.f.getDuration();
                    Log.d("durationPlayer", ">>finalTime: " + this.y);
                    this.z = (double) this.f.getCurrentPosition();
                    Log.d("durationPlayer", ">>startTime: " + this.z);
                }
            } else if (view.getId() == R.id.rec_list) {
                if (!this.g) {
                    startActivityForResult(new Intent(getApplicationContext(), RecordingListActivity.class), 90);
                }
            } else if (view.getId() == R.id.record_audio) {
                if (!this.g && this.m.getTag() != null) {
                    File file = this.j;
                    if (file != null && file.exists()) {
                        deleteDirectory(this.j);
                    }
                    startRecording();
                } else if (this.m.getTag() != null) {
                    stopRecording();
                }
            } else if (view.getId() == R.id.settings) {
                if (!this.g) {
                    startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), 96);
                }
            }


    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.act_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsIfNeeded();
        }

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        attributes.screenBrightness = -1.0f;
        getWindow().setAttributes(attributes);

        this.o = new Preference(this);
        findViews();
        File externalStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File file = new File(externalStorageDirectory.getAbsolutePath() + "/" + getString(R.string.app_name) + "/Saved");
        this.k = file;
        if (!file.exists()) {
            this.k.mkdirs();
        }
        String str = externalStorageDirectory.getAbsolutePath() + "/" + getString(R.string.app_name) + "/Saved/rec.mp3";
        this.r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                MediaPlayer mediaPlayer = MainActivity.this.f;
                if (mediaPlayer != null && z) {
                    mediaPlayer.seekTo(i);
                }
                Log.d("durationPlayer", ">>Seekbar on progress changed. " + i);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.e.setText(mainActivity.updateText((long) i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void requestPermissionsIfNeeded() {
        if (!hasPermissions(permissions)) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }


    private boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {

            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
//            if (allPermissionsGranted) {
//                Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show();
//            }
        }
    }




    @Override
    public void onPause() {
        super.onPause();
        try {
            if (this.g) {
                this.d.stop();
                this.d.release();
                this.g = false;
                this.d = null;
                this.chronometer.stop();
                this.chronometer.setVisibility(View.GONE);
                this.g = false;
                resetCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
