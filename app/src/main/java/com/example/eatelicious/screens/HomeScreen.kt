package com.example.eatelicious.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatelicious.R
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onTitleChanged: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
) {
    onTitleChanged("Eatelicious")

    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToExplore()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_page),
            contentDescription = "Home page image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.01f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Eatelicious",
                    color = Color.White,
                    fontSize = 24.sp,
                )
            }

        }
    }
}
