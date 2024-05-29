package com.social.people_book.views.person_details_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.model.util.image_converters.loadImageBitmap
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.ConfirmDeletionDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.redColor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.PersonDetailsScreen(
    navController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isDarkMode: Boolean,
    personId: String,
    viewModel: PersonDetailsViewModel,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val imageBitmap = loadImageBitmap("profile_$personId", context)
    fun loadPerson(id: Long) {
        val roomPerson = mainViewModel.personDao.getPersonById(id)
        viewModel.thisPerson = roomPerson
        viewModel.savedPerson = roomPerson
        viewModel.isFavorite = roomPerson.isFavorite
        viewModel.selectedImage = null

//        viewModel.downloadedImage =
//            roomPerson.image?.let { getBytesFromBitmap(it, Bitmap.CompressFormat.JPEG, 100) }
    }


    LaunchedEffect(key1 = Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            loadPerson(personId.toLong())
        }
    }


    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black
    val iconButtonColor = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    MyText(
                        text = viewModel.thisPerson.tag.toString(),
                        color = appBarTextColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    BackButtonArrow(iconColor = appBarTextColor, navController)
                },
//                actions = {
//                    IconButton(onClick = {
//                        viewModel.isFavorite = !viewModel.isFavorite
//                        viewModel.markFavorite(viewModel.isFavorite)
//                    }) {
//                        Icon(
//                            painter = painterResource(id = if (viewModel.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outlined),
//                            modifier = Modifier.size(30.dp),
//                            contentDescription = "Favorite"
//                        )
//                    }
//                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.showDialogState = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = redColor,
                        modifier = Modifier.size(35.dp)
                    )
                }


                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(Screens.PersonDetailsEditingScreen.route)
                    }) {
                    MyText(text = "Edit", fontSize = 17.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ConfirmDeletionDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
//                onConfirm = { viewModel.deletePerson(personId, context, navController) })
                onConfirm = {
                    viewModel.deletePerson(personId.toLong(), context, navController)
                })


            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.thisPerson.image == null) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_blank_profile),
                                contentDescription = "Profile",
                                tint = textColor,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "blank_profile$personId"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 1000)
                                        }
                                    )
                                    .clip(RoundedCornerShape(18.dp))
                            )
                        } else {
//                            Image(
//                                painter = rememberAsyncImagePainter(
//                                    viewModel.thisPerson.image
//                                ),
//                                modifier = Modifier
//                                    .sharedElement(
//                                        state = rememberSharedContentState(key = "user_profile$personId"),
//                                        animatedVisibilityScope = animatedVisibilityScope,
//                                        boundsTransform = { _, _ ->
//                                            tween(durationMillis = 1000)
//                                        }
//                                    )
//                                    .clip(RoundedCornerShape(18.dp)),
//                                contentDescription = "person Image"
//                            )

//                            AsyncImage(
//                                model = loadImageBitmap(viewModel.thisPerson.image!!, context),
//                                modifier = Modifier
//                                    .sharedElement(
//                                        state = rememberSharedContentState(key = "user_profile$personId"),
//                                        animatedVisibilityScope = animatedVisibilityScope,
//                                        boundsTransform = { _, _ ->
//                                            tween(durationMillis = 1000)
//                                        }
//                                    )
//                                    .clip(RoundedCornerShape(18.dp)),
//                                contentDescription = "person Image"
//                            )

                            if (imageBitmap != null) {
                                Image(
                                    bitmap = imageBitmap.asImageBitmap(),
                                    modifier = Modifier
                                        .sharedElement(
                                            state = rememberSharedContentState(key = "user_profile$personId"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            boundsTransform = { _, _ ->
                                                tween(durationMillis = 1000)
                                            }
                                        )
                                        .clip(RoundedCornerShape(18.dp)),
                                    contentDescription = "person Image"
                                )
                            }

                        }
                    }
                    MyText(
                        text = viewModel.thisPerson.name,
                        fontSize = 24.sp,
                        color = textColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .sharedElement(
                                state = rememberSharedContentState(key = "user_name$personId"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 1000)
                                }
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Phone:", color = textColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    MyText(text = viewModel.thisPerson.number.toString(), color = textColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        if (viewModel.thisPerson.number != null && viewModel.thisPerson.number != "") {
                            IconButton(onClick = {
                                viewModel.copyToClipboard(
                                    context,
                                    viewModel.thisPerson.number!!,
                                    "Number"
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_copy_icon),
                                    tint = iconButtonColor,
                                    contentDescription = "Copy"
                                )
                            }
                            if (viewModel.thisPerson.number!!.length > 4) {
                                IconButton(onClick = {
                                    viewModel.makePhoneCall(context, viewModel.thisPerson.number!!)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        tint = iconButtonColor,
                                        contentDescription = "Call"
                                    )
                                }
                            }
                        }
                    }


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
//                        .padding(top = 10.dp),
                        .padding(top = 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Email:", color = textColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    MyText(text = viewModel.thisPerson.email.toString(), color = textColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (viewModel.thisPerson.email != null && viewModel.thisPerson.email != "") {
                            IconButton(onClick = {
                                viewModel.copyToClipboard(
                                    context,
                                    viewModel.thisPerson.email!!,
                                    "Email"
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_copy_icon),
                                    tint = iconButtonColor,
                                    contentDescription = "Copy"
                                )
                            }
                            if (viewModel.isValidEmail(viewModel.thisPerson.email!!)) {
                                IconButton(onClick = {
                                    viewModel.openEmailComposer(
                                        context,
                                        viewModel.thisPerson.email!!,
                                        ""
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.MailOutline,
                                        tint = iconButtonColor,
                                        contentDescription = "Call"
                                    )
                                }
                            }
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(top = 10.dp)
                ) {
                    MyText(text = "About:", color = textColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    MyText(text = viewModel.thisPerson.about.toString(), color = textColor)
                }

                Spacer(modifier = Modifier.weight(1f))

//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    IconButton(onClick = {
//                        viewModel.showDialogState = true
//                    }) {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = "Delete",
//                            tint = redColor,
//                            modifier = Modifier.size(35.dp)
//                        )
//                    }
//
//
//                    Button(modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            navController.navigate(Screens.PersonDetailsEditingScreen.route)
//                        }) {
//                        MyText(text = "Edit", fontSize = 17.sp)
//                    }
//                }
            }
        }
    }
}