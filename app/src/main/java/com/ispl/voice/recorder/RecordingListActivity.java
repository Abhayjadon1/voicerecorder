package com.ispl.voice.recorder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispl.voice.recorder.R;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;


public class RecordingListActivity extends AppCompatActivity {
    public static ArrayList<String> recordings = new ArrayList<>();
    public RecyclerView a;
    public String b;
    public RingAdapter c;
    public boolean d = false;
    public TextView e;
    public RelativeLayout f;
   

    
    public class RingAdapter extends RecyclerView.Adapter<RingAdapter.ItemRowHolder> {

        
        
        public class AnonymousClass2 implements View.OnClickListener {
            public final  int a;

            public AnonymousClass2(int i) {
                this.a = i;
            }

            @Override 
            @SuppressLint({"SetTextI18n"})
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RecordingListActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.custom);
                ((TextView) dialog.findViewById(R.id.ringtoneName)).setText(RecordingListActivity.recordings.get(this.a) + ".mp3");
                ((RelativeLayout) dialog.findViewById(R.id.shareRingtone)).setOnClickListener(new View.OnClickListener() { 
                    @Override 
                    public void onClick(View view2) {
                        Uri parse = Uri.parse(RecordingListActivity.this.b + "/" + RecordingListActivity.recordings.get(AnonymousClass2.this.a) + ".mp3");
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("audio/*");
                        if (Build.VERSION.SDK_INT >= 22) {
                            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(RecordingListActivity.this.getApplicationContext(), getPackageName()+".provider", new File(String.valueOf(parse))));
                        } else {
                            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(parse.toString())));
                        }
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        RecordingListActivity.this.startActivity(Intent.createChooser(intent, "Share audio File"));
                        dialog.dismiss();
                    }
                });
                ((RelativeLayout) dialog.findViewById(R.id.deleteRingtone)).setOnClickListener(new View.OnClickListener() { 
                    @Override 
                    public void onClick(View view2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecordingListActivity.this, R.style.AlertDialogCustom);
                        builder.setTitle(RecordingListActivity.this.getResources().getString(R.string.dlg_confm_title));
                        builder.setMessage(RecordingListActivity.this.getResources().getString(R.string.dlg_delete_msg));
                        builder.setPositiveButton(RecordingListActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() { 
                            @Override 
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + RecordingListActivity.this.getResources().getString(R.string.app_name) + "/Saved/" + RecordingListActivity.recordings.get(AnonymousClass2.this.a) + ".mp3");
                                file.delete();
                                Const.deleteFile(RecordingListActivity.this, file, "audio/*");
                                RecordingListActivity.recordings.remove(AnonymousClass2.this.a);
                                RecordingListActivity recordingListActivity = RecordingListActivity.this;
                                Toast.makeText(recordingListActivity, recordingListActivity.getResources().getString(R.string.success_file_delete), Toast.LENGTH_SHORT).show();
                                RingAdapter.this.notifyDataSetChanged();
                                RecordingListActivity.this.refreshRingAdapter();
                            }
                        });
                        builder.setNegativeButton(RecordingListActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { 
                            @Override 
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                        dialog.dismiss();
                        RingAdapter.this.notifyDataSetChanged();
                    }
                });
                ((RelativeLayout) dialog.findViewById(R.id.renameRingtone)).setOnClickListener(new View.OnClickListener() { 
                    @Override 
                    @RequiresApi(api = 19)
                    public void onClick(View view2) {
                        final Dialog dialog2 = new Dialog(RecordingListActivity.this);
                        dialog2.requestWindowFeature(1);
                        dialog2.setContentView(R.layout.rename_d);
                        final EditText editText = (EditText) dialog2.findViewById(R.id.editText);
                        editText.setText(RecordingListActivity.recordings.get(AnonymousClass2.this.a));
                        ((Button) dialog2.findViewById(R.id.okay)).setOnClickListener(new View.OnClickListener() { 
                            @Override 
                            public void onClick(View view3) {
                                String obj = editText.getText().toString();
                                if (obj.equals("") || obj.equals(" ")) {
                                    editText.setError(RecordingListActivity.this.getResources().getString(R.string.error_filename));
                                }
                                int i = 0;
                                while (true) {
                                    if (i > RecordingListActivity.recordings.size() - 1) {
                                        break;
                                    } else if (Objects.equals(obj, RecordingListActivity.recordings.get(i))) {
                                        editText.setError(RecordingListActivity.this.getResources().getString(R.string.error_file_exist));
                                        RecordingListActivity.this.d = true;
                                        break;
                                    } else {
                                        RecordingListActivity.this.d = false;
                                        i++;
                                    }
                                }
                                if (!RecordingListActivity.this.d) {
                                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + RecordingListActivity.this.getResources().getString(R.string.app_name) + "/Saved/" + RecordingListActivity.recordings.get(AnonymousClass2.this.a) + ".mp3");
                                    file.renameTo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + RecordingListActivity.this.getResources().getString(R.string.app_name) + "/Saved/" + obj + ".mp3"));
                                    RecordingListActivity.recordings.set(AnonymousClass2.this.a, obj);
                                    RecordingListActivity.this.refreshRingAdapter();
                                    RingAdapter.this.notifyDataSetChanged();
                                    dialog2.dismiss();
                                }
                            }
                        });
                        ((Button) dialog2.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { 
                            @Override 
                            public void onClick(View view3) {
                                dialog2.dismiss();
                            }
                        });
                        dialog2.show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }

        
        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public RelativeLayout a;
            public RelativeLayout b;
            public TextView c;
            public TextView d = (TextView) this.itemView.findViewById(R.id.time);
            public TextView e = (TextView) this.itemView.findViewById(R.id.size);

            public ItemRowHolder(RingAdapter ringAdapter, View view) {
                super(view);
                this.a = (RelativeLayout) view.findViewById(R.id.rel);
                this.c = (TextView) view.findViewById(R.id.pretv);
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.more);
                this.b = relativeLayout;
                relativeLayout.setVisibility(View.VISIBLE);
            }
        }

        public RingAdapter() {
        }

        @Override 
        public int getItemCount() {
            return RecordingListActivity.recordings.size();
        }

        public void onBindViewHolder(@NonNull ItemRowHolder itemRowHolder, @SuppressLint({"RecyclerView"}) final int i) {
            itemRowHolder.c.setText(RecordingListActivity.recordings.get(i));
            File file = new File(RecordingListActivity.this.b + "/" + RecordingListActivity.recordings.get(i) + ".mp3");
            itemRowHolder.e.setText(RecordingListActivity.getStringSizeLengthFile(file.length()));
            try {
                itemRowHolder.d.setText(RecordingListActivity.this.getDuration(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemRowHolder.a.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(Uri.parse("file://" + RecordingListActivity.this.b + "/" + RecordingListActivity.recordings.get(i) + ".mp3"));
                    Log.d("iiiiiiiiiiiiiiiiii", sb.toString());
                    File file2 = new File(RecordingListActivity.this.b + "/" + RecordingListActivity.recordings.get(i) + ".mp3");
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(335544320);
                    if (Build.VERSION.SDK_INT >= 22) {
                        intent.setDataAndType(FileProvider.getUriForFile(RecordingListActivity.this.getApplicationContext(), getPackageName()+".provider", file2), "audio/mp3");
                    } else {
                        intent.setDataAndType(Uri.fromFile(file2), "audio/mp3");
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    RecordingListActivity.this.startActivity(intent);
                }
            });
            itemRowHolder.b.setOnClickListener(new AnonymousClass2(i));
        }

        @Override 
        @NonNull
        public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemRowHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, (ViewGroup) null));
        }
    }

    
    public String getDuration(File file) throws Exception {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(new FileInputStream(file).getFD());
        mediaPlayer.prepare();
        mediaPlayer.release();
        long duration = mediaPlayer.getDuration() / 1000;
        long j = (duration / 3600) * 3600;
        long j2 = (duration - j) / 60;
        long j3 = duration - (j + (60 * j2));
        StringBuilder sb = new StringBuilder();
        String str = "0";
        sb.append(j2 < 10 ? str : "");
        sb.append(j2);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        if (j3 >= 10) {
            str = "";
        }
        sb3.append(str);
        sb3.append(j3);
        String sb4 = sb3.toString();
        return sb2 + ":" + sb4;
    }

    private void getRingtones() {
        try {
            recordings.clear();
            this.b = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Saved";
            File[] listFiles = new File(this.b).listFiles();
            Arrays.sort(listFiles, new Comparator<File>() { 
                public int compare(File file, File file2) {
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                }
            });
            for (File file : listFiles) {
                recordings.add(Const.removeExtension(file.getName()));
            }
        } catch (NullPointerException unused) {
        }
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat.format(f / 1024.0f) + " KB";
        } else if (f < 1.07374182E9f) {
            return decimalFormat.format(f / 1048576.0f) + " MB";
        } else if (f >= 1.09951163E12f) {
            return "";
        } else {
            return decimalFormat.format(f / 1.07374182E9f) + " GB";
        }
    }

    @Override 
    public void onBackPressed() {
        setResult(102);
        super.onBackPressed();
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.act_recordinglist);




        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.FullscreenAd(this);




        new Preference(this);

        this.a = (RecyclerView) findViewById(R.id.recyclerView);
        this.e = (TextView) findViewById(R.id.emptyText);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back);
        this.f = relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                RecordingListActivity.this.setResult(102);
                RecordingListActivity.this.finish();
            }
        });
        getRingtones();
        this.c = new RingAdapter();
        this.a.setHasFixedSize(true);
        this.a.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.a.setAdapter(this.c);
        refreshRingAdapter();
    }

    @Override 
    public void onResume() {
        super.onResume();
        getRingtones();
        this.c.notifyDataSetChanged();
        refreshRingAdapter();
    }

    public void refreshRingAdapter() {
        if (recordings.size() == 0) {
            this.e.setVisibility(View.VISIBLE);
        } else {
            this.e.setVisibility(View.GONE);
        }
    }
}
