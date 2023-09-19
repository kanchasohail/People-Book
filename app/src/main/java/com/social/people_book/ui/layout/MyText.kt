package com.social.people_book.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 19,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.W500,
        fontSize = fontSize.sp,
        color = textColor
    )
}