package com.samvpn.app.auth.tools

import android.annotation.SuppressLint
import com.samvpn.app.R

@SuppressLint("HardwareIds")
object Util {


    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }



    fun getResId(input: String?): Int? {
        if (input == null) return null

        var result = input.toResId()
        if (result == -1) {
            result = "ic_$input".toResId()
        }

        if (result == -1) {
            result = "ic_${input.subSequence(0, 1)}".toResId()
        }
        return result
    }

}

fun String.toResId(): Int {
    return try {
        val clazz = R.drawable::class.java
        val field = clazz.getDeclaredField(this)
        field.getInt(field)
    } catch (exception: Exception) {
        -1
    }
}