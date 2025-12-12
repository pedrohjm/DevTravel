package com.example.faraway.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.hostNavItems
import com.example.faraway.ui.data.Reservation
import com.example.faraway.ui.data.ReservationStatus
import com.example.faraway.ui.theme.FarAwayTheme

// Tela minhas reservas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximos", "Pendente", "Concluídos", "Cancelados")
    val allReservations = remember {
        listOf(
            Reservation("Sofia Lima", "Casa Inteira", "Alfama", "14 Out 2025", "24 Out 2025", "€70", "10",
                ReservationStatus.CONFIRMED, 0, 4),
            Reservation("Miguel Santos", "Quarto Privado", "Centro", "18 Out 2025", "20 Out 2025", "€55", "2",
                ReservationStatus.PENDING, 0, 1),
            Reservation("Carlos Silva", "Apartamento", "Chiado", "01 Nov 2025", "05 Nov 2025", "€90", "4",
                ReservationStatus.CANCELED, 0, 2),
            Reservation("Ana Pereira", "Studio", "Baixa", "01 Set 2025", "05 Set 2025", "€60", "4",
                ReservationStatus.CONFIRMED, 0, 2)
        )
    }
    val filteredReservations = when (selectedTab) {
        0 -> allReservations.filter { it.status == ReservationStatus.CONFIRMED }
        1 -> allReservations.filter { it.status == ReservationStatus.PENDING }
        2 -> emptyList()
        3 -> allReservations.filter { it.status == ReservationStatus.CANCELED }
        else -> allReservations
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Reservas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                modifier = Modifier.background(
                    Brush.horizontalGradient(listOf(Color(0xFF0892B4), Color(0xFF033F4E)))
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController, hostNavItems, Destinations.HOST_DASHBOARD_ROUTE)
            NavigationBar(containerColor = Color.White) {

                // EXPLORAR
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Destinations.HOST_DASHBOARD_ROUTE) { launchSingleTop = true } },
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            "Explorar",
                            tint = Color.Gray
                        )
                    },
                    label = { Text("Explorar", color = Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                // 🔵 RESERVAS (SELECIONADO)
                NavigationBarItem(
                    selected = true,
                    onClick = { /* já está aqui */ },
                    icon = {
                        Icon(
                            Icons.Default.DateRange,
                            "Reservas",
                            tint = Color(0xFF00BCD4)   // 🔵 azul
                        )
                    },
                    label = {
                        Text("Reservas", color = Color(0xFF00BCD4))   // 🔵 azul
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                // CHAT
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Destinations.HOST_CHAT_ROUTE) { launchSingleTop = true } },
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, "Chat", tint = Color.Gray) },
                    label = { Text("Chat", color = Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                // PERFIL
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Destinations.HOST_PERFIL_ROUTE) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Person, "Perfil", tint = Color.Gray) },
                    label = { Text("Perfil", color = Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF00BCD4),
                        selectedTextColor = Color(0xFF00BCD4),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray),
                )
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF00D4FF),
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color(0xFF00D4FF) else Color.Gray,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (filteredReservations.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma reserva encontrada.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredReservations) { reservation ->
                        ReservationCard(reservation)
                    }
                }
            }
        }
    }
}

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
            ReservationStatus.CONFIRMED -> Color(0xFFDCFCE7)
            ReservationStatus.PENDING -> Color(0xFFFEF9C2)
            ReservationStatus.CANCELED -> Color(0xFFFEC2C2)
        }
    }
    val acceptColor = Color(0xFF16A34A) // Verde
    val declineColor = Color(0xFFEF4444) // Vermelho

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(reservation.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${reservation.type} • ${reservation.location}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text("Check-in: ${reservation.checkIn}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Check-out: ${reservation.checkOut}", fontSize = 12.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${reservation.price}/dia • ${reservation.nights} noites",
                        color = Color(0xFF00838F), // Teal
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = getStatusColor(reservation.status),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = getStatusText(reservation.status),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* Chat */ },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Chat", fontSize = 12.sp)
                    }
                }
            }
            if (reservation.status == ReservationStatus.PENDING) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Botão Aceitar
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, acceptColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = acceptColor)
                    ) {
                        Text("Aceitar")
                    }

                    // Botão Recusar
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, declineColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = declineColor)
                    ) {
                        Text("Recusar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyReservationScreenPreview() {
    FarAwayTheme {
        MyReservationScreen(navController = rememberNavController())
    }
}