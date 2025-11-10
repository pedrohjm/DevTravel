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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.ui.theme.TagColor
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
data class Guide(
    val name: String,
    val location: String,
    val description: String,
    val price: String,
    val rating: Double,
    val reviewCount: Int,
    val type: String, // "Guia" ou "Anfitrião"
    val imageUrl: String // URL ou ID do recurso da imagem
)

// ui.components.GuideCard.kt
@Composable
fun GuideCard(guide: Guide) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 1. Imagem e Tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Imagem (Use Coil ou Glide para carregar a imagem real)
                // Exemplo com um placeholder:
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray) // Cor de fundo cinza para o placeholder
                ) {
                    // Se quiser, pode adicionar um ícone de imagem no centro do Box
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Placeholder de Imagem",
                        tint = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                    )
                }

                // Tag "Guia" / "Anfitrião"
                Text(
                    text = guide.type,
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

            // 2. Conteúdo do Cartão
            Column(modifier = Modifier.padding(16.dp)) {
                // Nome e Avaliação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = guide.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Avaliação",
                            tint = Color(0xFFFFC107), // Amarelo para estrela
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${guide.rating} (${guide.reviewCount})",
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
                    Text(text = guide.location, fontSize = 14.sp)
                }
                Spacer(Modifier.height(8.dp))

                // Descrição
                Text(text = guide.description, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))

                // Preço e Botão
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = guide.price,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryDark
                    )
                    Button(
                        onClick = { /* Ação do botão */ },
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

// ui.components.SearchBar.kt
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
        // TextField para a entrada de texto
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

// ui.components.FilterTabs.kt
@Composable
fun FilterTabs(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
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
fun MainScreen(navController: NavController) {
    // Exemplo de dados
    val guideList = remember {
        listOf(
            Guide("Gabriel Pereira", "Lisboa, Portugal", "Guia especializado em história e cultura portuguesa. Tours personalizados.", "€25/hora", 4.9, 127, "Guia", "url_gabriel"),
            Guide("Maria Silva", "Porto, Portugal", "Anfitriã com casa de campo para aluguel. Experiências locais.", "€50/noite", 4.7, 85, "Anfitrião", "url_maria"),
            // Adicione mais itens aqui
        )
    }

    // Estado para o filtro selecionado
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Guias", "Anfitriões", "Amigos")

    // Estrutura principal da tela
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
                startRoute = Destinations.EXPLORE_ROUTE // Rota inicial do NavHost
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Header (Topo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryDark)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Olá, Membro! 👋",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                SearchBar()
            }

            // 2. Tabs/Filtros
            FilterTabs(
                filters = filters,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // 3. Conteúdo Principal (Lista de Cards)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(guideList) { guide ->
                    GuideCard(guide = guide)
                }
            }
        }
    }
}

// Componente de Navegação Inferior
@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        // Exemplo de itens de navegação
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Explorar") },
            label = { Text("Explorar", color = AccentColor) },
            selected = true, // Item selecionado
            onClick = { /* Ação */ },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = AccentColor)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Viagens") },
            label = { Text("Viagens") },
            selected = false,
            onClick = { /* Ação */ }
        )
        NavigationBarItem(
            icon = {Icon(Icons.Filled.People, contentDescription = "Social") },
            label = { Text("Social") },
            selected = false,
            onClick = { /* Ação */ }
        )
        NavigationBarItem(
            icon = {Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")},
            label = { Text("Chat") },
            selected = false,
            onClick = { /* Ação */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = false,
            onClick = { /* Ação */ }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    FarAwayTheme {
        MainScreen(navController = rememberNavController())
    }
}
