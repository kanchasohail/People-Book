package com.social.people_book.views.home_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.views.home_screen.components.ItemCard
import com.social.people_book.views.home_screen.components.SearchBar

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeScreenViewModel,
    isDarkMode: Boolean
) {

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val gridState = rememberLazyGridState()

    val searchBarText by viewModel.searchBarText.collectAsState()
    val persons by viewModel.searchedPersons.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        viewModel.onSearchTextChange("") // Reset the search text
        focusRequester.requestFocus()
    }

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
                        focusRequester = focusRequester
                    )
                },
            )
        },
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
                                    navController.popBackStack() // Pop the search screen
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

