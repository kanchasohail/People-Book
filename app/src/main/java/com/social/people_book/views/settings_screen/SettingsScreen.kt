package com.social.people_book.views.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.social.people_book.MainViewModel
import com.social.people_book.ui.common_views.ConfirmAccountDeletionDialog
import com.social.people_book.ui.common_views.ConfirmLogoutDialog
import com.social.people_book.ui.common_views.ConfirmResetPasswordDialog
import com.social.people_book.ui.common_views.EnterPasswordDialogBeforeDeleting
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.layout.navigateBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val context = LocalContext.current


//    val isDarkMode = mainViewModel.themeMode.value == ThemeMode.Dark
    val isDarkMode = mainViewModel.isDarkMode.value ?: isSystemInDarkTheme()

//    val textColor = if (isDarkMode) Color.White else Color.Black
    val textColor = MaterialTheme.colorScheme.onSurface

    val viewModel = viewModel<SettingsViewModel>()

    val passwordFocusRequester = remember { FocusRequester() }

    var showDialogState by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    ConfirmAccountDeletionDialog(
        showDialog = showDialogState,
        onDismiss = { showDialogState = false }) {
        if (viewModel.withPassword) {
            showDialogState = false
            showPasswordDialog = true
//            passwordFocusRequester.requestFocus()
        } else {
            showPasswordDialog = false
            viewModel.scheduleAccountDeletion(context, navController)
        }
    }

    EnterPasswordDialogBeforeDeleting(
        showDialog = showPasswordDialog,
        viewModel = viewModel,
        passwordFocusRequester = passwordFocusRequester,
        onDismiss = {
            showPasswordDialog = false
            viewModel.password = ""
        }) {
        viewModel.verifyPasswordAndDelete(context, navController)
    }

    SideEffect {
        viewModel.getUserDetails(context)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
//                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            modifier = Modifier.size(30.dp),
                            tint = Color.Transparent,
                            contentDescription = "Close"
                        )
                    }
                },
                title = {
                    MyText(
                        text = "Settings",
                        fontSize = 33.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp), textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Close"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

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

//            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
//                item {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
////                Spacer(modifier = Modifier.height(8.dp))
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    mainViewModel.setThemeMode(!isDarkMode)
//                                }
//                                .padding(horizontal = 12.dp, vertical = 18.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            MyText(text = "Dark Theme", fontSize = 18.sp, color = textColor)
//
//                            CustomSwitch(
//                                height = 10.dp,
//                                width = 20.dp,
//                                gapBetweenThumbAndTrackEdge = 1.6.dp,
//                                checked = isDarkMode,
//                                onCheckedChange = {
//                                    mainViewModel.setThemeMode(it)
//                                },
//                                modifier = Modifier.padding(top = 5.dp)
//                            )
//                        }
//
////                Spacer(modifier = Modifier.height(10.dp))
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    navController.navigate(Screens.TrashScreen.route)
//                                }
//                                .padding(horizontal = 12.dp, vertical = 18.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            MyText(text = "Trash", fontSize = 18.sp, color = textColor)
//                            Icon(
//                                imageVector = Icons.Default.Delete,
//                                contentDescription = "trash",
//                                tint = textColor,
//                                modifier = Modifier.size(28.dp)
//                            )
//                        }
//                    }
//                }

                item {
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
                            MyText(
                                text = "Account",
                                fontSize = 20.sp,
                                color = textColor
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {

                                if (viewModel.email != "null" && viewModel.email.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        MyText(text = "Email: ", color = textColor.copy(.8f))
                                        MyText(
                                            text = viewModel.email,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                        Box(modifier = Modifier)
//                                        IconButton(onClick = { /*TODO*/ }) {
//                                            Icon(
//                                                imageVector = Icons.Default.Edit,
//                                                contentDescription = "Edit email"
//                                            )
//                                        }
                                    }

//                                    Row(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(vertical = 8.dp),
//                                        horizontalArrangement = Arrangement.SpaceBetween,
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
                                    if (viewModel.withPassword) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            MyText(text = "Password: ", color = textColor.copy(.8f))
                                            MyText(
                                                text = "********",
                                                fontSize = 20.sp,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                            IconButton(onClick = {
                                                viewModel.showDialogState = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    contentDescription = "Edit password",

                                                    )
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
//                                }


//                                OutlinedButton(
//                                    onClick = { showDialogState = true },
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
//                                    MyText(text = "Delete Account")
////                                    Spacer(modifier = Modifier.width(3.dp))
////                                    MyText(text = "!", fontSize = 20.sp, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.error)
////                                    Icon(
////                                        imageVector = Icons.Default.Delete,
////                                        contentDescription = "Delete account",
////                                        modifier = Modifier.size(24.dp)
////                                    )
//                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedButton(
                                    onClick = { viewModel.logoutDialogState = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
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

                item {
                    AboutUsCard(textColor = textColor, viewModel = viewModel)
                    Spacer(modifier = Modifier.weight(1f))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        MyText(
                            text = "Delete Account",
                            fontWeight = FontWeight.W500,
                            fontSize = 17.sp,
                            color = Color(0xFFc72020),
                            modifier = Modifier
                                .clickable {
                                    showDialogState = true
                                }
                                .padding(horizontal = 8.dp)
                        )
                    }

                    MyText(
                        text = "App version: 1.0.0",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }

            }
        }
    }
}