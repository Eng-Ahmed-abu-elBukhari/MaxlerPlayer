package com.maxler.roaa.data.utils

import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.maxler.roaa.data.helper.App
import com.maxler.roaa.data.helper.SortOrder
import com.maxler.roaa.data.utils.Constants.SONG_SORT_ORDER


object PreferenceUtil {


    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext())




    var songSortOrder
        get() = sharedPreferences.getString(
            SONG_SORT_ORDER,
            SortOrder.SongSortOrder.SORT_ORDER_A_Z
        )
        set(value) = sharedPreferences.edit {
            putString(SONG_SORT_ORDER, value)
        }


    
}