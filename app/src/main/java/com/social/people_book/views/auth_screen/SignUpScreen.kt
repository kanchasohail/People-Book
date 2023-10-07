package com.social.people_book.views.auth_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.ExperimentalComposeUiApi
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
                MyText(text = "Email", modifier = Modifier.padding(start = 8.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.email = it
                        viewModel.isValidEmail(it)
                    },
                    supportingText = {
                        if (!viewModel.isEmailValid) {
                            MyText(
                                text = "Please enter a valid email",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field

                MyText(text = "Password", modifier = Modifier.padding(start = 8.dp))

                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it
                                    viewModel.isValidPassword(it)},
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
                    supportingText = {
                        if (!viewModel.isPasswordValid) {
                            MyText(
                                text = "Please enter at least 8 characters",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Login button
                if (!viewModel.isLoading) {
                    Button(
                        onClick = {
                            viewModel.signUp(navController, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
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
                                color = Color.Blue,
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