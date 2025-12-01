// ui/screen/HostChatScreen.kt
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
// CORES AUXILIARES (Prefixadas com HostChat para evitar conflito)
// -----------------------------------------------------------------
// Cor do cabeçalho (Dark Teal/Cyan - um pouco mais escuro que o Accent)
val HostChatPrimaryColor = Color(0xFF00838F)
// Cor de destaque (Bright Cyan/Turquoise)
val HostChatAccentColor = Color(0xFF00BCD4)
val HostChatTextColor = Color(0xFF333333) // Cor de texto padrão
val HostChatCardBackground = Color(0xFFFFFFFF) // Fundo branco

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS E NAVEGAÇÃO
// -----------------------------------------------------------------

data class HostChatItemData( // RENOMEADO
    val id: Int,
    val name: String,
    val lastMessage: String,
    val accommodationType: String, // Tipo de acomodação
    val reservationStatusOrDate: String, // Status ou data da reserva
    val time: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val imageUrl: String // Placeholder para a imagem
)

val sampleHostChats = listOf( // RENOMEADO
    HostChatItemData(
        id = 1,
        name = "Sofia Lima",
        lastMessage = "A que horas posso fazer o check-in?",
        accommodationType = "Casa Inteira",
        reservationStatusOrDate = "10 Out",
        time = "10:45",
        unreadCount = 1,
        isOnline = true,
        imageUrl = "url1"
    ),
    HostChatItemData(
        id = 2,
        name = "Luiza Assis",
        lastMessage = "Adorei a hospedagem! Voltarei em breve.",
        accommodationType = "Quarto Privado",
        reservationStatusOrDate = "Concluída",
        time = "2d atrás",
        unreadCount = 0,
        isOnline = false,
        imageUrl = "url2"
    ),
    HostChatItemData(
        id = 3,
        name = "Carlos Mendes",
        lastMessage = "Tive um imprevisto, preciso cancelar.",
        accommodationType = "Apartamento",
        reservationStatusOrDate = "Cancelada",
        time = "Ontem",
        unreadCount = 0,
        isOnline = false, // CORRIGIDO: Carlos Mendes não está online
        imageUrl = "url3"
    ),
    HostChatItemData(
        id = 4,
        name = "Mariana Pires",
        lastMessage = "Onde fica o mercado mais próximo?",
        accommodationType = "Casa de Campo",
        reservationStatusOrDate = "25 Nov",
        time = "3d atrás",
        unreadCount = 0, // CORRIGIDO: Mariana Pires não tem mensagens não lidas
        isOnline = false,
        imageUrl = "url4"
    ),
)

// Placeholder para NavItem (data class)
data class HostChatNavItem( // RENOMEADO
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Placeholder para BottomNavBar (Componente)
@Composable
fun HostChatBottomNavBarPlaceholder(navController: NavController) { // RENOMEADO
    val navItems = listOf(
        HostChatNavItem("explore", Icons.Filled.Search, "Explorar"),
        HostChatNavItem("reservas", Icons.Filled.CalendarToday, "Reservas"), // Calendário para Reservas
        HostChatNavItem("chat", Icons.Filled.Chat, "Chat"), // Ícone corrigido
        HostChatNavItem("perfil", Icons.Filled.PersonOutline, "Perfil")
    )

    BottomAppBar(
        containerColor = HostChatCardBackground,
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
                        tint = if (item.route == "chat") HostChatAccentColor else Color.Gray
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
fun HostChatScreen(navController: NavController) { // RENOMEADO
    Scaffold(
        bottomBar = {
            HostChatBottomNavBarPlaceholder(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HostChatTopBar()
            HostChatSearchBar()
            HostChatStatusTabs()
            HostChatList(chats = sampleHostChats) // USO ATUALIZADO
        }
    }
}

// -----------------------------------------------------------------
// 1. TOP BAR
// -----------------------------------------------------------------

@Composable
fun HostChatTopBar() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HostChatPrimaryColor)
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
            text = "Conversas com Hóspedes",
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
fun HostChatSearchBar() { // RENOMEADO
    OutlinedTextField(
        value = "",
        onValueChange = { /* Lógica de pesquisa */ },
        placeholder = { Text("Buscar hóspedes...") }, // Placeholder atualizado
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = HostChatTextColor
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
fun HostChatStatusTabs() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        HostStatusItem(number = 1, label = "Ativos", isSelected = true) // USO ATUALIZADO
        HostStatusItem(number = 1, label = "Não Lidas", isSelected = false) // USO ATUALIZADO
        HostStatusItem(number = 7, label = "Este mês", isSelected = false) // USO ATUALIZADO
    }
}

@Composable
fun HostStatusItem(number: Int, label: String, isSelected: Boolean) { // RENOMEADO
    val color = if (isSelected) HostChatAccentColor else Color.Gray
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
fun HostChatList(chats: List<HostChatItemData>) { // RENOMEADO
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(chats) { chat ->
            HostChatItem(chat = chat) // USO ATUALIZADO
            Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
        }
    }
}

@Composable
fun HostChatItem(chat: HostChatItemData) { // RENOMEADO
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

            // Ponto de Status Online (Verde)
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

        // Nome, Mensagem e Detalhes da Reserva
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chat.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HostChatTextColor
                )
                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) HostChatAccentColor else Color.Gray
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Home, // Ícone de Casa
                    contentDescription = "Acomodação",
                    modifier = Modifier.size(16.dp),
                    tint = HostChatAccentColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${chat.accommodationType} • ${chat.reservationStatusOrDate}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HostChatAccentColor // Cor de destaque para os detalhes da reserva
                )
            }
        }

        // Badge de Não Lidas
        if (chat.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(HostChatAccentColor),
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
fun HostChatScreenPreview() { // RENOMEADO
    MaterialTheme {
        HostChatScreen(navController = rememberNavController())
    }
}
