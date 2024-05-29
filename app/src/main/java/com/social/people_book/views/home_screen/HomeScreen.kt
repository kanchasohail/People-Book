package com.social.people_book.views.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.social.people_book.MainActivity
import com.social.people_book.MainViewModel
import com.social.people_book.R
import com.social.people_book.model.LocalFileStorageRepository
import com.social.people_book.model.room_database.Tag
import com.social.people_book.model.util.isScrollingUp
import com.social.people_book.navigation.Screens
import com.social.people_book.ui.common_views.CenterBox
import com.social.people_book.ui.layout.LoadingIndicator
import com.social.people_book.ui.layout.MyText
import com.social.people_book.views.auth_screen.fetchAndSaveDataFromFirebase
import com.social.people_book.views.home_screen.components.ItemCard
import com.social.people_book.views.home_screen.side_drawer.DrawerContent
import com.social.people_book.views.home_screen.side_drawer.DrawerHeader
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val localFileStorage = LocalFileStorageRepository(context)

    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black

    val gridState = rememberLazyStaggeredGridState()


//    val persons by viewModel.persons.collectAsState()
    val persons = viewModel.filteredPersons.collectAsState().value
    val isSearching by viewModel.isSearching.collectAsState()

    val localCoroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

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
                    mainViewModel = mainViewModel
                )
            }
        }) {

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarBackGroundColor,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
//                            localCoroutineScope.launch {
//                                drawerState.open()
//                            }
                            navController.navigate(Screens.SettingsScreen.route)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "More",
                                tint = appBarTextColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    title = {
                        MyText(text = "People Book", color = appBarTextColor)
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screens.SearchScreen.route)

//                            val auth = Firebase.auth
//                            val db = Firebase.firestore
//                            val storage = Firebase.storage
//                            val personDao = MainActivity.db.personDao()
//
//                            auth.currentUser?.uid?.let {
//                                fetchAndSaveDataFromFirebase(
//                                    db = db,
//                                    storage,
//                                    userId = it,
//                                    personDao = personDao,
//                                    localFileStorageRepository = LocalFileStorageRepository(context),
//                                    viewModelScope = viewModel.viewModelScope
//                                )
//                            }

                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search_icon),
                                contentDescription = "search",
                                tint = appBarTextColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate(Screens.FavoritesScreen.route)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_filled),
                                contentDescription = "Favorites",
                                tint = appBarTextColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { MyText(text = "Add", fontSize = 16.sp) },
                    icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add") },
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

                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy((-12).dp),
                    ) {
//                        viewModel.tags.forEach { tagItem ->
//                            TagsChip(
//                                chipText = tagItem,
//                                textColor = textColor,
//                                isSelected = tagItem == viewModel.selectedTagItem
//                            ) {
//                                viewModel.selectedTagItem = tagItem
//                            }
//                        }

                        viewModel.tagsList.map {
                            if (it != Tag.None) {
                                TagsChip(
                                    chipText = it.name,
                                    textColor = textColor,
                                    isSelected = it == viewModel.selectedTagItem,
                                    isDarkMode = isDarkMode
                                ) {
                                    if (viewModel.selectedTagItem == it) {
                                        viewModel.selectedTagItem = null
                                        viewModel.filterPerson(Tag.None)
                                        return@TagsChip
                                    }
                                    viewModel.selectedTagItem = it
                                    viewModel.filterPerson(it)
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
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Adaptive(150.dp),
                            state = gridState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(persons) {
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
        }
    }
}


@Composable
private fun TagsChip(
    modifier: Modifier = Modifier,
    chipText: String,
    textColor: Color,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val borderColor: Color = if (isSelected) Color.Gray else Color.Transparent

//    AssistChip(
    FilterChip(
        onClick = onClick,
        label = {
            if (isSelected) {

                MyText(
                    text = chipText,
//                    color = if (isSelected) Color.Black else textColor,
                )
            } else {
                MyText(
                    text = chipText,
                    color = textColor,
                )
            }
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (isSelected) Color.White else if(isDarkMode) Color.Gray.copy(.3f) else Color.LightGray.copy(.3f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = false,
            selected = isSelected,
            borderColor = borderColor
        ),
        modifier = modifier.padding(4.dp)
    )
}
