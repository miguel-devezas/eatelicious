package com.example.eatelicious.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.eatelicious.models.Restaurant
import com.example.eatelicious.repositories.RestaurantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    restaurantId: Int,
    onTitleChanged: (String) -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val restaurantState : MutableState<com.example.eatelicious.entities.Restaurant> = remember {
        mutableStateOf(com.example.eatelicious.entities.Restaurant(0, "", 0f, "", false))
    }

    val initialized = remember {
        mutableStateOf(false)
    }

    val currentContext = LocalContext.current

    if (restaurantId == 0) {
        onTitleChanged("Add restaurant")
    } else {
        // Make sure you initialize from the server only the very first
        // time this is composed
        if (!initialized.value) {
            onTitleChanged("Edit restaurant")

            fetchRestaurant(currentContext, restaurantId, restaurantState)

            initialized.value = true
        }
    }

    val restaurant = restaurantState.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 22.dp, end = 22.dp, top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(42.dp),
    ) {

        Column (
            Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Restaurant name",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleSmall,
            )

            OutlinedTextField(
                value = restaurant.name,
                onValueChange = {
                    restaurantState.value = restaurantState.value.copy(name = it)
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column (
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Restaurant rating",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleSmall,
            )

            OutlinedTextField(
                value = if (restaurant.rating > 0) restaurant.rating.toString() else "",
                onValueChange = {
                    if (it.toFloatOrNull() != null) {
                        restaurantState.value = restaurantState.value.copy(rating = it.toFloat())
                    }
                },
                label = { Text("Rating") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column (
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Restaurant image URL",
                modifier = Modifier
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleSmall,
            )

            OutlinedTextField(
                value = restaurant.imageUrl,
                onValueChange = {
                    restaurantState.value = restaurantState.value.copy(imageUrl = it)
                },
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = restaurant.isActive,
                onCheckedChange = {
                    restaurantState.value = restaurantState.value.copy(isActive = it)
                },
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = "Serving now",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Spacer to push content to the top
            Spacer(Modifier.fillMaxSize())

            Button(
                onClick = {
                    if (restaurantState.value.name.isNotEmpty() &&
                        restaurantState.value.imageUrl.isNotEmpty()
                    ) {
                        val newRestaurant = com.example.eatelicious.entities.Restaurant(
                            if (restaurantId == 0)
                                Random.nextInt(1000, 10000000)
                            else restaurantId,
                            restaurantState.value.name,
                            restaurantState.value.rating,
                            restaurantState.value.imageUrl,
                            restaurantState.value.isActive
                        )

                        if (restaurantId == 0) {
                            addRestaurant(currentContext, newRestaurant, onNavigateToExplore)
                        } else {
                            editRestaurant(currentContext, newRestaurant, onNavigateToExplore)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text("Submit")
            }
        }

    }
}

private fun fetchRestaurant(
    currentContext: Context,
    id: Int,
    restaurant: MutableState<com.example.eatelicious.entities.Restaurant>) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            restaurant.value = RestaurantRepository().getRestaurant(currentContext, id)
        } catch (_: Exception) {
        }
    }
}

private fun addRestaurant(
    currentContext: Context,
    restaurant: com.example.eatelicious.entities.Restaurant,
    onNavigateToExplore: () -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            RestaurantRepository().addRestaurant(currentContext, restaurant)
            onNavigateToExplore()
        } catch (_: Exception) {
        }
    }
}


private fun editRestaurant(
    currentContext: Context,
    restaurant: com.example.eatelicious.entities.Restaurant,
    onNavigateToExplore: () -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            RestaurantRepository().editRestaurant(currentContext, restaurant)
            onNavigateToExplore()
        } catch (_: Exception) {
        }
    }
}

