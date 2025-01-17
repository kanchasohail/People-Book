package com.social.people_book.views.add_person_screen

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.model.util.image_converters.compressImage
import com.social.people_book.ui.common_views.ConfirmSaveOrExitDialog
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.views.add_person_screen.components.DropDownMenu
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonScreen(
    navController: NavHostController,
    isDarkMode: Boolean,
    mainViewModel: MainViewModel
) {
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

    val avatarCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the cropped image
                val uriContent = result.uriContent
                val compressedUri = uriContent?.let { compressImage(context, it) }
//                viewModel.selectedImage = uriContent
                viewModel.selectedImage = compressedUri
            } else {
                // an error occurred cropping
                val exception = result.error
                Log.e("Image Cropper", "profileImageCropLauncher Exception $exception")
            }
        }

    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val cropImageOptions = CropImageOptions(
                cropShape = CropImageView.CropShape.RECTANGLE,
                aspectRatioX = 1,
                aspectRatioY = 1,
//            scaleType = CropImageView.ScaleType.CENTER,
                scaleType = CropImageView.ScaleType.CENTER_INSIDE,
                cornerShape = CropImageView.CropCornerShape.RECTANGLE,
                fixAspectRatio = true
            )
            val cropOptions = CropImageContractOptions(uri, cropImageOptions)
            avatarCropLauncher.launch(cropOptions)
        }
    }

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    BackHandler {
        if (viewModel.isChanged()) {
            viewModel.showDialogState = true
        } else {
            navController.popBackStack()
        }
    }

    ConfirmSaveOrExitDialog(
        showDialog = viewModel.showDialogState,
        onDismiss = {
            viewModel.showDialogState = false
            navController.popBackStack()
        },
        onConfirm = {
            viewModel.showDialogState = false
            viewModel.viewModelScope.launch {
                viewModel.addPerson(context, navController)
            }
//            navController.popBackStack()
        })

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        MyText(
//                            text = "Tag : ",
//                            color = appBarTextColor,
//                            fontSize = 22.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        MyText(
//                            text = "No Tag",
//                            color = appBarTextColor,
//                            fontSize = 21.sp,
//                        )
                    DropDownMenu(viewModel = viewModel, color = appBarTextColor)
//                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.isChanged()) {
                            viewModel.showDialogState = true
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = appBarTextColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.isFavorite = !viewModel.isFavorite
                        if (viewModel.isFavorite) {
                            Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Removed from Favorite", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = if (viewModel.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outlined),
                            tint = appBarTextColor,
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Favorite"
                        )
                    }
                    if (isDarkMode) {
                        OutlinedButton(
                            onClick = {
                                if (!viewModel.isLoading) {
                                    viewModel.viewModelScope.launch {
                                        viewModel.addPerson(context, navController)
                                    }
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            MyText(
                                text = if (!viewModel.isLoading) "Save" else "Saving...",
                                color = appBarTextColor,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (!viewModel.isLoading) {
                                    viewModel.viewModelScope.launch {
                                        viewModel.addPerson(context, navController)
                                    }
                                }
                            },
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            MyText(
                                text = if (!viewModel.isLoading) "Save" else "Saving...",
                                color = Color.Black,
                                fontSize = 16.sp
                            )
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
            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(170.dp)
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
                            ),
                            modifier = Modifier.clip(RoundedCornerShape(18.dp)),
                            contentDescription = "person Image"
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        shape = CardDefaults.shape,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    .padding(8.dp)
                                    .clickable {
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
                            if (viewModel.selectedImage != null)
                                TextButton(onClick = {
                                    viewModel.selectedImage = null
                                }) {
                                    MyText(
                                        text = "Remove",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 18.sp
                                    )
                                }

                        }

//                        OutlinedButton(onClick = {
//                            avatarPickerLauncher.launch("image/*")
//                        }) {
//                            MyText(text = "Add", fontSize = 18.sp)
//                            Spacer(modifier = Modifier.width(6.dp))
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_camera),
//                                contentDescription = "addImage",
//                                modifier = Modifier.size(24.dp)
//                            )
//                        }
                    }

                }

                OutlinedTextField(
                    value = viewModel.name,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    onValueChange = {
                        viewModel.name = it
                    }, keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    maxLines = 1,
                    label = { Text(text = "Name") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.85f)
                )



                OutlinedTextField(
                    value = viewModel.number,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    onValueChange = {
                        viewModel.number = it
                    },
                    label = { Text(text = "Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.85f)
                )

                OutlinedTextField(
                    value = viewModel.email,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    onValueChange = {
                        viewModel.email = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.85f)
                )


                OutlinedTextField(
                    value = viewModel.about,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    onValueChange = {
                        viewModel.about = it
                    },
                    label = { Text(text = "About") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.85f)
                )

            }
        }
    }
}