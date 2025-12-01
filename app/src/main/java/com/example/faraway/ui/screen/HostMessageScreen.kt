// ui/screen/HostMessageScreen.kt
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// -----------------------------------------------------------------
// CORES AUXILIARES (Prefixadas com HostMessage para evitar conflito)
// -----------------------------------------------------------------
// Cores do HostChatScreen.kt
val HostMessagePrimaryColor = Color(0xFF00838F) // Azul escuro do cabeçalho (Dark Teal/Cyan)
val HostMessageAccentColor = Color(0xFF00BCD4) // Cor de destaque (Bright Cyan/Turquoise)
val HostMessageBackground = Color(0xFFF0F0F0) // Fundo cinza claro
val HostMessageBubbleReceived = Color(0xFFFFFFFF) // Balão de mensagem recebida (branco)
val HostMessageBubbleSent = HostMessageAccentColor // Balão de mensagem enviada (ciano)
val HostMessageTextColor = Color(0xFF333333) // Cor de texto padrão

// -----------------------------------------------------------------
// PLACEHOLDERS PARA DADOS
// -----------------------------------------------------------------

enum class HostMessageSender { ME, OTHER } // CORRIGIDO: Renomeado para evitar redeclaração

data class HostMessage( // RENOMEADO
    val text: String?, // Texto da mensagem (opcional para mensagens com imagem)
    val time: String,
    val sender: HostMessageSender, // CORRIGIDO: Usando HostMessageSender
    val imageUrls: List<String> = emptyList() // URLs das imagens (placeholders)
)

val sampleHostMessages = listOf( // RENOMEADO
    HostMessage("Pronto, já foi confirmado, a que horas posso fazer o check-in?", "11:45", HostMessageSender.OTHER), // CORRIGIDO
    HostMessage("Isso mesmo, Sofia. É só clicar no botão \"Reservar\". Assim que a reserva for confirmada, eu te envio uma mensagem com todos os detalhes do check-in.", "11:26", HostMessageSender.ME), // CORRIGIDO
    HostMessage("Excelente! Queremos reservar! Como faço? Prossigo pelo aplicativo mesmo?", "11:25", HostMessageSender.OTHER), // CORRIGIDO
    HostMessage(
        text = null,
        time = "11:19",
        sender = HostMessageSender.ME, // CORRIGIDO
        imageUrls = listOf("img1", "img2", "img3", "img4", "img5") // Mensagem com imagens
    ),
    HostMessage("Olá, Sofia! Tudo ótimo, e com você? Vou mandar umas fotos.", "11:15", HostMessageSender.ME), // CORRIGIDO
    HostMessage("Olá, Fátima! Tudo bem? Vi o anúncio da sua casa e parece incrível, pode me mandar mais fotos?", "11:14", HostMessageSender.OTHER), // CORRIGIDO
)

// -----------------------------------------------------------------
// COMPONENTE PRINCIPAL
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostMessageScreen(navController: NavController) { // RENOMEADO
    Scaffold(
        topBar = { HostMessageHeader() }, // RENOMEADO
        bottomBar = { HostMessageInput() }, // RENOMEADO
        containerColor = HostMessageBackground // RENOMEADO
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            reverseLayout = true, // Para que as mensagens mais recentes fiquem na parte inferior
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(sampleHostMessages) { message -> // Usando a lista de mensagens do Host
                HostMessageBubble(message = message) // RENOMEADO
            }
        }
    }
}

// -----------------------------------------------------------------
// 1. HEADER
// -----------------------------------------------------------------

@Composable
fun HostMessageHeader() { // RENOMEADO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HostMessagePrimaryColor) // RENOMEADO
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
                    .border(1.5.dp, HostMessagePrimaryColor, CircleShape) // RENOMEADO
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nome e Status
        Column {
            Text(
                text = "Sofia Lima", // Nome do Hóspede
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

// -----------------------------------------------------------------
// 2. MESSAGE BUBBLE (Principal)
// -----------------------------------------------------------------

@Composable
fun HostMessageBubble(message: HostMessage) { // RENOMEADO
    val isMe = message.sender == HostMessageSender.ME // CORRIGIDO
    val bubbleColor = if (isMe) HostMessageBubbleSent else HostMessageBubbleReceived // RENOMEADO
    val textColor = if (isMe) Color.White else HostMessageTextColor // RENOMEADO
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
        // Balão de Mensagem
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp) // Limita a largura do balão
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(
                    start = if (message.imageUrls.isEmpty()) 12.dp else 0.dp,
                    top = if (message.imageUrls.isEmpty()) 8.dp else 0.dp,
                    end = if (message.imageUrls.isEmpty()) 12.dp else 0.dp,
                    bottom = if (message.imageUrls.isEmpty()) 8.dp else 0.dp
                ),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (message.imageUrls.isNotEmpty()) {
                HostMessageImageBubble(message = message) // NOVO COMPONENTE
            } else {
                // Mensagem de Texto
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = message.text ?: "",
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
    }
}

// -----------------------------------------------------------------
// 2.1. IMAGE BUBBLE (Mensagem com Imagens)
// -----------------------------------------------------------------

@Composable
fun HostMessageImageBubble(message: HostMessage) { // NOVO COMPONENTE
    val isMe = message.sender == HostMessageSender.ME // CORRIGIDO
    val totalImages = message.imageUrls.size
    val remainingImages = totalImages - 2 // +3 no mockup

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Imagem 1 (Placeholder)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = if (isMe) 16.dp else 4.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder para a imagem real
                Text("Imagem 1", color = Color.DarkGray)
            }

            // Imagem 2 com Overlay (+N) (Placeholder)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = if (isMe) 4.dp else 16.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder para a imagem real
                Text("Imagem 2", color = Color.DarkGray)

                // Overlay +N
                if (remainingImages > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+$remainingImages",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Time stamp no canto inferior direito do balão de imagem
        Text(
            text = message.time,
            color = if (isMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, bottom = 4.dp, top = 4.dp)
        )
    }
}

// -----------------------------------------------------------------
// 3. INPUT FIELD
// -----------------------------------------------------------------

@Composable
fun HostMessageInput() { // RENOMEADO
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
                focusedContainerColor = HostMessageBackground, // RENOMEADO
                unfocusedContainerColor = HostMessageBackground, // RENOMEADO
                cursorColor = HostMessageAccentColor // RENOMEADO
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
                .background(HostMessageAccentColor) // RENOMEADO
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar",
                tint = Color.White
            )
        }
    }
}

// -----------------------------------------------------------------
// PREVIEW
// -----------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun HostMessageScreenPreview() { // RENOMEADO
    MaterialTheme {
        HostMessageScreen(navController = rememberNavController()) // RENOMEADO
    }
}
