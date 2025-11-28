package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import com.example.faraway.ui.components.RadioOption
import com.example.faraway.ui.components.SectionTitle
import com.example.faraway.ui.components.SimpleField

@Composable
fun SignUpScreen(navController: NavController? = null) {

    // Estado para controlar a rolagem da tela
    val scrollState = rememberScrollState()

    // ------ ESTADOS DOS CAMPOS DE FORMULÁRIO ------
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Homem") }
    var cep by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var complement by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Campo condicional para quando "Outro" é selecionado no gênero
    var otherGender by remember { mutableStateOf("") }

    // ------ ESTADOS DE ERRO PARA VALIDAÇÃO ------
    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    var errorCPF by remember { mutableStateOf(false) }
    var errorPhone by remember { mutableStateOf(false) }
    var errorCEP by remember { mutableStateOf(false) }


    fun validateFields(): Boolean {
        // Regras de validação para cada campo
        errorEmail = !email.contains("@") // Email deve conter @
        errorPassword = password.length < 6 // Senha mínima de 6 caracteres
        errorCPF = cpf.length != 11 // CPF deve ter exatamente 11 dígitos
        errorPhone = phone.length < 10 // Telefone mínimo de 10 dígitos
        errorCEP = cep.length != 8 // CEP deve ter exatamente 8 dígitos

        // Retorna true apenas se nenhum campo tiver erro
        return !(errorEmail || errorPassword || errorCPF || errorPhone || errorCEP)
    }

    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda a tela disponível
            .verticalScroll(scrollState) // Habilita rolagem vertical
            .background(Color(0xFFF0F4F8)) // Cor de fundo cinza azulado claro
    ) {

        // ------ HEADER COM GRADIENTE E BOTÃO VOLTAR ------
        Row(
            modifier = Modifier
                .fillMaxWidth() // Ocupa toda a largura
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF123263), Color(0xFF123263)) // Gradiente azul
                    )
                )
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically // Alinha itens verticalmente ao centro
        ) {
            // Botão voltar com ícone
            IconButton(onClick = { navController?.navigateUp() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back", // Acessibilidade
                    tint = Color.White // Ícone branco
                )
            }
            // Título da tela
            Text(
                "Criar Conta",
                color = Color.White, // Texto branco
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp) // Espaço entre ícone e texto
            )
        }

        // ------ CONTEÚDO PRINCIPAL DO FORMULÁRIO ------
        Column(modifier = Modifier.padding(20.dp)) {

            // ---------- SEÇÃO: INFORMAÇÕES DA CONTA ----------
            SectionTitle("Informações da Conta")

            // Campo de email
            SimpleField("E-mail", email) { email = it }
            // Campo de senha com validação de mínimo 6 caracteres
            SimpleField("Senha (mínimo 6 caracteres)", password) { password = it }

            Spacer(Modifier.height(16.dp)) // Espaçamento entre seções

            // ---------- SEÇÃO: INFORMAÇÕES PESSOAIS ----------
            SectionTitle("Informações Pessoais")

            // Campos de informações pessoais
            SimpleField("Nome", firstName) { firstName = it }
            SimpleField("Sobrenome", lastName) { lastName = it }
            SimpleField("CPF (11 dígitos)", cpf) { cpf = it }
            SimpleField("Telefone", phone) { phone = it }
            SimpleField("Data de Nascimento", birthDate) { birthDate = it }

            Spacer(Modifier.height(16.dp))

            // ---------- SEÇÃO: SELEÇÃO DE GÊNERO ----------
            SectionTitle("Gênero")

            // Container para opções de gênero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                RadioOption("Homem", gender) { gender = it }
                RadioOption("Mulher", gender) { gender = it }
                RadioOption("Prefiro não informar", gender) { gender = it }
                RadioOption("Outro (especificar)", gender) { gender = it }

                // Campo condicional - aparece apenas quando "Outro" é selecionado
                if (gender == "Outro (especificar)") {
                    Spacer(Modifier.height(8.dp))
                    SimpleField("Especifique", otherGender) { otherGender = it }
                }
            }

            // ---------- SEÇÃO: ENDEREÇO ----------
            SectionTitle("Endereço")

            // Campos de endereço
            SimpleField("CEP (8 dígitos)", cep) { cep = it }
            SimpleField("Logradouro", address) { address = it }
            SimpleField("Número", number) { number = it }
            // Complemento é opcional
            SimpleField("Complemento (opcional)", complement) { complement = it }
            SimpleField("Bairro", district) { district = it }
            SimpleField("Cidade", city) { city = it }
            SimpleField("Estado", state) { state = it }
            SimpleField("País", country) { country = it }

            Spacer(Modifier.height(24.dp)) // Espaçamento antes do botão

            // ---------- BOTÃO DE CADASTRO ----------
            Button(
                onClick = {
                    // Valida campos antes de enviar
                    if (validateFields()) {
                        // TODO: Implementar chamada à API para cadastro
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AEEF)) // Cor azul
            ) {
                Text("Cadastrar", fontSize = 18.sp) // Texto do botão
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SignUpPreview() {
    SignUpScreen()
}