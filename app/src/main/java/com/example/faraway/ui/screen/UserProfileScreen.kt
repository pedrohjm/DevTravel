package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com UserProfile para evitar conflito)
// -----------------------------------------------------------------
val UserProfilePrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val UserProfileAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val UserProfileGradientStart = Color(0xFF42A5F5) // Azul claro para o gradiente do botão
val UserProfileGradientEnd = Color(0xFF1E88E5) // Azul escuro para o gradiente do botão
val UserProfileDescriptionBackground = Color(0xFFF5F5F5) // Fundo da caixa de descrição
val UserProfileTextColor = Color(0xFF333333) // Cor de texto padrão

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS E NAVEGAÇÃO
// -----------------------------------------------------------------

data class UserProfileNavItem( // RENOMEADO
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Placeholder para BottomNavBar (Componente)
@Composable
fun UserProfileBottomNavBarPlaceholder(navController: NavController) { // RENOMEADO
    val navItems = listOf(
        UserProfileNavItem("explore", Icons.Filled.Search, "Explorar"),
        UserProfileNavItem("trips", Icons.Filled.CalendarToday, "Viagens"),
        UserProfileNavItem("social", Icons.Filled.People, "Social"),
        UserProfileNavItem("chat", Icons.Filled.ChatBubbleOutline, "Chat"),
        UserProfileNavItem("perfil", Icons.Filled.Person, "Perfil")
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
                        tint = if (item.route == "perfil") UserProfileAccentColor else Color.Gray
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
fun UserProfileScreen(navController: NavController) { // RENOMEADO
    Scaffold(
        bottomBar = {
            UserProfileBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()), // Rolagem para garantir visibilidade
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserProfileHeader(navController)
            UserProfileDescriptionBox()
            UserProfileInterestChips()
            UserProfileViewProfileButton(navController, onClick = {navController.navigate("profile") {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }})
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun UserProfileHeader(navController: NavController) { // RENOMEADO
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UserProfilePrimaryBlue)
            .padding(bottom = 60.dp), // Espaço para a foto de perfil
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

        }
    }

    // Profile Picture, Name and Location (Overlay)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-40).dp), // Move para cima para sobrepor o header
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(4.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder para a imagem de perfil
            Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.Gray, modifier = Modifier.size(60.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // User Info
        Text(
            text = "Gabriel Ferreira",
            color = UserProfileTextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Lisboa, Portugal",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

// -----------------------------------------------------------------
// 2. DESCRIPTION BOX
// -----------------------------------------------------------------

@Composable
fun UserProfileDescriptionBox() { // NOVO COMPONENTE
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset(y = (-20).dp), // Ajuste de posição após o header
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UserProfileDescriptionBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = "Apaixonado por futebol, viagens e degustar bebidas. Amo conhecer novas culturas e compartilhar experiências únicas. Sempre busco aventuras e momentos especiais. Estou disposto em te ajudar :)",
            modifier = Modifier.padding(16.dp),
            color = UserProfileTextColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

// -----------------------------------------------------------------
// 3. INTEREST CHIPS
// -----------------------------------------------------------------

@Composable
fun UserProfileInterestChips() { // NOVO COMPONENTE
    val interests = listOf("Futebol", "Culinária", "Viagens", "Música", "Fotografia")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset(y = (-10).dp), // Ajuste de posição
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Primeira linha de chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            interests.take(4).forEach { interest ->
                UserProfileInterestChip(interest)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Segunda linha de chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            interests.drop(4).forEach { interest ->
                UserProfileInterestChip(interest)
            }
        }
    }
}

@Composable
fun UserProfileInterestChip(label: String) { // NOVO COMPONENTE
    Text(
        text = label,
        color = UserProfileTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

// -----------------------------------------------------------------
// 4. VIEW PROFILE BUTTON
// -----------------------------------------------------------------

@Composable
fun UserProfileViewProfileButton(navController: NavController,onClick:() -> Unit) { // NOVO COMPONENTE
    Button(
        onClick = {navController.navigate("perfil")
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(50.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(

            modifier = Modifier
                .fillMaxSize()
                .clickable{onClick()}
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(UserProfileGradientStart, UserProfileGradientEnd)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ver Perfil",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() { // RENOMEADO
    MaterialTheme {
        UserProfileScreen(navController = rememberNavController())
    }
}