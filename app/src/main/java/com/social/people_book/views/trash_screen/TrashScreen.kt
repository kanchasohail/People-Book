package com.social.people_book.views.trash_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.social.people_book.MainViewModel
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(mainViewModel: MainViewModel, navController: NavHostController) {
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
                TextButton(onClick = { /*TODO*/ }) {
                    MyText(text = "Empty Trash", fontSize = 18.sp)
                }
            }
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
        }
    }
}