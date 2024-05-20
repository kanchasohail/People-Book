package com.social.people_book.views.auth_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText

@Composable
fun GoogleSignUpButton(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = "Google",
            modifier = Modifier
                .size(32.dp)
                .padding(end = 8.dp)
        )
        if (!isLoading) {
            MyText(
                text = "$text with Google",
                fontSize = 18.sp,
            )
        } else {
            MyText(
                text = "Loading...",
                fontSize = 18.sp,
            )
        }
    }
}


@Composable
fun DividerWithText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
        MyText(
            text = "or",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Color.Gray
        )
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
    }
}

