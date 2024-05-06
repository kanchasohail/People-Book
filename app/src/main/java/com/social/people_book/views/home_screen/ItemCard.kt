package com.social.people_book.views.home_screen

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
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ItemCard(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    text: String,
    personId: Int,
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
    ) {
        if (image == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_blank_profile),
                tint = textColor,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "blank_profile$personId"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    ),
                contentDescription = "user_profile"
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(image),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .sharedElement(
                        state = rememberSharedContentState(key = "user_profile$personId"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    ),
                contentDescription = "person Image"
            )
        }


        MyText(text = text, color = textColor, fontSize = 20.sp, modifier = Modifier.padding(8.dp))

        MyText(
            text = "Tag", color = textColor, fontSize = 17.sp,
            modifier = Modifier.padding(
                horizontal = 8.dp
            )
        )
    }
}
