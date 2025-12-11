package com.example.faraway.ui.screen

import BottomNavBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.faraway.Destinations
import com.example.faraway.guideNavItems
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.*
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.viewmodel.AuthViewModelFactory
import com.example.faraway.ui.viewmodel.GuidePanelViewModel
import com.example.faraway.ui.viewmodel.GuidePanelViewModelFactory
import com.example.faraway.ui.viewmodel.FriendRequest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidePanelScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    repository: AuthRepository = AuthRepository(),
    viewModel: GuidePanelViewModel = viewModel(
        factory = GuidePanelViewModelFactory(repository)
    )
) {
    val userData by authViewModel.userData.collectAsState()
    val selectedItem = remember { mutableStateOf("Explorar") }

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
        viewModel.fetchPendingRequests() // Busca as solicitações ao iniciar
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Painel do Guia", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { "Carregando..." },
                            fontSize = 14.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ação do perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2364C8), Color(0xFF113162))
                    )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        // Barra Inferior
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navItems = guideNavItems,
                startRoute = Destinations.HOST_DASHBOARD_ROUTE // Rota inicial do NavHost
            )
            NavigationBar(containerColor = Color.White){
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") },
                    selected = selectedItem.value == "Explorar",
                    onClick = {
                        navController.navigate(Destinations.GUIDE_DASHBOARD_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Tours") },
                    label = { Text("Tours") },
                    selected = selectedItem.value == "Tours",
                    onClick = {
                        navController.navigate(Destinations.GUIDE_TOURS_ROUTE) {
                            // Configuração para evitar múltiplas instâncias
                            launchSingleTop = true
                            // Opcional: popUpTo para limpar a pilha, se necessário
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = selectedItem.value == "Chat",
                    onClick = {
                        navController.navigate(Destinations.GUIDE_CHAT_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem.value == "Perfil",
                    onClick = {
                        navController.navigate(Destinations.GUIDE_PROFILE_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        GuideDashboardContent(innerPadding, viewModel)
    }
}

/** O CONTEÚDO DA TELA DO GUIA*/
@Composable
private fun GuideDashboardContent(
    innerPadding: PaddingValues,
    viewModel: GuidePanelViewModel
) {
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Tour Atuais ---
        Text(
            "Tour Atuais",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = CurrentInfoCardBlue),

            ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Amanda Nunes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Data",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("13 Out 2025")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = "Hora",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("18:00")
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("Tour Gastronômico", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Novas Solicitações",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            if (pendingRequests.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .background(NotificationBadgeBlue, shape = CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "${pendingRequests.size}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // Cartões de Solicitação
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (pendingRequests.isEmpty()) {
            Text("Nenhuma solicitação pendente.", color = Color.Gray)
        } else {
            pendingRequests.forEach { request ->
                GuideRequestCard(
                    request = request,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun GuideRequestCard(
    request: FriendRequest,
    viewModel: GuidePanelViewModel
) {
    val sender = request.senderUser
    val name = "${sender?.firstName ?: "Usuário"} ${sender?.lastName ?: ""}".trim()
    val description = sender?.description ?: "Sem descrição."
    val date = "Data Indefinida" // Você pode formatar a data/hora do timestamp se necessário
    val time = "Hora Indefinida"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Imagem
                AsyncImage(
                    model = sender?.profileImageUrl,
                    contentDescription = "Foto de $name",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Data", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(date, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Hora", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(time, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(description, fontSize = 14.sp, maxLines = 2)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botão Aceitar
                Button(
                    onClick = { viewModel.updateRequestStatus(request.requestId, "accepted") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Aceitar", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Aceitar", color = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                // Botão Recusar
                OutlinedButton(
                    onClick = { viewModel.updateRequestStatus(request.requestId, "rejected") },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color(0xFFF44336)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Recusar", modifier = Modifier.size(16.dp), tint = Color(0xFFF44336))
                    Spacer(Modifier.width(4.dp))
                    Text("Recusar", color = Color(0xFFF44336))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuidePanelScreenPreview() {
    FarAwayTheme {
        // Mocking NavController and AuthViewModel for preview purposes
        // Note: This preview will not work correctly without a real Firebase setup
        // but serves as a structural check.
        GuidePanelScreen(
            navController = rememberNavController(),
            authViewModel = viewModel(factory = AuthViewModelFactory(AuthRepository()))
        )
    }
}
