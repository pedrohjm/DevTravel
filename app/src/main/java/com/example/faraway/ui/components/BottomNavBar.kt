// ui.components/BottomNavBar.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.faraway.Destinations // Importe o seu objeto de rotas
import com.example.faraway.ui.theme.AccentColor // Sua cor de destaque

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Definição dos itens da barra de navegação
    val items = listOf(
        Triple(Destinations.EXPLORE_ROUTE, Icons.Filled.Search, "Explorar"),
        Triple(Destinations.TRIPS_ROUTE, Icons.Filled.DateRange, "Viagens"),
        Triple(Destinations.SOCIAL_ROUTE, Icons.Filled.People, "Social"),
        Triple(Destinations.CHAT_ROUTE, Icons.AutoMirrored.Filled.Chat, "Chat"),
        Triple(Destinations.PROFILE_ROUTE, Icons.Filled.Person, "Perfil")
    )

    NavigationBar(
        containerColor = Color.White, // Fundo branco
        tonalElevation = 0.dp // Remove a sombra padrão
    ) {
        items.forEach { (route, icon, label) ->
            val selected = currentRoute == route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    // LÓGICA PARA O CÍRCULO NO ITEM SELECIONADO
                    Box(
                        modifier = Modifier
                            .size(40.dp) // Tamanho do círculo
                            .background(
                                color = if (selected) AccentColor.copy(alpha = 0.1f) else Color.Transparent, // Fundo com 10% de opacidade
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selected) AccentColor else Color.Gray, // Cor do ícone
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        color = if (selected) AccentColor else Color.Gray, // Cor do texto
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                // Remove o efeito de clique padrão para que o nosso Box controle o visual
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Remove o indicador padrão
                )
            )
        }
    }
}
