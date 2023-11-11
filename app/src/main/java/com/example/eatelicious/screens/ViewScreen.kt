package com.example.eatelicious.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eatelicious.R
import com.example.eatelicious.models.Restaurant
import com.example.eatelicious.repositories.RestaurantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ViewScreen(
    restaurantId: Int,
    onTitleChanged: (String) -> Unit,
) {
    val restaurant : MutableState<Restaurant> = remember {
        mutableStateOf(Restaurant(0, "", 0f, "", false))
    }

    val currentContext = LocalContext.current

    fetchRestaurant(currentContext, restaurantId, restaurant)

    onTitleChanged(restaurant.value.name)

    Column(
        Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (restaurant.value.isActive) 1.0f else 0.5f)
                .weight(0.7f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.value.imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.error),
                contentScale = ContentScale.Crop,
                contentDescription = "restaurant",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RectangleShape),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    restaurant.value.name ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    restaurant.value.rating.toString() ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp
                    )
                )
            }
        }

    }
}

private fun fetchRestaurant(
    currentContext : Context,
    id: Int,
    restaurant: MutableState<Restaurant>
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            restaurant.value = RestaurantRepository.getRestaurant(currentContext, id)
        } catch (_: Exception) {
        }
    }
}

