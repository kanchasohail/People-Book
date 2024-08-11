package com.social.people_book.views.home_screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.layout.MyText
import com.social.people_book.ui.layout.navigateBack
import com.social.people_book.views.home_screen.components.ItemCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FavoritesScreen(
    navController: NavHostController,
    animatedVisibilityScope: AnimatedContentScope,
    viewModel: HomeScreenViewModel,
    isDarkMode: Boolean
) {

    val textColor = if (isDarkMode) Color.White else Color.Black

    val favoritePerson = viewModel.getFavoritePersons()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
//                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            modifier = Modifier.size(30.dp),
                            tint = Color.Transparent,
                            contentDescription = "Close"
                        )
                    }
                },
                title = {
                    MyText(
                        text = "Favourites",
                        fontSize = 33.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp), textAlign = TextAlign.Center
                    )
                },
                actions = {
//                    BackButtonArrow(iconColor = Color.Transparent, navController)
                    IconButton(onClick = { navController.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Close"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (favoritePerson.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    MyText(text = "No Favorite", fontSize = 22.sp)
                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                items(favoritePerson) {
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