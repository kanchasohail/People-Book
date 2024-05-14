package com.social.people_book.views.home_screen.side_drawer


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.social.people_book.MainViewModel
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.layout.CustomSwitch
import com.social.people_book.ui.layout.MyText

@SuppressLint("UnrememberedMutableState")
@Composable
fun DrawerContent(navController: NavController, mainViewModel: MainViewModel) {

    val isDarkMode = mainViewModel.isDarkMode.value ?: isSystemInDarkTheme()

    val textColor = if (isDarkMode) Color.White else Color.Black

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(
                6.dp
            ), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ), shape = RoundedCornerShape(
                topStart = 0.dp, topEnd = 0.dp, bottomStart = 10.dp, bottomEnd = 10.dp
            )
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            mainViewModel.setThemeMode(!isDarkMode)
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Dark Theme", fontSize = 18.sp, color = textColor)

                    CustomSwitch(
                        height = 11.dp,
                        width = 22.dp,
                        gapBetweenThumbAndTrackEdge = 1.6.dp,
                        checked = isDarkMode,
                        onCheckedChange = {
                            mainViewModel.setThemeMode(it)
                        },
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screens.TrashScreen.route)
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Trash", fontSize = 18.sp, color = textColor)
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "trash",
                        tint = textColor,
                        modifier = Modifier.size(28.dp)
                    )
                }



                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screens.SettingsScreen.route)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Settings", fontSize = 18.sp, color = textColor)
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = textColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

            }
        }
    }
}