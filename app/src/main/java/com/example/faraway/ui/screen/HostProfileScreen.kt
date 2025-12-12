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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.faraway.Destinations
import com.example.faraway.hostNavItems
import com.example.faraway.ui.data.User
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.viewmodel.AuthViewModel

// -----------------------------------------------------------------
// CORES AUXILIARES
// -----------------------------------------------------------------
val HostPrimaryColor = Color(0xFF00BCD4)
val HostAccentColor = Color(0xFF00BCD4)
val HostLightBlue = Color(0xFFE0F7FA)
val HostCardBackground = Color(0xFFFFFFFF)
val HostTextColor = Color(0xFF333333)
val HostLogoutRed = Color(0xFFE57373)
val HostLogoutLightRed = Color(0xFFFFEBEE)

// -----------------------------------------------------------------
// COMPONENTE STATEFUL (COM LÓGICA)
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

    HostProfileContent(
        navController = navController,
        userData = userData,
        onLogout = {
            authViewModel.logout()
            navController.navigate(Destinations.AUTH_ROUTE) {
                popUpTo(navController.graph.id) { inclusive = false }
            }
        }
    )
}

// -----------------------------------------------------------------
// COMPONENTE STATELESS (SÓ VISUAL)
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostProfileContent(
    navController: NavController,
    userData: User?,
    onLogout: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = HostCardBackground,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                hostNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == Destinations.HOST_PERFIL_ROUTE,
                        onClick = {
                            navController.navigate(item.route) {
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { HostProfileHeader(navController = navController, userData = userData) }
            item { HostProfileStatsAndInfo() }
            item { HostProfileSettings(navController = navController, onLogout = onLogout) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------
@Composable
fun HostProfileHeader(navController: NavController, userData: User?) {
    fun String?.fallback(default: String = "--------"): String = this?.ifBlank { default } ?: default

    // Se userData for null, usa valores padrão
    val fullName = "${userData?.firstName.fallback("")} ${userData?.lastName.fallback("")}".trim().ifEmpty { "Anfitrião" }
    val description = userData?.description.fallback("Descrição da Propriedade")
    val location = userData?.location.fallback("Localização")
    val profileImageUrl = userData?.profileImageUrl

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(HostPrimaryColor)
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
            }
            Text("Meu Perfil", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* Configurações */ }) {
                Icon(Icons.Filled.Settings, "Configurações", tint = Color.White)
            }
        }

        // Info do Usuário
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
                    if (profileImageUrl.isNullOrEmpty()) {
                        Icon(Icons.Filled.Person, null, tint = Color.Gray, modifier = Modifier.size(80.dp))
                    } else {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Foto",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Filled.PhotoCamera, null, tint = HostPrimaryColor, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(location, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            /*Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Green))
                Text("Anfitrião Verificado", color = Color.White, fontSize = 12.sp)
            }*/

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        Text(text = label, color = Color.White, fontSize = 12.sp)
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
            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HostStatItem(Icons.Filled.People, "247", "Hóspedes", HostAccentColor)
                HostStatItem(Icons.Filled.AttachMoney, "€ 18.2K", "Receita", Color(0xFF4CAF50))
                HostStatItem(Icons.Filled.Star, "4.8", "Avaliação", Color(0xFFFFC107))
                HostStatItem(Icons.Filled.TrendingUp, "87%", "Ocupação", HostAccentColor)
            }*/
        }
    }
}

@Composable
fun HostStatItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = iconColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HostTextColor)
        Text(label, fontSize = 12.sp, color = HostTextColor.copy(alpha = 0.6f))
    }
}

// -----------------------------------------------------------------
// 3. CONFIGURAÇÕES
// -----------------------------------------------------------------
@Composable
fun HostProfileSettings(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (10).dp)
            .padding(horizontal = 16.dp)
    ) {
        Text("Configurações", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HostTextColor, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(Icons.Filled.Home, "Minha Propriedade", HostAccentColor, HostLightBlue) {
            navController.navigate(Destinations.HOST_PROPERTY_ROUTE)
        }
        Spacer(modifier = Modifier.height(12.dp))

        HostSettingsItem(Icons.Filled.CalendarMonth, "Documentos Profissionais", HostAccentColor, HostLightBlue) {
            navController.navigate(Destinations.AVAILABILITY_ROUTE)
        }
        Spacer(modifier = Modifier.height(12.dp))
        HostSettingsItem(Icons.AutoMirrored.Filled.ExitToApp, "Sair da Conta", HostLogoutRed, HostLogoutLightRed, onLogout)
    }
}

@Composable
fun HostSettingsItem(icon: ImageVector, label: String, iconColor: Color, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.height(64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = HostTextColor)
            }
            Icon(Icons.Filled.ArrowForward, "Avançar", tint = iconColor)
        }
    }
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HostProfilePreview() {
    FarAwayTheme {
        // Passamos null para o usuário, simulando o estado inicial ou de carregamento
        // Assim não dependemos da estrutura específica da classe User para o preview
        HostProfileContent(
            navController = rememberNavController(),
            userData = null,
            onLogout = {}
        )
    }
}