package com.social.people_book.views.person_details_screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.social.people_book.R
import com.social.people_book.model.util.image_converters.compressImage
import com.social.people_book.model.util.image_converters.loadImageBitmap
import com.social.people_book.ui.common_views.ConfirmBackDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.views.person_details_screen.components.DropDownMenuEditing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsEditingScreen(
    navController: NavHostController,
    isDarkMode: Boolean,
    viewModel: PersonDetailsViewModel,
) {
    val context = LocalContext.current
//    var imageBitmap = loadImageBitmap("profile_${viewModel.savedPerson.id}", context)
    viewModel.imageBitmap = loadImageBitmap("profile_${viewModel.savedPerson.id}", context)


    val avatarCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the cropped image
                val uriContent = result.uriContent
//                viewModel.selectedImage = uriContent
                viewModel.selectedImage = uriContent?.let { compressImage(context, it) }
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

    SideEffect {
        viewModel.loadForEditing()
    }
    BackHandler {
        if (viewModel.isChanged()) {
            viewModel.showDialogState = true
        } else {
            navController.popBackStack()
        }
    }
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
                    DropDownMenuEditing(viewModel = viewModel, color = appBarTextColor)
//                    }
                },
                navigationIcon = {
                    BackButtonArrow(iconColor = appBarTextColor, navController)
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.isFavorite = !viewModel.isFavorite
                    }) {
                        Icon(
                            painter = painterResource(id = if (viewModel.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outlined),
                            tint = appBarTextColor,
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Favorite"
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            if (!viewModel.isLoading) {
                                viewModel.viewModelScope.launch {
                                    viewModel.updatePerson(context, navController)
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
                .verticalScroll(rememberScrollState())
        ) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.thisPerson.image == null && viewModel.selectedImage == null || ( viewModel.imageBitmap == null && viewModel.selectedImage == null)) {
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
//                            Image(
//                                painter = rememberAsyncImagePainter(
//                                    viewModel.thisPerson.image
//                                ),
//                                modifier = Modifier.clip(RoundedCornerShape(18.dp)),
//                                contentDescription = "person Image"
//                            )
//                            AsyncImage(
//                                model = loadImageBitmap(viewModel.thisPerson.image!!, context),
//                                modifier = Modifier.clip(RoundedCornerShape(18.dp)),
//                                contentDescription = "person Image"
//                            )

                            if (viewModel.imageBitmap != null) {
                                Image(
                                    bitmap = viewModel.imageBitmap!!.asImageBitmap(),
                                    modifier = Modifier.clip(RoundedCornerShape(18.dp)),
                                    contentDescription = "person Image"
                                )
                            }

                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    viewModel.selectedImage
                                ), modifier = Modifier.clip(RoundedCornerShape(18.dp)),
                                contentDescription = "person Image"
                            )
                        }
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
                            if (viewModel.selectedImage != null || viewModel.imageBitmap != null && viewModel.thisPerson.image != null)
                                TextButton(onClick = {
                                    viewModel.selectedImage = null
                                    viewModel.imageBitmap = null
                                }) {
                                    MyText(text = "Remove", color = MaterialTheme.colorScheme.error)
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
                    },
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