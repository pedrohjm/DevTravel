package com.example.faraway.ui.screen

import BottomNavBar
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.guideNavItems
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.*
import com.example.faraway.ui.viewmodel.AuthViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// --- 1. COMPONENTE COM ESTADO (Stateful) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidePanelScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.collectAsState()
    val userName = "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { "Carregando..." }

    GuidePanelContent(
        navController = navController,
        userName = userName
    )
}

// --- 2. COMPONENTE SEM ESTADO (Stateless) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidePanelContent(
    navController: NavController,
    userName: String
) {
    val selectedItem = remember { mutableStateOf("Explorar") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Painel do Guia", fontWeight = FontWeight.Bold)
                        Text(text = userName, fontSize = 14.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ação do perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                modifier = Modifier.background(
                    Brush.horizontalGradient(colors = listOf(Color(0xFF2364C8), Color(0xFF113162)))
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, navItems = guideNavItems, startRoute = Destinations.HOST_DASHBOARD_ROUTE)
            NavigationBar(containerColor = Color.White){
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, "Explorar") },
                    label = { Text("Explorar") },
                    selected = selectedItem.value == "Explorar",
                    onClick = { navController.navigate(Destinations.GUIDE_DASHBOARD_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, "Tours") },
                    label = { Text("Tours") },
                    selected = selectedItem.value == "Tours",
                    onClick = { navController.navigate(Destinations.GUIDE_TOURS_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, "Chat") },
                    label = { Text("Chat") },
                    selected = selectedItem.value == "Chat",
                    onClick = { navController.navigate(Destinations.GUIDE_CHAT_ROUTE) { launchSingleTop = true } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem.value == "Perfil",
                    onClick = { navController.navigate(Destinations.GUIDE_PROFILE_ROUTE) { launchSingleTop = true } }
                )
            }
        }
    ) { innerPadding ->
        GuideDashboardContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GuideDashboardContent(innerPadding: PaddingValues) {
    val context = LocalContext.current

    // --- ESTADOS DO TOUR ATUAL (Editáveis) ---
    var clientName by remember { mutableStateOf("Amanda Nunes") }
    // Data inicial padrão
    var tourDateMillis by remember { mutableStateOf<Long?>(Calendar.getInstance().timeInMillis) }
    var tourTime by remember { mutableStateOf("18:00") }
    var tourType by remember { mutableStateOf("Tour Gastronômico") }

    // Controles de Diálogo
    var showEditDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Variáveis temporárias
    var tempName by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }
    var tempTime by remember { mutableStateOf("") }
    var tempType by remember { mutableStateOf("") }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("pt", "BR")) }

    // Função para abrir diálogo
    fun openEditDialog() {
        tempName = clientName
        tempDateMillis = tourDateMillis
        tempTime = tourTime
        tempType = tourType
        showEditDialog = true
    }

    // --- DIÁLOGO DE EDIÇÃO ---
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Tour Atual") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Nome do Cliente") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Data e Horário", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Botão Data
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tempDateMillis?.let {
                                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                                } ?: "Data",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        // Botão Hora
                        OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = tempTime.ifEmpty { "Hora" }, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempType,
                        onValueChange = { tempType = it },
                        label = { Text("Tipo de Tour") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clientName = tempName
                        tourDateMillis = tempDateMillis
                        tourTime = tempTime
                        tourType = tempType
                        showEditDialog = false
                        Toast.makeText(context, "Tour atualizado!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2364C8))
                ) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // DatePicker Dialog
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
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    // TimePicker Dialog
    if (showTimePicker) {
        val cal = Calendar.getInstance()
        if (tempTime.isNotEmpty() && tempTime.contains(":")) {
            val parts = tempTime.split(":")
            cal.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
            cal.set(Calendar.MINUTE, parts[1].toInt())
        }
        TimePickerDialog(
            context,
            { _, h, m ->
                tempTime = String.format("%02d:%02d", h, m)
                showTimePicker = false
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    // --- CONTEÚDO PRINCIPAL ---
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Tour Atuais (EDITÁVEL) ---
        Text("Tour Atuais", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openEditDialog() }, // Abre edição ao clicar
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = CurrentInfoCardBlue),
        ) {
            Box(Modifier.padding(16.dp)) {
                Column {
                    Text(clientName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Data", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            val dateStr = tourDateMillis?.let {
                                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
                            } ?: "--/--/----"
                            Text(dateStr)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Hora", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(tourTime)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(tourType, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // Ícone de Editar
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier.align(Alignment.TopEnd).size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---Novas Solicitações ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Novas Solicitações", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(NotificationBadgeBlue, shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("2", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(8.dp))

        // Cartões de Solicitação
        GuideRequestCard(
            name = "Maria Silva",
            date = "25 Nov 2025",
            time = "10:00",
            description = "Gostaria de conhecer a história e cultura de Lisboa..."
        )
        GuideRequestCard(
            name = "Pedro Santos",
            date = "28 Out 2025",
            time = "19:00",
            description = "Tour gastronômico pela cidade, adoro comida local!"
        )
    }
}

@Composable
private fun GuideRequestCard(
    name: String,
    date: String,
    time: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Imagem
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
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Data", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(date, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Hora", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(time, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(description, fontSize = 14.sp, maxLines = 2)
                }
            }
            Spacer(Modifier.height(16.dp))
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
                        contentColor =AcceptButtonColor2,
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

                // Botão Recusar
                OutlinedButton(
                    onClick = { /* Recusar */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, DeclineButtonColor),
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
                    Text("Recusar", fontSize = 10.sp)
                }
                Spacer(Modifier.width(8.dp))

                // Botão Pendente
                OutlinedButton(
                    onClick = { /* Ação de pendente */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, PendingButtonColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PendingButtonColor2
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Pendente", fontSize = 12.sp)
                }
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun GuidePanelPreview() {
    FarAwayTheme {
        GuidePanelContent(
            navController = rememberNavController(),
            userName = "Fátima Alves (Preview)"
        )
    }
}