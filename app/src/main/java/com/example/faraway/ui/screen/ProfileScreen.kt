package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.faraway.ui.theme.CardBackground
import com.example.faraway.ui.theme.InterestTextBlue
import com.example.faraway.ui.theme.LightBlue
import com.example.faraway.ui.theme.LogoutLightRed
import com.example.faraway.ui.theme.LogoutRed
import com.example.faraway.ui.theme.PrimaryBlue
import com.example.faraway.ui.theme.TextColor

// CORES LOCAIS (Para evitar erros de importação)
val AccentColor2 = Color(0xFF0093A8) // Azul Turquesa/Ciano (Seu tom preferido)
val InterestBgCyan = AccentColor2

// -----------------------------------------------------------------
// PLACEHOLDERS
// -----------------------------------------------------------------
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBarPlaceholder(navController: NavController) {
    val travelerNavItems = listOf(
        NavItem("explore", Icons.Filled.Search, "Explorar"),
        NavItem("trips", Icons.Filled.DateRange, "Viagens"),
        NavItem("social", Icons.Filled.People, "Social"),
        NavItem("chat", Icons.AutoMirrored.Filled.Chat, "Chat"),
        NavItem("profile", Icons.Filled.Person, "Perfil")
    )

    BottomAppBar(
        containerColor = CardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        travelerNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "profile",
                onClick = { /* Ação de Navegação */ },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AccentColor2,
                    selectedIconColor = Color.White,
                    selectedTextColor = AccentColor2,
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
fun ProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item { ProfileHeader() }
            item { ProfileStatsAndInterests() }
            item { ProfileSettings(navController = navController) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryBlue,
                        AccentColor2
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

        // Info do Usuário
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
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AccentColor2)
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = "Mudar Foto",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Gabriel Ferreira",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Apaixonado por futebol, viagens e degustar bebidas.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lisboa, Portugal",
                color = LightBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Text(
                    text = "Perfil Verificado",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Idiomas
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LanguageChip("Português")
                LanguageChip("Inglês")
                LanguageChip("Espanhol")
            }
        }
    }
}

@Composable
fun LanguageChip(label: String) {
    Text(
        text = label,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
}

// -----------------------------------------------------------------
// 2. ESTATÍSTICAS E INTERESSES
// -----------------------------------------------------------------

@Composable
fun ProfileStatsAndInterests() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp)
            .padding(horizontal = 16.dp)
    ) {
        // Card de Estatísticas
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    icon = Icons.Filled.People,
                    value = "156",
                    label = "Conexões",
                    iconColor = AccentColor2
                )
                StatItem(
                    icon = Icons.Filled.LocationOn,
                    value = "28",
                    label = "Países",
                    iconColor = AccentColor2
                )
                // --- MUDANÇA AQUI: ÍCONE DE ENCONTROS ---
                StatItem(
                    icon = Icons.Filled.Handshake, // Trocado de AttachMoney ($) para Handshake (🤝)
                    value = "156",
                    label = "Encontros",
                    iconColor = AccentColor2 // Trocado de Verde para Azul Turquesa
                )
                // ----------------------------------------
                StatItem(
                    icon = Icons.Filled.Star,
                    value = "4.9",
                    label = "Avaliação",
                    iconColor = Color(0xFFFFC107)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Meus Interesses",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chips de Interesses
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            InterestChip("Futebol")
            InterestChip("Culinária")
            InterestChip("Fotografia")
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
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
            color = TextColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextColor.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun InterestChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(InterestBgCyan.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = InterestTextBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// -----------------------------------------------------------------
// 3. CONFIGURAÇÕES
// -----------------------------------------------------------------

@Composable
fun ProfileSettings(navController: NavController) {
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
            color = TextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(
            icon = Icons.Filled.People,
            label = "Minhas Conexões",
            iconColor = AccentColor2,
            backgroundColor = LightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(
            icon = Icons.Filled.FavoriteBorder,
            label = "Interesses e Hobbies",
            iconColor = AccentColor2,
            backgroundColor = LightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Sair da Conta",
            iconColor = LogoutRed,
            backgroundColor = LogoutLightRed,
            onClick = {
                navController.navigate("auth") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable{onClick()}
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
                    color = TextColor
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
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}