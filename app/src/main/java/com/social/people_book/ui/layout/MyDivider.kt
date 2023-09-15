package com.social.people_book.ui.layout

import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun MyDivider(modifier: Modifier = Modifier) {
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.primary.copy(.4f),
        modifier = modifier.shadow(
            10.dp,
            spotColor = MaterialTheme.colorScheme.primary
        )
    )
}