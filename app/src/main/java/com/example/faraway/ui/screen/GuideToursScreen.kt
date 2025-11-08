package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faraway.ui.data.Tour
import com.example.faraway.ui.data.TourStatus

//Tela principal que exibe a lista de tours do guia turístico.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTourScreen() {
    // Estado que controla qual aba está selecionada (0 = Próximos, 1 = Concluídos, etc.)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximos", "Concluídos", "Pendente", "Cancelados")

    // Lista de dados utilizados para demonstração
    val tours = remember {
        listOf(
            Tour(
                guestName = "Ana Santos",
                tourType = "Tour Histórico",
                location = "Oceano Huesteco - Lisboa",
                date = "15 Out 2025",
                time = "18:00",
                tourName = "Tour Histórico",
                price = "€45",
                status = TourStatus.CONFIRMED,
                imageRes = 0,
                participants = 2
            ),
            Tour(
                guestName = "Pedro Costa",
                tourType = "Tour Gastronómico",
                location = "Baixa - Lisboa",
                date = "20 Out 2025",
                time = "14:00",
                tourName = "Tour Gastronómico",
                price = "€60",
                status = TourStatus.CANCELED,
                imageRes = 0,
                participants = 4
            ),
            Tour(
                guestName = "Maria Silva",
                tourType = "Tour Cultural",
                location = "Alfama - Lisboa",
                date = "25 Out 2025",
                time = "16:00",
                tourName = "Tour Cultural",
                price = "€35",
                status = TourStatus.PENDING,
                imageRes = 0,
                participants = 1
            )
        )
    }

    // Scaffold é o layout base que fornece estrutura padrão com TopBar, BottomBar e conteúdo
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Tours") },
                navigationIcon = {
                    IconButton(onClick = { /* ação de voltar */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF2364C8), Color(0xFF113162))
                        )
                    ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },

        bottomBar = {
            // Barra de navegação inferior com 4 opções principais do app
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = { /* ação Explorar */ },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* ação Tours */ },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Tours") },
                    label = { Text("Tours") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = false,
                    onClick = { /* Ação */ }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { /* ação Perfil */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }

    ) { padding ->
        // Conteúdo principal da tela
        Column(Modifier.padding(padding)) {
            // Componente de abas para filtrar os tours por status
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .padding(horizontal = 24.dp)
                            .height(2.dp),
                        color = Color(0xFF00D4FF)
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier.height(48.dp),
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color(0xFF00D4FF) else Color(0xFF6E6E6E),
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp,
                                letterSpacing = (-0.2).sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lista lazy (otimizada) que renderiza apenas os itens visíveis na tela
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tours) { tour ->
                    TourCard(tour = tour)
                }
            }
        }
    }
}

@Composable
fun TourCard(tour: Tour) {

    fun getStatusText(status: TourStatus): String {
        return when (status) {
            TourStatus.CONFIRMED -> "Concluído"
            TourStatus.PENDING -> "Pendente"
            TourStatus.CANCELED -> "Cancelado"
        }
    }

    fun getStatusColor(status: TourStatus): Color {
        return when (status) {
            TourStatus.CONFIRMED -> Color(0xFFDCFCE7) // Verde claro para concluído
            TourStatus.PENDING -> Color(0xFFFEF9C2)   // Amarelo claro para pendente
            TourStatus.CANCELED -> Color(0xFFFEC2C2)  // Vermelho claro para cancelado
        }
    }

    // Card que contém todas as informações do tour
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F6))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
            ) {
                // Layout em linha para imagem e informações do tour
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Placeholder para imagem do tour/cliente
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .background(Color(0xFF747481), RoundedCornerShape(10.dp))
                    )

                    Spacer(Modifier.width(10.dp))

                    // Informações principais e ações do tour
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Coluna com todas as informações detalhadas do tour
                        Column(Modifier.weight(1f)) {
                            Text(tour.guestName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                "${tour.location}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                "${tour.date} - ${tour.time} ",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Text(
                                "${tour.participants} participante${if (tour.participants > 1) "s" else ""}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                "${tour.price} • ${tour.tourName}",
                                color = Color(0xFF00D4FF),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                "${tour.tourType}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Coluna lateral com status e botão de chat
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            // Badge colorido mostrando o status do tour
                            Text(
                                getStatusText(tour.status),
                                color = Color.Black,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(
                                        getStatusColor(tour.status),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            )

                            Spacer(Modifier.height(6.dp))

                            // Botão para iniciar conversa com o cliente
                            Button(
                                onClick = { /* Abrir chat */ },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFB9CACE)
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text("Chat", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Seção adicional que aparece apenas para tours com status PENDENTE
                // Mostra botões para aceitar ou recusar o tour
                if (tour.status == TourStatus.PENDING) {
                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Botão verde para aceitar o tour
                        Button(
                            onClick = { /* ação aceitar */ },
                            modifier = Modifier
                                .width(130.dp)
                                .height(40.dp)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Aceitar", fontSize = 14.sp)
                        }

                        // Botão vermelho para recusar o tour
                        Button(
                            onClick = { /* ação recusar */ },
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA5761)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Recusar", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Visualizar a tela MyTourScreen no Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun MyTourScreenPreview() {
    MaterialTheme {
        MyTourScreen()
    }
}