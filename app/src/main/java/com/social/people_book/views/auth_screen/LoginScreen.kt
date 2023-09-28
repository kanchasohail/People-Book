package com.social.people_book.views.auth_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(isDarkMode: Boolean, viewModel: AuthViewModel, navController: NavController) {
    val context = LocalContext.current
    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val passwordFocusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    Text(
                        text = "Welcome Back",
                        color = appBarTextColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.W500
                    )
                },
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (isDarkMode) {
                MyDivider()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Email field
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            passwordFocusRequester.requestFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /* Handle the login action */ }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(passwordFocusRequester)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login button
                if (!viewModel.isLoading) {
                    Button(
                        onClick = {
                            viewModel.login(navController, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Login")
                    }
                } else {
                    LoadingIndicator()
                }

                Spacer(modifier = Modifier.height(16.dp))
                MyText(text = "Create a new account", modifier = Modifier.clickable {
                    navController.navigate(Screens.SignUpScreen.route)
                })
            }
        }
    }
}