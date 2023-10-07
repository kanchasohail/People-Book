package com.social.people_book.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomCornersBackground(
    topColor: Color = Color(0xFF009688),
    bottomColor: Color = Color(0xFFDB9936),
    cornersRadius: Dp = 45.dp,
    topSectionHeight: Float = .25f,
    headerComposable: @Composable() () -> Unit,
    bodyComposable: @Composable() () -> Unit
) {

    Box(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .background(topColor)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(bottomColor)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(topSectionHeight)
                    .background(
                        shape = RoundedCornerShape(bottomEnd = cornersRadius),
                        color = topColor
                    )
            ) {
                headerComposable()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        shape = RoundedCornerShape(topStart = cornersRadius),
                        color = bottomColor
                    )
            ) {
                bodyComposable()
            }
        }
    }
}