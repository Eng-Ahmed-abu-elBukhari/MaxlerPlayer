package com.play.maxler.di.module

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.play.maxler.R
import com.play.maxler.data.local.preferences.Storage
import com.play.maxler.di.MediaBrowseCallers
import com.play.maxler.domain.repository.MainRepository
import com.play.maxler.presentation.exoplayer.BrowseTree
import com.play.maxler.presentation.exoplayer.MediaStoreSource
import com.play.maxler.presentation.exoplayer.PlaybackPreparer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {


    @ServiceScoped
    @Provides
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()



    // Configure ExoPlayer to handle audio focus for us.
    @ServiceScoped
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }
/*    @ServiceScoped
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ) = DefaultDataSourceFactory(
        context,
        Util.getUserAgent(context, context.getString(R.string.app_name)
    ))*/


    @ServiceScoped
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ): DefaultDataSource.Factory = DefaultDataSource.Factory(context)


    @MediaBrowseCallers
    @Provides
    @ServiceScoped
    fun provideMediaBrowseCallers() = R.xml.allowed_media_browser_callers


    @Provides
    @ServiceScoped
    fun provideMediaStoreSource(
        @ApplicationContext context : Context ,
         repository: MainRepository
        ) : MediaStoreSource {
        return MediaStoreSource(context = context , repository )
    }

    @ServiceScoped
    @Provides
    fun provideBrowseTree(
        @ApplicationContext context: Context,
        mediaStoreSource: MediaStoreSource
    ) : BrowseTree {
        return BrowseTree(context,mediaStoreSource)
    }


    @ServiceScoped
    @Provides
    fun providePlaybackPreparer(
        musicSource: MediaStoreSource,
        exoPlayer: ExoPlayer,
        dataSourceFactory: DefaultDataSource.Factory,
        storage: Storage
    ) : PlaybackPreparer {
        return PlaybackPreparer(
            musicSource = musicSource,
            exoPlayer = exoPlayer,
            dataSourceFactory = dataSourceFactory,
            storage = storage
        )
    }


}