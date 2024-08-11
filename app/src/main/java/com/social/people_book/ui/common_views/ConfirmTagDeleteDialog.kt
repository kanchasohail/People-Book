package com.social.people_book.ui.common_views


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.ui.theme.dialogButtonBlackColor

@Composable
fun ConfirmTagDeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Delete Tag", fontSize = 22.sp)
            },
            text = {
                Text(
                    text = "If you remove the tag then those who have it will be assigned None",
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
                OutlinedButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = dialogButtonBlackColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(22.dp)
                    )
                    MyText("Remove", fontSize = 17.sp)
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
