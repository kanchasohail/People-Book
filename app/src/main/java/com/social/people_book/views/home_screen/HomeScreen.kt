package com.social.people_book.views.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.social.people_book.ui.layout.MyDivider
import com.social.people_book.ui.layout.MyText
import com.social.people_book.views.side_drawer.DrawerContent
import com.social.people_book.views.side_drawer.DrawerHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
//fun HomeScreen(navController: NavController, isDarkMode: Boolean) {
fun HomeScreen(
    navController: NavController = rememberNavController(),
    isDarkMode: Boolean = false
) {
    val appBarBackGroundColor =
        if (isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary
    val appBarTextColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
    val textColor = if (isDarkMode) Color.White else Color.Black


    val localCoroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var isTagExpanded by remember {
        mutableStateOf(false)
    }

    // Create a list of items to display in the grid
    val items = (1..50).map { "Item $it" }


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
                        Text(
                            text = "People Book",
                            color = appBarTextColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
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
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "More",
                                tint = appBarTextColor,
                                modifier = Modifier.size(35.dp)
                            )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(text = "Search by tags", textColor = textColor)
                    IconButton(onClick = {
                        isTagExpanded = !isTagExpanded
                    }) {
                        Icon(
                            imageVector = if (isTagExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "More",
                            tint = textColor,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

                if (isTagExpanded) {
                    LazyRow {
                        items(5) {
                            AssistChip(
                                onClick = { /*TODO*/ },
                                label = { MyText(text = "Friend") },
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                LazyVerticalStaggeredGrid(
                    state = rememberLazyStaggeredGridState(),
                    columns = StaggeredGridCells.Adaptive(150.dp),
                ) {
                    items(items.size) {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(
                                6.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = items[it])
                            }
                        }

                    }
                }

            }
        }
    }
}