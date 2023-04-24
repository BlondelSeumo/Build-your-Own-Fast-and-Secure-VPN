package com.samvpn.app

import android.content.res.Resources

object UtilsScreen {

    fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()
    fun dpToPx(dp: Float) = (dp * Resources.getSystem().displayMetrics.density).toInt()


}