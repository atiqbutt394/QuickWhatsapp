package com.atiq.quickwhatsapp.di

import android.content.Context
import androidx.room.Room
import com.atiq.quickwhatsapp.data.db.AppDatabase
import com.atiq.quickwhatsapp.data.db.RecentNumberDao
import com.atiq.quickwhatsapp.data.db.TemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "quickwhatsapp.db").build()

    @Provides
    fun provideRecentNumberDao(db: AppDatabase): RecentNumberDao = db.recentNumberDao()

    @Provides
    fun provideTemplateDao(db: AppDatabase): TemplateDao = db.templateDao()
}