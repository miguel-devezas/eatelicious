package com.example.eatelicious.navigation

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
import com.example.eatelicious.screens.AddEditScreen
import com.example.eatelicious.screens.ExploreScreen
import com.example.eatelicious.screens.HomeScreen
import com.example.eatelicious.screens.ViewScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object View : Screen("view")
    object AddOrEdit : Screen("addOrEdit")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantApp() {
    val appBarTitle = remember { mutableStateOf("") }
    val navController = rememberNavController()

    val onExplorePage = remember { mutableStateOf(false) }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        onExplorePage.value = destination.route == Screen.Explore.route
    }

    var showOnlyActiveRestaurants by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        appBarTitle.value,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (onExplorePage.value) {
                        Switch(
                            checked = showOnlyActiveRestaurants,
                            onCheckedChange = { showOnlyActiveRestaurants = it },
                            colors = SwitchDefaults.colors(
                                // Custom color when switch is checked
                                checkedThumbColor = Color.Green,
                                // Custom color for the track when switch is checked
                                checkedTrackColor = Color.LightGray,
                                // Custom color when switch is unchecked
                                uncheckedThumbColor = Color.Red,
                                // Custom color for the track when switch is unchecked
                                uncheckedTrackColor = Color.DarkGray
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (onExplorePage.value) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("${Screen.AddOrEdit.route}/0")
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    ) {
            contentPadding -> NavigationGraph(
        navController, contentPadding, showOnlyActiveRestaurants) {
            title -> appBarTitle.value = title
    }
    }
}


@Composable
fun NavigationGraph(
    navController: NavHostController,
    contentPadding: PaddingValues,
    showOnlyActiveRestaurants: Boolean,
    onTitleChanged: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(contentPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onTitleChanged = onTitleChanged,
                onNavigateToExplore = {
                    navController.navigate(Screen.Explore.route)
                }
            )
        }
        composable(Screen.Explore.route) {
            ExploreScreen(
                showOnlyActiveRestaurants = showOnlyActiveRestaurants,
                onTitleChanged = onTitleChanged,
                onNavigateToRestaurant = {id ->
                    navController.navigate(
                        "${Screen.View.route}/$id"
                    )
                },
                onNavigateToEdit = { id ->
                    navController.navigate(
                        "${Screen.AddOrEdit.route}/$id"
                    )
                }
            )
        }

        composable(
            "${Screen.View.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
                backStackEntry->
            ViewScreen(
                restaurantId = backStackEntry.arguments?.getInt("id")!!,
                onTitleChanged = onTitleChanged,
            )
        }
        composable(
            "${Screen.AddOrEdit.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
                backStackEntry->
            AddEditScreen(
                restaurantId = backStackEntry.arguments?.getInt("id")!!,
                onTitleChanged = onTitleChanged,
                onNavigateToExplore = {
                    navController.navigate(Screen.Explore.route)
                }
            )
        }
    }
}

