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
import com.example.faraway.guideNavItems
import com.example.faraway.ui.data.Tour
import com.example.faraway.ui.data.TourStatus
import com.example.faraway.ui.theme.FarAwayTheme

//Tela principal que exibe a lista de tours do guia turístico.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTourScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximos", "Concluídos", "Pendente", "Cancelados")
    val allTours = remember {
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
    val filteredTours = when (selectedTab) {
        0 -> allTours.filter { it.status == TourStatus.CONFIRMED }
        1 -> emptyList()
        2 -> allTours.filter { it.status == TourStatus.PENDING }
        3 -> allTours.filter { it.status == TourStatus.CANCELED }
        else -> allTours
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Tours") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            BottomNavBar(
                navController = navController,
                navItems = guideNavItems,
                startRoute = Destinations.HOST_DASHBOARD_ROUTE
            )
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Destinations.GUIDE_DASHBOARD_ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Search,
                        contentDescription = "Explorar") },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Já estamos aqui */ },
                    icon = { Icon(Icons.Default.DateRange,
                        contentDescription = "Tours") },
                    label = { Text("Tours") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = false,
                    onClick = {
                        navController.navigate(Destinations.GUIDE_CHAT_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Destinations.GUIDE_PROFILE_ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
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
            if (filteredTours.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum tour encontrado.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTours) { tour ->
                        TourCard(tour = tour)
                    }
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
            TourStatus.CONFIRMED -> Color(0xFFDCFCE7)
            TourStatus.PENDING -> Color(0xFFFEF9C2)
            TourStatus.CANCELED -> Color(0xFFFEC2C2)
        }
    }
    val acceptColor = Color(0xFF16A34A)
    val declineColor = Color(0xFFEF4444)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .background(Color(0xFF747481), RoundedCornerShape(10.dp))
                    )
                    Spacer(Modifier.width(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
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

                            OutlinedButton(
                                onClick = { /* Abrir chat */ },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("Chat", fontSize = 12.sp)
                            }
                        }
                    }
                }
                if (tour.status == TourStatus.PENDING) {
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* ação aceitar */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, acceptColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = acceptColor)
                        ) {
                            Text("Aceitar", fontSize = 14.sp)
                        }
                        OutlinedButton(
                            onClick = { /* ação recusar */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, declineColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = declineColor)
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
fun MyTourScreenPreview() {
    FarAwayTheme {
        MyTourScreen(navController = rememberNavController())
    }
}