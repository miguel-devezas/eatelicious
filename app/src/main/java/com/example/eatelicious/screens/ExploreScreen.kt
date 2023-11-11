package com.example.eatelicious.screens

import android.content.Context
import android.widget.RatingBar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eatelicious.R
import com.example.eatelicious.models.Restaurant
import com.example.eatelicious.repositories.RestaurantRepository
import com.example.eatelicious.screens.widgets.RatingBar
import kotlinx.coroutines.launch


@Composable
fun ExploreScreen(
    showOnlyActiveRestaurants: Boolean,
    onTitleChanged: (String) -> Unit,
    onNavigateToRestaurant: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
) {
    onTitleChanged("Explore")

    val restaurants = remember { mutableStateListOf<Restaurant>() }

    val currentContext = LocalContext.current

    LaunchedEffect(showOnlyActiveRestaurants) {
        fetchRestaurants(currentContext, restaurants, showOnlyActiveRestaurants)
    }

    RestaurantList(
        restaurants = restaurants,
        onNavigateToRestaurant = onNavigateToRestaurant,
        onNavigateToEdit = onNavigateToEdit,
        currentContext = currentContext
    )
}

@Composable
fun RestaurantList(
    restaurants: SnapshotStateList<Restaurant>,
    onNavigateToRestaurant: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    currentContext: Context,
) {
    // Creates a CoroutineScope bound to the RestaurantList's lifecycle
    val scope = rememberCoroutineScope()

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = restaurants,
        ) {restaurant ->

            RestaurantCard(
                restaurant = restaurant,
                onNavigateToRestaurant = onNavigateToRestaurant,
                onNavigateToEdit = onNavigateToEdit
            ) {
                    id: Int ->
                scope.launch {
                    RestaurantRepository.deleteRestaurant(currentContext, id)
                }
                val filteredRestaurants = restaurants.filterNot {
                        r: Restaurant -> r.id == id
                }

                restaurants.clear()
                restaurants.addAll(filteredRestaurants)
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onNavigateToRestaurant: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onDeleteRestaurant: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onNavigateToRestaurant(restaurant.id)
                    },
                    onDoubleTap = {
                        onDeleteRestaurant(restaurant.id)
                    },
                    onLongPress = {
                        onNavigateToEdit(restaurant.id)
                    }
                )
            },
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Row (
            Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.error),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = if (restaurant.isActive) Color.Green else Color.LightGray,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column (modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)) {
                Text(
                    restaurant.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                RatingBar(
                    restaurant.rating,
                    modifier = Modifier.padding(
                        start = 12.dp, end = 16.dp
                    )
                )
            }
        }
    }
}

private suspend fun fetchRestaurants(
    context: Context,
    restaurants: SnapshotStateList<Restaurant>,
    isActive: Boolean
) {
    try {
        val fetchedRestaurants = RestaurantRepository.getRestaurants(
            context, isActive)

        restaurants.clear()
        restaurants.addAll(fetchedRestaurants)
    } catch (_: Exception) {
    }
}


private suspend fun deleteRestaurant(
    currentContext: Context, id: Int, onDeleteRestaurant: (Int) -> Unit) {
    try {
        RestaurantRepository.deleteRestaurant(currentContext, id)
        onDeleteRestaurant(id)
    } catch (_: Exception) {
    }
}