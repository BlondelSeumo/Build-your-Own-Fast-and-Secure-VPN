package com.samvpn.app.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.samvpn.app.Config;
import com.samvpn.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnlockAllFragment extends Fragment implements PurchasesUpdatedListener, BillingClientStateListener {

    private final Activity activity;
    private BillingClient billingClient;
    private final Map<String, SkuDetails> skusWithSkuDetails = new HashMap<>();
    private final List<String> allSubs = new ArrayList<>(Arrays.asList(
            Config.all_month_id,
            Config.all_threemonths_id,
            Config.all_sixmonths_id,
            Config.all_yearly_id));

    public UnlockAllFragment(Activity activity) {
        this.activity = activity;
    }

    private MutableLiveData<Integer> all_check = new MutableLiveData<>();
    @BindView(R.id.one_month)
    CheckBox oneMonth;
    @BindView(R.id.three_month)
    CheckBox threeMonth;
    @BindView(R.id.six_month)
    CheckBox sixMonth;
    @BindView(R.id.one_year)
    CheckBox oneYear;

    @BindView(R.id.one_monthCV)
    CardView one_monthCV;
    @BindView(R.id.three_monthCV)
    CardView three_monthCV;
    @BindView(R.id.six_monthCV)
    CardView six_monthCV;
    @BindView(R.id.one_yearCV)
    CardView one_yearCV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unlock_all_willdev, container, false);

        ButterKnife.bind(this, view);

        billingClient = BillingClient
                .newBuilder(activity)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        connectToBillingService();

        all_check.setValue( -1);
        all_check.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 0:
                        setBackground(0);
                        threeMonth.setChecked(false);
                        sixMonth.setChecked(false);
                        oneYear.setChecked(false);
                        break;
                    case 1:
                        setBackground(1);
                        oneMonth.setChecked(false);
                        sixMonth.setChecked(false);
                        oneYear.setChecked(false);
                        break;
                    case 2:
                        setBackground(2);
                        threeMonth.setChecked(false);
                        oneMonth.setChecked(false);
                        oneYear.setChecked(false);
                        break;
                    case 3:
                        setBackground(3);
                        threeMonth.setChecked(false);
                        sixMonth.setChecked(false);
                        oneMonth.setChecked(false);
                        break;

                }
            }
        });

        oneMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) all_check.postValue(0);
            }
        });
        threeMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) all_check.postValue(1);
            }
        });
        sixMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) all_check.postValue(2);
            }
        });
        oneYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) all_check.postValue(3);
            }
        });


        one_monthCV.setOnClickListener(view1 -> {

            oneMonth.setChecked(true);
            threeMonth.setChecked(false);
            sixMonth.setChecked(false);
            oneYear.setChecked(false);

        });

        three_monthCV.setOnClickListener(view1 -> {

            oneMonth.setChecked(false);
            threeMonth.setChecked(true);
            sixMonth.setChecked(false);
            oneYear.setChecked(false);

        });

        six_monthCV.setOnClickListener(view1 -> {

            oneMonth.setChecked(false);
            threeMonth.setChecked(false);
            sixMonth.setChecked(true);
            oneYear.setChecked(false);

        });

        one_yearCV.setOnClickListener(view1 -> {

            oneMonth.setChecked(false);
            threeMonth.setChecked(false);
            sixMonth.setChecked(false);
            oneYear.setChecked(true);

        });

        return view;
    }

    private void setBackground(int a){

        switch (a){

            case 0:
                one_monthCV.setCardBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                three_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                six_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                one_yearCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                break;
            case 1:
                one_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                three_monthCV.setCardBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                six_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                one_yearCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                break;
            case 2:
                one_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                three_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                six_monthCV.setCardBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                one_yearCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                break;
            case 3:
                one_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                three_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                six_monthCV.setCardBackgroundColor(getResources().getColor(R.color.black));
                one_yearCV.setCardBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                break;

        }

    }

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
                    new ArrayList<>(allSubs)
            );
        }
    }

    @Override
    public void onBillingServiceDisconnected() {
        connectToBillingService();
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

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

    private void purchase(SkuDetails skuDetails) {
        BillingFlowParams params = BillingFlowParams
                .newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        billingClient.launchBillingFlow(activity, params);
    }

    @OnClick(R.id.all_pur)
    void unlockAll(){
        if (all_check.getValue() != null) {
            SkuDetails skuDetails = null;

            switch (all_check.getValue()) {
                case 0:
                    skuDetails = skusWithSkuDetails.get(Config.all_month_id);
                    break;
                case 1:
                    skuDetails = skusWithSkuDetails.get(Config.all_threemonths_id);
                    break;
                case 2:
                    skuDetails = skusWithSkuDetails.get(Config.all_sixmonths_id);
                    break;
                case 3:
                    skuDetails = skusWithSkuDetails.get(Config.all_yearly_id);
                    break;
            }

            if (skuDetails != null) purchase(skuDetails);
        }
    }
}