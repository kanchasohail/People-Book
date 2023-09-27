package com.social.people_book.views.settings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(isDarkMode: Boolean = true, navController: NavController= rememberNavController()) {
//fun SettingsScreen(isDarkMode: Boolean = true, navController: NavController) {
    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    MyText(
                        text = "Settings", color = appBarTextColor, fontSize = 26.sp,
                        fontWeight = FontWeight.W500
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            tint = appBarTextColor,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                })
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isDarkMode) {
                MyDivider()
            }

            //Actual Screen content
            Column(Modifier.fillMaxSize()) {
                AccountDetailsCard(textColor = textColor)
                AboutUsCard(textColor = textColor)

                Spacer(modifier = Modifier.weight(1f))
                MyText(text = "App version: 1.0.0", modifier = Modifier.padding(8.dp))
            }
        }
    }
}