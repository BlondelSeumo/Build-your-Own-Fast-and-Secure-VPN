package com.samvpn.app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.samvpn.app.Activities.MainActivity;
import com.samvpn.app.R;
import com.samvpn.app.AdapterWrappers.ServerListAdapterFree;
import com.samvpn.app.Config;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.samvpn.app.Utils.Constants;
import com.samvpn.app.model.Countries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

public class FragmentFree extends Fragment {
    private RecyclerView recyclerView;
    private ServerListAdapterFree adapter;
    int server;
    InterstitialAd mInterstitialAdMob;
    public com.facebook.ads.InterstitialAd facebookInterstitialAd;

    boolean isAds;
    private RelativeLayout animationHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servers_activity_willdev, container, false);
        recyclerView = view.findViewById(R.id.region_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        animationHolder = view.findViewById(R.id.animation_layout);

        adapter = new ServerListAdapterFree(getActivity());
        recyclerView.setAdapter(adapter);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }
                loadAdmobInterstitial();
            }
        });

        if (getResources().getBoolean(R.bool.ads_switch) && getResources().getBoolean(R.bool.facebook_list_ads) && (!Config.ads_subscription && !Config.all_subscription&& !Config.vip_subscription)) {

            isAds = true;
        } else if (getResources().getBoolean(R.bool.ads_switch) && getResources().getBoolean(R.bool.admob_list_ads) && (!Config.ads_subscription && !Config.all_subscription && !Config.vip_subscription)) {

            isAds = true;
        } else {
            isAds = false;

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadServers();
    }

    private void loadServers() {
        ArrayList<Countries> servers = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(Constants.FREE_SERVERS);
            for (int i=0; i < jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                servers.add(new Countries(object.getString("serverName"),
                        object.getString("flag_url"),
                        object.getString("ovpnConfiguration"),
                        object.getString("vpnUserName"),
                        object.getString("vpnPassword"),
                        true
                ));

                if((i % 2 == 0)&&(i > 0)){
                    if (!Config.vip_subscription && !Config.all_subscription) {
                        servers.add(null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        animationHolder.setVisibility(View.GONE);
        adapter.setData(servers);
    }

    private void loadAdmobInterstitial() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getActivity(), MainActivity.admob_interstitial_id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                         mInterstitialAdMob = interstitialAd;
                        Log.i("INTERSTITIAL", "onAdLoaded");

                        if (mInterstitialAdMob != null) {

                            mInterstitialAdMob.setFullScreenContentCallback(new FullScreenContentCallback(){
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                       Log.d("TAG", "The ad was dismissed.");
                                }

                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                     Log.d("TAG", "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    mInterstitialAdMob = null;
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });

                        } else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        Log.i("INTERSTITIAL", loadAdError.getMessage());
                        mInterstitialAdMob = null;

                    }
                });
    }
}
