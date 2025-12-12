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
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.data.User
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.viewmodel.AuthViewModel

val PrimaryBlue = Color(0xFF192F50)
val AccentColor = Color(0xFF00BCD4)
val LightBlue = Color(0xFFE0F7FA)
val CardBackground = Color(0xFFFFFFFF)
val TextColor = Color(0xFF333333)
val LogoutRed = Color(0xFFE57373)
val LogoutLightRed = Color(0xFFFFEBEE)
val InterestPurple = Color(0xFF8200DB)


data class NavItem(val route: String, val icon: ImageVector, val label: String)

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    ProfileContent(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    navController: NavController,
    userData: User?,
    onLogout: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBarPlaceholder(navController = navController, navItems = travelerNavItems, startRoute = "profile")
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { ProfileHeader(navController = navController, userData = userData) }
            item { ProfileStatsAndInterests(userData = userData) }
            item { ProfileSettings(navController = navController, onLogout = onLogout) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun ProfileHeader(navController: NavController, userData: User?) {
    val profileImageUrl = userData?.profileImageUrl
    fun String?.fallback(): String = this?.ifBlank { "--------" } ?: "--------"

    val fullName = "${userData?.firstName.fallback()} ${userData?.lastName.fallback()}".trim().ifEmpty { "--------" }
    val description = userData?.description.fallback()
    val location = userData?.location.fallback()
    val languages = userData?.languages ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
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
            IconButton(onClick = {
                navController.navigate(Destinations.CONFIG_ROUTE) {
                    popUpTo(navController.graph.id) { inclusive = false }
                }
            }) {
                Icon(Icons.Filled.Settings, "Configurações", tint = Color.White)
            }
        }

        // Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto
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
                        .background(AccentColor)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Filled.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(fullName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(location, color = AccentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun LanguageChip(label: String, isSelected: Boolean) {
    val textColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    val backgroundColor = if (isSelected) AccentColor else Color.LightGray.copy(alpha = 0.3f)

    Text(
        text = label,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = fontWeight,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun ProfileStatsAndInterests(userData: User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-10).dp)
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
        }

        Spacer(modifier = Modifier.height(24.dp))

         Text("Meus Interesses", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextColor, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
            val interests = userData?.interests ?: emptyList()
            if (interests.isEmpty()) {
                Text("--------", fontSize = 14.sp, color = TextColor.copy(alpha = 0.5f))
            } else {
                interests.forEach { interest -> InterestChip(interest) }
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = iconColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextColor)
        Text(label, fontSize = 12.sp, color = TextColor.copy(alpha = 0.6f))
    }
}

@Composable
fun InterestChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
            .border(1.dp, LightBlue, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, color = InterestPurple, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileSettings(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-0.9).dp)
            .padding(horizontal = 16.dp)
    ) {
        Text("Configurações", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextColor, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(Icons.Filled.People, "Minhas Conexões", AccentColor, LightBlue) {
            // navController.navigate(Destinations.MY_CONNECTIONS_ROUTE)
        }
        Spacer(modifier = Modifier.height(12.dp))

        SettingsItem(Icons.Filled.FavoriteBorder, "Interesses e Hobbies", AccentColor, LightBlue) { /* Ação */ }
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(Icons.AutoMirrored.Filled.ExitToApp, "Sair da Conta", LogoutRed, LogoutLightRed, onLogout)
    }
}

@Composable
fun SettingsItem(icon: ImageVector, label: String, iconColor: Color, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth().height(64.dp).clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextColor)
            }
            Icon(Icons.Filled.ArrowForward, "Avançar", tint = iconColor)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    FarAwayTheme {
        // Passamos null para simular o estado inicial ou de carregamento
        // Os textos aparecerão como "--------"
        ProfileContent(
            navController = rememberNavController(),
            userData = null,
            onLogout = {}
        )
    }
}