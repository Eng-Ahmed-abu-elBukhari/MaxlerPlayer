package com.maxler.roaa.data.model

import android.os.Parcel
import android.os.Parcelable

data class Song(
    val id:Long,
    val title: String?,
    val trackNumber:Int,
    val year:Int,
    val duration:Long,
    val date: String?,
    val dataModified:Long,
    val albumId:Long,
    val albumName: String?,
    val artistId:Long,
    val artistName: String?,
    val composer:String?,
    val albumArtist:String?

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeInt(trackNumber)
        parcel.writeInt(year)
        parcel.writeLong(duration)
        parcel.writeString(date)
        parcel.writeLong(dataModified)
        parcel.writeLong(albumId)
        parcel.writeString(albumName)
        parcel.writeLong(artistId)
        parcel.writeString(artistName)
        parcel.writeString(composer)
        parcel.writeString(albumArtist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}