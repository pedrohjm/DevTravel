package com.example.faraway.ui.screen

import androidx.compose.foundation.ScrollState
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
import com.example.faraway.Destinations
import com.example.faraway.amigosNavItems
import com.example.faraway.ui.data.User
import coil.compose.AsyncImage
import com.example.faraway.ui.viewmodel.AuthViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

val FriendProfilePrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val FriendProfileAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val FriendProfileLightBlue = Color(0xFFE0F7FA) // Azul claro para fundo de cards
val FriendProfileTextColor = Color(0xFF333333) // Cor de texto padrão
val FriendProfileRed = Color(0xFFD32F2F) // Vermelho para o botão de Sair da Conta

data class FriendProfileNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

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
                selected = item.route == "perfil",
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val scrollState = rememberScrollState()
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }
    Scaffold(
        bottomBar = {
            FriendProfileBottomNavBarPlaceholder(navController = navController,navItems = amigosNavItems ,Destinations.SOCIAL_PROFILE_ROUTE)
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
            FriendProfileSettings(navController = navController, authViewModel = authViewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


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
            IconButton(onClick = { /* Ação de Voltar */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
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
            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = Color.White)
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
                    Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White, modifier = Modifier.size(60.dp))
                } else {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, FriendProfilePrimaryBlue, CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable { navController.navigate(Destinations.EDIT_PROFILE_ROUTE) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = "Mudar Foto", tint = FriendProfilePrimaryBlue, modifier = Modifier.size(16.dp))
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
            Icon(Icons.Outlined.LocationOn, contentDescription = "Localização", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = userData?.location ?: "Localização desconhecida",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                FriendProfileStatItem(icon = Icons.Outlined.FavoriteBorder, value = "28", label = "Recomendações", color = FriendProfileAccentColor)
            }
        }
    }
}

@Composable
fun FriendProfileStatItem(icon: ImageVector, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FriendProfileTextColor)
        }
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun FriendProfileInterests(userData: User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-20).dp)
    ) {
        Text(
            text = "Meus Interesses",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = FriendProfileTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val interests = userData?.interests ?: emptyList()
            if (interests.isEmpty()) {
                Text(
                    text = "--------",
                    fontSize = 14.sp,
                    color = FriendProfileTextColor.copy(alpha = 0.5f)
                )
            } else {
                interests.forEach { interest ->
                    FriendProfileInterestChip(interest)
                }
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
fun FriendProfileSettings(navController: NavController, authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        FriendProfileSettingsItem(
            icon = Icons.Filled.PersonAdd,
            label = "Convidar Amigos",
            onClick = { /* Ação ao clicar */ }
        )
        FriendProfileSettingsItem(
            icon = Icons.Filled.Notifications,
            label = "Notificações",
            onClick = { /* Ação ao clicar */ }
        )
        FriendProfileSettingsItem(
            icon = Icons.Filled.Security,
            label = "Privacidade e Segurança",
            onClick = { /* Ação ao clicar */ }
        )
        FriendProfileSettingsItem(
            icon = Icons.Filled.Help,
            label = "Ajuda e Suporte",
            onClick = { /* Ação ao clicar */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FriendProfileSettingsItem(
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            label = "Sair da Conta",
            color = FriendProfileRed,
            onClick = {
                authViewModel.logout()
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
fun FriendProfileSettingsItem(
    icon: ImageVector,
    label: String,
    color: Color = FriendProfileTextColor,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = color)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = color, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Gray)
    }
}