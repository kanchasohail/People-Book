package com.social.people_book.views.home_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ItemCard(modifier: Modifier = Modifier, text: String , textColor:Color) {
    Card(
        modifier = modifier
            .size(150.dp)
            .padding(8.dp),
        border = BorderStroke(.7.dp , textColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = textColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}