package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.data.User
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.ui.viewmodel.ViewProfileViewModel
import com.example.faraway.ui.viewmodel.ViewProfileViewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ViewProfileScreen(
    navController: NavController,
    userId: String,
    repository: AuthRepository = AuthRepository(),
    viewModel: ViewProfileViewModel = viewModel(
        factory = ViewProfileViewModelFactory(repository)
    )
) {
    // Estado para gerenciar o texto e a ação do botão
    val requestStatus by viewModel.requestStatus.collectAsState()
    val buttonText = when (requestStatus) {
        "sending" -> "Enviando..."
        "sent" -> "Solicitação Enviada"
        "error" -> "Erro ao Enviar"
        else -> "Adicionar"
    }
    val isButtonEnabled = requestStatus != "sending" && requestStatus != "sent"
    val buttonColor = if (requestStatus == "sent") Color.Gray else Color.White
    // 1. Carregar dados do usuário
    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 2. Layout Principal
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de ${user?.firstName ?: "Usuário"}") },
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
        // Fundo com Gradiente Azul
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryDark, AccentColor)
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else if (user != null) {
                ProfileContentCard(
                    user = user!!,
                    buttonText = buttonText,
                    isButtonEnabled = isButtonEnabled,
                    buttonColor = buttonColor,
                    onAddFriendClick = {
                        if (isButtonEnabled) {
                            viewModel.sendFriendRequest(userId)
                        }
                    }
                )
            } else {
                Text("Perfil não encontrado.", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ProfileContentCard(
    user: User,
    buttonText: String,
    isButtonEnabled: Boolean,
    buttonColor: Color,
    onAddFriendClick: () -> Unit
) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val profileImageUrl = user.profileImageUrl
                if (profileImageUrl.isNullOrEmpty()) {
                    Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White, modifier = Modifier.size(80.dp))
                } else {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nome e Localização
            Text(
                text = "${user.firstName} ${user.lastName}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Localização", tint = Color(0xFF333333).copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = user.location ?: "Localização não informada",
                    color = Color(0xFF333333).copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descrição
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sobre ${user.firstName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.description ?: "Nenhuma descrição fornecida.",
                fontSize = 14.sp,
                color = Color(0xFF333333),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Interesses
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileSection(title = "Interesses", items = user.languages ?: emptyList(), icon = Icons.Filled.Favorite) { item ->
                InterestChip(item)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Linguagens
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileSection(title = "Linguagens", items = user.interests ?: emptyList(), icon = Icons.Filled.Language) { item ->
                LanguageChip(item, isSelected = true)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Adicionar
            Button(
                onClick = onAddFriendClick,
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "Adicionar Amigo",
                        tint = PrimaryDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = buttonText,
                        color = PrimaryDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun <T> ProfileSection(
    title: String,
    items: List<T>,
    icon: ImageVector,
    content: @Composable (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, tint = PrimaryDark, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (items.isEmpty()) {
            Text(
                text = "Nenhum(a) ${title.lowercase()} informado(a).",
                fontSize = 14.sp,
                color = Color(0xFF333333).copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEach { item ->
                    content(item)
                }
            }
        }
    }
}
