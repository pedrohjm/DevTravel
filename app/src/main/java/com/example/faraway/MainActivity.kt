package com.example.faraway

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
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.screen.AmigosScreen
import com.example.faraway.ui.screen.GuidePanelScreen
import com.example.faraway.ui.screen.MainScreen
import com.example.faraway.ui.screen.PainelDoAnfitriaoScreen
import com.example.faraway.ui.screen.AuthScreen
import com.example.faraway.ui.screen.ChatScreen
import com.example.faraway.ui.screen.FriendProfileScreen
import com.example.faraway.ui.screen.GuideChatScreen
import com.example.faraway.ui.screen.GuideMessageScreen
import com.example.faraway.ui.screen.GuideProfileScreen
import com.example.faraway.ui.screen.HostChatScreen
import com.example.faraway.ui.screen.HostMessageScreen
import com.example.faraway.ui.screen.HostProfileScreen
import com.example.faraway.ui.screen.MyTourScreen
import com.example.faraway.ui.screen.MyReservationScreen
import com.example.faraway.ui.screen.NavItem
import com.example.faraway.ui.screen.ProfileScreen
import com.example.faraway.ui.screen.SignUpScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.viewmodel.AuthViewModelFactory
import com.example.faraway.ui.screen.SocialScreen
import com.example.faraway.ui.screen.TripsScreen
import com.example.faraway.ui.screen.UserProfileScreen
import com.example.faraway.ui.theme.FarAwayTheme
import com.google.firebase.FirebaseApp

// Listas de Navegação
val travelerNavItems = listOf(
    NavItem(Destinations.EXPLORE_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.TRIPS_ROUTE, Icons.Filled.DateRange, "Viagens"),
    NavItem(Destinations.SOCIAL_ROUTE, Icons.Filled.People, "Social"),
    NavItem(Destinations.CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
    NavItem(Destinations.PROFILE_ROUTE, Icons.Filled.Person, "Perfil")
)

val hostNavItems = listOf(
    NavItem(Destinations.HOST_DASHBOARD_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.HOST_RESERVATION_ROUTE, Icons.Filled.DateRange, "Reservas"),
    NavItem(Destinations.HOST_CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
    NavItem(Destinations.HOST_PERFIL_ROUTE, Icons.Filled.Person, "Perfil")
)

val guideNavItems = listOf(
    NavItem(Destinations.GUIDE_DASHBOARD_ROUTE, Icons.Filled.Search, "Explorar"),
    NavItem(Destinations.GUIDE_TOURS_ROUTE, Icons.Filled.CalendarMonth, "Tours"),
    NavItem(Destinations.GUIDE_CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
    NavItem(Destinations.GUIDE_PROFILE_ROUTE, Icons.Filled.Person, "Perfil")
)

val amigosNavItems = listOf(
    NavItem(Destinations.SOCIAL_AMIGOS_ROUTE, Icons.Default.Search, "Social"),
    NavItem(Destinations.SOCIAL_CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
    NavItem(Destinations.SOCIAL_PROFILE_ROUTE, Icons.Default.Person, "Perfil")
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
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

    // CRIAÇÃO DO VIEWMODEL NO ESCOPO MAIS ALTO
    val authRepository = remember { AuthRepository() }
    val factory = remember(authRepository) { AuthViewModelFactory(authRepository) }
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    // A lógica de startDestination baseada na role do usuário logado deve ser feita aqui
    // Mas para simplificar, vamos manter a rota inicial como AUTH_ROUTE e garantir que o ViewModel seja compartilhado.

    NavHost(
        navController = navController,
        // startDestination = Destinations.SIGN_UP_ROUTE
        startDestination = Destinations.AUTH_ROUTE
    ) {
        // 1. Rota de Autenticação
        composable(Destinations.AUTH_ROUTE) {
            AuthScreen(navController = navController)
        }

        // Painel Cadastro
        composable(route = Destinations.SIGN_UP_ROUTE) {
            SocialScreen(navController = navController)
        }

        // 2. Rota de Exploração (Destino do Login)
        composable(Destinations.EXPLORE_ROUTE) {
            MainScreen(navController = navController)
        }

        // ROTA DE CADASTRO ---
        composable(
            route = Destinations.SIGN_UP_ROUTE,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "Membro"
            SignUpScreen(navController = navController, role = role)
        }

        // 3. Rota de Viagens
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

        composable(Destinations.HOST_CHAT_ROUTE){
            HostChatScreen(navController = navController)
        }

        composable(Destinations.HOST_PERFIL_ROUTE){
            HostProfileScreen(navController = navController)
        }

        // Painel do Anfitrião
        composable(route = Destinations.HOST_DASHBOARD_ROUTE) {
            PainelDoAnfitriaoScreen(navController = navController)
        }

        // Placeholders para Chat e Perfil (ainda vazios)
        composable(Destinations.CHAT_ROUTE) {
            ChatScreen(navController)
        }
        composable(Destinations.PROFILE_ROUTE) { ProfileScreen(navController = navController) }

        // --- Rota do Guia ---
        composable(route = Destinations.GUIDE_DASHBOARD_ROUTE) {
            // ALTERADO: Passa a instância única do AuthViewModel
            GuidePanelScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(route = Destinations.GUIDE_TOURS_ROUTE) {
            MyTourScreen(navController = navController)
        }

        composable(route = Destinations.GUIDE_CHAT_ROUTE) {
            GuideChatScreen(navController = navController)
        }

        composable(route = Destinations.GUIDE_PROFILE_ROUTE) {
            GuideProfileScreen(navController = navController)
        }
        // --- Rota do Guia ---

        // --- Rota Viajante ---
        composable(route = Destinations.SOCIAL_ROUTE) {
            SocialScreen(navController = navController)
        }


        // ---- Rota Amigos ---
        composable(Destinations.SOCIAL_AMIGOS_ROUTE) {
            AmigosScreen(navController = navController)
        }
        composable(Destinations.SOCIAL_PROFILE_ROUTE) {
            FriendProfileScreen(navController = navController)
        }

        composable(Destinations.SOCIAL_CHAT_ROUTE) {
            //AuthScreen(navController = navController)
        }



        composable(Destinations.GUIDE_MESSAGE_SCREEN){
            GuideMessageScreen(navController = navController)
        }

        composable(Destinations.HOST_MESSAGE_SCREEN){
            HostMessageScreen(navController = navController)
        }

        composable(Destinations.CONFIG_ROUTE){
            UserProfileScreen(navController = navController)
        }


    }
}
