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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.data.Trip
import com.example.faraway.ui.data.TripStatus
import com.example.faraway.ui.components.TripCard
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.ui.viewmodel.TripViewModel
import com.example.faraway.utils.Resource

@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripViewModel = viewModel() // Injeção do ViewModel
) {
    val tabs = listOf("Próximas", "Concluídas", "Pendentes", "Canceladas")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Observa o estado das viagens do ViewModel
    val tripsResource by viewModel.trips.collectAsState()

    // Extrai a lista de viagens se o estado for Success
    val allTrips = remember(tripsResource) {
        if (tripsResource is Resource.Success) {
            (tripsResource as Resource.Success<List<Trip>>).data ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Aplica o filtro na lista de viagens
    val filteredTrips = remember(allTrips, selectedTabIndex) {
        when (tabs[selectedTabIndex]) {
            "Próximas" -> allTrips.filter { it.status == TripStatus.CONFIRMED || it.status == TripStatus.PENDING }
            "Concluídas" -> allTrips.filter { it.status == TripStatus.COMPLETED }
            "Pendentes" -> allTrips.filter { it.status == TripStatus.PENDING }
            "Canceladas" -> allTrips.filter { it.status == TripStatus.CANCELED }
            else -> allTrips
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
                startRoute = Destinations.EXPLORE_ROUTE
            )
        }
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
                            .height(2.dp)
                            .background(color = AccentColor)
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

            // 3. Conteúdo (Loading, Error ou Lista)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (tripsResource) {
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Error -> {
                        Text("Erro ao carregar viagens: ${tripsResource.message}", color = Color.Red)
                    }
                    is Resource.Success -> {
                        if (filteredTrips.isEmpty()) {
                            Text("Nenhuma viagem encontrada nesta categoria.", color = Color.Gray)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                            ) {
                                items(filteredTrips) { trip ->
                                    TripCard(trip = trip)
                                }
                            }
                        }
                    }
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