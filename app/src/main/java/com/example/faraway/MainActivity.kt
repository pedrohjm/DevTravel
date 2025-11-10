package com.example.faraway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.screen.GuidePanelScreen
import com.example.faraway.ui.screen.PainelDoAnfitriaoScreen
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
        startDestination = Destinations.GUIDE_DASHBOARD_ROUTE
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


        // Painel do Guia
        composable(route = Destinations.GUIDE_DASHBOARD_ROUTE) {
            GuidePanelScreen()
        }

        // Painel do Anfitrião
        composable(route = Destinations.HOST_DASHBOARD_ROUTE) {
            PainelDoAnfitriaoScreen() // <-- NOME CORRETO
        }
    }

}
