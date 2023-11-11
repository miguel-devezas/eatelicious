package com.example.eatelicious.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eatelicious.dao.RestaurantDao
import com.example.eatelicious.entities.Restaurant

@Database(entities = [Restaurant::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}

