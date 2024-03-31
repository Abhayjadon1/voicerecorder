package com.ispl.voice.recorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ispl.voice.recorder.R;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;


public class Const {
    public static int AdClickCnt = 1;
    public static final char DIRECTORY_SEPARATOR = '/';
    public static final char EXTENSION_SEPARATOR = '.';
    public static RequestOptions albumoption = new RequestOptions().placeholder(R.drawable.empty_place).diskCacheStrategy(DiskCacheStrategy.ALL).override(Integer.MIN_VALUE);
    public static int rateCnt = 3;

    public static Date addDays(Date date, int i) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(5, i);
        return gregorianCalendar.getTime();
    }

    public static void deleteFile(Context context, File file, String str) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 20) {
            if (str.equals("video/*")) {
                context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data= ?", new String[]{file.getAbsolutePath()});
            } else {
                context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "_data= ?", new String[]{file.getAbsolutePath()});
            }
        } else if (i >= 19) {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        } else {
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file)));
        }
    }

    public static int indexOfExtension(String str) {
        int lastIndexOf;
        if (str != null && str.lastIndexOf(47) <= (lastIndexOf = str.lastIndexOf(46))) {
            return lastIndexOf;
        }
        return -1;
    }

    public static String removeExtension(String str) {
        if (str == null) {
            return null;
        }
        int indexOfExtension = indexOfExtension(str);
        return indexOfExtension == -1 ? str : str.substring(0, indexOfExtension);
    }
}
