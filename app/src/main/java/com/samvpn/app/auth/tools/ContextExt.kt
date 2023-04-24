package com.samvpn.app.auth.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import kotlin.math.roundToInt

fun Context?.sp2Px(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        this?.resources?.displayMetrics
    )
}

fun Context?.dp2Px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this?.resources?.displayMetrics
    ).roundToInt()
}

fun Activity?.updateColorStatusBar(@ColorRes colorRes: Int, flagLight: Boolean = false) {
    this ?: return
    val color = ContextCompat.getColor(this, colorRes)
    this.window?.statusBarColor = color

    val view = this.window.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (flagLight) {
            view.systemUiVisibility =
                view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            view.systemUiVisibility =
                view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

fun Context?.showKeyboard(editText: EditText) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity?.hideSoftKeyboard() {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this?.currentFocus?.windowToken, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun Activity?.hideKeyboardWhenTouchOutside(view: View?, callback: (() -> Unit)? = null) {
    if (view !is EditText) {
        view?.setOnTouchListener { _, _ ->
            this?.hideSoftKeyboard()
            callback?.invoke()
            return@setOnTouchListener false
        }
    }

    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            hideKeyboardWhenTouchOutside(innerView)
        }
    }
}

fun Context?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context?.readAsset(fileName: String) = this
    ?.assets
    ?.open(fileName)
    ?.bufferedReader()
    ?.use(BufferedReader::readText)

