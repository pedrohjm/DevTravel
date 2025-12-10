package com.example.faraway.ui.screen

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

//Painel do Anfitrião
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PainelDoAnfitriaoScreen(navController: NavController) {
    val selectedItem = remember { mutableStateOf("Explorar") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Painel do Anfitrião", fontWeight = FontWeight.Bold)
                        Text("Fátima Alves", fontSize = 14.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ação do perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0892B4), // Cor inicial
                            Color(0xFF033F4E)  // Cor final
                        )
                    )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Barra de Navegação do Anfitrião
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
                startRoute = Destinations.HOST_DASHBOARD_ROUTE
            )
            NavigationBar(containerColor = Color.White){
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") },
                    selected = selectedItem.value == "Explorar",
                    onClick = {
                        navController.navigate(Destinations.GUIDE_DASHBOARD_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
                    label = { Text("Reservas") },
                    selected = selectedItem.value == "Reservas",
                    onClick = {
                        navController.navigate(Destinations.HOST_RESERVATION_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = selectedItem.value == "Chat",
                    onClick = {
                        navController.navigate(Destinations.HOST_CHAT_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem.value == "Perfil",
                    onClick = {
                        navController.navigate(Destinations.HOST_PERFIL_ROUTE) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        HostDashboardContent(innerPadding)
    }
}

// Conteúdo da tela do Anfitrião
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HostDashboardContent(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var guestName by remember { mutableStateOf("Laura Resende") }
    val defaultDate = remember { Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis }
    var checkoutDateMillis by remember { mutableStateOf<Long?>(defaultDate) }
    var checkoutTime by remember { mutableStateOf("10:00") }
    var roomType by remember { mutableStateOf("Quarto Privado") }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }
    var tempTime by remember { mutableStateOf("") }
    var tempRoom by remember { mutableStateOf("") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy",
        Locale("pt", "BR")) }

    fun openEditDialog() {
        tempName = guestName
        tempDateMillis = checkoutDateMillis
        tempTime = checkoutTime
        tempRoom = roomType
        showEditDialog = true
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Hóspede Atual") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Nome do Hóspede") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))

                    Text("Check-out", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    // Linha com botões para Data e Hora
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Botão de Data
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tempDateMillis?.let {
                                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                                } ?: "Selecionar Data",
                                color = if (tempDateMillis != null) MaterialTheme.colorScheme.onSurface else Color.Gray
                            )
                        }
                        OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tempTime.ifEmpty { "Selecionar Hora" },
                                color = if (tempTime.isNotEmpty()) MaterialTheme.colorScheme.onSurface else Color.Gray
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempRoom,
                        onValueChange = { tempRoom = it },
                        label = { Text("Tipo de Quarto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempDateMillis != null && tempTime.isNotEmpty()) {
                            guestName = tempName
                            checkoutDateMillis = tempDateMillis
                            checkoutTime = tempTime
                            roomType = tempRoom
                            showEditDialog = false
                            Toast.makeText(context, "Hóspede atualizado!",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Por favor, selecione a data e o horário de check-out.",
                                Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00838F))
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = tempDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    tempDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        if (tempTime.isNotEmpty()) {
            try {
                val parts = tempTime.split(":")
                calendar.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
                calendar.set(Calendar.MINUTE, parts[1].toInt())
            } catch (e: Exception) { /* Ignora e usa a hora atual */ }
        }

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                tempTime = String.format("%02d:%02d", hourOfDay, minute)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Hóspedes Atuais", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, color = Color(0xFF1D1B20))
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openEditDialog() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFCEF4FD))
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                Column {
                    Text(
                        text = guestName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    val formattedDateTime = checkoutDateMillis?.let {
                        val dateStr = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                        "$dateStr às $checkoutTime"
                    } ?: "Data não definida"

                    Text(
                        text = "Check-out: $formattedDateTime",
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = roomType,
                        fontSize = 14.sp,
                        color = Color(0xFF49454F)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Hóspede",
                    tint = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier.align(Alignment.TopEnd).size(18.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Novas Solicitações", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1D1B20))
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFF00C4CC), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        HostRequestCard(
            name = "Sofia Mendes",
            dateInfo = "12 Out 2025 → 15 Out 2025 • 3 noites",
            guestInfo = "2 hóspedes",
            description = "Casal em lua de mel, procurando um lugar tranquilo e aconchegante."
        )
        HostRequestCard(
            name = "Miguel Santos",
            dateInfo = "18 Out 2025 → 20 Out 2025 • 2 noites",
            guestInfo = "1 hóspede",
            description = "Viagem de negócios, preciso de bom wifi e área de trabalho."
        )
    }
}
@Composable
private fun HostRequestCard(
    name: String,
    dateInfo: String,
    guestInfo: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de $name",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(dateInfo, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.People,
                            contentDescription = "Hóspedes",
                            modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(Modifier.width(4.dp))
                        Text(guestInfo, fontSize = 14.sp, color = Color.Gray)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(description, fontSize = 14.sp, maxLines = 2, color = Color.DarkGray)
                }
            }
            Spacer(Modifier.height(16.dp))
            // Botões de Ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botão Aceitar
                Button(
                    onClick = { /* Aceitar */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, AcceptButtonColor2),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AcceptButtonColor2,
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Aceitar",
                        modifier = Modifier.size(12.dp))
                    Text("Aceitar", fontSize = 12.sp)
                }
                Spacer(Modifier.width(8.dp))

                //Botão Recusar
                OutlinedButton(
                    onClick = { /* Recusar */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, DeclineButtonColor2),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DeclineButtonColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Recusar",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Recusar",
                        fontSize = 10.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { /* Ação de pendente */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, PendingButtonColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PendingButtonColor2
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Pendente",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PainelDoAnfitriaoPreview() {
    FarAwayTheme {
        PainelDoAnfitriaoScreen(navController = rememberNavController())
    }
}