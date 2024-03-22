package com.social.people_book.views.auth_screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.RobotoFontFamily
import com.social.people_book.util.google_sign_in.GoogleSignInHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel, isDarkMode: Boolean) {

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
    val request = remember { GoogleSignInHelper.getGoogleSignUpRequest() }

    // Result Launcher to handle Login
    val signInResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val credential = client.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                viewModel.singUpWithGoogle(idToken, context, navController)
            } else {
                Toast.makeText(context, "Failed to SingUp", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    Text(
                        text = "Sign Up",
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
                verticalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (!viewModel.isLoading) {
                        GoogleSignUpButton(text = "Signup") {
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

                // Name field
                MyText(text = "Name", modifier = Modifier.padding(start = 8.dp))
                OutlinedTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    placeholder = {
                        MyText(text = "Enter your name", color = Color.Gray)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
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
                            imeAction = ImeAction.Next,
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
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyText(text = "Password")
                        if (!viewModel.isPasswordValid) {
                            MyText(
                                text = "Please enter at least 8 characters",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = {
                            viewModel.password = it
                            viewModel.isValidPassword(it)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
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

                            MyText(
                                text = if (viewModel.isShowPassword) "Hide" else "Show",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        viewModel.isShowPassword = !viewModel.isShowPassword
                                    })

//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_eye_icon),
//                                contentDescription = "show password",
//                                tint = if (viewModel.isShowPassword) Color.Red else textColor,
//                                modifier = Modifier.clickable {
//                                    viewModel.isShowPassword = !viewModel.isShowPassword
//                                })
                        },
                        singleLine = true,
                        visualTransformation = if (viewModel.isShowPassword) VisualTransformation.None else PasswordVisualTransformation(
                            mask = '*'
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester)
                    )
                }

                // Login button
                if (!viewModel.isLoading) {
                    Button(
                        onClick = {
                            viewModel.signUp(navController, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        MyText("SignUp", fontSize = 18.sp)
                    }
                } else {
                    CenterBox {
                        LoadingIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                CenterBox {
                    val annotatedString = buildAnnotatedString {
                        append("Already have an account? ")
                        pushStringAnnotation(tag = "login", annotation = "nothing")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.inverseSurface,
                                fontFamily = RobotoFontFamily
                            )
                        ) {
                            append("login here")
                        }
                        pop()
                    }
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "login",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                navController.popBackStack()
                            }
                        },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontFamily = RobotoFontFamily
                        )
                    )
                }
            }
        }
    }
}