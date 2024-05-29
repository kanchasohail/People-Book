package com.social.people_book.views.home_screen.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.social.people_book.R
import com.social.people_book.model.LocalFileStorageRepository
import com.social.people_book.model.room_database.Person
import com.social.people_book.model.room_database.Tag
import com.social.people_book.model.util.image_converters.loadImageBitmap
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ItemCard(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    localFileStorageRepository: LocalFileStorageRepository,
    person: Person,
    textColor: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageBitmap = loadImageBitmap(person.image, context)
    Column(
        modifier = modifier
            .padding(8.dp)
//            .size(width = 150.dp, height = 250.dp)
//            .size(width = 150.dp)
            .border(.7.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
    ) {
        if (person.image != null && imageBitmap != null) {
//            Image(
//                painter = rememberAsyncImagePainter(person.image),
////                painter = rememberAsyncImagePainter(localFileStorageRepository.loadImageFromInternalStorage("")),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .aspectRatio(1f)
//                    .sharedElement(
//                        state = rememberSharedContentState(key = "user_profile${person.id}"),
//                        animatedVisibilityScope = animatedVisibilityScope,
//                    )
//                    .clip(RoundedCornerShape(8.dp)),
//                contentDescription = "person Image"
//            )

//            AsyncImage(
////                model = loadImageBitmap(person.image, context),
//                model = imageBitmap,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .aspectRatio(1f)
//                    .sharedElement(
//                        state = rememberSharedContentState(key = "user_profile${person.id}"),
//                        animatedVisibilityScope = animatedVisibilityScope,
//                    )
//                    .clip(RoundedCornerShape(8.dp)),
//                contentDescription = "person Image"
//            )

            Image(
                bitmap = imageBitmap.asImageBitmap(),
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

        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_person_icon),
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 4.dp, top = 4.dp),
                contentDescription = "Blank profile"
            )
        }

        if (person.name.isNotEmpty()) {
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
        }

        if (person.tag != Tag.None) {
            MyText(
                text = person.tag.toString(), color = textColor, fontSize = 17.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        } else {
            MyText(
                text = "No Tag", color = textColor.copy(.7f), fontSize = 17.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }

    }
}
