package com.example.faraway.ui.screen

import BottomNavBar
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.faraway.ui.viewmodel.AuthViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun PainelDoAnfitriaoScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        if (userData == null) {
            authViewModel.fetchUserData()
        }
    }

    val userName = "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { "Carregando..." }

    // Chama o conteúdo visual passando apenas dados simples
    PainelDoAnfitriaoContent(
        navController = navController,
        userName = userName
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PainelDoAnfitriaoContent(
    navController: NavController,
    userName: String
) {
    val selectedItem = remember { mutableStateOf("Explorar") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Painel do Anfitrião", fontWeight = FontWeight.Bold)
                        Text(
                            text = userName, // Usa a String recebida
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ação do perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF0892B4), Color(0xFF033F4E))
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
                    onClick = { navController.navigate(Destinations.GUIDE_DASHBOARD_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
                    label = { Text("Reservas") },
                    selected = selectedItem.value == "Reservas",
                    onClick = { navController.navigate(Destinations.HOST_RESERVATION_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = selectedItem.value == "Chat",
                    onClick = { navController.navigate(Destinations.HOST_CHAT_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem.value == "Perfil",
                    onClick = { navController.navigate(Destinations.HOST_PERFIL_ROUTE) { launchSingleTop = true } }
                )
            }
        }
    ) { innerPadding ->
        HostDashboardContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HostDashboardContent(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var guestName by remember { mutableStateOf("Laura Resende") }
    val defaultDate = remember { Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis }
    var checkoutDateMillis by remember { mutableStateOf<Long?>(defaultDate) }
    var checkoutTime by remember { mutableStateOf("10:00") }
    var roomType by remember { mutableStateOf("Quarto Privado") }

    // Controles de Diálogo
    var showEditDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Variáveis temporárias
    var tempName by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }
    var tempTime by remember { mutableStateOf("") }
    var tempRoom by remember { mutableStateOf("") }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("pt", "BR")) }

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
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            Toast.makeText(context, "Informações atualizadas!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Selecione data e hora.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0892B4))
                ) {
                    Text("Salvar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = tempDateMillis ?: System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    tempDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val initialHour = tempTime.split(":")[0].toIntOrNull() ?: 10
        val initialMinute = tempTime.split(":")[1].toIntOrNull() ?: 0
        TimePickerDialog(
            context,
            { _, hour, minute ->
                tempTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                showTimePicker = false
            },
            initialHour, initialMinute, true
        ).show()
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Hóspede Atual ---
        Text("Hóspede Atual", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Hóspede",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0892B4))
                            .padding(8.dp),
                        tint = Color.White
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(guestName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(roomType, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    IconButton(onClick = { openEditDialog() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
                Divider(Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Check-out", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text(
                            checkoutDateMillis?.let {
                                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                            } ?: "Data não definida",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Hora", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text(checkoutTime, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- Solicitações ---
        Text("Solicitações de Amizade", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        FriendRequestCard("Guia Turístico", "Solicita acesso ao seu calendário.")
    }
}

@Composable
fun ReservationCard(name: String, room: String, date: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(name, fontWeight = FontWeight.Bold)
                Text(room, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(date, style = MaterialTheme.typography.bodyMedium)
                Text(status, style = MaterialTheme.typography.bodySmall, color = if (status == "Confirmada") Color(0xFF4CAF50) else Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun FriendRequestCard(name: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = { /* Recusar */ },
                    border = BorderStroke(1.dp, Color(0xFFF44336)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Recusar", color = Color(0xFFF44336)) }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { /* Aceitar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Aceitar", color = Color.White) }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PainelDoAnfitriaoScreenPreview() {
    FarAwayTheme {
        PainelDoAnfitriaoContent(
            navController = rememberNavController(),
            userName = "Anfitrião Exemplo"
        )
    }
}