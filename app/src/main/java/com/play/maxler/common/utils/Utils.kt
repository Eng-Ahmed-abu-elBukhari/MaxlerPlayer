// Copyright (c) 2019 . Wilberforce Uwadiegwu. All Rights Reserved.

package com.play.maxler.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference



object Utils {
    /**
     * Removes the the first three character oif [MediaStore.Audio.Media.TRACK]
     */
    fun getTrackNumber(number: Int): String {
        val num = number.toString()
        return if (num.length >= 4) num.drop(3) else num
    }

    fun vibrateAfterAction(c: Context?) {
        val context = WeakReference(c).get()
        if (context != null) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(40)
                }
            }
        }
    }

    fun isPermissionGranted( permission : Array<String>, context: Context?): Boolean {
        val mContext = WeakReference(context).get()
        return  mContext?.let {
            ContextCompat.checkSelfPermission(it, permission[0])
        } == PackageManager.PERMISSION_GRANTED
                &&
                mContext?.let {
            ContextCompat.checkSelfPermission(it, permission[0])
        } == PackageManager.PERMISSION_GRANTED
    }

    val artworkUri: Uri = Uri.parse("content://media/external/audio/albumart")


}