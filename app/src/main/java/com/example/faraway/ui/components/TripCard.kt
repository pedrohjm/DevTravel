package com.example.faraway.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.faraway.ui.data.Trip
import com.example.faraway.ui.data.TripStatus
import com.example.faraway.ui.theme.AccentColor
import com.example.faraway.ui.theme.FarAwayTheme

@Composable
fun TripCard(trip: Trip) {

    fun getStatusText(status: TripStatus): String {
        return when (status) {
            TripStatus.CONFIRMED -> "Confirmado"
            TripStatus.PENDING -> "Pendente"
            TripStatus.CANCELED -> "Cancelado"
            TripStatus.COMPLETED -> "Concluído"
        }
    }

    fun getStatusColor(status: TripStatus): Color {
        return when (status) {
            TripStatus.CONFIRMED -> Color(0xFFE6FFEE) // Verde claro
            TripStatus.PENDING -> Color(0xFFFFFBEB) // Amarelo claro
            TripStatus.CANCELED -> Color(0xFFFFEBEB) // Vermelho claro
            TripStatus.COMPLETED -> Color(0xFFEBF8FF) // Azul claro
        }
    }

    fun getStatusTextColor(status: TripStatus): Color {
        return when (status) {
            TripStatus.CONFIRMED -> Color(0xFF10B981) // Verde escuro
            TripStatus.PENDING -> Color(0xFFF59E0B) // Amarelo escuro
            TripStatus.CANCELED -> Color(0xFFEF4444) // Vermelho escuro
            TripStatus.COMPLETED -> Color(0xFF3B82F6) // Azul escuro
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                        model = trip.imageUrl,
                        contentDescription = "Foto do Guia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF10B981)) // Verde
                            .align(Alignment.TopStart)
                            .offset(x = 4.dp, y = 4.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                // 2. Detalhes da Viagem
                Column(
                    modifier = Modifier.weight(1f), // Ocupa o espaço restante
                    verticalArrangement = Arrangement.Top // Compacto
                ) {
                    // Nome e Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trip.partnerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                        // Status Tag
                        Text(
                            text = getStatusText(trip.status),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = getStatusTextColor(trip.status),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(getStatusColor(trip.status))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Localização
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Localização",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = trip.location, // Localização do Guia
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // Data e Hora
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Data",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = trip.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Hora",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = trip.time, // Hora da Solicitação
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Preço e Duração
                    Text(
                        text = "${trip.price} • ${trip.duration}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = AccentColor // Cor de destaque para preço
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { /* Ação Chat */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF333333)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Chat",
                        modifier = Modifier.size(20.dp)
                    )
                        Text("Chat", fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripCardPreview() {
    val sampleTrip = Trip(
        partnerName = "Gabriel Pereira",
        location = "Lisboa, Portugal",
        date = "05 Out 2025",
        time = "10:00",
        price = "€25/hora",
        duration = "3 horas",
        status = TripStatus.CONFIRMED,
        imageUrl = "https://i.imgur.com/8Q9tY2t.png",
        details = "Detalhes da Viagem"
    )

    FarAwayTheme {
        TripCard(trip = sampleTrip)
    }
}