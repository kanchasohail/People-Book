package com.social.people_book.ui.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    scale: Float = 2f,
    width: Dp = 26.dp,
    height: Dp = 13.dp,
    strokeWidth: Dp = 1.dp,
    gapBetweenThumbAndTrackEdge: Dp = 1.8.dp,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    val switchON = remember {
        mutableStateOf(checked) // Initially the switch is ON
    }
    LaunchedEffect(key1 = checked){
        switchON.value = checked
    }
    val thumbColor = if (switchON.value) MaterialTheme.colorScheme.surface else Color.Gray
    val trackColor = if (switchON.value) MaterialTheme.colorScheme.primary else Color.Gray
    val backGroundColor =
        if (switchON.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
            .1f
        )

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() },
        label = "switchAnimatePosition"
    )

    Canvas(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value

                        onCheckedChange(switchON.value)
                    }
                )
            }
    ) {

        // Track
        drawRoundRect(
            color = trackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx()),
        )
        drawRoundRect(
            color = backGroundColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
        )

        // Thumb
        drawCircle(
            color = thumbColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}