package com.social.people_book.ui.common_views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterBox(modifier: Modifier = Modifier, item: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        item()
    }
}