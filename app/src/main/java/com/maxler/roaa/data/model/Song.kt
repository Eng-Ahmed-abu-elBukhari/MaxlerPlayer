package com.maxler.roaa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    val id:Long,
    val title: String?,
    val trackNumber:Int,
//    val year:Int,
    val duration:Long,
    val date: String?,
    val dataModified:Long,
    val albumId:Long,
    val albumName: String?,
    val artistId:Long,
    val artistName: String?,
    val composer:String?,
    val albumArtist:String?

):Parcelable