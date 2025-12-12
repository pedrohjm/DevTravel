package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Cores
val ChatAccentCyan = Color(0xFF00BCD4)
val ChatBackground = Color(0xFFF5F5F5)
val ChatOnlineGreen = Color(0xFF4CAF50)
val ChatUnreadBadge = Color(0xFF00BCD4)

// Modelo de dados para conversa
data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarUrl: String,
    val isOnline: Boolean,
    val unreadCount: Int = 0
)

@Composable
fun ChatFriendsScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(1) } // 0: Explorar, 1: Chat, 2: Perfil

    // Dados de exemplo
    val conversations = listOf(
        Conversation(
            id = "1",
            name = "Carlos Silva",
            lastMessage = "Que ótimo! Nos veremos às 10h então.",
            timestamp = "10:30",
            avatarUrl = "https://via.placeholder.com/48",
            isOnline = true,
            unreadCount = 0
        ),
        Conversation(
            id = "2",
            name = "Mariana Costa",
            lastMessage = "Sua reserva está confirmada!",
            timestamp = "Ontem",
            avatarUrl = "https://via.placeholder.com/48",
            isOnline = false,
            unreadCount = 2
        ),
        Conversation(
            id = "3",
            name = "Lucas Martins",
            lastMessage = "Vamos nos encontrar no café amanhã?",
            timestamp = "2d atrás",
            avatarUrl = "https://via.placeholder.com/48",
            isOnline = true,
            unreadCount = 0
        )
    )

    // Filtrar conversas baseado na busca
    val filteredConversations = if (searchQuery.isEmpty()) {
        conversations
    } else {
        conversations.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChatBackground)
    ) {
        // Cabeçalho com título e botão de voltar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatPrimaryBlue)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController?.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Conversas com Amigos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Barra de busca
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatPrimaryBlue)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar clientes...", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )
        }

        // Estatísticas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatPrimaryBlue)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(label = "Ativas", value = "2")
                StatisticItem(label = "Não Lidas", value = "3")
                StatisticItem(label = "Este mês", value = "12")
            }
        }

        // Lista de conversas
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
        ) {
            items(filteredConversations) { conversation ->
                ConversationItem(conversation = conversation)
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color(0xFFEEEEEE)
                )
            }
        }

        // Navegação inferior
        BottomNavigationBar(selectedTab = selectedTab) { newTab ->
            selectedTab = newTab
        }
    }
}

@Composable
fun StatisticItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ConversationItem(conversation: Conversation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navegar para a conversa */ }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar com indicador de status
        Box {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = conversation.name.first().toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChatPrimaryBlue
                )
            }

            // Indicador de status online
            if (conversation.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(ChatOnlineGreen)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Conteúdo da conversa
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = conversation.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Text(
                text = conversation.lastMessage,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Timestamp e badge de não lidas
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = conversation.timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )

            if (conversation.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ChatUnreadBadge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Explorar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { onTabSelected(0) }
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = "Explorar",
                tint = if (selectedTab == 0) ChatPrimaryBlue else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Explorar",
                fontSize = 10.sp,
                color = if (selectedTab == 0) ChatPrimaryBlue else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Chat
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { onTabSelected(1) }
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = if (selectedTab == 1) ChatAccentCyan else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Chat",
                fontSize = 10.sp,
                color = if (selectedTab == 1) ChatAccentCyan else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Perfil
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { onTabSelected(2) }
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = if (selectedTab == 2) ChatAccentCyan else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Perfil",
                fontSize = 10.sp,
                color = if (selectedTab == 2) ChatAccentCyan else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatFriendsScreenPreview() {
    ChatFriendsScreen()
}