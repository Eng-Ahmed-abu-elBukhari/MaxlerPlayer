package com.play.maxler.presentation.exoplayer

import android.content.Context
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.play.maxler.R
import com.play.maxler.common.data.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 *
 * Represents a tree of media that's used by [PlaybackService.onLoadChildren].
 *
 * [BrowseTree] maps a media id (see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) to one (or more)
 * [MediaMetadataCompat] objects, which are children of the media id.
 *
 * For example, given the following conceptual tree:
 * root
 *  +-- Albums
 *  |    +-- Album_A
 *  |    |    +-- Song_1
 *  |    |    +-- Song_2
 *  ...
 *  +-- Artists
 *  ...
 *
 * Requesting `browserTree["root"]` would return a list that included "Albums", "Artists", and any other direct
 * children. Taking the media ID of "Albums" ("Albums" in this example), `browseTree["Albums"]` would return a single
 * item list "Album_A", and, finally, `browseTree["Album_A"]` would return "Song_1" and "Song_2". Since those are leaf
 * nodes, requesting `browseTree["Song_1"]` would return null (there aren't any children of it).
 */
class BrowseTree @Inject constructor(
    @ApplicationContext val context: Context,
    private val musicSource: MediaStoreSource
    ){
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    operator fun get(parentId: String) = mediaIdToChildren[parentId]

    /**
     * Whether to allow clients which are unknown (non-whitelisted) to use search on this
     * [BrowseTree].
     */
    val searchableByUnknownCaller = true

    /**
     * In this example, there's a single root note (identified by the constant [Constants.BROWSABLE_ROOT].
     * The root's children are each album included in the [MusicSource],
     * and the children of each album are songs on that album. See [buildAlbum] for details
     * TODO: Expand to allow more browsing types.
     */
    init {
        // BROWSABLE_ROOT = /
        val rootList = mediaIdToChildren[Constants.BROWSABLE_ROOT] ?: mutableListOf()

        val songsMetadata = MediaMetadataCompat.Builder().apply {
            id = Constants.SONGS_ROOT
            title = context.getString(R.string.songs)
            albumArtUri =
                Constants.IMAGE_URI_ROOT + context.resources.getResourceEntryName(R.drawable.ic_song)
            flag = MediaItem.FLAG_PLAYABLE
        }.build()

        val albumsMetadata = MediaMetadataCompat.Builder().apply {
            id = Constants.ALBUMS_ROOT
            title = context.getString(R.string.albums)
            albumArtUri = Constants.IMAGE_URI_ROOT + context.resources.getResourceEntryName(R.drawable.ic_album)
        }.build()


        val artistsMetadata = MediaMetadataCompat.Builder().apply {
            id = Constants.ARTISTS_ROOT
            title = context.getString(R.string.artists)
            artist = Constants.IMAGE_URI_ROOT + context.resources.getResourceEntryName(R.drawable.ic_microphone)
        }.build()

        rootList += songsMetadata
        rootList += albumsMetadata
        rootList += artistsMetadata
        mediaIdToChildren[Constants.BROWSABLE_ROOT] = rootList
        musicSource.forEach {
            val albumMediaId = it.albumId.toString()
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(it)
            albumChildren += it


            val artistMediaId = it.artist
            val artistChildren = mediaIdToChildren[artistMediaId] ?: buildArtistRoot(it)
            artistChildren += it

            val  songsChildren = mediaIdToChildren[Constants.SONGS_ROOT] ?: mutableListOf()
            songsChildren += it
            mediaIdToChildren[Constants.SONGS_ROOT] = songsChildren
        }
    }

    /**
     * Builds a node, under the root, that represents an album, given
     * a [MediaMetadataCompat] object that's one of the songs on that album,
     * marking the item as [MediaItem.FLAG_BROWSABLE], since it will have child
     * node(s) AKA at least 1 song.
     */
    private fun buildAlbumRoot(metadata: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = MediaMetadataCompat.Builder().apply {
            id = metadata.albumId.toString()
            title = metadata.album
            artist = metadata.artist
            albumArt = metadata.albumArt
            flag = MediaItem.FLAG_BROWSABLE
        }.build()

        // Adds this album to the 'Albums' category.
        val rootList = mediaIdToChildren[Constants.ALBUMS_ROOT] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[Constants.ALBUMS_ROOT] = rootList

        // Insert the album's root with an empty list for its children, and return the list.
        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.id!!] = it
        }
    }

    /**
     * Builds a node, under the root, that represents an artist, given
     * a [MediaMetadataCompat] object that's one of the songs on that artist,
     * marking the item as [MediaItem.FLAG_BROWSABLE], since it will have child
     * node(s) AKA at least 1 song.
     */
    private fun buildArtistRoot(metadata: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val artistMetadata = MediaMetadataCompat.Builder().apply {
            id = metadata.artist!!
            title = metadata.artist
            albumArt = metadata.albumArt
            //            albumArtUri = metadata.albumArtUri.toString()
            flag = MediaItem.FLAG_BROWSABLE
        }.build()

        // Adds this artist to the 'Artists' category.
        val rootList = mediaIdToChildren[Constants.ARTISTS_ROOT] ?: mutableListOf()
        rootList += artistMetadata
        mediaIdToChildren[Constants.ARTISTS_ROOT] = rootList

        // Insert the album's root with an empty list for its children, and return the list.
        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[artistMetadata.id!!] = it
        }
    }
}