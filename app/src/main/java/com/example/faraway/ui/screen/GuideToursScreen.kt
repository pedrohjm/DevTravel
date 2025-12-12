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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.faraway.Destinations
import com.example.faraway.ui.data.Tour
import com.example.faraway.ui.data.TourStatus
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.viewmodel.TourViewModel
import com.example.faraway.utils.Resource
import com.example.faraway.ui.theme.AccentColor // Assumindo que AccentColor é a cor de destaque

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTourScreen(
    navController: NavController,
    viewModel: TourViewModel = viewModel() // Injeção do ViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximos", "Concluídos", "Pendentes", "Cancelados")

    // Observa o estado dos tours do ViewModel
    val toursResource by viewModel.tours.collectAsState()

    // Extrai a lista de tours se o estado for Success
    val allTours = remember(toursResource) {
        if (toursResource is Resource.Success) {
            (toursResource as Resource.Success<List<Tour>>).data ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Aplica o filtro na lista de tours
    val filteredTours = remember(allTours, selectedTab) {
        when (tabs[selectedTab]) {
            "Próximos" -> allTours.filter { it.status == TourStatus.CONFIRMED || it.status == TourStatus.PENDING }
            "Concluídos" -> allTours.filter { it.status == TourStatus.COMPLETED }
            "Pendentes" -> allTours.filter { it.status == TourStatus.PENDING }
            "Cancelados" -> allTours.filter { it.status == TourStatus.CANCELED }
            else -> allTours
        }
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
            // Sistema de tabs com indicador customizado
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

            // Conteúdo (Loading, Error ou Lista)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                when (toursResource) {
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Error -> {
                        Text("Erro ao carregar tours: ${toursResource.message}", color = Color.Red)
                    }
                    is Resource.Success -> {
                        if (filteredTours.isEmpty()) {
                            Text("Nenhum tour encontrado nesta categoria.", color = Color.Gray)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(filteredTours) { tour ->
                                    TourCard(tour = tour)
                                }
                            }
                        }
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
            TourStatus.CONFIRMED -> "Confirmado"
            TourStatus.PENDING -> "Pendente"
            TourStatus.CANCELED -> "Cancelado"
            TourStatus.COMPLETED -> "Concluído"
        }
    }

    fun getStatusColor(status: TourStatus): Color {
        return when (status) {
            TourStatus.CONFIRMED -> Color(0xFFE6FFEE) // Verde claro
            TourStatus.PENDING -> Color(0xFFFFFBEB) // Amarelo claro
            TourStatus.CANCELED -> Color(0xFFFFEBEB) // Vermelho claro
            TourStatus.COMPLETED -> Color(0xFFEBF8FF) // Azul claro
        }
    }

    fun getStatusTextColor(status: TourStatus): Color {
        return when (status) {
            TourStatus.CONFIRMED -> Color(0xFF10B981) // Verde escuro
            TourStatus.PENDING -> Color(0xFFF59E0B) // Amarelo escuro
            TourStatus.CANCELED -> Color(0xFFEF4444) // Vermelho escuro
            TourStatus.COMPLETED -> Color(0xFF3B82F6) // Azul escuro
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // 1. Imagem do Viajante (Tamanho Fixo e com corte na borda do card)
                Box(
                    modifier = Modifier
                        .size(90.dp) // Tamanho compacto
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                        model = tour.imageUrl,
                        contentDescription = "Foto do Viajante",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Ponto de Status (se necessário, como na imagem)
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF10B981)) // Verde
                            .align(Alignment.TopStart)
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                // 2. Detalhes do Tour
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top // Compacto
                ) {
                    // Nome e Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tour.guestName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                        // Status Tag
                        Text(
                            text = getStatusText(tour.status),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = getStatusTextColor(tour.status),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(getStatusColor(tour.status))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Localização
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Localização",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = tour.location,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Data e Hora
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Data",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = tour.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Hora",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = tour.time,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Preço e Nome do Tour
                    Text(
                        text = "${tour.price} • ${tour.tourName}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = AccentColor // Cor de destaque para preço
                    )
                }
            }

            // Botões de Ação (Canto Inferior Direito)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão Chat
                OutlinedButton(
                    onClick = { /* Ação Chat */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF333333)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Chat", fontSize = 14.sp)
                }

                // Botões Aceitar/Recusar (Apenas para Pendentes)
                if (tour.status == TourStatus.PENDING) {
                    Spacer(Modifier.width(12.dp))

                    // Botão Aceitar
                    Button(
                        onClick = { /* Ação Aceitar */ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Aceitar", fontSize = 14.sp, color = Color.White)
                    }

                    Spacer(Modifier.width(8.dp))

                    // Botão Recusar
                    OutlinedButton(
                        onClick = { /* Ação Recusar */ },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Recusar", fontSize = 14.sp)
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