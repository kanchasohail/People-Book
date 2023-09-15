package com.social.people_book.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlin.math.sqrt

private val DarkColorScheme = darkColorScheme(
    primary = darkPrimaryColor,
    surface = darkSurfaceColor,
    background = darkBackgroundColor,
    onPrimary = lightPrimaryColor
)

private val LightColorScheme = lightColorScheme(
    primary = lightPrimaryColor,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun PeopleBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if(darkTheme) colorScheme.surface.toArgb() else colorScheme.primary.toArgb()
            val statusBarColor = window.statusBarColor
            val rgb = intArrayOf(Color.red(statusBarColor) , Color.green(statusBarColor) , Color.blue(statusBarColor))
            val brightness = sqrt(
                rgb[0] + rgb[0] + .241 +
                        rgb[1] * rgb[1] * .691 +
                        rgb[2] * rgb[2] * .068
            )
            val isDark = brightness >= 127
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}