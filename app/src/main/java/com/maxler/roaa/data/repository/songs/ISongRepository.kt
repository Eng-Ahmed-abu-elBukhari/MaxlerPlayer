package com.maxler.roaa.data.repository.songs

import android.database.Cursor
import com.maxler.roaa.data.model.Song

interface ISongRepository {

    fun songs(): MutableList<Song>

    fun songs(cursor: Cursor?): MutableList<Song>

    fun songs(query: String): MutableList<Song>

    fun songsByFilePath(filePath: String): List<Song>

    fun song(cursor: Cursor?): Song

    fun song(songId: Long): Song
}