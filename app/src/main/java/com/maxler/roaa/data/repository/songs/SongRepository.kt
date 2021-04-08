package com.maxler.roaa.data.repository.songs

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
import androidx.core.database.getStringOrNull
import com.maxler.roaa.data.model.Song
import com.maxler.roaa.data.utils.Constants
import com.maxler.roaa.data.utils.Constants.projection
import com.maxler.roaa.data.utils.PreferenceUtil

class SongRepository( private val context: Context) :ISongRepository{


    /** return all songs of device without any query
     * */
    override suspend fun songs(): List<Song> {
        return songs(makeSongsCursor(null,null))
    }

    /** return list of songs
     * */
    override suspend fun songs(cursor: Cursor?): List<Song> {
        val songs = mutableListOf<Song>()
        if (cursor != null && cursor.moveToFirst()){
            do {
                songs.add(getSongFromCursorImpl(cursor))
            }while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    /** return single song
     * */
    override suspend fun song(cursor: Cursor?): Song {
        val song = if (cursor != null && cursor.moveToFirst()){
            getSongFromCursorImpl(cursor)
        }else{
            Constants.emptySong
        }
        cursor?.close()
        return song
    }



    override suspend fun songs(query: String): List<Song> {
        val selection = "${MediaStore.Images.ImageColumns.TITLE} LIKE ?"
        return songs(
            makeSongsCursor(selection = selection, arrayOf("%$query%"))
        )
    }

   // override fun songsByFilePath(filePath: String): List<Song> {}


    override suspend fun song(songId: Long): Song {
        val selection = "${MediaStore.Images.ImageColumns._ID} =?"
        return song(
            makeSongsCursor(selection = selection, arrayOf("%$songId%"))
        )
    }



    private fun getSongFromCursorImpl(cursor: Cursor):Song{

            val id = cursor.getLong(cursor.getColumnIndex(AudioColumns._ID))
            val title = cursor.getString(cursor.getColumnIndex(AudioColumns.TITLE))
            val trackNumber = cursor.getInt(cursor.getColumnIndex(AudioColumns.TRACK))
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
        sortOrder: String? = PreferenceUtil(context = context).songSortOrder
    ):Cursor?{

         val selectionFinal = Constants.IS_MUSIC

        // var selectionFinal: String? = selection

     //    selectionFinal = if (selection!!.isNotEmpty() && selection.trim() != ""){
       //       "${Constants.IS_MUSIC} AND $selectionFinal"
       // }else{
        //    Constants.IS_MUSIC
        //}


        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        }else{
            Media.EXTERNAL_CONTENT_URI
        }


        return try {
            context.contentResolver.query(
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

