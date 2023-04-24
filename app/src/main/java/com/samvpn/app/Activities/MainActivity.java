package com.samvpn.app.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.bumptech.glide.Glide;
import com.samvpn.app.model.Countries;
import com.samvpn.app.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;
import top.oneconnectapi.app.OpenVpnApi;
import top.oneconnectapi.app.core.OpenVPNThread;

public class MainActivity extends ContentsActivity implements PurchasesUpdatedListener, BillingClientStateListener {

    private Countries selectedCountry = null;
    private Locale locale;

    public static String type;
    public static String adIdWilldev;
    public static String admob_banner_id;
    public static String admob_interstitial_id;
    public static String admob_native_id;
    public static String adRewardedWilldev_id;
    public static String fbBannerWilldev_id;
    public static String fb_nativeWilldev_id;
    public static String fbInterstitialWilldev_id;
    public static String fbRewardedWilldev_id;
    public static boolean ad_switchWilldev = false;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAdMob;

    private OpenVPNThread vpnThread = new OpenVPNThread();

    private BillingClient billingClient;
    private Map<String, SkuDetails> skusWithSkuDetails = new HashMap<>();
    private final List<String> allSubs = new ArrayList<>(Arrays.asList(
            Config.all_month_id,
            Config.all_threemonths_id,
            Config.all_sixmonths_id,
            Config.all_yearly_id));

    private void connectToBillingService() {
        if (!billingClient.isReady()) {
            billingClient.startConnection(this);
        }
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            querySkuDetailsAsync(
                    BillingClient.SkuType.SUBS,
                    allSubs
            );
            queryPurchases();
        }
        updateSubscription();
    }

    @Override
    public void onBillingServiceDisconnected() {
        connectToBillingService();
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

    }

    private void queryPurchases() {
        Purchase.PurchasesResult result = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
        List<Purchase> purchases = result.getPurchasesList();
        List<String> skus = new ArrayList<>();

        if (purchases != null) {
            int i = 0;
            for (Purchase purchase : purchases) {
                skus.add(purchase.getSkus().get(i));
                Log.v("CHECKBILLING", purchase.getSkus().get(i));
                i++;
            }

            if (skus.contains(Config.all_month_id) ||
                    skus.contains(Config.all_threemonths_id) ||
                    skus.contains(Config.all_sixmonths_id) ||
                    skus.contains(Config.all_yearly_id)
            ) {
                Config.all_subscription = true;
            }
        }
    }

    private void querySkuDetailsAsync(@BillingClient.SkuType String skuType, List<String> skuList) {
        SkuDetailsParams params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(skuType)
                .build();

        billingClient.querySkuDetailsAsync(
                params, (billingResult, skuDetailsList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                        for (SkuDetails details : skuDetailsList) {
                            skusWithSkuDetails.put(details.getSku(), details);
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));

        billingClient = BillingClient
                .newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        connectToBillingService();

        getIP();
    }

    public void prepareVpn() {

        Glide.with(this)
                .load(selectedCountry.getFlagUrl())
                .into(imgFlag);
        flagName.setText(selectedCountry.getCountry());

        if (Utility.isOnline(getApplicationContext())) {

            if(selectedCountry != null) {
                Intent intent = VpnService.prepare(this);
                Log.v("CHECKSTATE", "start " + selectedCountry.getCountry());

                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else
                    startVpn(); //have already permission
            } else {
                showMessage("Please select a server first", "");
            }

        } else {
            showMessage("No Internet Connection", "error");
        }
    }

    protected void startVpn() {
        try {
            OpenVpnApi.startVpn(this, selectedCountry.getOvpn(), selectedCountry.getCountry(), selectedCountry.getOvpnUserName(), selectedCountry.getOvpnUserPassword());

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                updateUI(intent.getStringExtra("state"));
                Log.v("CHECKSTATE", intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";

                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void scrollViewPagerToSelectedCountry() {
        if (selectedCountry == null) {
            animateViewPager(false);
            return;
        }
        int i = 0;
        for (Countries countryNew : viewPagerServersAdapter.getServers()) {
            if (countryNew.getCountry().equals(selectedCountry)) {
                viewPager.setCurrentItem(i);
                animateViewPager(true);
                return;
            }
            i++;
        }
        animateViewPager(false);
    }

    private void animateViewPager(Boolean withDelay) {
        if (viewPager.getAlpha() == 0f) {
            viewPager.setScaleX(0.9f);
            viewPager.setScaleY(0.9f);
            viewPager.post(() -> viewPager.animate().alpha(1f).scaleX(1f).scaleY(1f).setStartDelay(withDelay ? 200 : 0).setDuration(200).start());
        }
    }

    @Override
    public void onConnectClick(Countries country, int position) {
        selectedCountry = country;
        prepareVpn();
    }

    public void getIP() {
        new GetPublicIP().execute();
    }

    public class GetPublicIP extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String publicIP = "";
            try {
                java.util.Scanner s = new java.util.Scanner(
                        new java.net.URL(
                                "https://api.ipify.org")
                                .openStream(), "UTF-8")
                        .useDelimiter("\\A");
                publicIP = s.next();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return publicIP;
        }

        @Override
        protected void onPostExecute(String publicIp) {
            super.onPostExecute(publicIp);
            Timber.d("PublicIP = " + publicIp);
            if (spro_ip_adress_30266325 != null && publicIp != null && !publicIp.trim().isEmpty())
                spro_ip_adress_30266325.setText(String.format(" %s", publicIp));
        }
    }

    @Override
    protected void disconnectFromVpn() {
        try {
            vpnThread.stop();
            updateUI("DISCONNECTED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startVpn();
        } else {
            showMessage("Permission Denied", "error");
        }
    }
}
