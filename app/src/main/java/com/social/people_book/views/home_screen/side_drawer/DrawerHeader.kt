package com.social.people_book.views.home_screen.side_drawer


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R
import com.social.people_book.ui.layout.MyText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerHeader(
    onNavigationIconClick: () -> Unit,
    isDarkMode: Boolean
) {
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    TopAppBar(
        title = {
            MyText(
                text = stringResource(
                    id = R.string.app_name
                ),
                color = appBarTextColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.W500
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Drawer",
                    tint = appBarTextColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
        )
    )
}