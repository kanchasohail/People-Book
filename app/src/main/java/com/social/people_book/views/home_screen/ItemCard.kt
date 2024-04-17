package com.social.people_book.views.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    image: ByteArray?,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .size(width = 150.dp, height = 250.dp)
            .border(.7.dp, textColor, shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_blank_profile),
//                tint = textColor,
//                modifier = Modifier.size(40.dp),
//                contentDescription = "user_profile"
//            )

            if (image == null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_blank_profile),
                    tint = textColor,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "user_profile"
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(image),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentDescription = "person Image"
                )
            }

            MyText(text = "Tag", color = textColor, fontSize = 17.sp)
        }
        MyText(text = text, color = textColor, fontSize = 18.sp)
    }
}
