package com.atiq.quickwhatsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atiq.quickwhatsapp.model.RecentNumber
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentNumberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentNumber: RecentNumber)

    @Query("SELECT * FROM recent_numbers ORDER BY timestamp DESC LIMIT 20")
    fun getAll(): Flow<List<RecentNumber>>

    @Query("DELETE FROM recent_numbers WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM recent_numbers")
    suspend fun deleteAll()

}