package com.social.people_book.views.add_person_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
//fun AddPersonScreen(navController: NavController, isDarkMode: Boolean) {
fun AddPersonScreen(
    navController: NavController = rememberNavController(),
    isDarkMode: Boolean = true
) {
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
                    Text(
                        text = "Add People",
                        color = appBarTextColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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
                    IconButton(onClick = {
                        viewModel.addPerson(context)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save",
                            tint = appBarTextColor,
                            modifier = Modifier.size(26.dp)
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
        ) {
            if (isDarkMode) {
                MyDivider()
            }

            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = viewModel.name,
                    onValueChange = {
                        viewModel.name = it
                    },
                    label = { Text(text = "Name") },
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    value = viewModel.number,
                    onValueChange = {
                        viewModel.number = it
                    },
                    label = { Text(text = "Number") },
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.email = it
                    },
                    label = { Text(text = "Email") },
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    value = viewModel.about,
                    onValueChange = {
                        viewModel.about = it
                    },
                    label = { Text(text = "About") },
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (!viewModel.isLoading) {
                    Button(
                        onClick = {
                            viewModel.addPerson(context)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Save")
                    }
                } else {
                    LoadingIndicator()
                }
            }

        }
    }
}