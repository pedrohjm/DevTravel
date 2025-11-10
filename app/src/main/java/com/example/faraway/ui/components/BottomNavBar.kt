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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.faraway.Destinations // Importe o seu objeto de rotas
import com.example.faraway.ui.theme.AccentColor // Sua cor de destaque

// ui.components/BottomNavBar.kt

// Defina uma classe de dados para os itens da barra de navegação
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(
    navController: NavController,
    // NOVO PARÂMETRO: Lista de itens a serem exibidos
    navItems: List<NavItem>,
    // NOVO PARÂMETRO: Rota inicial do NavHost (para o popUpTo)
    startRoute: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        // Itera sobre a lista de itens passada como parâmetro
        navItems.forEach { item ->
            // A lógica de seleção agora verifica se a rota atual começa com a rota do item
            // Isso é útil para telas aninhadas (ex: /reservas/detalhe)
            val selected = currentRoute?.startsWith(item.route) == true
            val iconColor = if (selected) AccentColor else Color.Gray

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // Navega para a rota do item
                    navController.navigate(item.route) {
                        // popUpTo agora usa a rota inicial passada como parâmetro
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Tamanho do item para o círculo
                            .background(
                                color = if (selected) AccentColor.copy(alpha = 0.1f) else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor, // Usa a cor definida acima
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    // IMPLEMENTAÇÃO DO RÓTULO
                    Text(
                        text = item.label,
                        color = iconColor // Usa a mesma cor para o texto
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
