package com.example.faraway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.screen.MainScreen
import com.example.faraway.ui.screen.TripsScreen
import com.example.faraway.ui.theme.FarAwayTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarAwayTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.EXPLORE_ROUTE
    ) {
        composable(Destinations.EXPLORE_ROUTE) {
            MainScreen(navController = navController)
        }

        composable(Destinations.TRIPS_ROUTE) {
            TripsScreen(navController = navController)
        }

        // Adicione as outras rotas (Social, Chat, Perfil)
        composable(Destinations.SOCIAL_ROUTE) { /* SocialScreen(navController) */ }
        composable(Destinations.CHAT_ROUTE) { /* ChatScreen(navController) */ }
        composable(Destinations.PROFILE_ROUTE) { /* ProfileScreen(navController) */ }
    }
}
