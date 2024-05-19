package com.social.people_book.views.person_details_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.social.people_book.ui.layout.MyText
import com.social.people_book.views.person_details_screen.PersonDetailsViewModel

@Composable
fun DropDownMenuEditing(viewModel: PersonDetailsViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { viewModel.isDropDownOpen = true }) {
            MyText(text = viewModel.selectedTag.name, fontSize = 18.sp)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop Down Arrow")
        }

        DropdownMenu(
            expanded = viewModel.isDropDownOpen,
            onDismissRequest = { viewModel.isDropDownOpen = false }
        ) {
            viewModel.tagsList.map {
                DropdownMenuItem(text = { MyText(text = it.name) }, onClick = {
                    viewModel.selectedTag = it
                    viewModel.isDropDownOpen = false
                })
            }
        }
    }
}