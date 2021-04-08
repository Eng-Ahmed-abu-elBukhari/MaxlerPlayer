package com.maxler.roaa.data.repository.songs

import android.database.Cursor
import com.maxler.roaa.data.model.Song

interface ISongRepository {

     suspend fun songs(): List<Song>

     suspend fun songs(cursor: Cursor?): List<Song>

     suspend fun songs(query: String): List<Song>

    //fun songsByFilePath(filePath: String): List<Song>

     suspend fun song(cursor: Cursor?): Song

    suspend fun song(songId: Long): Song
}