package com.example.eatelicious.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eatelicious.entities.Restaurant


// Marks the class as a Data Access Object.
@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE is_active = 1 ORDER BY rating DESC")
    fun getActiveRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
    fun getRestaurant(id: Int): Restaurant

    @Update
    fun editRestaurant(restaurant: Restaurant)

    @Insert
    fun addRestaurant(restaurant: Restaurant)

    @Delete
    fun deleteRestaurant(restaurant: Restaurant)
}
