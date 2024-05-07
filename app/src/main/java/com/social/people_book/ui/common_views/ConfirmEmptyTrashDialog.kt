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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.RobotoFontFamily


@Composable
fun ConfirmEmptyTrashDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = {
                MyText("Empty Trash" , fontSize = 22.sp)
            },
            text = {
                Text(
                    text = buildAnnotatedString {
                        append("All people in the trash will be deleted ")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = RobotoFontFamily,
                                fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Permanently")
                        }
                    }, style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontSize = 16.sp
                    )
                )
            },

            onDismissRequest = onDismiss,
            confirmButton = {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
//                    MyText("Yes", fontSize = 18.sp)
//                }

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
//                MyText("No", fontSize = 18.sp, modifier = Modifier.clickable {
//                    onDismiss()
//                })
                TextButton(onClick = onDismiss) {
                    MyText("Cancel", fontSize = 17.sp, color = MaterialTheme.colorScheme.outline)
                }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}
