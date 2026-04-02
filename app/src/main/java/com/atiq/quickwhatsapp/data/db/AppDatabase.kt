package com.atiq.quickwhatsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atiq.quickwhatsapp.model.MessageTemplate
import com.atiq.quickwhatsapp.model.RecentNumber

@Database(
    entities = [RecentNumber::class, MessageTemplate::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentNumberDao(): RecentNumberDao
    abstract fun templateDao(): TemplateDao
}
