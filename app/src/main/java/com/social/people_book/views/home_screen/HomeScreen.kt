package com.social.people_book.views.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.ShowAlert
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.theme.ThemeViewModel
import com.social.people_book.views.side_drawer.DrawerContent
import com.social.people_book.views.side_drawer.DrawerHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(navController: NavController, isDarkMode: Boolean, themeViewModel: ThemeViewModel) {

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    val localCoroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val viewModel = viewModel<HomeScreenViewModel>()

    SideEffect {
        viewModel.loadPerson()
        viewModel.loadUserName()
    }

    // To close the drawer on backPress
    BackHandler(drawerState.isOpen) {
        localCoroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.85f),
                drawerShape = RectangleShape,
            ) {
                DrawerHeader(isDarkMode = isDarkMode, onNavigationIconClick = {
                    localCoroutineScope.launch {
                        drawerState.close()
                    }
                })

                DrawerContent(
                    navController = navController,
                    viewModel = themeViewModel,
                    userName = viewModel.userName
                )
            }
        }) {

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = appBarBackGroundColor,
                    ),
                    title = {
                        MyText(
                            text = "People Book",
                            color = appBarTextColor,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.W500
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            localCoroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "More",
                                tint = appBarTextColor,
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_settings_icon),
                                contentDescription = "More",
                                tint = appBarTextColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(Screens.AddPersonScreen.route)
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(8.dp))
                    MyText(text = "Add")
                }
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

                //Actual Home Screen Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyText(text = "Search by tags", color = textColor)
                        IconButton(onClick = {
                            viewModel.isTagExpanded = !viewModel.isTagExpanded
                        }) {
                            Icon(
                                imageVector = if (viewModel.isTagExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "More",
                                tint = textColor,
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }

                    if (viewModel.isTagExpanded) {
                        FlowRow {
                            viewModel.tags.forEach { tagItem ->
                                TagsChip(
                                    chipText = tagItem,
                                    textColor = textColor,
                                    isSelected = tagItem == viewModel.selectedTagItem
                                ) {
                                    viewModel.selectedTagItem = tagItem
                                }
                            }
                        }
                    }

                    LazyVerticalGrid(
                        state = rememberLazyGridState(),
                        columns = GridCells.Adaptive(150.dp),
                    ) {
                        items(viewModel.items) {
                            ItemCard(
                                text = it.name,
                                textColor = textColor,
                                onClick = {
                                    navController.navigate(
                                        Screens.PersonDetailsScreen.withArgs(
                                            it.personId
                                        )
                                    )
                                })
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsChip(
    modifier: Modifier = Modifier,
    chipText: String,
    textColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor: Color? = if (isSelected) Color.White else null

    AssistChip(
        onClick = onClick, label = {
            MyText(
                text = chipText,
                color = if (isSelected) Color.Black else textColor,
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) Color.White else Color.Transparent
        ),
//        border = borderColor?.let {
//            AssistChipDefaults.assistChipBorder(
//                borderColor = it
//            )
//        },
        modifier = modifier.padding(4.dp)
    )


//    Card(
//        modifier = modifier
//            .padding(4.dp)
//            .clickable {
//                onClick()
//            }, colors = CardDefaults.cardColors(
//            containerColor = if (isSelected) Color.White else Color.Gray,
//        )
//    ) {
//        MyText(
//            text = chipText,
//            textColor = if (isSelected) Color.Black else textColor,
//            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
//        )
//    }
}