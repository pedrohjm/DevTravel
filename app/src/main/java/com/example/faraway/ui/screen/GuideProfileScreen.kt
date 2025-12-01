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
import com.example.faraway.ui.theme.GuideAccentColor
import com.example.faraway.ui.theme.GuideCardBackground
import com.example.faraway.ui.theme.GuideLightBlue
import com.example.faraway.ui.theme.GuideLogoutLightRed
import com.example.faraway.ui.theme.GuideLogoutRed
import com.example.faraway.ui.theme.GuidePrimaryBlue
import com.example.faraway.ui.theme.GuideTextColor
import com.example.faraway.ui.theme.navSelectedColor

// -----------------------------------------------------------------
// PLACEHOLDERS
// -----------------------------------------------------------------
data class GuideNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun GuideBottomNavBarPlaceholder(navController: NavController) {
    val guideNavItems = listOf(
        GuideNavItem("explore", Icons.Filled.Search, "Explorar"),
        GuideNavItem("trips", Icons.Filled.DateRange, "Viagens"),
        GuideNavItem("social", Icons.Filled.People, "Social"),
        GuideNavItem("chat", Icons.AutoMirrored.Filled.Chat, "Chat"),
        GuideNavItem("profile", Icons.Filled.Person, "Perfil")
    )


    BottomAppBar(
        containerColor = GuideCardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        guideNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "profile",
                onClick = { /* Ação de Navegação */ },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                // --- MUDANÇA DA COR AQUI ---
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = navSelectedColor, // O fundo oval
                    selectedIconColor = Color.White,   // Ícone branco para contraste
                    selectedTextColor = navSelectedColor, // Texto da mesma cor do fundo
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
fun GuideProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            GuideBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item { GuideProfileHeader() }
            item { GuideProfileStatsAndInfo() }
            item { GuideProfileSettings() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun GuideProfileHeader() {
    val lighterBlueTop = Color(0xFF2E548A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lighterBlueTop,
                        GuidePrimaryBlue
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

        // Info do Guia
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        .background(GuideAccentColor)
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
                text = "Gabriel Pereira",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tours Históricos e Culturais",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lisboa, Portugal",
                color = GuideAccentColor,
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
                    text = "Guia Verificado",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // IDIOMAS
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GuideLanguageChip("Português")
                GuideLanguageChip("Inglês")
                GuideLanguageChip("Espanhol")
            }
        }
    }
}

@Composable
fun GuideLanguageChip(label: String) {
    Text(
        text = label,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
}

// -----------------------------------------------------------------
// 2. ESTATÍSTICAS
// -----------------------------------------------------------------

@Composable
fun GuideProfileStatsAndInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp)
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GuideCardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GuideStatItem(
                    icon = Icons.Filled.People,
                    value = "247",
                    label = "Conexões",
                    iconColor = GuideAccentColor
                )
                GuideStatItem(
                    icon = Icons.Filled.Star,
                    value = "4.9",
                    label = "Avaliação",
                    iconColor = Color(0xFFFFC107)
                )
                GuideStatItem(
                    icon = Icons.Filled.AttachMoney,
                    value = "€12.5K",
                    label = "Ganhos",
                    iconColor = Color(0xFF4CAF50)
                )
                GuideStatItem(
                    icon = Icons.Filled.CheckCircle,
                    value = "90%",
                    label = "Serviços",
                    iconColor = GuideAccentColor
                )
            }
        }
    }
}

@Composable
fun GuideStatItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
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
            color = GuideTextColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = GuideTextColor.copy(alpha = 0.6f)
        )
    }
}

// -----------------------------------------------------------------
// 3. CONFIGURAÇÕES
// -----------------------------------------------------------------

@Composable
fun GuideProfileSettings() {
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
            color = GuideTextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(
            icon = Icons.Filled.DateRange,
            label = "Minha disponibilidade",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(
            icon = Icons.Filled.Description,
            label = "Documentos Profissionais",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(
            icon = Icons.Filled.Help,
            label = "Central de Ajuda",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Sair da Conta",
            iconColor = GuideLogoutRed,
            backgroundColor = GuideLogoutLightRed
        )
    }
}

@Composable
fun GuideSettingsItem(
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
                    color = GuideTextColor
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
fun GuideProfileScreenPreview() {
    MaterialTheme {
        GuideProfileScreen(navController = rememberNavController())
    }
}