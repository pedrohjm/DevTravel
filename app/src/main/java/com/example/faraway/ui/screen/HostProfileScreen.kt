package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.theme.HostCardBackground
import com.example.faraway.ui.theme.HostGradientEnd
import com.example.faraway.ui.theme.HostGradientStart
import com.example.faraway.ui.theme.HostLightBlue
import com.example.faraway.ui.theme.HostLogoutLightRed
import com.example.faraway.ui.theme.HostLogoutRed
import com.example.faraway.ui.theme.HostTextColor

// -----------------------------------------------------------------
// PLACEHOLDERS PARA COMPONENTES DE NAVEGAÇÃO
// -----------------------------------------------------------------

data class HostNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun HostBottomNavBarPlaceholder(navController: NavController) {
    val hostNavItems = listOf(
        HostNavItem("explore", Icons.Filled.Search, "Explorar"),
        HostNavItem("reservas", Icons.Filled.CalendarMonth, "Reservas"),
        HostNavItem("chat", Icons.AutoMirrored.Filled.Chat, "Chat"),
        HostNavItem("profile", Icons.Filled.Person, "Perfil")
    )

    BottomAppBar(
        containerColor = HostCardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        hostNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "profile",
                onClick = { /* Ação de Navegação */ },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                // CONFIGURAÇÃO DE CORES PARA COMBINAR
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = HostGradientStart, // Usa o Ciano principal no fundo oval
                    selectedIconColor = Color.White,    // Ícone branco para contraste
                    selectedTextColor = HostGradientStart,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

// -----------------------------------------------------------------
// COMPONENTE PRINCIPAL
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White, // Garante fundo branco geral
        bottomBar = {
            HostBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White) // Garante fundo branco na lista
        ) {
            item { HostProfileHeader() }
            item { HostProfileStatsAndInfo() }
            item { HostProfileSettings() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER (COM DEGRADÊ APLICADO)
// -----------------------------------------------------------------

@Composable
fun HostProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // APLICAÇÃO DO DEGRADÊ CIANO -> ESCURO
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        HostGradientStart,
                        HostGradientEnd
                    )
                )
            )
            .padding(bottom = 80.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Ação de Voltar */ }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
            Text(
                text = "Meu Perfil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Ação de Configurações */ }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Configurações",
                    tint = Color.White
                )
            }
        }

        // Info do Anfitrião
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Foto de Perfil Placeholder",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                }
                // Ícone de Câmera
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = "Mudar Foto",
                        tint = HostGradientStart, // Usa a cor principal
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Fátima Alves",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Casa Inteira • 2 Quartos",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Porto, Portugal",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Anfitrião Verificado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green) // Verde mantido para status
                )
                Text(
                    text = "Anfitrião Verificado",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HostAmenityChip("Cozinha")
                HostAmenityChip("WiFi")
                HostAmenityChip("Estacionamento")
            }
        }
    }
}

@Composable
fun HostAmenityChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f)) // Fundo translúcido
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

// -----------------------------------------------------------------
// 2. ESTATÍSTICAS
// -----------------------------------------------------------------

@Composable
fun HostProfileStatsAndInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp)
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HostCardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HostStatItem(
                    icon = Icons.Filled.People,
                    value = "247",
                    label = "Hóspedes",
                    iconColor = HostGradientStart // Cor combinando
                )
                HostStatItem(
                    icon = Icons.Filled.AttachMoney,
                    value = "€ 18.2K",
                    label = "Receita",
                    iconColor = Color(0xFF4CAF50)
                )
                HostStatItem(
                    icon = Icons.Filled.Star,
                    value = "4.8",
                    label = "Avaliação",
                    iconColor = Color(0xFFFFC107)
                )
                HostStatItem(
                    icon = Icons.Filled.TrendingUp,
                    value = "87%",
                    label = "Ocupação",
                    iconColor = HostGradientStart // Cor combinando
                )
            }
        }
    }
}

@Composable
fun HostStatItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HostTextColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = HostTextColor.copy(alpha = 0.6f)
        )
    }
}

// -----------------------------------------------------------------
// 3. CONFIGURAÇÕES
// -----------------------------------------------------------------

@Composable
fun HostProfileSettings() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-40).dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Configurações",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HostTextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(
            icon = Icons.Filled.Home,
            label = "Minha Propriedade",
            iconColor = HostGradientStart,
            backgroundColor = HostLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(
            icon = Icons.Filled.CalendarMonth,
            label = "Disponibilidade",
            iconColor = HostGradientStart,
            backgroundColor = HostLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(
            icon = Icons.Filled.Help,
            label = "Central de Ajuda",
            iconColor = HostGradientStart,
            backgroundColor = HostLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Sair da Conta",
            iconColor = HostLogoutRed,
            backgroundColor = HostLogoutLightRed
        )
    }
}

@Composable
fun HostSettingsItem(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = HostTextColor
                )
            }
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Avançar",
                tint = iconColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HostProfileScreenPreview() {
    MaterialTheme {
        HostProfileScreen(navController = rememberNavController())
    }
}