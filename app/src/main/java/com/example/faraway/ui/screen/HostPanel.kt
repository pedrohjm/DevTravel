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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.*
import com.example.faraway.ui.viewmodel.AuthViewModel

/** Painel do Anfitrião.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PainelDoAnfitriaoScreen(navController: NavController, authViewModel: AuthViewModel) {
    val selectedItem = remember { mutableStateOf("Explorar") }

    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Painel do Anfitrião", fontWeight = FontWeight.Bold)
                        Text(text = "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { "Carregando..." },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
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
                        colors = listOf(
                            Color(0xFF0892B4), // Cor inicial
                            Color(0xFF033F4E)  // Cor final
                        )
                    )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Barra de Navegação do Anfitrião
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
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
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
                    label = { Text("Reservas") }, // Diferente do Guia
                    selected = selectedItem.value == "Reservas",
                    onClick = {
                        navController.navigate(Destinations.HOST_RESERVATION_ROUTE) {
                        launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = selectedItem.value == "Chat",
                    onClick = {
                        navController.navigate(Destinations.HOST_CHAT_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem.value == "Perfil",
                    onClick = {
                        navController.navigate(Destinations.HOST_PERFIL_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        HostDashboardContent(innerPadding)
    }
}

/** Conteúdo da tela do Anfitrião */
@Composable
private fun HostDashboardContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Seção Hóspedes Atuais
        Text("Hóspedes Atuais", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = CurrentInfoCardBlue) // Cor do tema
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Laura Resende", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Check-out: 10 Out 2025")
                Text("Quarto Privado", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Seção Novas Solicitações
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Novas Solicitações", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(NotificationBadgeBlue, shape = CircleShape) // Cor do tema
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("2", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(8.dp))

        // Cartões de Solicitação
        HostRequestCard(
            name = "Sofia Mendes",
            dateInfo = "12 Out 2025 → 15 Out 2025 • 3 noites",
            guestInfo = "2 hóspedes",
            description = "Casal em lua de mel, procurando um lugar tranquilo e aconchegante."
        )
        HostRequestCard(
            name = "Miguel Santos",
            dateInfo = "18 Out 2025 → 20 Out 2025 • 2 noites",
            guestInfo = "1 hóspede",
            description = "Viagem de negócios, preciso de bom wifi e área de trabalho."
        )
    }
}

/**
Cartão de Solicitação do Anfitrião
 */
@Composable
private fun HostRequestCard(
    name: String,
    dateInfo: String,
    guestInfo: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de $name",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(dateInfo, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.People, contentDescription = "Hóspedes", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(guestInfo, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(description, fontSize = 14.sp, maxLines = 2)
                }
            }
            Spacer(Modifier.height(16.dp))
            // Botões de Ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botão Aceitar
                Button(
                    onClick = { /* Aceitar */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, AcceptButtonColor2),
                    // cor do conteúdo (ícone E texto)
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor =AcceptButtonColor2,
                    ),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Aceitar",
                        //modifier = Modifier.size(ButtonDefaults.IconSize),
                        modifier = Modifier.size(12.dp))
//                    Spacer(Modifier.width(4.dp))
                    Text("Aceitar", fontSize = 12.sp)
                }
                Spacer(Modifier.width(8.dp))

                //Botão Recusar
                OutlinedButton(
                    onClick = { /* Recusar */ },
                    modifier = Modifier.weight(1f),
                    // cor da borda
                    border = BorderStroke(1.dp, DeclineButtonColor2),
                    // cor do conteúdo (ícone E texto)
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DeclineButtonColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Recusar",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Recusar",
                        fontSize = 10.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                // Botão Pendente
                OutlinedButton(
                    onClick = { /* Ação de pendente */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, PendingButtonColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PendingButtonColor2
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Pendente",
                        fontSize = 12.sp
                    )
                }


            }
        }
    }
}
