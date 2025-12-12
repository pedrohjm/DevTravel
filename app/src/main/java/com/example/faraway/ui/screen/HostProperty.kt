package com.example.faraway.ui.screen

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faraway.ui.theme.DarkText
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.LightTeal
import com.example.faraway.ui.theme.PrimaryTeal
import com.example.faraway.ui.theme.PropertyBgColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostPropertyScreen(navController: NavController? = null, ) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var title by remember { mutableStateOf("Apartamento Moderno no Centro") }
    var address by remember { mutableStateOf("Rua das Flores, 123 - Centro, SP") }
    var description by remember { mutableStateOf("Lindo apartamento com acabamento de primeira qualidade...") }
    var bedrooms by remember { mutableStateOf("3") }
    var bathrooms by remember { mutableStateOf("2") }
    var area by remember { mutableStateOf("95") }
    var price by remember { mutableStateOf("450,00") }
    val amenities = remember { mutableStateListOf("Wi-Fi", "Ar Condicionado", "Garagem") }
    var newAmenityText by remember { mutableStateOf("") }
    var startDateMillis by remember { mutableStateOf<Long?>(null) }
    var endDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf("14:00") }
    var showDatePicker by remember { mutableStateOf(false) }
    var isPickingStartDate by remember { mutableStateOf(true) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                return utcTimeMillis >= today
            }
        }
    )
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    fun formatDate(millis: Long?): String {
        return millis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate().format(dateFormatter)
        } ?: "Selecionar"
    }
    Scaffold(
        containerColor = PropertyBgColor,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { navController?.popBackStack() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            "Voltar", tint = PrimaryTeal)
                        Spacer(Modifier.width(4.dp))
                        Text("Voltar", color = PrimaryTeal, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PropertyBgColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, LightTeal, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, null, tint = PrimaryTeal)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Minha Propriedade", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkText)
                    Text("Edite as informações abaixo", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Nome do Anúncio", fontSize = 11.sp, color = PrimaryTeal, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DarkText),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryTeal,
                            cursorColor = PrimaryTeal
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Endereço Completo",
                        fontSize = 11.sp,
                        color = PrimaryTeal,
                        fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 12.sp, color = DarkText),
                        leadingIcon = { Icon(Icons.Default.LocationOn,
                            null,
                            tint = PrimaryTeal,
                            modifier = Modifier.size(16.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryTeal,
                            cursorColor = PrimaryTeal
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Estrutura do Imóvel", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EditableStructureCard(Icons.Outlined.Bed, bedrooms, "Quartos") { bedrooms = it }
                EditableStructureCard(Icons.Outlined.Bathtub, bathrooms, "Banheiros") { bathrooms = it }
                EditableStructureCard(Icons.Outlined.SquareFoot, area, "m²") { area = it }
            }
            Spacer(Modifier.height(24.dp))
            Text("Descrição", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                textStyle = TextStyle(fontSize = 12.sp, lineHeight = 18.sp, color = DarkText),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = PrimaryTeal,
                    cursorColor = PrimaryTeal
                )
            )

            Spacer(Modifier.height(24.dp))
            Text("Comodidades", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
            Text("Adicione, edite ou remova itens da lista", fontSize = 11.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    amenities.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            OutlinedTextField(
                                value = item,
                                onValueChange = { amenities[index] = it },
                                modifier = Modifier.weight(1f).height(50.dp),
                                textStyle = TextStyle(fontSize = 12.sp, color = DarkText),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedBorderColor = PrimaryTeal
                                )
                            )
                            IconButton(onClick = { amenities.removeAt(index) }) {
                                Icon(Icons.Default.Delete,
                                    "Remover",
                                    tint = Color.Red.copy(alpha = 0.6f))
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newAmenityText,
                            onValueChange = { newAmenityText = it },
                            placeholder = { Text("Nova comodidade...", color = Color.Gray) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            textStyle = TextStyle(fontSize = 12.sp, color = DarkText),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                unfocusedBorderColor = Color.LightGray,
                                focusedBorderColor = PrimaryTeal
                            )
                        )
                        IconButton(
                            onClick = {
                                if (newAmenityText.isNotBlank()) {
                                    amenities.add(newAmenityText)
                                    newAmenityText = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.AddCircle,
                                "Adicionar",
                                tint = PrimaryTeal)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Disponibilidade", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) {
                    Text("Data Inicial", fontSize = 11.sp, color = DarkText)
                    OutlinedButton(
                        onClick = {
                            isPickingStartDate = true
                            showDatePicker = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(formatDate(startDateMillis),
                            color = if (startDateMillis != null) DarkText else Color.Gray, fontSize = 12.sp)
                    }
                }
                Column(Modifier.weight(1f)) {
                    Text("Data Final", fontSize = 11.sp, color = DarkText)
                    OutlinedButton(
                        onClick = {
                            isPickingStartDate = false
                            showDatePicker = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(formatDate(endDateMillis),
                            color = if (endDateMillis != null) DarkText else Color.Gray,
                            fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Column(Modifier.fillMaxWidth()) {
                Text("Horário de Entrada (Check-in)", fontSize = 11.sp, color = DarkText)
                OutlinedButton(
                    onClick = {
                        val cal = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hour, minute -> selectedTime = String.format("%02d:%02d", hour, minute) },
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Text(selectedTime, color = DarkText, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(32.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = LightTeal),
                border = BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Valor da Diária (R$)",
                        fontSize = 12.sp,
                        color = PrimaryTeal,
                        fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryTeal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = PrimaryTeal,
                            unfocusedTextColor = PrimaryTeal,
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (startDateMillis != null && endDateMillis != null) {
                                Toast.makeText(context, "Alterações Salvas!",
                                    Toast.LENGTH_LONG).show()
                                navController?.popBackStack()
                            } else {
                                Toast.makeText(context, "Preencha as datas!",
                                    Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Icon(Icons.Default.Save,
                            null,
                            modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Salvar Alterações", fontSize = 16.sp)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    if (isPickingStartDate) {
                        startDateMillis = datePickerState.selectedDateMillis
                    } else {
                        endDateMillis = datePickerState.selectedDateMillis
                    }
                    showDatePicker = false
                }) { Text("OK", color = PrimaryTeal) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray) }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = PrimaryTeal,
                    todayDateBorderColor = PrimaryTeal,
                    todayContentColor = PrimaryTeal
                )
            )
        }
    }
}

@Composable
fun RowScope.EditableStructureCard(
    icon: ImageVector,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null,
                tint = PrimaryTeal,
                modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(4.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(40.dp)
                    .background(LightTeal, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp)
            )

            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HostPropertyPreview() {
    FarAwayTheme {
        HostPropertyScreen()
    }
}