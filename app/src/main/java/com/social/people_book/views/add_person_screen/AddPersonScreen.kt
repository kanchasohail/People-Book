package com.social.people_book.views.add_person_screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.model.room_database.PersonRoom
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonScreen(navController: NavController, isDarkMode: Boolean, mainViewModel: MainViewModel) {
    val context = LocalContext.current

    val viewModel = viewModel<AddPersonViewModel>()

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    val avatarCropLauncher = rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            val uriContent = result.uriContent
           viewModel.selectedImage = uriContent
        } else {
            // an error occurred cropping
            val exception = result.error
            Log.e("Image Cropper", "profileImageCropLauncher Exception $exception")
        }
    }

    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        val cropImageOptions = CropImageOptions(
            cropShape = CropImageView.CropShape.RECTANGLE,
            aspectRatioX = 1,
            aspectRatioY = 1,
            scaleType = CropImageView.ScaleType.CENTER,
            cornerShape = CropImageView.CropCornerShape.RECTANGLE,
            fixAspectRatio = true
        )
        val cropOptions = CropImageContractOptions(uri, cropImageOptions)
        avatarCropLauncher.launch(cropOptions)

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MyText(
                            text = "Tag : ",
                            color = appBarTextColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        MyText(
                            text = "No Tag",
                            color = appBarTextColor,
                            fontSize = 21.sp,
                        )
                    }
                },
                navigationIcon = {
                    BackButtonArrow(iconColor = appBarTextColor) {
                        navController.popBackStack()
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = {
                            if (!viewModel.isLoading) {
                                viewModel.addPerson(context, navController)
                                val personRoom = PersonRoom(
                                    id = null,
                                    name = viewModel.name,
                                    number = viewModel.number,
                                    email = viewModel.email,
                                    about = viewModel.about
                                )
                                mainViewModel.addPerson(personRoom)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        if (!viewModel.isLoading) {
                            MyText(text = "Save", color = appBarTextColor, fontSize = 16.sp)
                        } else {
                            LoadingIndicator()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
        ) {
            if (isDarkMode) {
                MyDivider()
            }

            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp).verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.selectedImage == null) {
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
                                viewModel.selectedImage
                            ), contentDescription = "person Image"
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        OutlinedButton(onClick = {
                            avatarPickerLauncher.launch("image/*")
                        }) {
                            MyText(text = "Add", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "addImage",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                }

                OutlinedTextField(
                    value = viewModel.name,
                    singleLine = true,
                    onValueChange = {
                        viewModel.name = it
                    },
                    label = { Text(text = "Name") },
                    modifier = Modifier.padding(8.dp)
                )



                OutlinedTextField(
                    value = viewModel.number,
                    singleLine = true,
                    onValueChange = {
                        viewModel.number = it
                    },
                    label = { Text(text = "Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                OutlinedTextField(
                    value = viewModel.email,
                    singleLine = true,
                    onValueChange = {
                        viewModel.email = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    label = { Text(text = "Email") },
                    modifier = Modifier.padding(8.dp)
                )


                OutlinedTextField(
                    value = viewModel.about,
                    onValueChange = {
                        viewModel.about = it
                    },
                    label = { Text(text = "About") },
                    modifier = Modifier.padding(8.dp)
                )

            }
        }
    }
}