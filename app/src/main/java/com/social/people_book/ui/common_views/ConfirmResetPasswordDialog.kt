package com.social.people_book.ui.common_views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.ui.theme.dialogButtonBlackColor

@Composable
fun ConfirmResetPasswordDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    email: String
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Reset Password" , fontSize = 22.sp)
            },
            text = {
                Text(
                    text = buildAnnotatedString {
                        append("This will send a reset password link to ")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = OutfitFontFamily,
                                fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(email)
                        }
                        append("\n\nPlease make sure to check the Spam folder in case you can't find it in your inbox.")
                    }, style = TextStyle(
                        fontFamily = OutfitFontFamily,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                )
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
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "reset")
                    MyText("Reset", fontSize = 17.sp)
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    MyText("Cancel", fontSize = 17.sp, color = MaterialTheme.colorScheme.onSurface.copy(.75f))
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}