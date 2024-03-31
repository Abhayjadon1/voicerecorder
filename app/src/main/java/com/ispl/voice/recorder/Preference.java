package com.ispl.voice.recorder;

import android.content.Context;
import android.content.SharedPreferences;


public class Preference {
    public static final String PREF_NAME = "app.echorecord";
    public SharedPreferences a;
    public SharedPreferences.Editor b;
    public int c = 0;

    public Preference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.a = sharedPreferences;
        this.b = sharedPreferences.edit();
    }

    public void clearPreference() {
        this.b.clear();
        this.b.commit();
    }

    public Boolean getBoolean(String str) {
        return Boolean.valueOf(this.a.getBoolean(str, false));
    }

    public Integer getInteger(String str) {
        return Integer.valueOf(this.a.getInt(str, 0));
    }

    public String getString(String str) {
        return this.a.getString(str, null);
    }

    public void setBoolean(String str, Boolean bool) {
        this.b.putBoolean(str, bool.booleanValue());
        this.b.commit();
    }

    public void setInteger(String str, Integer num) {
        this.b.putInt(str, num.intValue());
        this.b.commit();
    }

    public void setString(String str, String str2) {
        this.b.putString(str, str2);
        this.b.commit();
    }

    public Boolean getBoolean(String str, Boolean bool) {
        return Boolean.valueOf(this.a.getBoolean(str, bool.booleanValue()));
    }

    public String getString(String str, String str2) {
        return this.a.getString(str, str2);
    }
}
