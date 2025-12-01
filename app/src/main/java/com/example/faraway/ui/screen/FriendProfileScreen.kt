// ui/screen/FriendProfileScreen.kt
package com.example.faraway.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com FriendProfile para evitar conflito)
// -----------------------------------------------------------------
val FriendProfilePrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val FriendProfileAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val FriendProfileLightBlue = Color(0xFFE0F7FA) // Azul claro para fundo de cards
val FriendProfileTextColor = Color(0xFF333333) // Cor de texto padrão
val FriendProfileRed = Color(0xFFD32F2F) // Vermelho para o botão de Sair da Conta

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS E NAVEGAÇÃO
// -----------------------------------------------------------------

data class FriendProfileNavItem( // RENOMEADO
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Placeholder para BottomNavBar (Componente)
@Composable
fun FriendProfileBottomNavBarPlaceholder(navController: NavController) { // RENOMEADO
    val navItems = listOf(
        FriendProfileNavItem("explore", Icons.Filled.Search, "Explorar"),
        FriendProfileNavItem("chat", Icons.Filled.Chat, "Chat"),
        FriendProfileNavItem("perfil", Icons.Filled.Person, "Perfil")
    )

    BottomAppBar(
        containerColor = Color.White,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "perfil", // Perfil selecionado
                onClick = { /* Ação de Navegação */ },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (item.route == "perfil") FriendProfileAccentColor else Color.Gray
                    )
                },
                label = { Text(item.label, fontSize = 10.sp) }
            )
        }
    }
}

// -----------------------------------------------------------------
// COMPONENTE PRINCIPAL
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(navController: NavController) { // RENOMEADO
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            FriendProfileBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState) // ROLAGEM ADICIONADA
        ) {
            FriendProfileHeader()
            FriendProfileStatsCard()
            FriendProfileInterests()
            FriendProfileSettings()
            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento final
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun FriendProfileHeader() { // RENOMEADO
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendProfilePrimaryBlue)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Ação de Voltar */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Text(
                text = "Meu Perfil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Ação de Configurações */ }) {
                Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = Color.White)
            }
        }

        // Profile Picture
        Box(contentAlignment = Alignment.BottomEnd) {
            // Placeholder para a imagem de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White, modifier = Modifier.size(60.dp))
            }

            // Camera Icon Overlay
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, FriendProfilePrimaryBlue, CircleShape)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = "Mudar Foto", tint = FriendProfilePrimaryBlue, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // User Info
        Text(
            text = "Marcos Lima",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Apaixonado em aprender coisas novas",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.LocationOn, contentDescription = "Localização", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Lisboa, Portugal",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Verification Badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Green.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Verified, contentDescription = "Verificado", tint = Color.Green, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Perfil Verificado", color = Color.Green, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Language Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FriendProfileLanguageChip("Português", isSelected = true)
            FriendProfileLanguageChip("Inglês", isSelected = false)
            FriendProfileLanguageChip("Espanhol", isSelected = false)
        }
    }
}

@Composable
fun FriendProfileLanguageChip(label: String, isSelected: Boolean) { // RENOMEADO
    val color = if (isSelected) FriendProfileAccentColor else Color.White.copy(alpha = 0.8f)
    Text(
        text = label,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .border(1.dp, color, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// -----------------------------------------------------------------
// 2. STATS CARD
// -----------------------------------------------------------------

@Composable
fun FriendProfileStatsCard() { // RENOMEADO
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-20).dp), // Subindo o card para sobrepor o header
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FriendProfileStatItem(icon = Icons.Outlined.People, value = "156", label = "Conexões", color = FriendProfileAccentColor)
                FriendProfileStatItem(icon = Icons.Filled.Flag, value = "28", label = "Países", color = FriendProfileAccentColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FriendProfileStatItem(icon = Icons.Filled.AttachMoney, value = "156", label = "Encontros", color = FriendProfileAccentColor)
                FriendProfileStatItem(icon = Icons.Filled.Star, value = "4.9", label = "Avaliação", color = FriendProfileAccentColor)
            }
        }
    }
}

@Composable
fun FriendProfileStatItem(icon: ImageVector, value: String, label: String, color: Color) { // RENOMEADO
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FriendProfileTextColor)
        }
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

// -----------------------------------------------------------------
// 3. INTERESTS
// -----------------------------------------------------------------

@Composable
fun FriendProfileInterests() { // RENOMEADO
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Meus Interesses",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = FriendProfileTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FriendProfileInterestChip("Cultura")
            FriendProfileInterestChip("Música")
            FriendProfileInterestChip("Fórmula 1")
        }
    }
}

@Composable
fun FriendProfileInterestChip(label: String) { // RENOMEADO
    Text(
        text = label,
        color = Color(0xFF8200DB), // Cor roxa
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF8200DB).copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// -----------------------------------------------------------------
// 4. SETTINGS
// -----------------------------------------------------------------

@Composable
fun FriendProfileSettings() { // RENOMEADO
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = "Configurações",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = FriendProfileTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Minhas Conexões
        FriendProfileSettingsItem(
            icon = Icons.Outlined.People,
            label = "Minhas Conexões",
            iconColor = FriendProfileAccentColor,
            backgroundColor = FriendProfileLightBlue
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Interesses e lugares visitados
        FriendProfileSettingsItem(
            icon = Icons.Outlined.FavoriteBorder,
            label = "Interesses e lugares visitados",
            iconColor = FriendProfileAccentColor,
            backgroundColor = FriendProfileLightBlue
        )
        Spacer(modifier = Modifier.height(8.dp))

        // NOVO BOTÃO: Sair da Conta
        FriendProfileSettingsItem(
            icon = Icons.Filled.Logout,
            label = "Sair da Conta",
            iconColor = FriendProfileRed,
            backgroundColor = FriendProfileRed.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun FriendProfileSettingsItem(icon: ImageVector, label: String, iconColor: Color, backgroundColor: Color) { // RENOMEADO
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (iconColor == FriendProfileRed) FriendProfileRed else FriendProfileTextColor
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Avançar",
                tint = if (iconColor == FriendProfileRed) FriendProfileRed else Color.Gray
            )
        }
    }
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun FriendProfileScreenPreview() { // RENOMEADO
    MaterialTheme {
        FriendProfileScreen(navController = rememberNavController())
    }
}
