
package com.play.maxler.presentation.exoplayer.playback


import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.play.maxler.common.data.Model
import com.play.maxler.presentation.exoplayer.duration
import com.play.maxler.presentation.exoplayer.id
import kotlinx.parcelize.Parcelize


@Parcelize
data class MediaItemData(
    override val id: String,
    val title: String,
    val subtitle: String, // Artist
    val description: String, //Album
    val albumArtUri: Uri?,
    val isBrowsable: Boolean,
    var isPlaying: Boolean,
    var isBuffering: Boolean,
    var duration: Long = 0L
) : Model(), Parcelable {

    val playingOrBuffering get() = isPlaying || isBuffering

    constructor(item: MediaItem, isPlaying: Boolean, isBuffering: Boolean) : this(
        id = item.mediaId!!,
        title = item.description.title!!.toString(),
        subtitle = item.description.subtitle!!.toString(),
        albumArtUri = item.description.iconUri,
        description = item.description.description.toString(),
        isBrowsable = item.isBrowsable,
        isPlaying = isPlaying,
        isBuffering = isBuffering,
        duration = item.description.duration
    )

    constructor(item: MediaMetadataCompat, isPlaying: Boolean, isBuffering: Boolean) : this(
        id = item.id!!,
        title = item.description.title!!.toString(),
        subtitle = item.description.subtitle!!.toString(),
        albumArtUri = item.description.iconUri,
        description = item.description.description.toString(),
        isBrowsable = false,
        isPlaying = isPlaying,
        isBuffering = isBuffering,
        duration = item.duration
    )

    // We don't want to use all the feeds to check for equality because some of them might change.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItemData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun areContentsTheSame(other: MediaItemData?): Boolean {
        if (this != other) return false
        return id == other.id
                && title == other.title
                && subtitle == other.subtitle
                && duration == other.duration
                && isPlaying == other.isPlaying
                && albumArtUri == other.albumArtUri
                && description == other.description
                && isBrowsable == other.isBrowsable
                && isBuffering == other.isBuffering
    }


}