package com.example.eatelicious.repositories

import android.content.Context
import androidx.room.Room
import com.example.eatelicious.database.RestaurantDatabase
import com.example.eatelicious.models.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantRepository {

    companion object {
        private const val RESTAURANT_DB = "restaurants.db"

        @Volatile private var INSTANCE: RestaurantDatabase? = null

        fun getInstance(context: Context): RestaurantDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                RestaurantDatabase::class.java, RESTAURANT_DB
            ).build()
    }

    suspend fun getRestaurants(context: Context, isActive: Boolean) : List<com.example.eatelicious.entities.Restaurant> {
        val db = getInstance(context)

        lateinit var restaurants: List<com.example.eatelicious.entities.Restaurant>

        withContext(Dispatchers.IO) {
            try {
                restaurants = if (isActive) {
                    db.restaurantDao().getActiveRestaurants()
                } else{
                    db.restaurantDao().getRestaurants()
                }
            } catch (e: Exception) {
                println("Failed to fetch restaurant data: ${e.message}")
            }
        }

        return restaurants
    }

    suspend fun getRestaurant(context: Context, id : Int) : com.example.eatelicious.entities.Restaurant {
        lateinit var restaurant: com.example.eatelicious.entities.Restaurant

        val db = getInstance(context)

        withContext(Dispatchers.IO) {
            try {
                restaurant = db.restaurantDao().getRestaurant(id)
            } catch (e: Exception) {
                println("Failed to fetch restaurant data: ${e.message}")
            }
        }

        return restaurant
    }

    suspend fun addRestaurant(context: Context, restaurant: com.example.eatelicious.entities.Restaurant) {
        val db = getInstance(context)

        withContext(Dispatchers.IO) {
            try {
                db.restaurantDao().addRestaurant(restaurant)
            } catch (e: Exception) {
                println("Failed to add restaurant data: ${e.message}")
            }
        }
    }

    suspend fun editRestaurant(context: Context, restaurant: com.example.eatelicious.entities.Restaurant) {
        val db = getInstance(context)

        withContext(Dispatchers.IO) {
            try {
                db.restaurantDao().editRestaurant(restaurant)
            } catch (e: Exception) {
                println("Failed to edit restaurant data: ${e.message}")
            }
        }
    }

    suspend fun deleteRestaurant(context: Context, restaurant: com.example.eatelicious.entities.Restaurant) {
        val db = getInstance(context)

        withContext(Dispatchers.IO) {
            try {
                db.restaurantDao().deleteRestaurant(restaurant)
            } catch (e: Exception) {
                println("Failed to delete restaurant data: ${e.message}")
            }
        }
    }

}

