package com.social.people_book.views.trash_screen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.social.people_book.MainViewModel
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.ConfirmDeletionDialog
import com.social.people_book.ui.common_views.ConfirmEmptyTrashDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.util.image_converters.getBytesFromBitmap
import com.social.people_book.views.trash_screen.components.DeletedItemCard
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun TrashScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    val viewModel = viewModel<TrashScreenViewModel>()
    val deletedPeople =
        mainViewModel.personDao.getAllDeletedPerson().collectAsState(initial = emptyList())

    val gridState = rememberLazyGridState()

    val textColor = if (isDarkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButtonArrow(iconColor = MaterialTheme.colorScheme.outline) {
                        navController.popBackStack()
                    }
                },
                title = {
                    MyText(text = "Trash")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Trash",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            ConfirmEmptyTrashDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
                onConfirm = {
                    mainViewModel.viewModelScope.launch {
                        viewModel.emptyTrash(context, navController)
                    }
                })

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    LoadingIndicator()
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(35.dp)
                    )
                    MyText(text = "Items are delete forever after 30 days.", fontSize = 18.sp)
                    Spacer(modifier = Modifier)
                }
                TextButton(onClick = {
                    viewModel.showDialogState = true
                }) {
                    MyText(text = "Empty Trash", fontSize = 18.sp)
                }
            }

            if (deletedPeople.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Trash",
                        modifier = Modifier.size(150.dp)
                    )
                    MyText(text = "No Items In Trash", fontSize = 20.sp)
                }
            } else {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(150.dp),
                ) {
                    items(deletedPeople.value) {
                        DeletedItemCard(
                            text = it.name,
                            textColor = textColor,
                            image = it.image?.let { it1 ->
                                getBytesFromBitmap(
                                    it1,
                                    Bitmap.CompressFormat.JPEG,
                                    50
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}