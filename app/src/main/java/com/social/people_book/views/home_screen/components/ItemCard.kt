package com.social.people_book.views.home_screen.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ItemCard(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    person: Person,
    textColor: Color,
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
    ) {
        if (person.image == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_blank_profile),
                tint = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "blank_profile${person.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
//                        boundsTransform = { _, _ ->
//                            tween(durationMillis = 1000)
//                        }
                    )
                    .clip(RoundedCornerShape(18.dp)),
                contentDescription = "user_profile"
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(person.image),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "user_profile${person.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = "person Image"
            )
        }


        MyText(
            text = person.name,
            color = textColor,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(8.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "user_name${person.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
        )

        MyText(
            text = "Tag", color = textColor, fontSize = 17.sp,
            modifier = Modifier.padding(
                horizontal = 8.dp
            )
        )
    }
}
