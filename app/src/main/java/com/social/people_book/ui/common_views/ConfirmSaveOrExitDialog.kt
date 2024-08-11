package com.social.people_book.ui.common_views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText

@Composable
fun ConfirmSaveOrExitDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Exit", fontSize = 22.sp)
            },
            text = {
                MyText(
                    text = "Are you sure to exit? Any changes will not be saved",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                )
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    MyText(text = "Save", fontSize = 17.sp)
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