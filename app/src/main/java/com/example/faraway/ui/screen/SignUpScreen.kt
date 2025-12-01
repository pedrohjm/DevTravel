package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import com.example.faraway.ui.components.RadioOption
import com.example.faraway.ui.components.SectionTitle
import com.example.faraway.ui.components.SimpleField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController? = null) {

    val scrollState = rememberScrollState()

    // --- ESTADOS DOS CAMPOS
    var email by remember { mutableStateOf("") }

    // Senhas
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Dados Pessoais
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    // Campos com Máscara
    var cpf by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var gender by remember { mutableStateOf("Homem") }
    var otherGender by remember { mutableStateOf("") }

    // --- ESTADOS DO CALENDÁRIO
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // --- ESTADOS DE ERRO
    var errorEmail by remember { mutableStateOf(false) }
    var errorPasswordLength by remember { mutableStateOf(false) }
    var errorPasswordMatch by remember { mutableStateOf(false) }
    var errorCPF by remember { mutableStateOf(false) }
    var errorPhone by remember { mutableStateOf(false) }
    var errorDate by remember { mutableStateOf(false) }

    // Função de Validação
    fun validateFields(): Boolean {
        errorEmail = false
        errorPasswordLength = false
        errorPasswordMatch = false
        errorCPF = false
        errorPhone = false
        errorDate = false

        var isValid = true

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail = true
            isValid = false
        }
        if (password.length < 6) {
            errorPasswordLength = true
            isValid = false
        }
        if (password != confirmPassword) {
            errorPasswordMatch = true
            isValid = false
        }
        if (cpf.length != 11) {
            errorCPF = true
            isValid = false
        }
        if (phone.length != 11) {
            errorPhone = true
            isValid = false
        }
        if (birthDate.isEmpty()) {
            errorDate = true
            isValid = false
        }

        return isValid
    }

    // --- DIÁLOGO DO CALENDÁRIO
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        birthDate = convertMillisToDate(millis)
                        errorDate = false
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF0F4F8))
    ) {

        // --- HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF123263), Color(0xFF123263))
                    )
                )
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 16.dp, end = 16.dp, bottom = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController?.navigate("auth") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                } }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Text(
                "Criar Conta",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // --- FORMULÁRIO
        Column(modifier = Modifier.padding(20.dp)) {

            SectionTitle("Informações da Conta")

            // --- EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { newValue ->
                    if (newValue.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }) {
                        email = newValue
                        errorEmail = false
                    }
                },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorEmail,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo Branco
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                supportingText = {
                    if (errorEmail) Text("E-mail inválido", color = MaterialTheme.colorScheme.error)
                }
            )

            Spacer(Modifier.height(8.dp))

            // --- SENHA
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (errorPasswordLength) errorPasswordLength = false
                },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorPasswordLength,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                supportingText = {
                    if (errorPasswordLength) Text("Mínimo 6 caracteres", color = MaterialTheme.colorScheme.error)
                }
            )

            // --- CONFIRMAÇÃO DE SENHA
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (errorPasswordMatch) errorPasswordMatch = false
                },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorPasswordMatch,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                supportingText = {
                    if (errorPasswordMatch) Text("As senhas não coincidem", color = MaterialTheme.colorScheme.error)
                }
            )

            Spacer(Modifier.height(16.dp))

            SectionTitle("Informações Pessoais")

            // --- NOME
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo -> nao tinha colocando
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(8.dp))

            // --- SOBRENOME
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Sobrenome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo -> nao tinha colocando
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(8.dp))

            // --- CPF
            OutlinedTextField(
                value = cpf,
                onValueChange = { newValue ->
                    if (newValue.length <= 11 && newValue.all { it.isDigit() }) {
                        cpf = newValue
                        errorCPF = false
                    }
                },
                label = { Text("CPF") },
                placeholder = { Text("000.000.000-00") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorCPF,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = MaskVisualTransformation("###.###.###-##"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                supportingText = {
                    if (errorCPF) Text("CPF inválido", color = MaterialTheme.colorScheme.error)
                }
            )

            // --- TELEFONE
            OutlinedTextField(
                value = phone,
                onValueChange = { newValue ->
                    if (newValue.length <= 11 && newValue.all { it.isDigit() }) {
                        phone = newValue
                        errorPhone = false
                    }
                },
                label = { Text("Telefone") },
                placeholder = { Text("(00) 00000-0000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorPhone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = MaskVisualTransformation("(##) #####-####"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fundo
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                supportingText = {
                    if (errorPhone) Text("Telefone inválido", color = MaterialTheme.colorScheme.error)
                }
            )

            // --- DATA DE NASCIMENTO
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { },
                    label = { Text("Data de Nascimento") },
                    placeholder = { Text("DD/MM/AAAA") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    isError = errorDate,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White, // Fundo
                        unfocusedContainerColor = Color.White,
                        errorContainerColor = Color.White
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker = true
                                    }
                                }
                            }
                        }
                )
            }
            if (errorDate) {
                Text(
                    text = "Selecione uma data",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            SectionTitle("Gênero")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                RadioOption("Homem", gender) { gender = it }
                RadioOption("Mulher", gender) { gender = it }
                RadioOption("Prefiro não informar", gender) { gender = it }
                RadioOption("Outro (especificar)", gender) { gender = it }

                if (gender == "Outro (especificar)") {
                    Spacer(Modifier.height(8.dp))
                    SimpleField("Especifique", otherGender) { otherGender = it }
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- BOTÃO
            Button(
                onClick = {
                    if (validateFields()) {
                        println("Cadastro Sucesso! CPF: $cpf, Data: $birthDate")
                        navController?.navigate("auth") {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AEEF))
            ) {
                Text("Cadastrar", fontSize = 18.sp)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

class MaskVisualTransformation(val mask: String) : VisualTransformation {
    private val specialChars = mask.filter { !it.isLetterOrDigit() && it != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.text.forEach { char ->
            while (maskIndex < mask.length && specialChars.contains(mask[maskIndex])) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var offsetTotal = 0
                var count = 0
                while (count < offset && offsetTotal < mask.length) {
                    if (!specialChars.contains(mask[offsetTotal])) {
                        count++
                    }
                    offsetTotal++
                }
                return offsetTotal
            }

            override fun transformedToOriginal(offset: Int): Int {
                val length = text.text.length
                var transformedOffset = offset
                var specialCharsCount = 0
                var i = 0
                while (i < transformedOffset && i < mask.length) {
                    if (specialChars.contains(mask[i])) {
                        specialCharsCount++
                    }
                    i++
                }
                val originalOffset = transformedOffset - specialCharsCount
                return originalOffset.coerceIn(0, length)
            }
        })
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SignUpPreview() {
    SignUpScreen()
}