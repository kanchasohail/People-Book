package com.social.people_book.views.home_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText

@Composable
fun ItemCard(modifier: Modifier = Modifier, text: String, textColor: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .size(150.dp)
            .padding(8.dp)
            .clip(CardDefaults.shape)
            .clickable {
                onClick()
            },
        border = BorderStroke(.7.dp, textColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = textColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            shape = CircleShape,
                            width = 2.dp,
                            color = textColor
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_blank_profile),
                        tint = textColor,
                        modifier = Modifier.size(30.dp),
                        contentDescription = "user_profile"
                    )
                }
                MyText(text = "Tag", color = textColor, fontSize = 20.sp)
            }
            MyText(text = text, color = textColor, fontSize = 24.sp)
        }
    }
}