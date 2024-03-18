package com.social.people_book.views.auth_screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.util.google_sign_in.GoogleSignInHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(isDarkMode: Boolean, viewModel: AuthViewModel, navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordFocusRequester = remember { FocusRequester() }

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    // Instance of GoogleSignInClient and BeginSignInRequest
    val client = remember { GoogleSignInHelper.getGoogleSignInClient(context) }
    val request = remember { GoogleSignInHelper.getGoogleLoginRequest() }

    // Result Launcher to handle Login
    val signInResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val credential = client.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
               viewModel.loginWithGoogle(idToken, navController)
            } else {
                Toast.makeText(context, "Failed to Login", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor
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
                .imePadding()
        ) {

            if (isDarkMode) {
                MyDivider()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.auth_screen_logo),
                        contentDescription = "image",
                        contentScale = ContentScale.FillWidth
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    if (!viewModel.isLoading) {
                        GoogleSignUpButton (text = "Login"){
                            viewModel.isLoading = true
                            client.beginSignIn(request).addOnCompleteListener { task ->
                                viewModel.isLoading = false
                                if (task.isSuccessful) {
                                    val intentSender = task.result.pendingIntent.intentSender
                                    val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                                    signInResultLauncher.launch(intentSenderRequest)
                                } else {
                                    Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        CenterBox {
                            LoadingIndicator()
                        }
                    }

                    DividerWithText()
                }
                // Email field
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyText(text = "Email")
                        if (!viewModel.isEmailValid) {
                            MyText(
                                text = "Please enter a valid email",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.email = it
                            viewModel.isValidEmail(it)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        placeholder = {
                            MyText(text = "example@email.com", color = Color.Gray)
                        },
                        keyboardActions = KeyboardActions(
                            onNext = {
                                passwordFocusRequester.requestFocus()
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }


                // Password field
                MyText(text = "Password", modifier = Modifier.padding(start = 8.dp))
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            passwordFocusRequester.freeFocus()
                        }
                    ),
                    placeholder = {
                        MyText(text = "Type password", color = Color.Gray)
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_eye_icon),
                            contentDescription = "show password",
                            tint = if (viewModel.isShowPassword) Color.Red else textColor,
                            modifier = Modifier.clickable {
                                viewModel.isShowPassword = !viewModel.isShowPassword
                            })
                    },
                    singleLine = true,
                    visualTransformation = if (viewModel.isShowPassword) VisualTransformation.None else PasswordVisualTransformation(
                        mask = '*'
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(passwordFocusRequester)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    MyText(
                        text = "Forgot password?",
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.ForgotPasswordScreen.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login button
                if (!viewModel.isLoading) {
                    Button(
                        onClick = {
                            viewModel.login(navController, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        MyText("Login", fontSize = 18.sp)
                    }
                } else {
                    CenterBox {
                        LoadingIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                CenterBox {
                    MyText(text = "Create a new account", modifier = Modifier.clickable {
                        navController.navigate(Screens.SignUpScreen.route)
                    })
                }
            }
        }
    }
}
