package com.social.people_book.views.splash_screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, isDarkMode: Boolean, auth: FirebaseAuth) {

    val iconColor = MaterialTheme.colorScheme.primary

    val scale = remember {
        Animatable(3.2f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            1.2f,
            animationSpec = tween(durationMillis = 800,
                easing = { OvershootInterpolator(2.2f).getInterpolation(it) })
        )
        delay(800L)
        navController.popBackStack()
        if (auth.currentUser != null) {
            navController.navigate(Screens.HomeScreen.route)
        } else {
            navController.navigate(Screens.AuthScreen.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.surface),
            .background(if (isDarkMode) Color(0xFF0F1F2C) else Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Spacer(modifier = Modifier.fillMaxWidth())

//        Icon(
        Image(
            painter = painterResource(id = if (isDarkMode) R.drawable.splash_logo_dark else R.drawable.splash_logo_light),
//            tint = iconColor,
            contentDescription = "logo",
            modifier = Modifier
                .scale(scale.value)
        )

//        Icon(
//            painter = painterResource(id = R.drawable.app_brand_logo),
//            tint = iconColor,
//            contentDescription = "brand_logo",
//            modifier = Modifier
//                .fillMaxWidth(.4f)
//                .padding(vertical = 32.dp)
//                .padding(bottom = 16.dp)
//        )

    }
}