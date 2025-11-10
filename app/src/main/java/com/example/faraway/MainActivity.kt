package com.example.faraway

import NavItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.screen.GuidePanelScreen
import com.example.faraway.ui.screen.MainScreen
import com.example.faraway.ui.screen.PainelDoAnfitriaoScreen
import com.example.faraway.ui.screen.AuthScreen
import com.example.faraway.ui.screen.MyTourScreen
import com.example.faraway.ui.screen.MyReservationScreen
import com.example.faraway.ui.screen.TripsScreen
import com.example.faraway.ui.theme.FarAwayTheme

val travelerNavItems = listOf(
    NavItem(Destinations.EXPLORE_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.TRIPS_ROUTE, Icons.Filled.DateRange, "Viagens"),
    NavItem(Destinations.SOCIAL_ROUTE, Icons.Filled.People, "Social"),
    NavItem(Destinations.CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
    NavItem(Destinations.PROFILE_ROUTE, Icons.Filled.Person, "Perfil")
)

val hostNavItems = listOf(
    NavItem(Destinations.HOST_RESERVATION_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.HOST_RESERVATION_ROUTE, Icons.Filled.CalendarMonth, "Reservas"),
)

val guideNavItems = listOf(
    NavItem(Destinations.GUIDE_DASHBOARD_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.GUIDE_TOURS_ROUTE, Icons.Filled.CalendarMonth, "Tours"),
)

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
        startDestination = Destinations.AUTH_ROUTE
    ) {
        // 1. Rota de Autenticação
        composable(Destinations.AUTH_ROUTE) {
            AuthScreen(navController = navController)
        }

        // 2. Rota de Exploração (Destino do Login)
        composable(Destinations.EXPLORE_ROUTE) {
            MainScreen(navController = navController)
        }

        // 3. Rota de Viagens (Já estava correta)
        composable(Destinations.TRIPS_ROUTE) {
            TripsScreen(navController = navController)
        }

        // 4. Rota do Guia (Destino do Cadastro Guia)
        composable(Destinations.GUIDE_TOURS_ROUTE) {
            MyTourScreen(navController = navController)
        }

        // 5. Rota do Anfitrião (Destino do Cadastro Anfitrião)
        composable(Destinations.HOST_RESERVATION_ROUTE) {
            MyReservationScreen(navController = navController)
        }

        // Adicione as outras rotas (Social, Chat, Perfil)
        composable(Destinations.SOCIAL_ROUTE) { /* SocialScreen(navController) */ }
        composable(Destinations.CHAT_ROUTE) { /* ChatScreen(navController) */ }
        composable(Destinations.PROFILE_ROUTE) { /* ProfileScreen(navController) */ }

        // Painel do Guia
        composable(route = Destinations.GUIDE_DASHBOARD_ROUTE) {
            GuidePanelScreen(navController = navController)
        }

        // Painel do Anfitrião
        composable(route = Destinations.HOST_DASHBOARD_ROUTE) {
            PainelDoAnfitriaoScreen(navController = navController)
        }
    }
}
