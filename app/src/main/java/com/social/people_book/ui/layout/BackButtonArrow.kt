package com.social.people_book.ui.layout

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.social.people_book.R

@Composable
fun BackButtonArrow(iconColor: Color, navController: NavHostController) {
    IconButton(onClick = {
        navController.navigateBack()
    }) {
        Icon(
//            painter = painterResource(id = R.drawable.back_arrow),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}


val NavHostController.canGoBack: Boolean get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack() {
    if (canGoBack) {
        popBackStack()
    }
}
