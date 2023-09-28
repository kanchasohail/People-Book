package com.social.people_book.views.add_person_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonScreen(navController: NavController, isDarkMode: Boolean) {
    val context = LocalContext.current

    val viewModel = viewModel<AddPersonViewModel>()

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

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
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        MyText(
                            text = "No Tag",
                            color = appBarTextColor,
                            fontSize = 25.sp,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = appBarTextColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = {
                            if (!viewModel.isLoading) {
                                viewModel.addPerson(context, navController)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        if (!viewModel.isLoading) {
                            MyText(text = "Save", color = appBarTextColor)
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
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_blank_profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .size(200.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Name :", fontSize = 20.sp, color = textColor)
                    TextField(
                        value = viewModel.name,
                        onValueChange = {
                            viewModel.name = it
                        },
                        label = { Text(text = "Name") },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Number :", fontSize = 20.sp, color = textColor)
                    TextField(
                        value = viewModel.number,
                        onValueChange = {
                            viewModel.number = it
                        },
                        label = { Text(text = "Number") },
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "Email :", fontSize = 20.sp, color = textColor)
                    TextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.email = it
                        },
                        label = { Text(text = "Email") },
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText(text = "About :", fontSize = 20.sp, color = textColor)
                    TextField(
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
}