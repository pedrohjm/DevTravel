package com.example.faraway.ui.screen

import BottomNavBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.ui.theme.TagColor
import androidx.navigation.NavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.data.User
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.viewmodel.MainViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun GuideCard(user: User, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (user.profileImageUrl.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Placeholder de Imagem",
                            tint = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp)
                        )
                    }
                } else {
                    AsyncImage(
                        model = user.profileImageUrl,
                        contentDescription = "Foto do Guia",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Tag "Guia" ou "Anfitrião"
                Text(
                    text = user.role,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(TagColor, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }


            Column(modifier = Modifier.padding(16.dp)) {
                // Nome e Avaliação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Avaliação",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "4.9 (127)", // Placeholder
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))

                // Localização
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Localização",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = user.location ?: "--------", fontSize = 14.sp)
                }
                Spacer(Modifier.height(8.dp))

                // Descrição
                Text(text = user.description ?: "--------", fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))

                // Preço e Botão
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "€25/hora", // Placeholder
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryDark
                    )
                    Button(
                        onClick = {
                            navController.navigate("${Destinations.VIEW_PROFILE_ROUTE}/${user.uid}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Ver Perfil", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Ícone de Busca",
            tint = Color.DarkGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = "Buscar destino, guia, acomodação...",
            onValueChange = { /* Atualizar estado de busca */ },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray
            ),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Ícone de Localização",
            tint = AccentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun FilterTabs(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            val isSelected = filter == selectedFilter
            Button(
                onClick = { onFilterSelected(filter) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) PrimaryDark else Color.White,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(18.dp),
                border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(filter, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController, authViewModel: AuthViewModel, mainViewModel: MainViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    val guides by mainViewModel.guides.collectAsState()
    val hosts by mainViewModel.hosts.collectAsState()

    val combinedList = guides + hosts

    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Guias", "Anfitriões")

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
                startRoute = Destinations.EXPLORE_ROUTE
            ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryDark)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Olá, " + "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { "Carregando..." },
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                SearchBar()
            }

            // 2. Tabs/Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Centraliza o bloco de botões
            ) {
                FilterTabs(
                    filters = filters,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                val filteredList = when (selectedFilter) {
                    "Guias" -> combinedList.filter { it.role == "Guia" }
                    "Anfitriões" -> combinedList.filter { it.role == "Anfitrião" }
                    else -> combinedList
                }

                items(filteredList) { user ->
                    GuideCard(user = user, navController = navController)
                }
            }
        }
    }
}
