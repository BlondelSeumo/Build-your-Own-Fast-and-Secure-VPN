package com.samvpn.app.auth.tools

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class MyClickSpan: ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
    }
}