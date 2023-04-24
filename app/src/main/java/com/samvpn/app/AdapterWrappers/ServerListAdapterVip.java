package com.samvpn.app.AdapterWrappers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samvpn.app.R;
import com.samvpn.app.Activities.MainActivity;
import com.samvpn.app.model.Countries;

import java.util.ArrayList;
import java.util.List;

public class ServerListAdapterVip extends RecyclerView.Adapter<ServerListAdapterVip.mViewhoder> {

    ArrayList<Countries> datalist;
    private Context context;
    public ServerListAdapterVip(Context ctx) {
        this.context=ctx;
    }

    @NonNull
    @Override
    public mViewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.pro_list_willdev,parent,false);
        mViewhoder mvh=new mViewhoder(item);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewhoder holder, int position)
    {
        Countries data = datalist.get(position);
        holder.app_name.setText(data.getCountry());

        Glide.with(context)
                .load(data.getFlagUrl())
                .into(holder.flag);

        holder.limit.setText("VIP");
        holder.limit.setTextColor(context.getResources().getColor(R.color.primary));

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

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public static class mViewhoder extends RecyclerView.ViewHolder
    {
        TextView app_name,limit;
        ImageView flag;

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
