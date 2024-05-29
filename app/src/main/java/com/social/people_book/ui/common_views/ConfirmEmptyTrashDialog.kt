package com.social.people_book.ui.common_views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.OutfitFontFamily
import com.social.people_book.ui.theme.RobotoFontFamily
import com.social.people_book.ui.theme.dialogButtonBlackColor


@Composable
fun ConfirmEmptyTrashDialog(
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
                    text = "All items in the trash will be deleted forever and you will not be able to restore them.",
                    style = TextStyle(
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
                    Icon(painter = painterResource(id = R.drawable.ic_delete_forever_icon), contentDescription = "delete")
                    Spacer(modifier = Modifier.width(4.dp))
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
