package com.social.people_book.views.home_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.util.isScrollingUp
import com.social.people_book.views.home_screen.components.ItemCard
import com.social.people_book.views.home_screen.components.SearchBar

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isDarkMode: Boolean,
    viewModel: HomeScreenViewModel,
    mainViewModel: MainViewModel
) {

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val gridState = rememberLazyGridState()


//    val viewModel = viewModel<HomeScreenViewModel>()

    val searchBarText by viewModel.searchBarText.collectAsState()
    val persons by viewModel.persons.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarBackGroundColor,
                ),
                title = {
                    SearchBar(
                        text = searchBarText,
                        onTextChanged = viewModel::onSearchTextChange,
                        focusRequester = FocusRequester()
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screens.SearchScreen.route)
                    }) {
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
                            ItemCard(
                                animatedVisibilityScope = animatedVisibilityScope,
                                textColor = textColor,
                                person = it,
                                onClick = {
                                    navController.navigate(
                                        Screens.PersonDetailsGroup.withArgs(
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
}