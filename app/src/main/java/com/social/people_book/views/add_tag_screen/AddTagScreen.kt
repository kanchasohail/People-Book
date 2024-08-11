package com.social.people_book.views.add_tag_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.social.people_book.MainActivity
import com.social.people_book.MainViewModel
import com.social.people_book.ui.common_views.ConfirmTagDeleteDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTagScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    isDarkMode: Boolean
) {
    val viewModel = viewModel<AddTagScreenViewModel>()
    val context = LocalContext.current
    var showDialogState by remember { mutableStateOf(false) }

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val tagsList = MainActivity.tagsRepository.tagsList.collectAsState()
    val defaultTags = MainActivity.tagsRepository.defaultTags

    var deleteAbleTag by remember {
        mutableStateOf("")
    }
    val maxCharacter = 12
    ConfirmTagDeleteDialog(showDialog = showDialogState, onDismiss = { showDialogState = false }) {
        viewModel.deleteTag(deleteAbleTag)
        showDialogState = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButtonArrow(iconColor = textColor, navController = navController)
                },
                title = {
                    MyText(text = "Customise Tags")
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    value = viewModel.tagName,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    onValueChange = {
                        viewModel.tagName = it.take(maxCharacter)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    maxLines = 1,
                    label = { Text(text = "New Tag") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(.65f)
                )

                Button(
                    onClick = {
                        if (!viewModel.isLoading) {
                            viewModel.saveTag(context)
                        }
                    },
                    modifier = Modifier.height(45.dp)
                ) {
                    MyText(text = if (viewModel.isLoading) "Saving..." else "Add", fontSize = 20.sp)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ), elevation = CardDefaults.cardElevation(
                    4.dp
                )
            ) {
                if (tagsList.value.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MyText(text = "Custom Tags UnAvailable", fontSize = 22.sp)
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(12.dp)
                ) {
                    items(tagsList.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(start = 12.dp)
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MyText(text = it)
                            if (defaultTags.contains(it)) {
                                MyText(
                                    text = "Default",
                                    fontSize = 15.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(8.dp).padding(vertical = 4.dp)
                                )
                            } else {
                                IconButton(onClick = {
                                    showDialogState = true
                                    deleteAbleTag = it
                                }) {
                                    Icon(Icons.Rounded.Clear, contentDescription = "Delete Tag")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}