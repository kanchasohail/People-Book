package com.social.people_book.views.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.social.people_book.MainViewModel
import com.social.people_book.ui.common_views.ConfirmLogoutDialog
import com.social.people_book.ui.common_views.ConfirmResetPasswordDialog
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.CustomSwitch
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current


    val isDarkMode = mainViewModel.isDarkMode.value ?: isSystemInDarkTheme()

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val viewModel = viewModel<SettingsViewModel>()

    SideEffect {
        viewModel.getUserDetails(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    MyText(
                        text = "Settings", color = appBarTextColor, fontSize = 26.sp,
                        fontWeight = FontWeight.W500
                    )
                },
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isDarkMode) {
                MyDivider()
            }

            ConfirmResetPasswordDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
                onConfirm = { viewModel.sendPasswordResetEmail(context) },
                email = viewModel.email
            )

            ConfirmLogoutDialog(
                showDialog = viewModel.logoutDialogState,
                onDismiss = { viewModel.logoutDialogState = false },
                onConfirm = {
                    viewModel.logOut(navController)
                })


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        mainViewModel.setThemeMode(!isDarkMode)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyText(text = "Dark Theme", fontSize = 18.sp, color = textColor)

                CustomSwitch(
                    height = 11.dp,
                    width = 22.dp,
                    gapBetweenThumbAndTrackEdge = 1.6.dp,
                    checked = isDarkMode,
                    onCheckedChange = {
                        mainViewModel.setThemeMode(it)
                    },
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //Todo open trash
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyText(text = "Trash", fontSize = 18.sp, color = textColor)
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "trash",
                    tint = textColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            //Actual Screen content
            Column(Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ), elevation = CardDefaults.cardElevation(
                        4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 8.dp)
                    ) {
                        MyText(text = "Account Settings", fontSize = 22.sp, color = textColor)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 8.dp)
                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                MyText(text = "Name: ")
//                                MyText(text = viewModel.name)
//                                IconButton(onClick = { /*TODO*/ }) {
//                                    Icon(
//                                        imageVector = Icons.Default.Edit,
//                                        contentDescription = "Edit email"
//                                    )
//                                }
//                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MyText(text = "Email: ")
                                MyText(text = viewModel.email)
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit email"
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MyText(text = "Password: ")
                                MyText(text = "********")
                                IconButton(onClick = {
                                    viewModel.showDialogState = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit password"
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(onClick = { /*TODO*/ }) {
                                    MyText(text = "Delete Account")
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete account",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                OutlinedButton(onClick = { viewModel.logoutDialogState = true }) {
                                    MyText(text = "Log Out")
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Log Out",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                        }
                    }
                }


                AboutUsCard(textColor = textColor)

                Spacer(modifier = Modifier.weight(1f))
                MyText(text = "App version: 1.0.0", modifier = Modifier.padding(8.dp))
            }

        }
    }
}