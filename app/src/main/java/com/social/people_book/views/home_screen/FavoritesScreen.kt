package com.social.people_book.views.home_screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.social.people_book.model.LocalFileStorageRepository
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.layout.BackButtonArrow
import com.social.people_book.ui.layout.MyText
import com.social.people_book.views.home_screen.components.ItemCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FavoritesScreen(
    navController: NavHostController,
    animatedVisibilityScope: AnimatedContentScope,
    viewModel: HomeScreenViewModel,
    isDarkMode: Boolean
) {

    val context = LocalContext.current
    val localFileStorage = LocalFileStorageRepository(context)

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary

    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val favoritePerson = viewModel.getFavoritePersons()

    Scaffold(
        topBar = {
            TopAppBar(title = { MyText(text = "Favorites") }, navigationIcon = {
                BackButtonArrow(
                    iconColor = appBarTextColor,
                    navController = navController
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoritePerson) {
                    ItemCard(
                        animatedVisibilityScope = animatedVisibilityScope,
                        localFileStorageRepository = localFileStorage,
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