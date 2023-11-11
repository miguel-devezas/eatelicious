package com.example.eatelicious.repositories

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eatelicious.database.RestaurantDatabaseHandler
import com.example.eatelicious.models.Restaurant
import com.example.eatelicious.screens.AddEditScreen
import com.example.eatelicious.screens.ExploreScreen
import com.example.eatelicious.screens.HomeScreen
import com.example.eatelicious.screens.ViewScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RestaurantRepository {

    suspend fun getRestaurants(context: Context, isActive : Boolean) : List<Restaurant> {

        lateinit var restaurants: List<Restaurant>

        val restaurantDBHandler = RestaurantDatabaseHandler(context = context)

        withContext(Dispatchers.IO) {
            try {
                restaurants = restaurantDBHandler.getRestaurants(isActive)
            } catch (e: Exception) {
                println("Failed to fetch restaurant data: ${e.message}")
            }
        }

        return restaurants
    }

    suspend fun getRestaurant(context: Context, id : Int) : Restaurant {

        lateinit var restaurant: Restaurant

        val restaurantDBHandler = RestaurantDatabaseHandler(context = context)

        withContext(Dispatchers.IO) {
            try {
                restaurant = restaurantDBHandler.getRestaurant(id)
            } catch (e: Exception) {
                println("Failed to fetch restaurant data: ${e.message}")
            }
        }

        return restaurant
    }

    suspend fun addRestaurant(context: Context, restaurant: Restaurant) {
        val restaurantDBHandler = RestaurantDatabaseHandler(context = context)

        withContext(Dispatchers.IO) {
            try {
                restaurantDBHandler.addRestaurant(restaurant)
            } catch (e: Exception) {
                println("Failed to add restaurant data: ${e.message}")
            }
        }
    }

    suspend fun editRestaurant(context: Context, restaurant: Restaurant) {
        val restaurantDBHandler = RestaurantDatabaseHandler(context = context)

        withContext(Dispatchers.IO) {
            try {
                restaurantDBHandler.editRestaurant(restaurant)
            } catch (e: Exception) {
                println("Failed to edit restaurant data: ${e.message}")
            }
        }
    }

    suspend fun deleteRestaurant(context: Context, id: Int) {
        val restaurantDBHandler = RestaurantDatabaseHandler(context = context)

        withContext(Dispatchers.IO) {
            try {
                restaurantDBHandler.deleteRestaurant(id)
            } catch (e: Exception) {
                println("Failed to delete restaurant data: ${e.message}")
            }
        }
    }

}


