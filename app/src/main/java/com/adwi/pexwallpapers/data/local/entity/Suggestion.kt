package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestions_table")
data class Suggestion(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val isAddedOnSubmit: Boolean = false
)

val suggestionNameList = mutableListOf(
    "Nature",
    "Food",
    "Photography",
    "Pretty",
    "Red",
    "Blue",
    "Orange",
    "Black",
    "Gray",
    "Christmas",
    "Easter",
    "Animals",
    "Abstract",
    "White",
    "Love",
    "Heart",
    "Friendship",
    "Happiness",
    "Sea",
    "Water",
    "Landscape",
    "Art",
    "Creative",
    "Yellow",
    "Purple",
    "Cars",
    "Horses",
    "Dogs",
    "Cats",
    "Beach",
    "Butterfly",
    "Flowers",
    "Racing",
    "Bmw",
    "Audi",
    "Cycling",
    "Rose"
)