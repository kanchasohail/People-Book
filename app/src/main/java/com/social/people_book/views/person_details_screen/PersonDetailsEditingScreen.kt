package com.social.people_book.views.person_details_screen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.social.people_book.R
import com.social.people_book.ui.common_views.ConfirmBackDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsEditingScreen(
    navController: NavController,
    isDarkMode: Boolean,
    viewModel: PersonDetailsViewModel
) {
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.selectedImage = uri
            viewModel.viewModelScope.launch {
//                viewModel.saveProfileImage(context)
            }
        }

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    SideEffect {
        viewModel.loadForEditing()
    }
    BackHandler {
        viewModel.showDialogState = true
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
                        viewModel.showDialogState = true
                    }
                },
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

            ConfirmBackDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
                onConfirm = {
                    viewModel.showDialogState = false
                    navController.popBackStack()
                })

            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.downloadedImage == null && viewModel.selectedImage == null) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_blank_profile),
                            contentDescription = "Profile",
                            tint = textColor,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(18.dp))
                        )
                    } else {
                        if (viewModel.selectedImage == null) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    viewModel.downloadedImage
                                ), contentDescription = "person Image"
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    viewModel.selectedImage
                                ), contentDescription = "person Image"
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        IconButton(onClick = {
                            galleryLauncher.launch("image/*")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "addImage",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Name :", fontSize = 17.sp, color = textColor)
                    OutlinedTextField(
                        value = viewModel.name,
                        singleLine = true,
                        onValueChange = {
                            viewModel.name = it
                        },
                        label = { Text(text = "Name") },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Number :", fontSize = 17.sp, color = textColor)
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
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Email :", fontSize = 17.sp, color = textColor)
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
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "About :", fontSize = 17.sp, color = textColor)
                    OutlinedTextField(
                        value = viewModel.about,
                        onValueChange = {
                            viewModel.about = it
                        },
                        label = { Text(text = "About") },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (!viewModel.isLoading) {
                            viewModel.updatePerson(context, navController)
                        }
                    },
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    if (!viewModel.isLoading) {
                        MyText(text = "Save", color = appBarTextColor, fontSize = 16.sp)
                    } else {
                        LoadingIndicator()
                    }
                }

            }
        }
    }
}