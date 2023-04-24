package com.samvpn.app.AdapterWrappers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.samvpn.app.R;
import com.samvpn.app.Activities.MainActivity;
import com.samvpn.app.model.Countries;

import java.util.ArrayList;
import java.util.List;

public class ServerListAdapterFree extends RecyclerView.Adapter<ServerListAdapterFree.mViewhoder> {

    ArrayList<Countries> datalist = new ArrayList<>();
    com.facebook.ads.AdView facebookAdview;

    private Context context;
    private int AD_TYPE = 0;
    private int CONTENT_TYPE = 1;
    public ServerListAdapterFree( Context ctx) {
        this.context=ctx;
    }

    @NonNull
    @Override
    public mViewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        AdView adview;

        if (viewType == AD_TYPE) {
            if(MainActivity.type.equals("ad")){
            adview = new AdView(context);
            adview.setAdSize(AdSize.BANNER);
            adview.setAdUnitId(MainActivity.admob_banner_id);
            float density = context.getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            adview.setLayoutParams(params);
            AdRequest request = new AdRequest.Builder().build();
            adview.loadAd(request);
                return new mViewhoder(adview);

            }else{
                LinearLayout linearLayout =new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(params);
                facebookAdview = new com.facebook.ads.AdView(context,MainActivity.fbBannerWilldev_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                linearLayout.addView(facebookAdview);
                facebookAdview.loadAd();
                return new mViewhoder(linearLayout);
            }
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.free_list_willdev, parent, false);
            return new mViewhoder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewhoder holder, int position) {
        if(getItemViewType(position) == CONTENT_TYPE){

            Countries data = datalist.get(position);
            holder.app_name.setText(data.getCountry());

            Glide.with(context)
                    .load(data.getFlagUrl())
                    .into(holder.flag);

            holder.limit.setImageResource(R.drawable.server_signal_3);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("c",data);
                    intent.putExtra("type",MainActivity.type);
                    intent.putExtra("admob_banner",MainActivity.admob_banner_id);
                    intent.putExtra("admob_interstitial",MainActivity.admob_interstitial_id);
                    intent.putExtra("fbBannerWilldev",MainActivity.fbBannerWilldev_id);
                    intent.putExtra("fbInterstitialWilldev",MainActivity.fbInterstitialWilldev_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    @Override
    public int getItemViewType(int position) {
        return datalist.get(position) ==null? AD_TYPE:CONTENT_TYPE;
    }

    public static class mViewhoder extends RecyclerView.ViewHolder
    {
        TextView app_name;
        ImageView flag,limit;

        public mViewhoder(View itemView) {
            super(itemView);
            app_name=itemView.findViewById(R.id.region_title);
             limit=itemView.findViewById(R.id.region_limit);
             flag=itemView.findViewById(R.id.country_flag);
        }
    }

    public void setData(List<Countries> servers) {
        datalist.clear();
        datalist.addAll(servers);
        notifyDataSetChanged();
    }
}
