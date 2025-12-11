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
import com.example.faraway.Destinations
import com.example.faraway.guideNavItems
import com.example.faraway.hostNavItems
import com.example.faraway.ui.viewmodel.AuthViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.faraway.ui.data.User
import coil.compose.AsyncImage // Importação para exibir imagens da web

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com Host para evitar conflito)
// -----------------------------------------------------------------
val HostPrimaryColor = Color(0xFF00BCD4) // Cor principal do cabeçalho (Ciano/Turquesa)
val HostAccentColor = Color(0xFF00BCD4) // Cor de destaque (A mesma do cabeçalho)
val HostLightBlue = Color(0xFFE0F7FA) // Azul claro para os cards de configuração
val HostCardBackground = Color(0xFFFFFFFF) // Fundo branco para cards
val HostTextColor = Color(0xFF333333) // Cor de texto padrão
val HostLogoutRed = Color(0xFFE57373) // Vermelho para o botão de Sair
val HostLogoutLightRed = Color(0xFFFFEBEE) // Vermelho claro para o fundo do botão de Sair

// -----------------------------------------------------------------
// PLACEHOLDERS PARA COMPONENTES DE NAVEGAÇÃO
// -----------------------------------------------------------------

// Placeholder para NavItem (data class)
// Placeholder para BottomNavBar (Componente)
@Composable
fun HostBottomNavBarPlaceholder(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    BottomAppBar(
        containerColor = HostCardBackground,
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
fun HostProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }
    Scaffold(
        bottomBar = {
            HostBottomNavBarPlaceholder(navController = navController, navItems = hostNavItems, startRoute = "profile")
        }
    ) { paddingValues ->
        // LazyColumn garante que a tela inteira seja rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { HostProfileHeader(navController = navController, userData = userData) }
            item { HostProfileStatsAndInfo() }
            item { HostProfileSettings(navController = navController, authViewModel = authViewModel) }
            item { Spacer(modifier = Modifier.height(32.dp)) } // Espaço extra no final
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER (Cabeçalho do Anfitrião)
// -----------------------------------------------------------------

@Composable
fun HostProfileHeader(navController: NavController, userData: User?) {
    fun String?.fallback(default: String = "--------"): String = this?.ifBlank { default } ?: default

    val fullName = "${userData?.firstName.fallback("")} ${userData?.lastName.fallback("")}".trim().ifEmpty { "Anfitrião" }
    val description = userData?.description.fallback("Descrição da Propriedade")
    val location = userData?.location.fallback("Localização")
    val profileImageUrl = userData?.profileImageUrl
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HostPrimaryColor)
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
            IconButton(onClick = {
                navController.navigate(Destinations.CONFIG_ROUTE) {
                    popUpTo(navController.graph.id) { inclusive = false }
                }
            }) { Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Configurações",
                    tint = Color.White
                )
            }
        }

        // Foto de Perfil e Informações do Anfitrião
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
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = "Mudar Foto",
                        tint = HostPrimaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nome do Anfitrião
            Text(
                text = fullName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descrição da Propriedade
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Localização
            Text(
                text = location,
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
                        .background(Color.Green)
                )
                Text(
                    text = "Anfitrião Verificado",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comodidades (Chips)
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
            .background(Color.White.copy(alpha = 0.2f))
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
// 2. ESTATÍSTICAS DO ANFITRIÃO
// -----------------------------------------------------------------

@Composable
fun HostProfileStatsAndInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-60).dp) // Move o card para cima, sobrepondo o header
            .padding(horizontal = 16.dp)
    ) {
        // Card de Estatísticas do Anfitrião
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
                    iconColor = HostAccentColor
                )
                HostStatItem(
                    icon = Icons.Filled.AttachMoney,
                    value = "€ 18.2K",
                    label = "Receita",
                    iconColor = Color(0xFF4CAF50) // Verde para dinheiro
                )
                HostStatItem(
                    icon = Icons.Filled.Star,
                    value = "4.8",
                    label = "Avaliação",
                    iconColor = Color(0xFFFFC107) // Amarelo para estrela
                )
                HostStatItem(
                    icon = Icons.Filled.TrendingUp,
                    value = "87%",
                    label = "Ocupação",
                    iconColor = HostAccentColor
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
// 3. CONFIGURAÇÕES DO ANFITRIÃO
// -----------------------------------------------------------------

@Composable
fun HostProfileSettings(navController: NavController, authViewModel: AuthViewModel) {
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
            color = HostTextColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(
            icon = Icons.Filled.Home,
            label = "Minha Propriedade",
            iconColor = HostAccentColor,
            backgroundColor = HostLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Disponibilidade
        HostSettingsItem(
            icon = Icons.Filled.CalendarMonth,
            label = "Disponibilidade",
            iconColor = HostAccentColor,
            backgroundColor = HostLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Central de Ajuda
        HostSettingsItem(
            icon = Icons.Filled.Help,
            label = "Central de Ajuda",
            iconColor = HostAccentColor,
            backgroundColor = HostLightBlue,
            onClick = { /* Ação ao clicar */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sair da Conta (Vermelho)
        HostSettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            label = "Sair da Conta",
            iconColor = HostLogoutRed,
            backgroundColor = HostLogoutLightRed,
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
fun HostSettingsItem(
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
