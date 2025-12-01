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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.travelerNavItems

// -----------------------------------------------------------------
// CORES AUXILIARES (Baseado no design)
// -----------------------------------------------------------------
val PrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val AccentColor = Color(0xFF00BCD4) // Cor de destaque (Turquesa/Ciano)
val LightBlue = Color(0xFFE0F7FA) // Azul claro para os cards de configuração
val CardBackground = Color(0xFFFFFFFF) // Fundo branco para cards
val TextColor = Color(0xFF333333) // Cor de texto padrão
val LogoutRed = Color(0xFFE57373) // Vermelho para o botão de Sair
val LogoutLightRed = Color(0xFFFFEBEE) // Vermelho claro para o fundo do botão de Sair
val InterestPurple = Color(0xFF8200DB) // Cor para o texto dos chips de interesse

// -----------------------------------------------------------------
// PLACEHOLDERS PARA COMPONENTES DE NAVEGAÇÃO (Para evitar erros de referência)
// -----------------------------------------------------------------

// Placeholder para NavItem (data class)
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
// Placeholder para BottomNavBar (Componente)
@Composable
fun BottomNavBarPlaceholder(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    BottomAppBar(
        containerColor = CardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "profile",
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
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
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBarPlaceholder(navController = navController, navItems = travelerNavItems, startRoute = "profile")
        }
    ) { paddingValues ->
        // LazyColumn garante que a tela inteira seja rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { ProfileHeader() }
            item { ProfileStatsAndInterests() }
            item { ProfileSettings(navController = navController) }
            item { Spacer(modifier = Modifier.height(32.dp)) } // Espaço extra no final
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER (Cabeçalho)
// -----------------------------------------------------------------

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            .padding(bottom = 80.dp) // Espaço para o card de estatísticas
    ) {
        // Top Bar (Voltar e Configurações)
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

        // Foto de Perfil e Informações
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil com Câmera (Placeholder)
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
                        .background(AccentColor)
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

            // Nome
            Text(
                text = "Gabriel Ferreira",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descrição
            Text(
                text = "Apaixonado por futebol, viagens e degustar bebidas.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Localização
            Text(
                text = "Lisboa, Portugal",
                color = AccentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Perfil Verificado
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

            // Idiomas (Chips)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Aumentei o espaçamento
            ) {
                LanguageChip("Português", isSelected = true)
                LanguageChip("Inglês", isSelected = false)
                LanguageChip("Espanhol", isSelected = false)
            }
        }
    }
}

// LanguageChip ATUALIZADO para remover o fundo do item selecionado
@Composable
fun LanguageChip(label: String, isSelected: Boolean) {
    val textColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Text(
        text = label,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = fontWeight
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
            .offset(y = (-60).dp) // Move o card para cima, sobrepondo o header
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
                    iconColor = AccentColor
                )
                StatItem(
                    icon = Icons.Filled.LocationOn,
                    value = "28",
                    label = "Países",
                    iconColor = AccentColor
                )
                StatItem(
                    icon = Icons.Filled.AttachMoney,
                    value = "156",
                    label = "Encontros",
                    iconColor = Color(0xFF4CAF50) // Verde para dinheiro
                )
                StatItem(
                    icon = Icons.Filled.Star,
                    value = "4.9",
                    label = "Avaliação",
                    iconColor = Color(0xFFFFC107) // Amarelo para estrela
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Meus Interesses
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

// InterestChip ATUALIZADO com a cor roxa e borda LightBlue
@Composable
fun InterestChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent) // Fundo transparente
            .border(1.dp, LightBlue, RoundedCornerShape(16.dp)) // Borda LightBlue
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = InterestPurple, // Cor atualizada para 8200DB
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
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
            .offset(y = (-40).dp) // Ajuste para compensar o offset do card de estatísticas
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

        // Item de Configuração: Minhas Conexões (Cor padrão)
        SettingsItem(
            icon = Icons.Filled.People,
            label = "Minhas Conexões",
            iconColor = AccentColor,
            backgroundColor = LightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Item de Configuração: Interesses e Hobbies (Cor padrão)
        SettingsItem(
            icon = Icons.Filled.FavoriteBorder,
            label = "Interesses e Hobbies",
            iconColor = AccentColor,
            backgroundColor = LightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(24.dp)) // Espaço maior antes do botão de logout

        // Item de Configuração: Sair da Conta (Cor de destaque - Vermelho)
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp, // Ícone de saída
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

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Usando o tema padrão para o preview
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
