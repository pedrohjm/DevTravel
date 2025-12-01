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
import com.example.faraway.Destinations
import com.example.faraway.guideNavItems
import com.example.faraway.travelerNavItems

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com Chat para evitar conflito)
// -----------------------------------------------------------------
val ChatPrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val ChatAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val ChatTextColor = Color(0xFF333333) // Cor de texto padrão
val ChatCardBackground = Color(0xFFFFFFFF) // Fundo branco

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS E NAVEGAÇÃO
// -----------------------------------------------------------------

data class ChatItemData(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val imageUrl: String // Placeholder para a imagem
)

val sampleChats = listOf(
    ChatItemData(1, "Carlos Silva", "Que ótimo! Nos vemos às 10h então.", "10:30", 0, true, "url1"),
    ChatItemData(2, "Mariana Costa", "Sua reserva está confirmada!", "Ontem", 2, false, "url2"),
    ChatItemData(3, "Lucas Martins", "Vamos nos encontrar no café amanhã?", "2d atrás", 0, true, "url3"),
    ChatItemData(4, "Ana Souza", "Você pode me enviar os documentos?", "1 semana", 0, false, "url4"),
    ChatItemData(5, "Pedro Rocha", "Tudo certo para o tour de hoje.", "10:00", 1, false, "url5"), // CORRIGIDO: isOnline = false
)

// Placeholder para NavItem (data class)
data class ChatNavItem( // RENOMEADO AQUI
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Placeholder para BottomNavBar (Componente)
@Composable
fun ChatBottomNavBarPlaceholder(
        navController: NavController,
        navItems: List<NavItem>,
        startRoute: String
    ) {

    BottomAppBar(
        containerColor = ChatCardBackground,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == "chat", // Chat selecionado
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (item.route == "chat") ChatAccentColor else Color.Gray
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
fun ChatScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            ChatBottomNavBarPlaceholder(navController = navController, navItems = travelerNavItems, Destinations.CHAT_ROUTE)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ChatTopBar()
            ChatSearchBar()
            ChatStatusTabs()
            ChatList(chats = sampleChats)
        }
    }
}

// -----------------------------------------------------------------
// 1. TOP BAR
// -----------------------------------------------------------------

@Composable
fun ChatTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatPrimaryBlue)
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
fun ChatSearchBar() {
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
            cursorColor = ChatTextColor
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
fun ChatStatusTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatusItem(number = 2, label = "Ativos", isSelected = true)
        StatusItem(number = 3, label = "Não Lidas", isSelected = false)
        StatusItem(number = 12, label = "Este mês", isSelected = false)
    }
}

@Composable
fun StatusItem(number: Int, label: String, isSelected: Boolean) {
    val color = if (isSelected) ChatAccentColor else Color.Gray
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
fun ChatList(chats: List<ChatItemData>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(chats) { chat ->
            ChatItem(chat = chat)
            Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
        }
    }
}

@Composable
fun ChatItem(chat: ChatItemData) {
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

        // Nome e Mensagem
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ChatTextColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Hora e Badge de Não Lidas
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = chat.time,
                fontSize = 12.sp,
                color = if (chat.unreadCount > 0) ChatAccentColor else Color.Gray
            )
            if (chat.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(ChatAccentColor),
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
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    MaterialTheme {
        ChatScreen(navController = rememberNavController())
    }
}
