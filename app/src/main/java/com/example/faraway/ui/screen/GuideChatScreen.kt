// ui/screen/GuideChatScreen.kt
package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com GuideChat para evitar conflito)
// -----------------------------------------------------------------
val GuideChatPrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val GuideChatAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val GuideChatTextColor = Color(0xFF333333) // Cor de texto padrão
val GuideChatCardBackground = Color(0xFFFFFFFF) // Fundo branco

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS E NAVEGAÇÃO
// -----------------------------------------------------------------

data class GuideChatItemData( // RENOMEADO
    val id: Int,
    val name: String,
    val lastMessage: String,
    val tourType: String, // NOVO CAMPO
    val time: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val imageUrl: String // Placeholder para a imagem
)

val sampleGuideChats = listOf( // RENOMEADO
    GuideChatItemData(1, "Maria Silva", "Obrigada! Estou ansiosa pelo tour amanhã!", "Tour Histórico", "Agora", 1, true, "url1"),
    GuideChatItemData(2, "Pedro Costa", "Podemos incluir uma visita à pastelaria de Porto?", "Tour Gastronômico", "10:07", 2, true, "url2"),
    GuideChatItemData(3, "Ana Santos", "Podemos cancelar? Tive um imprevisto.", "Tour Cultural", "Ontem", 0, false, "url3"),
    GuideChatItemData(4, "Ricardo Alves", "Qual o melhor horário para ver o por do sol?", "Tour Fotográfico", "2d atrás", 0, false, "url4"),
)

// Placeholder para NavItem (data class)
data class GuideChatNavItem( // RENOMEADO
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Placeholder para BottomNavBar (Componente)
@Composable
fun GuideChatBottomNavBarPlaceholder(navController: NavController) { // RENOMEADO
    val navItems = listOf(
        GuideChatNavItem("explore", Icons.Filled.Search, "Explorar"),
        GuideChatNavItem("tours", Icons.Filled.DateRange, "Tours"), // Tours em vez de Viagens
        GuideChatNavItem("chat", Icons.AutoMirrored.Filled.Chat, "Chat"),
        GuideChatNavItem("perfil", Icons.Filled.Person, "Perfil")
    )

    BottomAppBar(
        containerColor = GuideChatCardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "chat", // Chat selecionado
                onClick = { /* Ação de Navegação */ },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (item.route == "chat") GuideChatAccentColor else Color.Gray
                    )
                },
                label = { Text(item.label, fontSize = 10.sp) }
            )
        }
    }
}

// -----------------------------------------------------------------
// COMPONENTE PRINCIPAL
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideChatScreen(navController: NavController) { // RENOMEADO
    Scaffold(
        bottomBar = {
            GuideChatBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GuideChatTopBar()
            GuideChatSearchBar()
            GuideChatStatusTabs()
            GuideChatList(chats = sampleGuideChats) // USO ATUALIZADO
        }
    }
}

// -----------------------------------------------------------------
// 1. TOP BAR
// -----------------------------------------------------------------

@Composable
fun GuideChatTopBar() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideChatPrimaryBlue)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Ação de Voltar */ }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Conversas com Clientes",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// -----------------------------------------------------------------
// 2. SEARCH BAR
// -----------------------------------------------------------------

@Composable
fun GuideChatSearchBar() { // RENOMEADO
    OutlinedTextField(
        value = "",
        onValueChange = { /* Lógica de pesquisa */ },
        placeholder = { Text("Buscar clientes...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = GuideChatTextColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    )
}

// -----------------------------------------------------------------
// 3. STATUS TABS
// -----------------------------------------------------------------

@Composable
fun GuideChatStatusTabs() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        GuideStatusItem(number = 2, label = "Ativos", isSelected = true) // USO ATUALIZADO
        GuideStatusItem(number = 3, label = "Não Lidas", isSelected = false) // USO ATUALIZADO
        GuideStatusItem(number = 12, label = "Este mês", isSelected = false) // USO ATUALIZADO
    }
}

@Composable
fun GuideStatusItem(number: Int, label: String, isSelected: Boolean) { // RENOMEADO
    val color = if (isSelected) GuideChatAccentColor else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = color
        )
    }
}

// -----------------------------------------------------------------
// 4. CHAT LIST
// -----------------------------------------------------------------

@Composable
fun GuideChatList(chats: List<GuideChatItemData>) { // RENOMEADO
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(chats) { chat ->
            GuideChatItem(chat = chat) // USO ATUALIZADO
            Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
        }
    }
}

@Composable
fun GuideChatItem(chat: GuideChatItemData) { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto de Perfil com Status Online
        Box(contentAlignment = Alignment.BottomEnd) {
            // Placeholder para a imagem de perfil
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White)
            }

            // Ponto de Status Online
            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nome, Mensagem e Tipo de Tour
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chat.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GuideChatTextColor
                )
                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) GuideChatAccentColor else Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.tourType,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GuideChatAccentColor // Cor de destaque para o tipo de tour
            )
        }

        // Badge de Não Lidas (movido para o lado direito da mensagem na imagem)
        if (chat.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(GuideChatAccentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun GuideChatScreenPreview() { // RENOMEADO
    MaterialTheme {
        GuideChatScreen(navController = rememberNavController())
    }
}
