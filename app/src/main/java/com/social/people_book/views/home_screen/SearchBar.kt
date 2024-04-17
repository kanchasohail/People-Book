package com.social.people_book.views.home_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.theme.RobotoFontFamily

@Composable
fun SearchBar(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White.copy(.9f),
    textColor: Color = Color.White
) {
    BasicTextField(
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = textColor,
            fontSize = 20.sp,
            fontFamily = RobotoFontFamily
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                // You can perform a search action here
            }
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .border(
//                        width = 1.dp,
//                        color = iconTint,
//                        shape = RoundedCornerShape(8.dp)
//                    ),
                        ,
                contentAlignment = Alignment.CenterStart

            ) {
                HorizontalDivider(modifier = Modifier.align(Alignment.TopCenter))

                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = iconTint,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(3.dp)
                            .padding(bottom = 8.dp)
                    )
                    if (text.isEmpty()) Text(
                            text = "Search",
                            fontSize = 20.sp,
                            color = iconTint,
                            fontFamily = RobotoFontFamily
                        )
                    // you have to invoke this function then cursor will focus and you will able to write something
                    innerTextField.invoke()
                }

                HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
            }

        },
        modifier = modifier.fillMaxWidth()
    )

}
