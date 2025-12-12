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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations


val GuideMessagePrimaryBlue = Color(0xFF192F50) // Azul escuro do cabeçalho
val GuideMessageAccentColor = Color(0xFF00BCD4) // Cor de destaque (Ciano/Turquesa)
val GuideMessageBackground = Color(0xFFF0F0F0) // Fundo cinza claro
val GuideMessageBubbleReceived = Color(0xFFFFFFFF) // Balão de mensagem recebida (branco)
val GuideMessageBubbleSent = GuideMessageAccentColor // Balão de mensagem enviada (ciano)
val GuideMessageTextColor = Color(0xFF333333) // Cor de texto padrão



enum class MessageSender { ME, OTHER }

data class Message(
    val text: String,
    val time: String,
    val sender: MessageSender
)

val sampleGuideMessages = listOf( // RENOMEADO
    Message("Olá! Vi seu perfil e adoraria conhecer mais sobre os tours que você oferece.", "09:15", MessageSender.OTHER),
    Message("Olá! Que ótimo ter você por aqui! Eu ofereço tours personalizados pela cidade.", "09:50", MessageSender.ME),
    Message("Que legal! E como funciona? Você tem roteiros prontos ou montamos um juntos?", "10:00", MessageSender.OTHER),
    Message("Que bom que perguntou! O roteiro é totalmente personalizado. O que você mais gostaria de conhecer na cidade?", "10:05", MessageSender.ME),
    Message("Adorei a flexibilidade! Tenho muito interesse na parte de gastronomia.", "10:06", MessageSender.OTHER),
    Message("Podemos incluir uma visita à pastelaria de Porto?", "10:07", MessageSender.OTHER),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMessageScreen(navController: NavController) { // RENOMEADO
    Scaffold(
        topBar = { GuideMessageHeader(navController = navController) }, // RENOMEADO
        bottomBar = { GuideMessageInput() }, // RENOMEADO
        containerColor = GuideMessageBackground // RENOMEADO
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            reverseLayout = true, // Para que as mensagens mais recentes fiquem na parte inferior
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(sampleGuideMessages.reversed()) { message -> // RENOMEADO
                GuideMessageBubble(message = message) // RENOMEADO
            }
        }
    }
}


@Composable
fun GuideMessageHeader(navController: NavController) { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideMessagePrimaryBlue) // RENOMEADO
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            navController.navigate(Destinations.GUIDE_CHAT_ROUTE) {
                launchSingleTop = true
            }
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        // Foto de Perfil com Status Online
        Box(contentAlignment = Alignment.BottomEnd) {
            // Placeholder para a imagem de perfil
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color.White)
            }

            // Ponto de Status Online
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                    .border(1.5.dp, GuideMessagePrimaryBlue, CircleShape) // RENOMEADO
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nome e Status
        Column {
            Text(
                text = "Pedro Costa",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Online",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}



@Composable
fun GuideMessageBubble(message: Message) { // RENOMEADO
    val isMe = message.sender == MessageSender.ME
    val bubbleColor = if (isMe) GuideMessageBubbleSent else GuideMessageBubbleReceived // RENOMEADO
    val textColor = if (isMe) Color.White else GuideMessageTextColor // RENOMEADO
    val alignment = if (isMe) Alignment.End else Alignment.Start

    // Define o formato do balão
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isMe) 16.dp else 4.dp,
        bottomEnd = if (isMe) 4.dp else 16.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 300.dp) // Limita a largura do balão
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message.time,
                color = if (isMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
}



@Composable
fun GuideMessageInput() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone de Imagem
        IconButton(onClick = { /* Ação de Anexar Imagem */ }) {
            Icon(
                Icons.Filled.Image,
                contentDescription = "Anexar Imagem",
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Campo de Texto
        OutlinedTextField(
            value = "",
            onValueChange = { /* Lógica de Digitação */ },
            placeholder = { Text("Digite sua mensagem", color = Color.Gray) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = GuideMessageBackground, // RENOMEADO
                unfocusedContainerColor = GuideMessageBackground, // RENOMEADO
                cursorColor = GuideMessageAccentColor // RENOMEADO
            ),
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 48.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Botão de Enviar
        IconButton(
            onClick = { /* Ação de Enviar */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(GuideMessageAccentColor) // RENOMEADO
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar",
                tint = Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GuideMessageScreenPreview() { // RENOMEADO
    MaterialTheme {
        GuideMessageScreen(navController = rememberNavController()) // RENOMEADO
    }
}
