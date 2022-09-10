package com.lovepreet.gallery.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lovepreet.gallery.dataSource.ImageDataSource
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Singleton
    @Provides
    fun providePicasso(@ApplicationContext context: Context): Picasso {
        return Picasso.Builder(context)
            .indicatorsEnabled(true)
            .loggingEnabled(true)
            .listener { _, _, exception -> exception.printStackTrace() }
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .serializeSpecialFloatingPointValues()
        .create()

    @Singleton
    @Provides
    fun provideImageSource(@ApplicationContext context: Context, gson: Gson): ImageDataSource{
        return ImageDataSource(context, gson)
    }
}