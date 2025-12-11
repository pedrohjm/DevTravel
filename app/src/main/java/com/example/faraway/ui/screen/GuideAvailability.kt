package com.example.faraway.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faraway.ui.theme.FarAwayTheme
import com.example.faraway.ui.theme.FooterBlue
import com.example.faraway.ui.theme.GrayLight
import com.example.faraway.ui.theme.GraySelected
import com.example.faraway.ui.theme.QuickBgColor
import com.example.faraway.ui.theme.SliderTrack
import com.example.faraway.ui.theme.TealLight
import com.example.faraway.ui.theme.TealSelected
import com.example.faraway.ui.theme.TextGreen
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale



data class DayConfig(
    val isWorking: Boolean = true,
    val limitHour: Float = 12f
)
data class DayModel(val date: LocalDate)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAgendaScreen(navController: NavController? = null) {

    var startDate by remember {
        mutableStateOf(
            LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        )
    }
    val dayConfigMap = remember { mutableStateMapOf<LocalDate, DayConfig>() }

    fun getConfig(date: LocalDate): DayConfig {
        return dayConfigMap[date] ?: DayConfig()
    }

    fun updateConfig(date: LocalDate, newConfig: DayConfig) {
        dayConfigMap[date] = newConfig
    }

    var selectedIndex by remember { mutableStateOf(0) }

    val weekDays = remember(startDate) {
        (0..6).map { offset ->
            DayModel(startDate.plusDays(offset.toLong()))
        }
    }

    val currentDay = weekDays[selectedIndex]
    val currentConfig = getConfig(currentDay.date)
    val isCurrentDayWorking = currentConfig.isWorking
    val currentSliderValue = currentConfig.limitHour
    val todayDate = remember { LocalDate.now() }
    val dayNameFormatter = DateTimeFormatter.ofPattern("EEE", Locale("pt", "BR"))
    val dayNumFormatter = DateTimeFormatter.ofPattern("dd")
    val fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("pt", "BR"))
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale("pt", "BR"))

    Scaffold(
        containerColor = QuickBgColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = FooterBlue)
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(FooterBlue, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Disponibilidade", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                        Text("Configure sua disponibilidade", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(Modifier.height(16.dp))

                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { startDate = startDate.minusWeeks(1) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                null,
                                tint = FooterBlue,
                                modifier = Modifier.size(16.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val startDayNum = weekDays.first().date.dayOfMonth
                            val endDayNum = weekDays.last().date.dayOfMonth
                            val monthName = weekDays.first().date.format(monthFormatter).replaceFirstChar { it.uppercase() }

                            Text("Semana de", fontSize = 10.sp, color = Color.Gray)
                            Text("$startDayNum a $endDayNum de $monthName", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        IconButton(onClick = { startDate = startDate.plusWeeks(1) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                null,
                                tint = FooterBlue,
                                modifier = Modifier.size(16.dp).graphicsLayer { rotationZ = 180f })
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        weekDays.forEachIndexed { index, dayModel ->
                            val dayName = dayModel.date.format(dayNameFormatter).replaceFirstChar { it.uppercase() }.take(3)
                            val dayNum = dayModel.date.format(dayNumFormatter)
                            val thisDayConfig = getConfig(dayModel.date)

                            WeekDayCard(
                                dayName = dayName,
                                dayNum = dayNum,
                                isSelected = index == selectedIndex,
                                isWorkingDay = thisDayConfig.isWorking,
                                isToday = dayModel.date.isEqual(todayDate), // Verifica se é HOJE
                                onClick = { selectedIndex = index }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val fullDateText = currentDay.date.format(fullDateFormatter)
                .split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

            Spacer(Modifier.height(16.dp))
            Text(fullDateText, fontSize = 12.sp, color = Color.Gray)

            if (isCurrentDayWorking) {
                Text("Você vai trabalhar", fontSize = 24.sp, color = Color(0xFF263238))
                Text("Aceitando tours até às ${String.format("%02d:00", currentSliderValue.toInt())}",
                    fontSize = 12.sp, color = Color.Gray)
            } else {
                Text("Dia de folga", fontSize = 24.sp, color = GraySelected)
                Text("Nenhum tour será agendado", fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Vou trabalhar neste dia", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            if (isCurrentDayWorking) "Disponível para agendamentos" else "Bloqueado para agendamentos",
                            fontSize = 11.sp, color = Color.Gray
                        )
                    }
                    Switch(
                        checked = isCurrentDayWorking,
                        onCheckedChange = { isChecked ->
                            updateConfig(currentDay.date, currentConfig.copy(isWorking = isChecked))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.Black,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            if (isCurrentDayWorking) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Atalhos Rápidos:", fontSize = 12.sp, color = Color.Gray)
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ShortcutButton("Meio Dia", "12:00", isSelected = currentSliderValue.toInt() == 12) {
                                updateConfig(currentDay.date, currentConfig.copy(limitHour = 12f))
                            }
                            ShortcutButton("Tarde Toda", "16:00", isSelected = currentSliderValue.toInt() == 16) {
                                updateConfig(currentDay.date, currentConfig.copy(limitHour = 16f))
                            }
                            ShortcutButton("Dia Todo", "22:00", isSelected = currentSliderValue.toInt() == 22) {
                                updateConfig(currentDay.date, currentConfig.copy(limitHour = 22f))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Aceito tours até:", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            String.format("%02d:00", currentSliderValue.toInt()),
                            fontSize = 48.sp,
                            color = TealSelected,
                            fontWeight = FontWeight.Light
                        )

                        Spacer(Modifier.height(8.dp))

                        Slider(
                            value = currentSliderValue,
                            onValueChange = { newValue ->
                                updateConfig(currentDay.date, currentConfig.copy(limitHour = newValue))
                            },
                            valueRange = 6f..22f,
                            steps = 15,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.Black,
                                inactiveTrackColor = SliderTrack
                            )
                        )

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("06:00", fontSize = 10.sp, color = Color.Gray)
                            Text("14:00", fontSize = 10.sp, color = Color.Gray)
                            Text("22:00", fontSize = 10.sp, color = Color.Gray)
                        }

                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F8E9), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check,
                                    null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Alterações salvas automaticamente", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }

            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.BeachAccess,
                            contentDescription = "Folga",
                            tint = Color(0xFFFF7043),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text("Aproveite seu dia de folga!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F))
                        Text("Nenhum tour será agendado para este dia", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun WeekDayCard(
    dayName: String,
    dayNum: String,
    isSelected: Boolean,
    isWorkingDay: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected && isWorkingDay -> TealSelected
        !isSelected && isWorkingDay -> TealLight
        isSelected && !isWorkingDay -> GraySelected
        else -> GrayLight
    }

    val textColor = if (isSelected) Color.White else (if (isWorkingDay) TextGreen else Color.Gray)

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .width(45.dp)
            .height(65.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dayName, fontSize = 10.sp, color = textColor)
            Text(dayNum, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)

            if (isToday) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else textColor)
                )
            } else {
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun RowScope.ShortcutButton(title: String, time: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) TealSelected else Color(0xFFF5F5F5)
    val textColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) TealSelected else Color(0xFFEEEEEE)

    Card(
        modifier = Modifier
            .weight(1f)
            .height(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, color = textColor.copy(alpha = 0.7f))
            Text(time, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuickAgendaPreview() {
    FarAwayTheme {
        QuickAgendaScreen()
    }
}