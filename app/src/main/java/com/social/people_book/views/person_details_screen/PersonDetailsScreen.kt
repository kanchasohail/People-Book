package com.social.people_book.views.person_details_screen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.model.data_models.Person
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.ConfirmDeletionDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.redColor
import com.social.people_book.util.image_converters.getBytesFromBitmap
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun PersonDetailsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    personId: String,
    viewModel: PersonDetailsViewModel,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    fun loadPerson(id: Int) {
        val roomPerson = mainViewModel.personDao.getPersonById(id)
        val thisPerson = Person(
            personId = roomPerson.id.toString(),
            name = roomPerson.name,
            number = roomPerson.number,
            email = roomPerson.email,
            about = roomPerson.about,
        )
        viewModel.thisPerson = thisPerson
        viewModel.downloadedImage =
            roomPerson.image?.let { getBytesFromBitmap(it, Bitmap.CompressFormat.JPEG, 100) }
    }

   fun deletePerson() {

        GlobalScope.launch(Dispatchers.IO){

            val roomPerson = mainViewModel.personDao.getPersonById(personId.toInt())
            mainViewModel.personDao.deletePerson(roomPerson)
        }
        navController.popBackStack()

        Toast.makeText(context, "Person Deleted Successfully", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(key1 = Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            loadPerson(personId.toInt())
        }
    }


    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    MyText(
                        text = viewModel.thisPerson.name,
                        color = appBarTextColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    BackButtonArrow(iconColor = appBarTextColor) {
                        navController.popBackStack()
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ConfirmDeletionDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
//                onConfirm = { viewModel.deletePerson(personId, context, navController) })
                onConfirm = {
                    mainViewModel.viewModelScope.launch {
                        deletePerson()
                    } })


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
                            .height(160.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.downloadedImage == null) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_blank_profile),
                                contentDescription = "Profile",
                                tint = textColor,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    viewModel.downloadedImage
                                ), contentDescription = "person Image"
                            )
                        }
                    }
                    MyText(
                        text = viewModel.thisPerson.name,
                        fontSize = 24.sp,
                        color = textColor
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(top = 10.dp)
                ) {
                    MyText(text = "Phone:", color = textColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    MyText(text = viewModel.thisPerson.number.toString(), color = textColor)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(top = 10.dp)
                ) {
                    MyText(text = "Email:", color = textColor)
                    Spacer(modifier = Modifier.width(10.dp))
                    MyText(text = viewModel.thisPerson.email.toString(), color = textColor)
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        viewModel.showDialogState = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = redColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }


                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate(Screens.PersonDetailsEditingScreen.route)
                        }) {
                        MyText(text = "Edit", fontSize = 17.sp, color = appBarTextColor)
                    }
                }
            }
        }
    }
}