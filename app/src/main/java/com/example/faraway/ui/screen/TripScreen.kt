package com.example.faraway.ui.screen

import BottomNavBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.ui.data.Trip
import com.example.faraway.ui.data.TripStatus
import com.example.faraway.ui.components.TripCard
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.PrimaryDark

@Composable
fun TripsScreen(navController: NavController) {
    val tabs = listOf("Próximas", "Concluídas", "Pendentes", "Canceladas")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Dados de exemplo (adicione mais para testar a rolagem)
    val allTrips = remember {
        listOf(
            Trip("Gabriel Pereira", "Lisboa, Portugal", "05 Out 2025", "10:00", "€25/hora", "3 horas", TripStatus.CONFIRMED, "url_gabriel"),
            Trip("Fátima Alves", "Porto, Portugal", "10 Out 2025", "Check-in: 14:00", "€45/diária", "3 noites", TripStatus.CONFIRMED, "url_fatima"),
            Trip("Lucas Martins", "Faro, Portugal", "15 Out 2025", "18:00", "Troca Cultural", "Café", TripStatus.PENDING, "url_lucas", details = "Troca Cultural • Café"),
            // ... mais itens
        )
    }

    val filteredTrips = when (tabs[selectedTabIndex]) {
        "Próximas" -> allTrips.filter { it.status == TripStatus.CONFIRMED || it.status == TripStatus.PENDING }
        "Concluídas" -> allTrips.filter { it.status == TripStatus.COMPLETED }
        "Pendentes" -> allTrips.filter { it.status == TripStatus.PENDING }
        "Canceladas" -> allTrips.filter { it.status == TripStatus.CANCELED }
        else -> allTrips
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Top Bar (Header)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryDark)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() },
                        tint = Color.White

                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Minhas Viagens",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // 2. Tabs de Filtro
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = Color.White,
                contentColor = AccentColor,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .fillMaxWidth()
                            .height(2.dp) // Altura da linha
                            .background(color = AccentColor) // Cor da linha
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) AccentColor else Color.Gray
                            )
                        }
                    )
                }
            }

            // 3. Lista de Viagens Filtradas
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
            ) {
                items(filteredTrips) { trip ->
                    TripCard(trip = trip)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    FarAwayTheme {
        TripsScreen(navController = rememberNavController())
    }
}