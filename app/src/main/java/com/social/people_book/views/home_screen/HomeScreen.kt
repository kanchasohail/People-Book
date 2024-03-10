package com.social.people_book.views.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.MainViewModel
import com.social.people_book.util.isScrollingUp
import com.social.people_book.views.side_drawer.DrawerContent
import com.social.people_book.views.side_drawer.DrawerHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(navController: NavController, isDarkMode: Boolean, mainViewModel: MainViewModel) {

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    val localCoroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val gridState = rememberLazyGridState()


    val viewModel = viewModel<HomeScreenViewModel>()

    val searchBarText by viewModel.searchBarText.collectAsState()
//    val persons by viewModel.persons.collectAsState()
    val persons by mainViewModel.personDao.getAll().collectAsState(initial = emptyList())
    val isSearching by viewModel.isSearching.collectAsState()

    SideEffect {
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
                    viewModel = mainViewModel,
                    userName = viewModel.userName
                )
            }
        }) {

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarBackGroundColor,
                    ),
                    title = {
                        SearchBar(
                            text = searchBarText,
                            onTextChanged = viewModel::onSearchTextChange
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
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { MyText(text = "Add", fontSize = 16.sp) },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                    onClick = { navController.navigate(Screens.AddPersonScreen.route) },
                    expanded = gridState.isScrollingUp()
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

                if (viewModel.isLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CenterBox {
                        LoadingIndicator()
                    }
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
                            .padding(start = 8.dp)
                            .clickable {
                                viewModel.isTagExpanded = !viewModel.isTagExpanded

                            },
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

                    AnimatedVisibility(
                        visible = viewModel.isTagExpanded,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy((-8).dp),
                        ) {
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

                    if (isSearching) {
                        Box(
                            modifier = Modifier.fillMaxSize(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    } else {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Adaptive(150.dp),
                        ) {
                            items(persons) {
//                            items(viewModel.items) {
                                ItemCard(
                                    text = it.name,
                                    textColor = textColor,
                                    onClick = {
                                        navController.navigate(
                                            Screens.PersonDetailsGroup.withArgs(
//                                                it.personId
                                                (it.id ?: 1).toString()
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
}

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