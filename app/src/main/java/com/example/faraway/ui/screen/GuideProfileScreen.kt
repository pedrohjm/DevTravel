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
import com.example.faraway.guideNavItems
import com.example.faraway.ui.data.User
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.viewmodel.AuthViewModel

val GuidePrimaryBlue = Color(0xFF192F50)
val GuideAccentColor = Color(0xFF00BCD4)
val GuideLightBlue = Color(0xFFE0F7FA)
val GuideCardBackground = Color(0xFFFFFFFF)
val GuideTextColor = Color(0xFF333333)
val GuideLogoutRed = Color(0xFFE57373)
val GuideLogoutLightRed = Color(0xFFFFEBEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    GuideProfileContent(
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
fun GuideProfileContent(
    navController: NavController,
    userData: User?,
    onLogout: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = GuideCardBackground,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                guideNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == Destinations.GUIDE_PROFILE_ROUTE,
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
            item { GuideProfileHeader(userData = userData, navController = navController) }
            item { GuideProfileStatsAndInfo() }
            item { GuideProfileSettings(navController = navController, onLogout = onLogout) }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun GuideProfileHeader(userData: User?, navController: NavController) {
    fun String?.fallback(): String = this?.ifBlank { "--------" } ?: "--------"

    val fullName = "${userData?.firstName.fallback()} ${userData?.lastName.fallback()}".trim().ifEmpty { "--------" }
    val description = userData?.description.fallback()
    val location = userData?.location.fallback()
    val languages = userData?.languages ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuidePrimaryBlue)
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
                        .background(GuideAccentColor)
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
            Text(location, color = GuideAccentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (languages.isEmpty()) {
                    Text("--------", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                } else {
                    languages.forEach { lang -> GuideLanguageChip(lang, true) }
                }
            }
        }
    }
}

@Composable
fun GuideLanguageChip(label: String, isSelected: Boolean) {
    val textColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    Text(text = label, color = textColor, fontSize = 14.sp, fontWeight = fontWeight)
}

@Composable
fun GuideProfileStatsAndInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (10).dp)
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GuideCardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
        }
    }
}

@Composable
fun GuideStatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, tint = GuidePrimaryBlue)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GuideTextColor)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun GuideProfileSettings(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (10).dp)
            .padding(horizontal = 16.dp)
    ) {
        Text("Configurações", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GuideTextColor, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(Icons.Filled.DateRange, "Minha disponibilidade", GuideAccentColor, GuideLightBlue) {
            navController.navigate(Destinations.AVAILABILITY_ROUTE)
        }
        Spacer(modifier = Modifier.height(12.dp))

        GuideSettingsItem(Icons.Filled.Description, "Documentos Profissionais", GuideAccentColor, GuideLightBlue){
            navController.navigate(Destinations.DocumentosScreen_Route)
        }
        Spacer(modifier = Modifier.height(12.dp))

        /*GuideSettingsItem(Icons.Filled.Help, "Central de Ajuda", GuideAccentColor, GuideLightBlue) { /* Ação */ }
        Spacer(modifier = Modifier.height(12.dp))*/

        GuideSettingsItem(Icons.AutoMirrored.Filled.ExitToApp, "Sair da Conta", GuideLogoutRed, GuideLogoutLightRed, onLogout)
    }
}

@Composable
fun GuideSettingsItem(icon: ImageVector, label: String, iconColor: Color, backgroundColor: Color, onClick: () -> Unit) {
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
                Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = GuideTextColor)
            }
            Icon(Icons.Filled.ArrowForward, "Avançar", tint = iconColor)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GuideProfilePreview() {
    FarAwayTheme {
        // Passamos null para o userData, usando os fallbacks definidos na tela
        GuideProfileContent(
            navController = rememberNavController(),
            userData = null,
            onLogout = {}
        )
    }
}