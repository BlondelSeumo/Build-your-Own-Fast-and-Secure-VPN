package com.samvpn.app;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;


import com.pixplicity.easyprefs.library.Prefs;

import timber.log.Timber;

public class App extends Application {




    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();



        if (BuildConfig.DEBUG) initTimber();

    }

    private void initTimber() {
        Timber.plant(new Timber.DebugTree());
    }



    public SharedPreferences getPrefs() {
        return getSharedPreferences(BuildConfig.SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
