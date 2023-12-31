package com.example.eatelicious.screens

import android.content.Context
import android.widget.RatingBar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
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

    val restaurants = remember { mutableStateListOf<com.example.eatelicious.entities.Restaurant>() }

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
    restaurants: SnapshotStateList<com.example.eatelicious.entities.Restaurant>,
    onNavigateToRestaurant: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    currentContext: Context,
) {
    // Creates a CoroutineScope bound to the RestaurantList's lifecycle
    val scope = rememberCoroutineScope()

    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                    currRestaurant: com.example.eatelicious.entities.Restaurant ->
                scope.launch {
                    RestaurantRepository().deleteRestaurant(currentContext, currRestaurant)
                }
                val filteredRestaurants = restaurants.filterNot {
                        r: com.example.eatelicious.entities.Restaurant -> r.id == currRestaurant.id
                }

                restaurants.clear()
                restaurants.addAll(filteredRestaurants)
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: com.example.eatelicious.entities.Restaurant,
    onNavigateToRestaurant: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onDeleteRestaurant: (com.example.eatelicious.entities.Restaurant) -> Unit
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
                        onDeleteRestaurant(restaurant)
                    },
                    onLongPress = {
                        onNavigateToEdit(restaurant.id)
                    }
                )
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column (
            modifier = Modifier.padding(15.dp).align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (restaurant.isActive) 1.0f else 0.4f)
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
                        .size(200.dp)
                        .clip(RectangleShape)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                restaurant.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp
                ).align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            RatingBar(
                restaurant.rating,
                modifier = Modifier.padding(
                    start = 8.dp, end = 8.dp
                ).align(Alignment.CenterHorizontally)
            )
        }
    }
}

private suspend fun fetchRestaurants(
    context: Context,
    restaurants: SnapshotStateList<com.example.eatelicious.entities.Restaurant>,
    isActive: Boolean
) {
    try {
        val fetchedRestaurants = RestaurantRepository().getRestaurants(
            context, isActive)

        restaurants.clear()
        restaurants.addAll(fetchedRestaurants)
    } catch (_: Exception) {
    }
}
