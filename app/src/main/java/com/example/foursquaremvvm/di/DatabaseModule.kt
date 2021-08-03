package com.example.foursquaremvvm.di

import android.content.Context
import androidx.room.Room
import com.example.foursquaremvvm.data.database.FourSquareDatabase
import com.example.foursquaremvvm.data.database.dao.FoursquareDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): FourSquareDatabase =
        Room.databaseBuilder(
            context,
            FourSquareDatabase::class.java,
            "foursquare.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideLogDao(database: FourSquareDatabase): FoursquareDao {
        return database.foursquareDao()
    }

}