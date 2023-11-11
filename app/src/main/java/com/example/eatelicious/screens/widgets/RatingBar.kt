package com.example.eatelicious.screens.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(rating: Float, modifier: Modifier) {
    val maxRating = 5f

    Column (
        modifier = modifier
    ) {
        Row {
            repeat(maxRating.toInt()) { index ->
                val currentRating = index + 1f
                val isSelected = currentRating <= rating
                Icon(
                    imageVector = if (isSelected)
                        Icons.Filled.Star
                    else
                        Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (isSelected) Color.Cyan else Color.Gray,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

