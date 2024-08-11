package com.social.people_book.views.trash_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.util.image_converters.loadImageBitmap
import com.social.people_book.ui.layout.MyText

@Composable
fun DeletedItemCard(
    modifier: Modifier = Modifier,
    person: Person,
    textColor: Color,
    remainingDays: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageBitmap = loadImageBitmap("profile_${person.id}", context)

    Column(
        modifier = modifier
            .padding(8.dp)
            .border(.7.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        if (person.image == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_person_icon),
                tint = textColor,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 4.dp, start = 4.dp),
                contentDescription = "user_profile"
            )
        } else {
//            Image(
//                painter = rememberAsyncImagePainter(person.image),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentDescription = "person Image"
//            )

//            AsyncImage(
//                model = loadImageBitmap(person.image, context),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentDescription = "person Image"
//            )

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentDescription = "person Image"
                )
            }
        }

        if (person.name.isNotEmpty()) {
            MyText(
                text = person.name,
                color = textColor,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        if (person.tag != null) {
            MyText(
                text = person.tag, color = textColor, fontSize = 17.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            MyText(
                text = "No Tag", color = textColor.copy(.7f), fontSize = 17.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

//        Spacer(modifier = Modifier.height(8.dp))

        MyText(
            text = "$remainingDays left",
            color = textColor,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 8.dp)
        )

    }
}
