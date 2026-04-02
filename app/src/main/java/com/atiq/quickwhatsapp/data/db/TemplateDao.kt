package com.atiq.quickwhatsapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atiq.quickwhatsapp.model.MessageTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: MessageTemplate)

    @Query("SELECT * FROM message_templates ORDER BY id ASC")
    fun getAll(): Flow<List<MessageTemplate>>

    @Delete
    suspend fun delete(template: MessageTemplate)
}