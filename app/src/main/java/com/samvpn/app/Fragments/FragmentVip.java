package com.samvpn.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samvpn.app.R;
import com.samvpn.app.Activities.UnlockAllActivity;
import com.samvpn.app.AdapterWrappers.ServerListAdapterVip;
import com.samvpn.app.Config;
import com.samvpn.app.Utils.Constants;
import com.samvpn.app.model.Countries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentVip extends Fragment {

    private RecyclerView recyclerView;
    private ServerListAdapterVip adapter;
    private RelativeLayout animationHolder;
    private RelativeLayout mPurchaseLayout;
    private ImageButton mUnblockButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.servers_app_main_willdev, container, false);
        recyclerView = view.findViewById(R.id.region_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        animationHolder = view.findViewById(R.id.animation_layout);

        mPurchaseLayout = view.findViewById(R.id.purchase_layout);
        mUnblockButton = view.findViewById(R.id.vip_unblock);

        mUnblockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UnlockAllActivity.class));
            }
        });

        if (Config.vip_subscription || Config.all_subscription) {
            mPurchaseLayout.setVisibility(View.GONE);
        }

        adapter = new ServerListAdapterVip(getActivity());
        recyclerView.setAdapter(adapter);
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
            JSONArray jsonArray = new JSONArray(Constants.PREMIUM_SERVERS);

            for (int i=0; i < jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                servers.add(new Countries(object.getString("serverName"),
                        object.getString("flag_url"),
                        object.getString("ovpnConfiguration"),
                        object.getString("vpnUserName"),
                        object.getString("vpnPassword"),
                        false
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.setData(servers);
        animationHolder.setVisibility(View.GONE);
    }
}