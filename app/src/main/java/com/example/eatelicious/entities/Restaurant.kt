package com.example.eatelicious.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey
    val id: Int,

    var name: String,
    var rating: Float,

    @ColumnInfo(name = "image_url")
    var imageUrl: String,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean,
)
