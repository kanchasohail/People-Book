package com.social.people_book.views.side_drawer

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.social.people_book.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerHeader(
    onNavigationIconClick: () -> Unit,
    isDarkMode:Boolean
) {
    val appBarTextColor = if(isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    id = R.string.app_name
                ),
                color = appBarTextColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
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
                    modifier = Modifier.size(35.dp)
                )
            }
        },
       colors = TopAppBarDefaults.smallTopAppBarColors(
           containerColor = if(isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
       ),
    )


}