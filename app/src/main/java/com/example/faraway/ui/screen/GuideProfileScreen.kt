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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.guideNavItems
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import coil.compose.AsyncImage // Importação para exibir imagens da web

// -----------------------------------------------------------------
// CORES AUXILIARES (Renomeadas para evitar conflito)
// -----------------------------------------------------------------
val GuidePrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val GuideAccentColor = Color(0xFF00BCD4) // Cor de destaque (Turquesa/Ciano)
val GuideLightBlue = Color(0xFFE0F7FA) // Azul claro para os cards de configuração
val GuideCardBackground = Color(0xFFFFFFFF) // Fundo branco para cards
val GuideTextColor = Color(0xFF333333) // Cor de texto padrão
val GuideLogoutRed = Color(0xFFE57373) // Vermelho para o botão de Sair
val GuideLogoutLightRed = Color(0xFFFFEBEE) // Vermelho claro para o fundo do botão de Sair

// -----------------------------------------------------------------
// PLACEHOLDERS PARA COMPONENTES DE NAVEGAÇÃO
// -----------------------------------------------------------------


// Placeholder para BottomNavBar (Componente)
@Composable
fun GuideBottomNavBarPlaceholder(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    BottomAppBar(
        containerColor = GuideCardBackground,
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
fun GuideProfileScreen(navController: NavController, authViewModel: AuthViewModel) { // ALTERADO: Recebe AuthViewModel
    val userData by authViewModel.userData.collectAsState()

    Scaffold(
        bottomBar = {
            GuideBottomNavBarPlaceholder(navController = navController, navItems = guideNavItems, startRoute = "profile")
        }
    ) { paddingValues ->
        // LazyColumn garante que a tela inteira seja rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { GuideProfileHeader(userData, navController = navController) }
            item { GuideProfileStatsAndInfo() }
            item { GuideProfileSettings(navController = navController, authViewModel = authViewModel) }
            item { Spacer(modifier = Modifier.height(32.dp)) } // Espaço extra no final
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER (Cabeçalho do Guia)
// -----------------------------------------------------------------

@Composable
fun GuideProfileHeader(userData: User?, navController: NavController) {

    // Função auxiliar para o fallback "--------"
    fun String?.fallback(): String = this?.ifBlank { "--------" } ?: "--------"

    val fullName = "${userData?.firstName.fallback()} ${userData?.lastName.fallback()}".trim().ifEmpty { "--------" }
    val description = userData?.description.fallback()
    val location = userData?.location.fallback()
    val languages = userData?.languages ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuidePrimaryBlue)
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
            IconButton(onClick = { } ) {
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
            IconButton(onClick = {
                navController.navigate(Destinations.CONFIG_ROUTE) {
                    popUpTo(navController.graph.id) { inclusive = false }
                }
            } ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Configurações",
                    tint = Color.White
                )
            }
        }

        // Foto de Perfil e Informações do Guia
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
                    val profileImageUrl = userData?.profileImageUrl
                    if (profileImageUrl.isNullOrEmpty()) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Foto de Perfil Placeholder",
                            tint = Color.Gray,
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Foto de Perfil",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }
                // Ícone de Câmera
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

            // Nome do Guia
            Text(
                text = fullName, // ALTERADO: Usa o nome completo
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Especialidade do Guia (Descrição)
            Text(
                text = description, // ALTERADO: Usa a descrição
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Localização
            Text(
                text = location, // ALTERADO: Usa a localização
                color = GuideAccentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Guia Verificado
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

            // Idiomas
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (languages.isEmpty()) {
                    Text(
                        text = "--------",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                } else {
                    languages.forEach { lang ->
                        GuideLanguageChip(lang, isSelected = true) // Assume-se que todas as línguas listadas são as faladas
                    }
                }
            }
        }
    }
}

@Composable
fun GuideLanguageChip(label: String, isSelected: Boolean) {
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
// 2. ESTATÍSTICAS DO GUIA
// -----------------------------------------------------------------

@Composable
fun GuideProfileStatsAndInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp) // Move o card para cima, sobrepondo o header
            .padding(horizontal = 16.dp)
    ) {
        // Card de Estatísticas do Guia
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
                    label = "Clientes"
                )
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )
                GuideStatItem(
                    icon = Icons.Filled.Star,
                    value = "4.9",
                    label = "Avaliação"
                )
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )
                GuideStatItem(
                    icon = Icons.Filled.DateRange,
                    value = "12",
                    label = "Tours Feitos"
                )
            }
        }
    }
}

@Composable
fun GuideStatItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = GuidePrimaryBlue)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GuideTextColor)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

// -----------------------------------------------------------------
// 3. CONFIGURAÇÕES DO GUIA (CORRIGIDO O ESPAÇAMENTO)
// -----------------------------------------------------------------

@Composable
fun GuideProfileSettings(navController: NavController, authViewModel: AuthViewModel) {
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
            color = GuideTextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Minha disponibilidade
        GuideSettingsItem(
            icon = Icons.Filled.DateRange,
            label = "Minha disponibilidade",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Documentos Profissionais
        GuideSettingsItem(
            icon = Icons.Filled.Description,
            label = "Documentos Profissionais",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp)) // ESPAÇAMENTO ADICIONADO

        // Central de Ajuda
        GuideSettingsItem(
            icon = Icons.Filled.Help,
            label = "Central de Ajuda",
            iconColor = GuideAccentColor,
            backgroundColor = GuideLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp)) // ESPAÇAMENTO ADICIONADO

        // Sair da Conta (Vermelho)
        GuideSettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Sair da Conta",
            iconColor = GuideLogoutRed,
            backgroundColor = GuideLogoutLightRed,
            onClick = {
                authViewModel.logout()
                navController.navigate(Destinations.AUTH_ROUTE) {
                    popUpTo(navController.graph.id) { inclusive = false }
                }
            }
        )
    }
}

@Composable
fun GuideSettingsItem(
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
            .clickable{onClick()}
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