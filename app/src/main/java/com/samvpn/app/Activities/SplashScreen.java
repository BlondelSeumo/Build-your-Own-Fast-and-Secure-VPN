package com.samvpn.app.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samvpn.app.R;
import com.samvpn.app.Utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import timber.log.Timber;
import top.oneconnectapi.app.api.OneConnect;

public class SplashScreen extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference typeRef = database.getReference("type");
        DatabaseReference adIdWilldev = database.getReference("adIdWilldev");
        DatabaseReference adBannerWilldev = database.getReference("adBannerWilldev");
        DatabaseReference adNativeWilldev = database.getReference("adNativeWilldev");
        DatabaseReference fbBannerWilldev = database.getReference("fbBannerWilldev");
        DatabaseReference fb_nativeWilldev = database.getReference("fb_nativeWilldev");
        DatabaseReference fbInterstitialWilldev = database.getReference("fbInterstitialWilldev");
        DatabaseReference adInterstitialWilldev = database.getReference("adInterstitialWilldev");
        DatabaseReference fbRewardedWilldev = database.getReference("fbRewardedWilldev");
        DatabaseReference adRewardedWilldev = database.getReference("adRewardedWilldev");
        DatabaseReference ad_switchWilldev = database.getReference("ad_switchWilldev");

        adRewardedWilldev.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Timber.d("onDataChange. adRewardedWilldev = " + value);
                MainActivity.adRewardedWilldev_id = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fbRewardedWilldev.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Timber.d("onDataChange. fbRewardedWilldev = " + value);
                MainActivity.fbRewardedWilldev_id = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String TAG = "Firebase";
        typeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Timber.d("onDataChange. type = " + value);
                MainActivity.type = value;
                Log.d(TAG, "Type" + value);
                Log.d(TAG, "Type" + MainActivity.type);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        adNativeWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.admob_native_id = value;
                Log.d(TAG, "Native" + value);
                Log.d(TAG, "Native" + MainActivity.admob_native_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adIdWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.adIdWilldev = value;
                Log.d(TAG, "Admob ID" + value);
                Log.d(TAG, "Admob ID" + MainActivity.adIdWilldev);
                try {
                    ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = applicationInfo.metaData;
                    applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", MainActivity.adIdWilldev);
                    String apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    Log.d(TAG, "The saved id is " + MainActivity.adIdWilldev);
                    Log.d(TAG, "The saved id is " + apiKey);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adBannerWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.admob_banner_id = value;
                Log.d(TAG, "Admob Banner" + value);
                Log.d(TAG, "Admob Banner" + MainActivity.admob_banner_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adInterstitialWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.admob_interstitial_id = value;
                Log.d(TAG, "Admob interstitial" + value);
                Log.d(TAG, "Admob interstitial" + MainActivity.admob_interstitial_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        fb_nativeWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 String value = dataSnapshot.getValue(String.class);
                MainActivity.fb_nativeWilldev_id = value;
                Log.d(TAG,"fb_nativeWilldev"+value);
                Log.d(TAG,"fb_nativeWilldev"+MainActivity.fb_nativeWilldev_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        fbBannerWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.fbBannerWilldev_id = value;
                Log.d(TAG, "fbBannerWilldev" + value);
                Log.d(TAG, "fbBannerWilldev" + MainActivity.fbBannerWilldev_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        fbInterstitialWilldev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MainActivity.fbInterstitialWilldev_id = value;
                Log.d(TAG, "fbInterstitialWilldev" + value);
                Log.d(TAG, "fbInterstitialWilldev" + MainActivity.fbInterstitialWilldev_id);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ad_switchWilldev.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                MainActivity.ad_switchWilldev = false;

                assert value != null;
                if(value.equalsIgnoreCase("on")) {
                    MainActivity.ad_switchWilldev = true;
                }

                Log.d(TAG,"ad_switchWilldev "+value);
                Log.d(TAG,"ad_switchWilldev "+MainActivity.ad_switchWilldev);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

                MainActivity.ad_switchWilldev = false;
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_willdev);
        coordinatorLayout = findViewById(R.id.cordi);

        if (!Utility.isOnline(getApplicationContext())) {


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Check internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();


        } else {
            checkServers();
        }

    }

    private void checkServers() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OneConnect oneConnect = new OneConnect();
                    oneConnect.initialize(SplashScreen.this, "b1Cf1gGlzNC8..71jidRTNcQrEvWVC7cddS136CJIJsXQu4kV3");  // You need to put Your Own Api Key

                    try {
                        Constants.FREE_SERVERS = oneConnect.fetch(true);
                        Constants.PREMIUM_SERVERS = oneConnect.fetch(false);

                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
