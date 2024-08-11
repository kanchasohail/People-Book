package com.social.people_book.ui.common_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.ui.theme.dialogButtonBlackColor
import com.social.people_book.views.settings_screen.SettingsViewModel

@Composable
fun EnterPasswordDialogBeforeDeleting(
    showDialog: Boolean,
    viewModel: SettingsViewModel,
    passwordFocusRequester: FocusRequester,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Confirm Password", fontSize = 22.sp)
            },
            text = {
                // Password field
//                MyText(text = "Password", modifier = Modifier.padding(start = 8.dp))
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
//                            keyboardController?.hide()
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
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_eye_icon),
//                            contentDescription = "show password",
//                            tint = if (viewModel.isShowPassword) Color.Red else textColor,
//                            modifier = Modifier.clickable {
//                                viewModel.isShowPassword = !viewModel.isShowPassword
//                            })
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = OutfitFontFamily, fontSize = 18.sp),
                    visualTransformation = if (viewModel.isShowPassword) VisualTransformation.None else PasswordVisualTransformation(
                        mask = '*'
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(passwordFocusRequester)
                )
//                Text(
//                    text = "Enter Your Password to delete your account",
//                    style = TextStyle(
//                        fontFamily = OutfitFontFamily,
//                        fontSize = 16.sp,
//                        lineHeight = 24.sp
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
            },

            onDismissRequest = onDismiss,
            confirmButton = {
                OutlinedButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = dialogButtonBlackColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Confirm",
                        modifier = Modifier.size(22.dp)
                    )
                    MyText(if (viewModel.isLoading) "Deleting..." else "Confirm", fontSize = 17.sp)
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    MyText(
                        "Cancel",
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(.75f)
                    )
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }

}