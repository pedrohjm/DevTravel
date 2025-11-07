package com.example.faraway

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ChatPage(messages: List<ChatMessage>) {
    var search by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 8.dp)
    ) {
        // Barra superior
        Text(
            text = "Mensagens",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Campo de busca
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            leadingIcon = { Icon(painterResource(id = R.drawable.ic_search), contentDescription = "Search") },
            placeholder = { Text("Buscar conversas...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        // Lista de mensagens
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { msg ->
                ChatItem(msg)
            }
        }
    }
}

@Composable
fun ChatItem(msg: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagem de perfil
        Box(
            modifier = Modifier.size(56.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            if (msg.isOnline) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00C853))
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = msg.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = msg.message,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(text = msg.time, fontSize = 12.sp, color = Color.Gray)
            if (msg.unreadCount > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                ) {
                    Text(
                        text = msg.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

