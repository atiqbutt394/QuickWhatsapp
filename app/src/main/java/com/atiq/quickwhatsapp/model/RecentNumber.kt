package com.atiq.quickwhatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_numbers")
data class RecentNumber (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dialCode: String,
    val number: String,
    val timestamp: Long = System.currentTimeMillis()
)
