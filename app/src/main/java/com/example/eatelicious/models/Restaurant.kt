package com.example.eatelicious.models

data class Restaurant(
    val id: Int,
    var name: String,
    var rating: Float,
    var imageUrl: String,
    var isActive: Boolean
)