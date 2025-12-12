package com.example.faraway.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.faraway.Destinations
import com.example.faraway.amigosNavItems
import com.example.faraway.ui.data.User
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.viewmodel.AuthViewModel

// --- CORES ---
val FriendProfilePrimaryBlue = Color(0xFF192F50)
val FriendProfileAccentColor = Color(0xFF00BCD4)
val FriendProfileLightBlue = Color(0xFFE0F7FA) // Azul claro dos botões
val FriendProfileTextColor = Color(0xFF333333)
val FriendProfileRed = Color(0xFFD32F2F)
val FriendProfileRedBg = Color(0xFFFFEBEE) // Fundo claro para o ícone de sair

// --- NAV ITEMS ---
data class FriendProfileNavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun FriendProfileBottomNavBarPlaceholder(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    BottomAppBar(
        containerColor = Color.White,
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
// 1. TELA COM LÓGICA (Stateful)
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    FriendProfileContent(
        navController = navController,
        userData = userData,
        onLogout = {
            authViewModel.logout()
            navController.navigate("auth") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    )
}

// -----------------------------------------------------------------
// 2. CONTEÚDO VISUAL (Stateless)
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileContent(
    navController: NavController,
    userData: User?,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            FriendProfileBottomNavBarPlaceholder(navController = navController, navItems = amigosNavItems, Destinations.SOCIAL_PROFILE_ROUTE)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            FriendProfileHeader(navController = navController, userData = userData)
            FriendProfileStatsCard()
            FriendProfileInterests(userData = userData)
            FriendProfileSettings(navController = navController, onLogout = onLogout)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// -----------------------------------------------------------------
// HEADER
// -----------------------------------------------------------------
@Composable
fun FriendProfileHeader(navController: NavController, userData: User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendProfilePrimaryBlue)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
            }
            Text("Meu Perfil", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* Config */ }) {
                Icon(Icons.Filled.Settings, "Configurações", tint = Color.White)
            }
        }

        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val profileImageUrl = userData?.profileImageUrl
                if (profileImageUrl.isNullOrEmpty()) {
                    Icon(Icons.Filled.Person, "Foto", tint = Color.White, modifier = Modifier.size(60.dp))
                } else {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, FriendProfilePrimaryBlue, CircleShape)
                    .clickable { /* Editar Foto */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PhotoCamera, "Mudar Foto", tint = FriendProfilePrimaryBlue, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${userData?.firstName ?: "Nome"} ${userData?.lastName ?: "Sobrenome"}",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userData?.description ?: "Sem descrição",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.LocationOn, "Localização", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = userData?.location ?: "Localização desconhecida",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        /*Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Green.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Verified, "Verificado", tint = Color.Green, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Perfil Verificado", color = Color.Green, fontSize = 12.sp)
        }*/

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            userData?.languages?.forEach { lang ->
                FriendProfileLanguageChip(lang, isSelected = true)
            }
        }
    }
}

@Composable
fun FriendProfileLanguageChip(label: String, isSelected: Boolean) {
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

@Composable
fun FriendProfileStatsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-20).dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        /*Column(modifier = Modifier.padding(16.dp)) {
            /*Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                FriendProfileStatItem(Icons.Outlined.People, "156", "Conexões", FriendProfileAccentColor)
                FriendProfileStatItem(Icons.Filled.Flag, "28", "Países", FriendProfileAccentColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                FriendProfileStatItem(Icons.Filled.AttachMoney, "156", "Encontros", FriendProfileAccentColor)
                FriendProfileStatItem(Icons.Outlined.FavoriteBorder, "28", "Recomendações", FriendProfileAccentColor)
            } */
        }*/
    }
}

@Composable
fun FriendProfileStatItem(icon: ImageVector, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, label, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FriendProfileTextColor)
        }
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun FriendProfileInterests(userData: User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-0.9).dp)
    ) {
        Text("Meus Interesses", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FriendProfileTextColor)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val interests = userData?.interests ?: emptyList()
            if (interests.isEmpty()) {
                Text("--------", fontSize = 14.sp, color = FriendProfileTextColor.copy(alpha = 0.5f))
            } else {
                interests.forEach { interest -> FriendProfileInterestChip(interest) }
            }
        }
    }
}

@Composable
fun FriendProfileInterestChip(label: String) {
    Text(
        text = label,
        color = Color(0xFF8200DB),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .background(Color(0xFFF3E5F5), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun FriendProfileSettings(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Configurações", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FriendProfileTextColor)
        Spacer(modifier = Modifier.height(12.dp))

        // Botão Azul Claro
        FriendProfileSettingsItem(
            icon = Icons.Filled.People,
            label = "Minhas Conexões",
            containerColor = FriendProfileLightBlue,
            textColor = FriendProfileTextColor,
            iconColor = FriendProfileTextColor,
            onClick = { /* Ação */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botão Azul Claro
        FriendProfileSettingsItem(
            icon = Icons.Outlined.FavoriteBorder,
            label = "Interesses e lugares visitados",
            containerColor = FriendProfileLightBlue,
            textColor = FriendProfileTextColor,
            iconColor = FriendProfileTextColor,
            onClick = { /* Ação */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Sair (Branco com Borda Vermelha)
        FriendProfileSettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp, // Ícone de saída
            label = "Sair da Conta",
            containerColor = Color.White,
            textColor = FriendProfileRed,
            iconColor = FriendProfileRed,
            borderColor = FriendProfileRed,
            isLogout = true,
            onClick = onLogout
        )
    }
}

@Composable
fun FriendProfileSettingsItem(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    textColor: Color,
    iconColor: Color,
    borderColor: Color? = null,
    isLogout: Boolean = false,
    onClick: () -> Unit
) {
    val border = if (borderColor != null) BorderStroke(1.dp, borderColor) else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = border,
        elevation = if (isLogout) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo ao redor do ícone (opcional, como na imagem)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isLogout) FriendProfileRedBg else Color.Transparent), // Fundo vermelhinho no ícone de sair
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = if(isLogout) FriendProfileRed else Color.Gray.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FriendProfilePreview() {
    FarAwayTheme {
        FriendProfileContent(
            navController = rememberNavController(),
            userData = null, // Sem mockUser
            onLogout = {}
        )
    }
}