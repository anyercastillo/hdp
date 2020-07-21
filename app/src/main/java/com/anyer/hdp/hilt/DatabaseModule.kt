package com.anyer.hdp.hilt

import android.content.Context
import com.anyer.hdp.dao.DevicesDao
import com.anyer.hdp.db.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return AppRoomDatabase.getDatabase(context)
    }

    @Provides
    fun provideDevicesDao(database: AppRoomDatabase): DevicesDao {
        return database.devicesDao()
    }
}