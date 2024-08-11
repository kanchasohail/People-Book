package com.social.people_book.views.home_screen.side_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.social.people_book.MainViewModel
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.ThemeMode
import com.social.people_book.views.add_person_screen.AddPersonViewModel

@Composable
fun ThemeDropDownMenu(viewModel: MainViewModel, color: Color) {

    Box {
        TextButton(onClick = { viewModel.isDropDownOpen = true }) {
            MyText(text = (viewModel.themeMode.value ?: "System").toString(), fontSize = 18.sp, color = color)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                tint = color,
                contentDescription = "Drop Down Arrow"
            )
        }

        DropdownMenu(
            expanded = viewModel.isDropDownOpen,
            onDismissRequest = { viewModel.isDropDownOpen = false }
        ) {
            ThemeMode.values().map {
                DropdownMenuItem(text = {
                    MyText(
                        text = it.name,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }, onClick = {
                    viewModel.setThemeMode(it)
                    viewModel.isDropDownOpen = false
                })
                HorizontalDivider()
            }
        }
    }
}