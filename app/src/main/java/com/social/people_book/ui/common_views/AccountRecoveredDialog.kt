package com.social.people_book.ui.common_views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily

@Composable
fun AccountRecoveredDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Welcome Back!", fontSize = 22.sp)
            },
            text = {
                Text(
                    text = "This account was scheduled to be deleted within 7 days. Due to logging in within 7 days the deletion process has been cancelled",
                    style = TextStyle(
                        fontFamily = OutfitFontFamily,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },

            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm,

                ) {
                    MyText("Understood", fontSize = 17.sp, color = MaterialTheme.colorScheme.primary)
                }

            },
            shape = RoundedCornerShape(8.dp)
        )
    }

}