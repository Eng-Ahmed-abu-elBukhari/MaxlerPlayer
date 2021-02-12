package com.maxler.roaa.data.repository.songs

import android.database.Cursor
import com.maxler.roaa.data.model.Song

class SongRepository {

    private var songs = mutableListOf<Song>()







    private fun getSongFromCursorImpl(cursor: Cursor):MutableList<Song>{

        cursor.let {

        }
    }


    private fun makeSongsCursor(
        selection:String,
        selectionArgs:Array<String>,
        sortOrder:String
    ):Cursor{

    }


}

