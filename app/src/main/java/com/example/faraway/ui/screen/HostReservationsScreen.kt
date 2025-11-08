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
import androidx.compose.material.icons.filled.Email
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
import com.example.faraway.ui.data.Reservation
import com.example.faraway.ui.data.ReservationStatus

/**
 * Tela principal que exibe a lista de reservas de hospedagem do anfitrião.
 *
 * Esta screen mostra as reservas organizadas em abas por status (Próximos, Concluídos,
 * Pendente, Cancelados) e permite ao anfitrião gerenciar suas reservas.
 *
 * @sample MyReservationScreenPreview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationScreen() {
    // Estado que controla qual aba está selecionada (0 = Próximos, 1 = Concluídos, etc.)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximos", "Concluídos", "Pendente", "Cancelados")

    // Lista de dados mockados para demonstração - em app real viria de uma API ou banco de dados
    val reservations = remember {
        listOf(
            Reservation("Sofia Lima",
                "Casa Inteira",
                "Alfama",
                "14 Out 2025",
                "24 Out 2025",
                "€70", "10",
                ReservationStatus.CONFIRMED,
                0,
                4),

            Reservation("Miguel Santos",
                "Quarto Privado",
                "Centro",
                "18 Out 2025",
                "20 Out 2025",
                "€55",
                "2",
                ReservationStatus.PENDING,
                0,
                1),

            Reservation("Carlos Silva",
                "Apartamento",
                "Chiado",
                "01 Nov 2025",
                "05 Nov 2025",
                "€90",
                "4",
                ReservationStatus.CANCELED,
                0,
                2)
        )
    }

    // Scaffold é o layout base que fornece estrutura padrão com TopBar, BottomBar e conteúdo
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Reservas") },
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
                            colors = listOf(Color(0xFF0892B4), Color(0xFF033F4E))
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
                    onClick = { /* ação Reservas */ },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
                    label = { Text("Reservas") }
                )

                NavigationBarItem(
                    icon = {Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")},
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
            // Componente de abas para filtrar as reservas por status
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
                items(reservations) { reservation ->
                    ReservationCard(reservation)
                }
            }
        }
    }
}

//Componente que representa um card individual de reserva na lista.

/**
  Exibe todas as informações da reserva: nome do hóspede, tipo de acomodação, localização,
  datas de check-in/check-out, número de hóspedes, preço e status. Também mostra botões
  de ação para reservas pendentes.
 */
@Composable
fun ReservationCard(reservation: Reservation) {

    fun getStatusText(status: ReservationStatus): String {
        return when (status) {
            ReservationStatus.CONFIRMED -> "Confirmado"
            ReservationStatus.PENDING -> "Pendente"
            ReservationStatus.CANCELED -> "Cancelado"
        }
    }

    fun getStatusColor(status: ReservationStatus): Color {
        return when (status) {
            ReservationStatus.CONFIRMED -> Color(0xFFDCFCE7) // Verde claro para confirmado
            ReservationStatus.PENDING -> Color(0xFFFEF9C2)   // Amarelo claro para pendente
            ReservationStatus.CANCELED -> Color(0xFFFEC2C2)  // Vermelho claro para cancelado
        }
    }

    // Card que contém todas as informações da reserva
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
                // Layout em linha para imagem e informações da reserva
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Placeholder para imagem da acomodação/hóspede
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .background(Color(0xFF747481), RoundedCornerShape(10.dp))
                    )

                    Spacer(Modifier.width(10.dp))

                    // Informações principais e ações da reserva
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Coluna com todas as informações detalhadas da reserva
                        Column(Modifier.weight(1f)) {
                            // Nome do hóspede
                            Text(reservation.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                            // Tipo de acomodação e localização
                            Text(
                                "${reservation.type} • ${reservation.location}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            // Data de check-in
                            Text(
                                "Check-in: ${reservation.checkIn}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            // Data de check-out
                            Text(
                                "Check-out: ${reservation.checkOut}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            // Número de hóspedes (com plural correto)
                            Text(
                                "${reservation.guests} hóspede${if (reservation.guests > 1) "s" else ""}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            // Preço por dia e número total de noites
                            Text(
                                "${reservation.price}/dia • ${reservation.nights} noites",
                                color = Color(0xFF00D4FF),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }

                        // Coluna lateral com status e botão de chat
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            // Badge colorido mostrando o status da reserva
                            Text(
                                getStatusText(reservation.status),
                                color = Color.Black,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(
                                        getStatusColor(reservation.status),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            )

                            Spacer(Modifier.height(6.dp))

                            // Botão para iniciar conversa com o hóspede
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

                // Seção adicional que aparece apenas para reservas com status PENDENTE
                // Mostra botões para aceitar ou recusar a reserva
                if (reservation.status == ReservationStatus.PENDING) {
                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Botão verde para aceitar a reserva
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

                        // Botão vermelho para recusar a reserva
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

@Preview(showBackground = true)
@Composable
fun MyReservationScreenPreview() {
    MaterialTheme {
        MyReservationScreen()
    }
}