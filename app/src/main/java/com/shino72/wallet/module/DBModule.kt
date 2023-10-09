package com.shino72.wallet.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shino72.wallet.db.OttDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DBModule {

    @Singleton
    @Provides
    fun providePlanDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        OttDatabase::class.java,
        "database-contacts"
    ).build()


    @Provides
    fun providePlanDao(database: OttDatabase) = database.ottDao()
}
