package com.samvpn.app.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.samvpn.app.AdapterWrappers.ViewPagerServersAdapter;
import com.samvpn.app.BuildConfig;
import com.samvpn.app.Config;
import com.samvpn.app.Fragments.UnlockAllFragment;
import com.samvpn.app.OnTapToConnectVariantClickListener;
import com.samvpn.app.R;
import com.samvpn.app.Utils.Constants;
import com.samvpn.app.Utils.Helper;
import com.samvpn.app.Utils.LocalFormatter;
import com.samvpn.app.model.Countries;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public abstract class ContentsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        OnTapToConnectVariantClickListener {

    boolean vpn_toast_check = true;
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    Handler handlerTrafic = null;

    private boolean isFirst = true;

    protected static final String TAG = "ADVERTS";
    AdRequest adRequest = new AdRequest.Builder().build();

    private com.facebook.ads.NativeAd fbNativeAd;

    private int adCount = 0;
    int progressBarValue = 0;
    Handler handler = new Handler();
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.downloading)
    TextView textDownloading;

    @BindView(R.id.uploading)
    TextView textUploading;

    @BindView(R.id.connection_status)
    TextView t_connection_status;

    @BindView(R.id.connection_status_image)
    ImageView i_connection_status_image;

    @BindView(R.id.vpn_location)
    FrameLayout vpn_location;


    @BindView(R.id.connect_btn)
    CardView connectBtnTextView;


    @BindView(R.id.flag_image)
    ImageView imgFlag;

    @BindView(R.id.spro_rc_30266325)
    RecyclerView rcvFree;

    @BindView(R.id.flag_name)
    TextView flagName;

    @BindView(R.id.footer)
    RelativeLayout footer;

    @BindView(R.id.tvState)
    TextView tvState;

    @BindView(R.id.viewProgress)
    View viewProgress;

    @BindView(R.id.spro_page_time_30266325)
    ViewPager2 viewPager;

    @BindView(R.id.progress_circular)
    CircularProgressIndicator progressIndicator;

    @BindView(R.id.btn_disconnect)
    MaterialButton btnDisconnect;

    protected ViewPagerServersAdapter viewPagerServersAdapter;

    protected int chosenServerPosition = -1;
    protected Countries chosenServer = null;

    com.facebook.ads.AdView facebookAdview;

    private NativeAd nativeAd;
    NativeAdLayout nativeAdLayout;
    LinearLayout frameLayout;
    public InterstitialAd mInterstitialAd;
    public com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private RewardedAd mRewardedAd;
    private String STATUS = "DISCONNECTED";
    private boolean loadingAd = false;
    //private DrawerLayout drawer;

    private boolean isRecentlyRewardedWasShown = false;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAdMob;

    //ADMOB
    private RewardedAd rewardedAdmob;
    private void connectToVpn() {
            viewPagerServersAdapter.notifyItemChanged(chosenServerPosition);
            onConnectClick(chosenServer, chosenServerPosition);
    }

    //FACEBOOK
    private RewardedVideoAd rewardedFacebook;
    private RewardedVideoAdListener rewardedFacebookListener;


    BottomNavigationView bottomNavigationView;
    TextView spro_ip_adress_30266325;

    private ValueAnimator animator = null;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_willdev);

        spro_ip_adress_30266325 = findViewById(R.id.spro_ip_adress_30266325);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        nativeAdLayout = findViewById(R.id.native_ad_container);
        frameLayout = findViewById(R.id.admob_adplaceholder);

        LineChart dev_graph = findViewById(R.id.dev_graph);
        LineChart dev30266325 = findViewById(R.id.dev30266325);

        ArrayList<Entry> uploadList = new ArrayList<>();
        uploadList.add(new Entry(0, 5));
        uploadList.add(new Entry(1, 30));
        uploadList.add(new Entry(2, 100));
        uploadList.add(new Entry(3, 65));
        uploadList.add(new Entry(4, 80));

        ArrayList<Entry> downloadList = new ArrayList<>();
        downloadList.add(new Entry(0, 5));
        downloadList.add(new Entry(1, 30));
        downloadList.add(new Entry(2, 65));
        downloadList.add(new Entry(3, 50));
        downloadList.add(new Entry(4, 100));

        initGraph(dev_graph, uploadList);
        initGraph(dev30266325, downloadList);

        lottieAnimationView = findViewById(R.id.lottieLogo);
        lottieAnimationView.playAnimation();


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        ButterKnife.bind(this);
        btnDisconnect.setOnClickListener(v -> disconnectAlert());

        setSupportActionBar(toolbar);

        initBtnConnect(false);

        vpn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServerList();
            }
        });

        loadServers();

        if (canShowAd()) {
            try {

                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                        Log.v(TAG, "Admob initialized");
                        loadAdmobRewarded();
                    }
                });

                AudienceNetworkAds.initialize(this);
                loadFacebookRewarded();

            } catch (Exception e) {
                Timber.d("Exception = " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void setViewPager(List<Countries> servers) {
        viewPagerServersAdapter = new ViewPagerServersAdapter(servers, ContextCompat.getDrawable(this, R.drawable.ic_crowd), this, ContentsActivity.this);
        int pageOffset = getResources().getDimensionPixelSize(R.dimen.main_view_pager_item_offset);
        int pageMargin = getResources().getDimensionPixelSize(R.dimen.main_view_pager_item_margin);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setPageTransformer((page, position) -> {
                    Log.d("viewPager", "position = " + position);
                    float myOffset = position * -(2 * pageOffset + pageMargin);
                    if (position < -1) {
                        Log.d("viewPager", "set translationX = " + -myOffset);
                        page.setTranslationX(-myOffset);
                    } else if (position <= 1) {
                        float scaleFactor = Math.max(0.75f, 1 - Math.abs(position));
                        Log.d("viewPager", "set translationX = " + myOffset + ", scaleFactor = " + scaleFactor);
                        page.setTranslationX(myOffset);
                        page.setScaleY(scaleFactor);
                        page.setScaleX(scaleFactor);
                        page.setAlpha(1f);
                    } else {
                        Log.d("viewPager", "translationX = " + myOffset);
                        page.setAlpha(0);
                        page.setTranslationX(myOffset);
                    }
                }
        );
        viewPager.setAdapter(viewPagerServersAdapter);
        progressIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onBuySubscriptionClick() {
        openUnlockActivity();
    }

    @Override
    public void onWatchAdClick(Countries country, int position) {
        chosenServerPosition = position;
        chosenServer = country;


        if (!STATUS.equals("DISCONNECTED")) {
            disconnectAlert();
        } else {
            if (!Utility.isOnline(this)) {
                showMessage("No Internet Connection", "error");
            } else {

                if(canShowAd()) {
                        if(MainActivity.type.equalsIgnoreCase("ad")) {
                            if (mRewardedAd != null) {

                                mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                        Log.d(TAG, "The user earned the reward.");
                                        connectToVpn();
                                    }
                                });
                            } else {
                                new AlertDialog.Builder(this)
                                        .setTitle("Ads not available")
                                        .setMessage(getString(R.string.ads_not_available_buy_subscription))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        } else {
                            if (rewardedFacebook != null && rewardedFacebook.isAdLoaded())
                                rewardedFacebook.show();
                            else {
                                new AlertDialog.Builder(this)
                                        .setTitle("Ads not available")
                                        .setMessage(getString(R.string.ads_not_available_buy_subscription))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                } else {
                    connectToVpn();
                }

            }
        }
    }

    @Override
    public void onDirectConnect(Countries country, int position) {
        chosenServerPosition = position;
        chosenServer = country;

        if (!STATUS.equals("DISCONNECTED")) {
            disconnectAlert();
        } else {
            if (!Utility.isOnline(this)) {
                showMessage("No Internet Connection", "error");
            } else {
                showInterstitialAndConnect();
            }
        }
    }

    protected void loadAdmobRewarded() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, MainActivity.adRewardedWilldev_id,
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.v(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        Log.v(TAG, "Admob loaded");
                        mRewardedAd = rewardedAd;
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {

                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                                loadAdmobRewarded();
                            }
                        });
                    }
                });
    }

    protected void loadFacebookRewarded() {
        Timber.d("loadFacebookRewarded");
        destroyFacebookRewarded();
        String fbRewardedWilldev_id = MainActivity.fbRewardedWilldev_id;
        if (fbRewardedWilldev_id == null) return;
        rewardedFacebook = new RewardedVideoAd(this, fbRewardedWilldev_id);
        if (rewardedFacebookListener == null)
            rewardedFacebookListener = new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoCompleted() {
                    Timber.d("rewardedFacebookListener. onRewardedVideoCompleted");
                    connectToVpn();
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Timber.d("rewardedFacebookListener. onLoggingImpression");
                }

                @Override
                public void onRewardedVideoClosed() {
                    Timber.d("rewardedFacebookListener. onRewardedVideoClosed");
                    isRecentlyRewardedWasShown = true;
                    loadFacebookRewarded();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Timber.d("rewardedFacebookListener. onError. message = " + adError.getErrorMessage() + ", code = " + adError.getErrorCode());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Timber.d("rewardedFacebookListener. onAdLoaded");
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Timber.d("rewardedFacebookListener. onAdClicked");
                }
            };
        rewardedFacebook.loadAd(
                rewardedFacebook.buildLoadAdConfig()
                        .withAdListener(rewardedFacebookListener)
                        .build()
        );
    }

    @Override
    public abstract void onConnectClick(Countries country, int position);

    public final static String TAG_1 = "servers";

    //need to check changes in subs, ads etc
    private Boolean IS_ADS = null;

    protected void loadServers() {

        ArrayList<Countries> servers = new ArrayList<>();

        try {
            JSONArray jsonArray1 = new JSONArray(Constants.PREMIUM_SERVERS);
            JSONArray jsonArray2 = new JSONArray(Constants.FREE_SERVERS);

            int premiumSize = jsonArray1.length();
            int freeSize = jsonArray2.length();

            int arrSize = Math.max(premiumSize, freeSize);

            for (int i = 0; i < arrSize; i++) {

                if (i < premiumSize) {
                    JSONObject object1 = (JSONObject) jsonArray1.get(i);

                    servers.add(new Countries(object1.getString("serverName"),
                            object1.getString("flag_url"),
                            object1.getString("ovpnConfiguration"),
                            object1.getString("vpnUserName"),
                            object1.getString("vpnPassword"),
                            false
                    ));
                }

                if (i < freeSize) {
                    JSONObject object2 = (JSONObject) jsonArray2.get(i);

                    servers.add(new Countries(object2.getString("serverName"),
                            object2.getString("flag_url"),
                            object2.getString("ovpnConfiguration"),
                            object2.getString("vpnUserName"),
                            object2.getString("vpnPassword"),
                            true
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("JSONERROR", e.getMessage());
        }

        setViewPager(servers);
        scrollViewPagerToSelectedCountry();
    }

    public boolean canShowAd() {
        return MainActivity.ad_switchWilldev && !Config.ads_subscription && !Config.all_subscription && !Config.vip_subscription;
    }

    protected abstract void scrollViewPagerToSelectedCountry();

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void initGraph(LineChart chart, ArrayList<Entry> list) {

        LineDataSet set1 = new LineDataSet(list, "DataSet 1");
        set1.setLineWidth(1.75F);
        set1.setCircleRadius(5F);
        set1.setCircleHoleRadius(2.5F);
        set1.setDrawFilled(true);
        set1.setFillColor(getResources().getColor(R.color.colorBackgroundLineChart));
        set1.setColor(getResources().getColor(R.color.colorLineChart));
        set1.setCircleColor(getResources().getColor(R.color.colorLineChart));

        LineData data = new LineData(set1);

        data.setDrawValues(false);

        if (chart.getDescription() != null) {
            chart.getDescription().setEnabled(false);
        }

        chart.setTouchEnabled(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);

        chart.setViewPortOffsets(10F, 0F, 10F, 0F);

        chart.setData(data);
        chart.invalidate();

        if (chart.getLegend() != null) {
            chart.getLegend().setEnabled(false);
        }

        if (chart.getAxisLeft() != null) {
            chart.getAxisLeft().setEnabled(false);
        }

        if (chart.getAxisLeft() != null) {
            chart.getAxisLeft().setSpaceTop(40F);
        }

        if (chart.getAxisLeft() != null) {
            chart.getAxisLeft().setSpaceBottom(40F);
        }

        if (chart.getAxisRight() != null) {
            chart.getAxisLeft().setEnabled(false);
        }

        if (chart.getXAxis() != null) {
            chart.getAxisLeft().setEnabled(false);
        }

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getXAxis().setEnabled(false);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void loadAdAgain() {

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {


            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using this Free VPN App, it's provide all servers free https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundPremium));
                }


                break;


            case R.id.nav_policy:

                Uri uri = Uri.parse(getResources().getString(R.string.privacy_policy_link));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundPremium));
                }


                break;

            case R.id.nav_rate:

                Uri ur = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, ur);
                 goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }


                break;

            case R.id.bottom_navigation_home:
                removeFragment();
                bottomNavigationView.getMenu().getItem(0).setEnabled(false);
                bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.mainColor));
                }
                break;
            case R.id.bottom_navigation_premium:
                loadFragment(new UnlockAllFragment(this));
                bottomNavigationView.getMenu().getItem(1).setEnabled(false);
                bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundPremium));
                }
                break;


        }

        return true;

    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder) != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder));
            fragmentTransaction.commit();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume");

        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_home);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }

        if (facebookAdview != null) {
            facebookAdview.destroy();
        }

        destroyFacebookRewarded();
        super.onDestroy();
    }

    private void destroyFacebookRewarded() {
        if (rewardedFacebook != null) {
            rewardedFacebook.destroy();
            rewardedFacebook = null;
        }
    }

    @OnClick(R.id.connect_btn)
    public void onConnectBtnClick(View v) {
        Timber.d("onConnectBtnClick");

        fakeProgress(500L);

        lottieAnimationView.setRepeatMode(LottieDrawable.RESTART);
        lottieAnimationView.playAnimation();



    }

    protected void updateUI(String vpnState) {

        updateViewPagerItemState(vpnState);
        STATUS = vpnState;

        switch (vpnState) {
            case "CONNECTED": {
                textDownloading.setVisibility(View.VISIBLE);
                textUploading.setVisibility(View.VISIBLE);

                lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                lottieAnimationView.playAnimation();


                connectBtnTextView.setEnabled(true);
                tvState.setText(getString(R.string.disconnect));
                timer();
                getIP();
                hideConnectProgress();

                Helper.saveServer("server", chosenServerPosition, this);
                break;
            }
            case "WAIT":
            case "AUTH":
            case "RECONNECTING":
            case "LOAD": {

                tvState.setText(getString(R.string.connect));
                connectBtnTextView.setEnabled(true);


                textDownloading.setText("0 kB/s");
                textUploading.setText("0 kB/s");
                break;
            }
            case "NONETWORK":
            case "DISCONNECTED": {
                t_connection_status.setText("Not Selected");
                tvState.setText(getString(R.string.connect));
                i_connection_status_image.setImageResource(R.drawable.ic_dot);
                getIP();

                textDownloading.setText("0 kB/s");
                textUploading.setText("0 kB/s");

                Helper.deleteSaveServer("server",this);
                break;
            }
            default: {

            }
        }
    }

    public void updateConnectionStatus(String duration, String lastPacketReceive, String byteIn, String byteOut) {

        String[] byteinKb = byteIn.split(" - ");
        String[] byteoutKb = byteOut.split(" - ");

        textDownloading.setText(byteinKb[1]);
        textUploading.setText(byteoutKb[1]);

        //CHECK IF CONNECTED TO VPN
        if(isFirst) {
            isFirst = false;
            if(Integer.parseInt(Helper.getSavedServer("server", this)) > -1) {
                chosenServerPosition = Integer.parseInt(Helper.getSavedServer("server", this));
                updateUI("CONNECTED");
            }
        }
    }

    private void updateViewPagerItemState(String vpnState) {
        if (chosenServerPosition == -1 || viewPagerServersAdapter == null || viewPager == null)
            return;

        boolean isAlreadyHandleVPNState = viewPagerServersAdapter.isAlreadyHandleVPNState(vpnState, chosenServerPosition);

        //if (!viewPagerServersAdapter.isAlreadyHandleVPNState(vpnState, chosenServerPosition)) {
            viewPagerServersAdapter.updateItemVpnState(vpnState, chosenServerPosition);

            if (btnDisconnect != null && progressIndicator != null) {
                progressIndicator.setIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary2));

                if (vpnState.equals("LOAD") ||
                        vpnState.equals("WAIT") ||
                        vpnState.equals("AUTH") ||
                        vpnState.equals("RECONNECTING") ||
                        vpnState.equals("GET_CONFIG") ||
                        vpnState.equals("ASSIGN_IP") ||
                        vpnState.equals("RESOLVE")) {
                    progressIndicator.setVisibility(View.VISIBLE);
                }
                else if (vpnState.equals("CONNECTED")) {
                    btnDisconnect.setVisibility(View.VISIBLE);
                    progressIndicator.setVisibility(View.GONE);
                } else if (vpnState.equals("DISCONNECTED") || vpnState.equals("NONETWORK") || vpnState.equals("USERPAUSE")) {
                    btnDisconnect.setVisibility(View.GONE);
                    progressIndicator.setVisibility(View.GONE);
                }
            }
        //}
    }

    protected void updateTrafficStats(long outBytes, long inBytes) {
        String outString = LocalFormatter.easyRead(outBytes, false);
        String inString = LocalFormatter.easyRead(inBytes, false);

    }


    protected void hideConnectProgress() {
    }

    protected void showMessage(String msg) {
        Toast.makeText(ContentsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void rateUs() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    protected void timer() {
        if (adCount == 0) {
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            timeSwapBuff += timeInMilliseconds;

        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            customHandler.postDelayed(this, 0);
        }

    };


    private AlertDialog alertDialog = null;

    protected void disconnectAlert() {
        Timber.d("disconnectAlert");
        if (alertDialog != null && alertDialog.isShowing()) {
            Timber.d("disconnectAlert is showing");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to disconnect?");
        builder.setOnDismissListener(dialog -> {
                    Timber.d("disconnectAlert. dismiss");
                    alertDialog = null;
                }
        );
        builder.setPositiveButton("Disconnect",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        disconnectFromVpn();
                        vpn_toast_check = true;
                        STATUS = "DISCONNECTED";
                        textDownloading.setText("0 kB/s");
                        textUploading.setText("0 kB/s");
                        Toasty.success(ContentsActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                        btnDisconnect.setVisibility(View.GONE);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.success(ContentsActivity.this, "VPN Remains Connected", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialog = builder.show();
    }


    private void populateUnifiedNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }





    private void setBannerAdmobAds() {
        LinearLayout placeholder = findViewById(R.id.admob_adplaceholder);
        if (placeholder.getChildCount() > 0) return;

        AdView adView = new AdView(this);
        adView.setAdUnitId(MainActivity.admob_banner_id);
        placeholder.addView(adView);

        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(bannerAdRequest);
    }

    private com.google.android.gms.ads.AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @OnClick(R.id.purchase_layout)
    void goPurchase() {
        openUnlockActivity();
    }

    private void openUnlockActivity() {
        startActivity(new Intent(this, UnlockAllActivity.class));
    }

    void showServerList() {
        startActivityForResult(new Intent(this, Servers.class), 1001);
    }

    public void updateSubscription() {

        if (canShowAd()) {
            //native
            Log.d(TAG, "onStart----: ");
            try {
                if (MainActivity.type.equals("ad")) {
                    setBannerAdmobAds();
                    //refreshAd();
                } else {
                    AdSettings.addTestDevice("da8f9bce-fe47-4bdb-a7f0-9dbd3614b50a");
                    AdSettings.addTestDevice("95df4ed8-be34-43da-9998-248f14465352");
                    AudienceNetworkAds.initialize(this);

                    try {
                        if (MainActivity.type.equals("ad")) {
                            setBannerAdmobAds();
                            //refreshAd();
                        } else {
                            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
                            if (adContainer.getChildCount() == 0) {
                                AudienceNetworkAds.initialize(this);
                                facebookAdview = new com.facebook.ads.AdView(this, MainActivity.fbBannerWilldev_id, AdSize.BANNER_HEIGHT_50);
                                adContainer.addView(facebookAdview);

                                com.facebook.ads.AdListener adListener = new
                                        com.facebook.ads.AdListener() {
                                            public void onError(Ad ad, AdError adError) {
                                                Log.d("FACEBOOKAD", adError.getErrorMessage());
                                            }

                                            public void onAdLoaded(Ad ad) {

                                            }

                                            public void onAdClicked(Ad ad) {

                                            }

                                            public void onLoggingImpression(Ad ad) {

                                            }
                                        };

                                facebookAdview.loadAd(facebookAdview.buildLoadAdConfig().withAdListener(adListener).build());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initBtnConnect(Boolean connected) {

        if (connected) {

            tvState.setText(getString(R.string.disconnect));
            viewProgress.setVisibility(View.VISIBLE);

            if (animator != null) {
                animator.cancel();
            }

            viewProgress.setLayoutParams(((FrameLayout.LayoutParams) (viewProgress.getLayoutParams())));
            viewProgress.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;

        } else {
            tvState.setText(getString(R.string.connect));
            viewProgress.setVisibility(View.VISIBLE);
        }

    }

    private void fakeProgress(Long startDelay) {

        viewProgress.setVisibility(View.VISIBLE);

        FrameLayout.LayoutParams layoutParam = ((FrameLayout.LayoutParams) (viewProgress.getLayoutParams()));
        int originWidth = viewProgress.getWidth();

        animator = ValueAnimator.ofFloat(0F, 100F);
        animator.setDuration(5000L);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setStartDelay(startDelay);
        animator.addUpdateListener(valueAnimator -> {

            float value = (Float) valueAnimator.getAnimatedValue();
            float process = originWidth * value / 100;
            layoutParam.width = (int) process;
            tvState.setText(getString(R.string.connecting_, (int) value + "%"));
            viewProgress.setLayoutParams(layoutParam);

            if (viewProgress.getVisibility() == View.INVISIBLE) {
                viewProgress.setVisibility(View.VISIBLE);
            }

        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvState.setText(getString(R.string.waiting));
            }
        });

        if (animator != null) {
            animator.start();
        }

    }

    public void showInterstitialAndConnect() {

        if (canShowAd()) {

            if(MainActivity.type.equalsIgnoreCase("ad")) {
                InterstitialAd.load(ContentsActivity.this, MainActivity.admob_interstitial_id, adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                  mInterstitialAdMob = interstitialAd;
                                Log.i("INTERSTITIAL", "onAdLoaded");

                                if (mInterstitialAdMob != null) {

                                    mInterstitialAdMob.show(ContentsActivity.this);

                                    mInterstitialAdMob.setFullScreenContentCallback(new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                             Log.d("TAG", "The ad was dismissed.");
                                            Log.d("TESTAD", " dismissed update");
                                            connectToVpn();
                                        }

                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                             Log.d("TAG", "The ad failed to show.");
                                            connectToVpn();
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            mInterstitialAdMob = null;
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });

                                } else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                    connectToVpn();
                                }
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                Log.i("INTERSTITIAL", loadAdError.getMessage());
                                mInterstitialAdMob = null;
                                connectToVpn();
                            }
                        });
            } else {
                AudienceNetworkAds.initialize(ContentsActivity.this);
                //Advertizement listener
                com.facebook.ads.InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {

                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {

                        connectToVpn();

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.d("ADerror", adError.getErrorMessage());
                        connectToVpn();
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                };
                facebookInterstitialAd = new com.facebook.ads.InterstitialAd(ContentsActivity.this, MainActivity.fbInterstitialWilldev_id);
                facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
            }

        } else {
            connectToVpn();
        }
    }

    protected void showMessage(String msg, String type) {

        if(type == "success") {
            Toasty.success(
                    this,
                    msg + "",
                    Toast.LENGTH_SHORT
            ).show();
        } else if (type == "error") {
            Toasty.error(
                    this,
                    msg + "",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toasty.normal(
                    this,
                    msg + "",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    protected abstract void disconnectFromVpn();
    protected abstract void getIP();
}

