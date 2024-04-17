package com.social.people_book.model.other

import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavigationItemModel(
    val icon: ImageVector,
    val iconOutlined: ImageVector,
    val route: String,
    val routeName:String
)
