package com.example.faraway.ui.screen

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
// import androidx.compose.material3.FlowRow (Removido pois FlowRow está em foundation.layout)
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Definição das Cores Personalizadas para replicar o design
val LightTeal = Color(0xFFE0F7FA) // Cor de fundo principal
val PrimaryTeal = Color(0xFF00BCD4) // Cor de destaque (ícones, botões)
val LightBlueChip = Color(0xFFE0F7FA) // Cor de fundo dos chips e itens de lista
val DarkText = Color(0xFF263238) // Cor do texto principal
val SecondaryText = Color(0xFF78909C) // Cor do texto secundário

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
// 2. Componente principal da tela
@Composable
fun InterestsScreen() {
    // Usando um Scaffold para a estrutura básica da tela
    Scaffold(
        topBar = {
            // Top App Bar simples com o ícone de voltar
            TopAppBar(
                title = { Text("Interesses e lugares visitados", color = DarkText) },
                navigationIcon = {
                    IconButton(onClick = { /* Ação de voltar */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightTeal, // Cor de fundo da barra superior
                    titleContentColor = DarkText
                )
            )
        },
        // O fundo da tela é LightTeal
        modifier = Modifier.fillMaxSize().background(LightTeal)
    ) { paddingValues ->
        // Conteúdo principal, alinhado ao centro para o card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // O Card principal com cantos arredondados e sombra
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Seção de Cabeçalho (Ícone, Título, Subtítulo e Menu)
                    HeaderSection()
                    Spacer(modifier = Modifier.height(24.dp))

                    // Seção de Paixões (Chips)
                    Text(
                        text = "Paixões",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    InterestsChips(
                        interests = listOf("Fotografia", "Viagens", "Café Especial", "Cultura")
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Seção de Lugares Visitados (Lista)
                    Text(
                        text = "Lugares visitados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    VisitedPlacesList(
                        places = listOf(
                            Place("Estocolmo", "Suécia"),
                            Place("Grécia", "Balcãs"),
                            Place("Lisboa", "Portugal")
                        )
                    )
                }
            }
        }
    }
}

// 3. Componente da Seção de Cabeçalho
@Composable
fun HeaderSection() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Ícone de Coração (Grande)
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Interesses",
                tint = PrimaryTeal,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(LightBlueChip)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Título e Subtítulo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Interesses e lugares\nvisitados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    lineHeight = 22.sp
                )
                Text(
                    text = "Suas paixões e lugares visitados",
                    fontSize = 12.sp,
                    color = SecondaryText
                )
            }

            // Ícone de Menu
            IconButton(onClick = { /* Ação do menu */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = PrimaryTeal
                )
            }
        }

        // Botão "Adicionar" flutuante, alinhado à direita e ligeiramente abaixo do título
        Button(
            onClick = { /* Ação de adicionar */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal.copy(alpha = 0.8f)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 50.dp) // Ajuste para posicionar como na imagem
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Adicionar",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Adicionar", color = Color.White, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
// 4. Componente para os Chips de Interesses
@Composable
fun InterestsChips(interests: List<String>) {
    // FlowRow para quebrar os chips em múltiplas linhas se necessário
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        interests.forEach { interest ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LightBlueChip,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = interest,
                    color = PrimaryTeal,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// 5. Data Class para os Lugares Visitados
data class Place(val name: String, val subtitle: String)

// 6. Componente para a Lista de Lugares Visitados
@Composable
fun VisitedPlacesList(places: List<Place>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        places.forEach { place ->
            VisitedPlaceItem(place = place)
        }
    }
}

// 7. Componente para um Item da Lista de Lugares Visitados
@Composable
fun VisitedPlaceItem(place: Place) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightBlueChip,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = place.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = place.subtitle,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
    }
}

// 8. Preview para visualização no Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewInterestsScreen() {
    // Um tema simples para o preview
    MaterialTheme(
        colorScheme = androidx.compose.material3.lightColorScheme(
            primary = PrimaryTeal,
            background = LightTeal,
            surface = Color.White
        )
    ) {
        InterestsScreen()
    }
}