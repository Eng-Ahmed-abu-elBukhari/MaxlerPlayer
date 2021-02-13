package com.maxler.roaa.data.utils

import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi

object Constants {


    const val SONG_SORT_ORDER = "song_sort_order"

    @RequiresApi(Build.VERSION_CODES.Q)
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.TRACK,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_TAKEN,
        MediaStore.Audio.Media.DATE_MODIFIED,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.COMPOSER,
        MediaStore.Audio.Media.ALBUM_ARTIST
    )


    const val IS_MUSIC = "${MediaStore.Audio.AudioColumns.IS_MUSIC} =1 AND ${MediaStore.Audio.AudioColumns.TITLE} !=''"


}

const val FILTER_SONG = "filter_song"
