package com.samvpn.app

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class MainViewPagerServersItem(context: Context) : ConstraintLayout(context) {

    lateinit var actionBtn: TextView
    lateinit var buySubscriptionBtn: TextView
    lateinit var watchAdBtn: TextView
    lateinit var containerForWatchAdAndBuySubsBtn: LinearLayout
    lateinit var premiumIcon: ImageView
    lateinit var flagIcon: ImageView
    lateinit var flagTitle: TextView

    var STATE = "DISCONNECTED"
        private set

    companion object {
        const val STATE_NOT_CONNECTED = 1
        const val STATE_CONNECTING = 2
        const val STATE_CONNECTED = 3
        fun getStateString(state : Int) = when(state) {
            STATE_NOT_CONNECTED -> "STATE_NOT_CONNECTED"
            STATE_CONNECTING -> "STATE_CONNECTING"
            STATE_CONNECTED -> "STATE_CONNECTED"
            else -> "-1"
        }
    }

    init {
        id = View.generateViewId()
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val padding = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_padding_side)
        setPadding(padding, 0, padding, 0)
        addViews()
    }

    private fun addViews() {
        ConstraintSet().apply {
            val backgroundWhiteCard = ImageView(context).apply {
                id = View.generateViewId()
                background = GradientDrawable().also {
                    val radius = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_card_bg_radius).toFloat()
                    it.cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
                    it.setColor(ContextCompat.getColor(context, android.R.color.white))
                }
            }
            constrainWidth(backgroundWhiteCard.id, MATCH_CONSTRAINT)
            constrainHeight(backgroundWhiteCard.id, MATCH_CONSTRAINT)
            constrainPercentHeight(backgroundWhiteCard.id, 0.6f)
            setVerticalBias(backgroundWhiteCard.id, 1f)
            connect(backgroundWhiteCard.id, START, PARENT_ID, START)
            connect(backgroundWhiteCard.id, END, PARENT_ID, END)
            connect(backgroundWhiteCard.id, BOTTOM, PARENT_ID, BOTTOM)
            connect(backgroundWhiteCard.id, TOP, PARENT_ID, TOP)


            val guideline = Guideline(context).apply {
                id = View.generateViewId()
            }
            create(guideline.id, HORIZONTAL_GUIDELINE)
            setGuidelineEnd(guideline.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_guideline_margin_bottom))

            val buttonsHeight = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_buttons_height)

            actionBtn = TextView(context).apply {
                id = View.generateViewId()
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 15f
                gravity = Gravity.CENTER
                typeface = ResourcesCompat.getFont(context, R.font.campton_medium)
                elevation = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_tap_to_connect_btn_elevation).toFloat()
            }
            constrainWidth(actionBtn.id, MATCH_CONSTRAINT)
            constrainHeight(actionBtn.id, buttonsHeight)
            connect(actionBtn.id, START, PARENT_ID, START)
            connect(actionBtn.id, END, PARENT_ID, END)
            connect(actionBtn.id, BOTTOM, PARENT_ID, BOTTOM)
            connect(actionBtn.id, TOP, guideline.id, BOTTOM)
            val marginSide = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_tap_to_connect_btn_margin_side)
            setMargin(actionBtn.id, START, marginSide)
            setMargin(actionBtn.id, END, marginSide)
            setMargin(actionBtn.id, BOTTOM, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_tap_to_connect_btn_margin_bottom))


            val foregroundWhiteCard = ImageView(context).apply {
                id = View.generateViewId()
                background = GradientDrawable().also {
                    val radius = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_card_bg_radius).toFloat()
                    it.cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
                    it.setColor(ContextCompat.getColor(context, android.R.color.white))
                }
                elevation = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_elevation).toFloat()
            }
            constrainWidth(foregroundWhiteCard.id, MATCH_CONSTRAINT)
            constrainHeight(foregroundWhiteCard.id, MATCH_CONSTRAINT)
            constrainMinHeight(foregroundWhiteCard.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_min_height))
            connect(foregroundWhiteCard.id, START, backgroundWhiteCard.id, START)
            connect(foregroundWhiteCard.id, END, backgroundWhiteCard.id, END)
            connect(foregroundWhiteCard.id, TOP, PARENT_ID, TOP)
            connect(foregroundWhiteCard.id, BOTTOM, guideline.id, TOP)
            val foregroundWhiteCardMarginSide = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_margin_side)
            setMargin(foregroundWhiteCard.id, START, foregroundWhiteCardMarginSide)
            setMargin(foregroundWhiteCard.id, END, foregroundWhiteCardMarginSide)
            setMargin(foregroundWhiteCard.id, BOTTOM, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_margin_bottom))


            buySubscriptionBtn = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(0, buttonsHeight).apply {
                    weight = 1f
                    setMargins(0, 0, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_container_for_btns_margin), 0)
                }
                setBackgroundColor(ContextCompat.getColor(context, R.color.main_view_pager_item_buy_subscription_btn_bg))
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                textSize = 14f
                gravity = Gravity.CENTER
                typeface = ResourcesCompat.getFont(context, R.font.campton_medium)
                text = resources.getString(R.string.view_pager_servers_buy_subscription)
                elevation = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_tap_to_connect_btn_elevation).toFloat()
            }

            watchAdBtn = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(0, buttonsHeight).apply { weight = 1f }
                setBackgroundColor(ContextCompat.getColor(context, R.color.main_view_pager_item_watch_ad_btn_or_tap_to_connect_btn_bg))
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                typeface = ResourcesCompat.getFont(context, R.font.campton_medium)
                textSize = 14f
                gravity = Gravity.CENTER
                text = resources.getString(R.string.view_pager_servers_watch_ad)
                elevation = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_tap_to_connect_btn_elevation).toFloat()
            }

            containerForWatchAdAndBuySubsBtn = LinearLayout(context).apply {
                id = View.generateViewId()
                orientation = LinearLayout.HORIZONTAL
                addView(buySubscriptionBtn)
                addView(watchAdBtn)
            }
            constrainWidth(containerForWatchAdAndBuySubsBtn.id, MATCH_CONSTRAINT)
            constrainHeight(containerForWatchAdAndBuySubsBtn.id, WRAP_CONTENT)
            connect(containerForWatchAdAndBuySubsBtn.id, START, PARENT_ID, START)
            connect(containerForWatchAdAndBuySubsBtn.id, END, PARENT_ID, END)
            connect(containerForWatchAdAndBuySubsBtn.id, TOP, guideline.id, BOTTOM)
            connect(containerForWatchAdAndBuySubsBtn.id, BOTTOM, PARENT_ID, BOTTOM)
            val marginContainer = resources.getDimensionPixelSize(R.dimen.main_view_pager_item_container_for_btns_margin)
            setMargin(containerForWatchAdAndBuySubsBtn.id, END, marginContainer)
            setMargin(containerForWatchAdAndBuySubsBtn.id, START, marginContainer)
            setVisibility(actionBtn.id, ConstraintSet.GONE)


            flagIcon = ImageView(context).apply {
                id = View.generateViewId()
            }
            constrainWidth(flagIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_flag_width))
            constrainHeight(flagIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_flag_height))
            connect(flagIcon.id, START, foregroundWhiteCard.id, START)
            connect(flagIcon.id, END, foregroundWhiteCard.id, END)
            connect(flagIcon.id, TOP, foregroundWhiteCard.id, TOP)
            connect(flagIcon.id, BOTTOM, foregroundWhiteCard.id, BOTTOM)
            setTranslationZ(flagIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_elevation).toFloat())

            flagTitle = TextView(context).apply {
                id = View.generateViewId()
                setTextColor(ContextCompat.getColor(context, R.color.main_view_pager_item_flag_title))
                textSize = 15f
                typeface = ResourcesCompat.getFont(context, R.font.campton_bold)
            }
            constrainWidth(flagTitle.id, WRAP_CONTENT)
            constrainHeight(flagTitle.id, WRAP_CONTENT)
            connect(flagTitle.id, START, flagIcon.id, START)
            connect(flagTitle.id, END, flagIcon.id, END)
            connect(flagTitle.id, TOP, flagIcon.id, BOTTOM)
            setMargin(flagTitle.id, TOP, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_flag_name_margin_top))
            setTranslationZ(flagTitle.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_elevation).toFloat())

            premiumIcon = ImageView(context).apply {
                id = View.generateViewId()
            }
            constrainWidth(premiumIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_premium_icon_size))
            constrainHeight(premiumIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_premium_icon_size))
            connect(premiumIcon.id, TOP, foregroundWhiteCard.id, TOP)
            connect(premiumIcon.id, END, foregroundWhiteCard.id, END)
            setMargin(premiumIcon.id, TOP, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_premium_icon_margin_top))
            setMargin(premiumIcon.id, END, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_premium_icon_margin_end))
            setTranslationZ(premiumIcon.id, resources.getDimensionPixelSize(R.dimen.main_view_pager_item_foreground_card_elevation).toFloat())


            addView(backgroundWhiteCard)
            addView(guideline)
            addView(foregroundWhiteCard)
            addView(flagIcon)
            addView(flagTitle)
            addView(premiumIcon)
            addView(actionBtn)
            addView(containerForWatchAdAndBuySubsBtn)

        }.applyTo(this)
    }

    fun setButtonsToDefault() {
        ConstraintSet().apply {
            clone(this@MainViewPagerServersItem)

            setVisibility(actionBtn.id, ConstraintSet.VISIBLE)
            setVisibility(containerForWatchAdAndBuySubsBtn.id, ConstraintSet.GONE)

        }.applyTo(this)
    }

    fun setState(newState :String){

        STATE = newState
        when(STATE){
            "DISCONNECTED"->{
                actionBtn.apply {
                    text = resources.getString(R.string.view_pager_servers_tap_to_connect_not_connected)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_not_connected))
                }
            }
            "NONETWORK" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_nonetwork)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_not_connected))
                }
            }
            "USERPAUSE" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_paused)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_not_connected))
                }
            }
            "WAIT" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_wait)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "RESOLVE" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_wait)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "AUTH" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_auth)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "VPN_GENERATE_CONFIG",
            "GET_CONFIG" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_config)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "ASSIGN_IP" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_assign_ip)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "RECONNECTING" ->{
                actionBtn.apply {
                    text = resources.getString(R.string.state_reconnecting)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.view_pager_servers_tap_to_connect_connecting))
                }
            }
            "CONNECTED" -> {
                actionBtn.apply {
                    text = resources.getString(R.string.view_pager_servers_tap_to_connect_connected)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.splashscreencolor))
                }
            }

        }
    }

    fun onTapToConnectVip() {
        Log.d(javaClass.simpleName, "onTapToConnectVip")
        ConstraintSet().apply {
            clone(this@MainViewPagerServersItem)

            setVisibility(actionBtn.id, ConstraintSet.GONE)
            setVisibility(containerForWatchAdAndBuySubsBtn.id, ConstraintSet.VISIBLE)

        }.applyTo(this)
    }
}