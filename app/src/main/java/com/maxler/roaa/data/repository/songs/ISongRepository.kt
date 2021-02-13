package com.maxler.roaa.data.repository.songs

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.maxler.roaa.data.model.Song

interface ISongRepository {

    fun songs(): LiveData<List<Song>>

    fun songs(cursor: Cursor?): LiveData<List<Song>>

    fun songs(query: String): LiveData<List<Song>>

    fun songsByFilePath(filePath: String): LiveData<List<Song>>

    fun song(cursor: Cursor?): Song

    fun song(songId: Long): Song
}