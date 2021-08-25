package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
class Settings(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val lastQuery: String = ""
)