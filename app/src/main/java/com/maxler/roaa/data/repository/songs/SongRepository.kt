package com.maxler.roaa.data.repository.songs

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import com.maxler.roaa.data.model.Song
import com.maxler.roaa.data.utils.Constants.IS_MUSIC
import com.maxler.roaa.data.utils.Constants.projection
import com.maxler.roaa.data.utils.PreferenceUtil

class SongRepository( private val application: Application) :ISongRepository{



    // SONGS IMPLEMENTATION

    override fun songs(): LiveData<List<Song>> {
        return songs(makeSongsCursor(null,null))
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


    @SuppressLint("Recycle")
    private fun makeSongsCursor(
        selection:String?,
         selectionArgs:Array<String>?,
        sortOrder: String? = PreferenceUtil.songSortOrder
    ):Cursor?{

        var selectionFinal = selection

        selectionFinal = if (selection!!.isNotEmpty() && selection.trim { it <= ' ' } != ""){
              "$IS_MUSIC AND $selectionFinal"
        }else{
            IS_MUSIC
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        }else{
            Media.EXTERNAL_CONTENT_URI
        }


        return try {
            application.contentResolver.query(
                uri,
                projection,
                selectionFinal,
                selectionArgs,
                sortOrder
            )
        } catch (ex: SecurityException) {
            return null
        }


    }






}

