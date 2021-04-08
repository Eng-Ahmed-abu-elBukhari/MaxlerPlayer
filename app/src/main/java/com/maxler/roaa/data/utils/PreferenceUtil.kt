package com.maxler.roaa.data.utils

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.maxler.roaa.data.helper.SortOrder
import com.maxler.roaa.data.utils.Constants.SONG_SORT_ORDER


class PreferenceUtil constructor(context: Context){



      private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


        var songSortOrder
            get() =  sharedPreferences.getString(
                Constants.SONG_SORT_ORDER,
                SortOrder.SongSortOrder.SORT_ORDER_A_Z
            )
            set(value) = sharedPreferences.edit {
                this.putString(SONG_SORT_ORDER, value)
            }



}