package com.samvpn.app.AdapterWrappers

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samvpn.app.Activities.MainActivity
import com.samvpn.app.Config
import com.samvpn.app.MainViewPagerServersItem
import com.samvpn.app.OnTapToConnectVariantClickListener
import com.samvpn.app.model.Countries

class ViewPagerServersAdapter(val servers: List<Countries>, private val premiumDrawable: Drawable, var listener: OnTapToConnectVariantClickListener, var context: Context) : RecyclerView.Adapter<ViewPagerServersAdapter.ViewPagerViewHolder>() {

    private var currentVpnState:String? = "DISCONNECTED"
    private var currentIndexOfConnectedServer = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(MainViewPagerServersItem(parent.context))
    }

    override fun getItemCount(): Int = servers.size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val country = servers[position]

        holder.view.apply {

            Glide.with(context)
                .load(country.flagUrl)
                .into(flagIcon)

            flagTitle.text = country.country
            premiumIcon.setImageDrawable(if (country.isFree) null else premiumDrawable)
            setButtonsToDefault()

            if (currentIndexOfConnectedServer == position && currentVpnState != null)
                setState(currentVpnState!!)
            else
                setState("DISCONNECTED")

            actionBtn.setOnClickListener {
                val hasVip = !(!Config.ads_subscription && !Config.all_subscription && !Config.vip_subscription)
                if (country.isFree || hasVip || currentIndexOfConnectedServer == position || !MainActivity.ad_switchWilldev)
                    listener.onDirectConnect(country, position)
                else
                    onTapToConnectVip()
            }

            watchAdBtn.setOnClickListener { listener.onWatchAdClick(country, position) }
            buySubscriptionBtn.setOnClickListener { listener.onBuySubscriptionClick() }
        }
    }

    fun updateItemVpnState(vpnState: String, indexOfConnectedServer: Int) {
        currentVpnState = vpnState
        currentIndexOfConnectedServer = indexOfConnectedServer
        if (indexOfConnectedServer != -1)
            notifyItemChanged(indexOfConnectedServer)
    }

    fun isAlreadyHandleVPNState(vpnState: String, indexOfConnectedServer: Int) = vpnState == currentVpnState && indexOfConnectedServer == currentIndexOfConnectedServer


    class ViewPagerViewHolder(itemView: MainViewPagerServersItem) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
    }
}