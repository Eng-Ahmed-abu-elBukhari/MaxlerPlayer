package com.maxler.roaa.data.repository.songs

import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.maxler.roaa.data.model.Song
import com.maxler.roaa.data.utils.Constants.IS_MUSIC

import com.maxler.roaa.data.utils.PreferenceUtil
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData

class SongRepository :ISongRepository{

    private var songs = mutableListOf<Song>()







    private fun getSongFromCursorImpl(cursor: Cursor):Song{

            val id = cursor.getLong(cursor.getColumnIndex(AudioColumns._ID))
            val title = cursor.getString(cursor.getColumnIndex(AudioColumns.TITLE))
            val trackNumber = cursor.getInt(cursor.getColumnIndex(AudioColumns.TRACK))
            val year = cursor.getInt(cursor.getColumnIndex(AudioColumns.YEAR))
            val duration = cursor.getLong(cursor.getColumnIndex(AudioColumns.DURATION))
            val data = cursor.getString(cursor.getColumnIndex(AudioColumns.DATE_TAKEN))
            val dateModified = cursor.getLong(cursor.getColumnIndex(AudioColumns.DATE_MODIFIED))
            val albumId = cursor.getLong(cursor.getColumnIndex(AudioColumns.ALBUM_ID))
            val albumName = cursor.getStringOrNull(cursor.getColumnIndex(AudioColumns.ALBUM))
            val artistId = cursor.getLong(cursor.getColumnIndex(AudioColumns.ARTIST_ID))
            val artistName = cursor.getStringOrNull(cursor.getColumnIndex(AudioColumns.ARTIST))
            val composer = cursor.getStringOrNull(cursor.getColumnIndex(AudioColumns.COMPOSER))
            val albumArtist = cursor.getStringOrNull(cursor.getColumnIndex(Media.ALBUM_ARTIST))

        return Song(
            id,
            title,
            trackNumber,
            year,
            duration,
            data,
            dateModified,
            albumId,
            albumName ?: "",
            artistId,
            artistName ?: "",
            composer ?: "",
            albumArtist ?: ""
        )
    }


    private fun makeSongsCursor(
        selection:String?,
        selectionArgs:Array<String>?,
        sortOrder: String? = PreferenceUtil.songSortOrder
    ):Cursor{

        var selectionFinal = selection
        var selectionArgsFinal = selectionArgs

        selectionFinal = if (selection!!.isNotEmpty() && selection.trim { it <= ' ' } != ""){
              "$IS_MUSIC AND $selectionFinal"
        }else{
            IS_MUSIC
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        }else{
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }


        


    }



    // SONGS IMPLEMENTATION

    override fun songs(): LiveData<List<Song>> {

    }

    override fun songs(cursor: Cursor?): LiveData<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun songs(query: String): LiveData<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun songsByFilePath(filePath: String): LiveData<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun song(cursor: Cursor?): Song {
        TODO("Not yet implemented")
    }

    override fun song(songId: Long): Song {
        TODO("Not yet implemented")
    }




}

