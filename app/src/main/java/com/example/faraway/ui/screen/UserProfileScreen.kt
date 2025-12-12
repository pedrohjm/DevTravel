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
import coil.compose.AsyncImage // Importação do Coil
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.viewmodel.ProfileViewModel
import com.example.faraway.ui.viewmodel.ProfileViewModelFactory
import androidx.compose.runtime.LaunchedEffect // Importação necessária
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

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
fun UserProfileScreen(
    navController: NavController,
    repository: AuthRepository = AuthRepository(),
    factory: ProfileViewModelFactory = ProfileViewModelFactory(repository),
    profileViewModel: ProfileViewModel = viewModel(factory = factory)
) {
    val user by profileViewModel.user.collectAsState()
    val profileImageUrl by profileViewModel.profileImageUrl.collectAsState()

    // Efeito para recarregar o perfil sempre que a tela for exibida (útil ao voltar da edição)
    LaunchedEffect(Unit) {
        profileViewModel.fetchCurrentUserProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        // Fundo com Gradiente Azul (Cores do ViewProfileScreen)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(UserProfilePrimaryBlue, UserProfileAccentColor) // Usando as cores do UserProfileScreen como base
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Card Branco Centralizado
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()), // Adicionado scroll para o conteúdo do card
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Foto de Perfil
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUrl.isNullOrEmpty()) {
                            Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White, modifier = Modifier.size(80.dp))
                        } else {
                            AsyncImage(
                                model = profileImageUrl,
                                contentDescription = "Foto de Perfil",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Nome e Localização
                    Text(
                        text = "${user?.firstName ?: "Usuário"} ${user?.lastName ?: ""}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Localização", tint = Color(0xFF333333).copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user?.location ?: "Localização não informada",
                            color = Color(0xFF333333).copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Descrição
                    Text(
                        text = user?.description ?: "Nenhuma descrição fornecida.",
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 4. Botões de Ação (Essência do UserProfileScreen)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp), // Removido padding extra
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Botão "Ver Perfil"
                        Button(
                            onClick = { navController.navigate("profile") },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UserProfilePrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Ver Perfil", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // Botão "Editar Perfil"
                        Button(
                            onClick = { navController.navigate("edit_profile") },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UserProfileAccentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Editar Perfil", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun UserProfileHeader(
    navController: NavController,
    userFullName: String,
    userLocation: String,
    profileImageUrl: String?
) { // RENOMEADO
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UserProfilePrimaryBlue)
            .padding(bottom = 100.dp), // Aumenta o padding inferior para empurrar o conteúdo para baixo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar (REMOVIDO: Ícone de Voltar e Engrenagem)
        // Apenas um Spacer para manter o padding superior, se necessário
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Profile Picture, Name and Location (Overlay)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 0.dp), // Remove o offset negativo para que a foto fique no centro vertical do padding
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
            if (profileImageUrl.isNullOrEmpty()) {
                // Placeholder se não houver URL
                Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.Gray, modifier = Modifier.size(60.dp))
            } else {
                // Imagem do Coil
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // User Info
        Text(
            text = userFullName, // DADO DO VIEWMODEL
            color = Color.White, // Alterado para branco para visibilidade no fundo azul
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userLocation, // DADO DO VIEWMODEL
            color = Color.White.copy(alpha = 0.7f), // Alterado para branco suave para visibilidade
            fontSize = 14.sp
        )
    }
}

// -----------------------------------------------------------------
// 2. DESCRIPTION BOX
// -----------------------------------------------------------------

@Composable
fun UserProfileDescriptionBox(description: String) { // NOVO COMPONENTE
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
            text = description, // DADO DO VIEWMODEL
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
fun UserProfileInterestChips(interests: List<String>) {
    if (interests.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset(y = (-10).dp), // Ajuste de posição
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exibe todos os chips em LazyRow para melhor manuseio de muitos itens
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(interests) { interest ->
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
// 4. PROFILE ACTION BUTTON
// -----------------------------------------------------------------

@Composable
fun ProfileActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                text = text,
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
fun UserProfileScreenPreview() {
    MaterialTheme {
        UserProfileScreen(navController = rememberNavController())
    }
}
