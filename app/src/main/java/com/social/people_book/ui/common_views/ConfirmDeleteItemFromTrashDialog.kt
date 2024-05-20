package com.social.people_book.ui.common_views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.RobotoFontFamily


@Composable
fun ConfirmDeleteItemFromTrash(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Delete Forever?", fontSize = 22.sp)
            },
            text = {
                Text(
                    text = "This Item be deleted forever and you will not be able to restore them.",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontSize = 16.sp
                    )
                )
            },

            onDismissRequest = onDismiss,
            confirmButton = {
                OutlinedButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                    MyText("Delete", fontSize = 17.sp)
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    MyText("Cancel", fontSize = 17.sp, color = MaterialTheme.colorScheme.outline)
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}
