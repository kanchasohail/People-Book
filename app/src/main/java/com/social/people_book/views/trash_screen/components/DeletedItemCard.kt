package com.social.people_book.views.trash_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.social.people_book.R
import com.social.people_book.model.room_database.Person
import com.social.people_book.ui.layout.MyText

@Composable
fun DeletedItemCard(
    modifier: Modifier = Modifier,
    person: Person,
    textColor: Color,
) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .size(width = 150.dp, height = 250.dp)
            .border(.7.dp, textColor, shape = RoundedCornerShape(8.dp))
    ) {
        if (person.image == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_blank_profile),
                tint = textColor,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentDescription = "user_profile"
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(person.image),
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = "person Image"
            )
        }


        MyText(text = person.name, color = textColor, fontSize = 18.sp, modifier = Modifier.padding(8.dp))

        MyText(
            text = "Tag", color = textColor, fontSize = 17.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
