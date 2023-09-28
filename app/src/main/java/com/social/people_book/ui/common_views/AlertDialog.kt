package com.social.people_book.ui.common_views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowAlert(
    msg: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = {
                Text("Confirm deletion")
            },
            text = { Text(text = msg, fontSize = 20.sp) },

            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Yes", fontSize = 18.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No", fontSize = 18.sp)
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}