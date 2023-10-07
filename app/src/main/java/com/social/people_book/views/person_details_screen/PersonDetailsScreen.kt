package com.social.people_book.views.person_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.ConfirmDeletionDialog
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.redColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    personId: String,
    viewModel: PersonDetailsViewModel
) {
    val context = LocalContext.current

    SideEffect {
        viewModel.loadPerson(personId)
    }

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
                        text = viewModel.thisPerson.name,
                        color = appBarTextColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = appBarTextColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(onClick = {
                            navController.navigate(Screens.PersonDetailsEditingScreen.route)
                        }) {
                            MyText(text = "Edit", fontSize = 20.sp, color = appBarTextColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = appBarTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(onClick = {
                            viewModel.showDialogState = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = redColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isDarkMode) {
                MyDivider()
            }

            ConfirmDeletionDialog(
                showDialog = viewModel.showDialogState,
                onDismiss = { viewModel.showDialogState = false },
                onConfirm = { viewModel.deletePerson(personId, context, navController) })


            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            //Actual Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_blank_profile),
                        contentDescription = "person photo",
                        tint = textColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .size(160.dp)
                    )
                    MyText(
                        text = viewModel.thisPerson.name,
                        fontSize = 28.sp,
                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyText(text = "Phone:", color = textColor)
                    Spacer(modifier = Modifier.width(5.dp))
                    MyText(text = viewModel.thisPerson.number.toString(), color = textColor)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyText(text = "Email:", color = textColor)
                    Spacer(modifier = Modifier.width(5.dp))
                    MyText(text = viewModel.thisPerson.email.toString(), color = textColor)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyText(text = "About:", color = textColor)
                    Spacer(modifier = Modifier.width(5.dp))
                    MyText(text = viewModel.thisPerson.about.toString(), color = textColor)
                }
            }
        }
    }
}