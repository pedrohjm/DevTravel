package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.amigosNavItems
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import com.example.faraway.ui.viewmodel.MainViewModel
import com.example.faraway.ui.viewmodel.MainViewModelFactory


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AmigoBottomBar(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true
            val iconColor = if (selected) AccentColor else Color.Gray

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (selected) AccentColor.copy(alpha = 0.1f) else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = iconColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


val PrimaryDark = Color(0xFF007BFF) // Azul escuro original


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AmigosScreen(
    navController: NavController,
    repository: AuthRepository = AuthRepository(),
    factory: MainViewModelFactory = MainViewModelFactory(repository),
    mainViewModel: MainViewModel = viewModel(factory = factory)
) {

    val friends by mainViewModel.friends.collectAsState()


    val socialUsers: List<User> = friends

    var searchText by remember { mutableStateOf("") }


    val gradientBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF113162), // 0%
                Color(0xFF194891), // 50%
                Color(0xFF2364C8)  // 100%
            )
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush) // Aplicando o gradiente
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "AmigosPage", // Título
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Ação de Voltar */ }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Fundo transparente para mostrar o Box com gradiente
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            AmigoBottomBar(
                navController = navController,
                navItems = amigosNavItems,
                startRoute = Destinations.SOCIAL_AMIGOS_ROUTE
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)) // Fundo cinza claro
        ) {

            SocialSearchBar(
                searchText = searchText,
                onSearchTextChanged = { searchText = it }
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(socialUsers) { user ->
                    user?.let {
                        SocialUserCard(
                            user = it.toSocialUser(),
                            modifier = Modifier.clickable {
                                navController.navigate("${Destinations.VIEW_PROFILE_ROUTE}/${it.uid}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmigosScreenPreview() {
    AmigosScreen(navController = rememberNavController())
}