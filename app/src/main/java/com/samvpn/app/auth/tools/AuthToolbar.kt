package com.samvpn.app.auth.tools

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.samvpn.app.R

class AuthToolbar(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val itemView = View.inflate(context, R.layout.toolbar_main_willdev, this)

    init {
        initAttrs(context, attrs)
        initView()
    }

    var onBtnLeftClicked: (() -> Boolean)? = null
    var onBtnRightClicked: (() -> Unit)? = null

    var leftIcon: Drawable? = null
        set(value) {
            field = value
            field?.let {
                itemView.findViewById<ImageButton>(R.id.btnLeft).setImageDrawable(leftIcon)
                itemView.findViewById<ImageButton>(R.id.btnLeft).visibility = View.VISIBLE
            } ?: run {
                itemView.findViewById<ImageButton>(R.id.btnLeft).visibility = View.INVISIBLE
            }
        }

    var title: String? = null
        set(value) {
            field = value
            field?.let {
                itemView.findViewById<TextView>(R.id.tvTitle).text = it
                itemView.findViewById<TextView>(R.id.tvTitle).visibility = View.VISIBLE
            } ?: run {
                itemView.findViewById<TextView>(R.id.tvTitle).visibility = View.INVISIBLE
            }
        }

    var rightText: String? = null
        set(value) {
            field = value
            field?.let {
                itemView.findViewById<TextView>(R.id.btnRight).text = it
                itemView.findViewById<TextView>(R.id.btnRight).visibility = View.VISIBLE
            } ?: run {
                itemView.findViewById<TextView>(R.id.btnRight).visibility = View.INVISIBLE
            }
        }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.obtainStyledAttributes(attrs, R.styleable.AuthToolbar)
            ?.apply {
                leftIcon = this.getDrawable(R.styleable.AuthToolbar_atb_left)
                title = this.getString(R.styleable.AuthToolbar_atb_title)
                rightText = this.getString(R.styleable.AuthToolbar_atb_right)
            }
            ?.recycle()
    }

    private fun initView() {
        itemView.findViewById<ImageButton>(R.id.btnLeft).setOnClickListener(this::handleBtnLeftClicked)
        itemView.findViewById<TextView>(R.id.btnRight).setOnClickListener(this::handleBtnRightClicked)
    }

    private fun handleBtnLeftClicked(view: View) {
        val isUse = onBtnLeftClicked?.invoke()
        if (isUse == true) {
            return
        }
        (context as? Activity)?.finish()
    }

    private fun handleBtnRightClicked(view: View) {
        onBtnRightClicked?.invoke()
    }

}