package com.example.faraway.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faraway.ui.data.Trip
import com.example.faraway.ui.data.TripStatus
// Importe as cores do seu tema
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.R // Certifique-se de que o R está importado

// Cores de Status (Defina no seu Color.kt ou use diretamente aqui)
val ConfirmedColor = Color(0xFFE8F5E9) // Verde claro para Confirmed
val PendingColor = Color(0xFFFFFDE7) // Amarelo claro para Pending
val ConfirmedTextColor = Color(0xFF4CAF50) // Verde escuro
val PendingTextColor = Color(0xFFFFC107) // Amarelo escuro

@Composable
fun TripCard(trip: Trip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Imagem do Parceiro
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray) // Placeholder de cor
            ) {
                // Imagem (Use Coil/Glide para carregar trip.imageUrl)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray), // Fundo cinza para o placeholder
                    contentAlignment = Alignment.Center // Centraliza o ícone
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image, // Ícone de imagem do Material Design
                        contentDescription = "Placeholder de Imagem",
                        tint = Color.Gray, // Cor do ícone
                        modifier = Modifier.size(48.dp) // Tamanho do ícone
                    )
                }

                // Ponto de Status (Se for Próxima Viagem)
                if (trip.status == TripStatus.CONFIRMED) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ConfirmedTextColor)
                            .align(Alignment.TopStart)
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // 2. Detalhes da Viagem
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.partnerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))

                // Localização
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Localização",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = trip.location, fontSize = 13.sp)
                }
                Spacer(Modifier.height(8.dp))

                // Data e Hora/Check-in
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Data",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = trip.date, fontSize = 13.sp)

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "Hora",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = trip.time, fontSize = 13.sp)
                }
                Spacer(Modifier.height(8.dp))

                // Preço e Duração
                Text(
                    text = "${trip.price} • ${trip.duration}",
                    fontSize = 13.sp,
                    color = AccentColor,
                    fontWeight = FontWeight.SemiBold
                )

                // Detalhes Adicionais (Toca Cultural)
                trip.details?.let {
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = PrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 3. Status e Botão Chat
            Column(horizontalAlignment = Alignment.End) {
                StatusTag(trip.status)
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { /* Abrir Chat */ },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChatBubbleOutline,
                        contentDescription = "Chat",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Chat", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatusTag(status: TripStatus) {
    val (text, backgroundColor, textColor) = when (status) {
        TripStatus.CONFIRMED -> Triple("Confirmado", ConfirmedColor, ConfirmedTextColor)
        TripStatus.PENDING -> Triple("Pendente", PendingColor, PendingTextColor)
        TripStatus.COMPLETED -> Triple("Concluído", ConfirmedColor, ConfirmedTextColor)
        TripStatus.CANCELED -> Triple("Cancelado", Color(0xFFFFCDD2), Color(0xFFF44336))
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
