package com.social.people_book.views.home_screen.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.theme.RobotoFontFamily

@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White.copy(.9f),
    onClick: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
        .padding(top = 4.dp)
        .clickable { onClick() }) {
        Row(
            modifier = modifier.fillMaxWidth().padding(start = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = iconTint,
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "Search",
                fontSize = 20.sp,
                color = iconTint,
                fontFamily = RobotoFontFamily
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}