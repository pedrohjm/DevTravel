package com.example.faraway.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.faraway.Destinations
import com.example.faraway.ui.data.AuthRepository
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.viewmodel.AuthViewModelFactory

// Definição das cores baseadas na imagem
val FarwayBlueDark = Color(0xFF16213E)
val FarwayBlueLight = Color(0xFF1A1A2E)
val FarwayAccent = Color(0xFF00BCD4) // Cor do botão Entrar e ícone
val FarwayCardBackground = Color.White
val FarwayTextDark = Color(0xFF1C1B1F)
val FarwayTextLight = Color.White
val FarwayInputBackground = Color(0xFFF5F5F5)

@Composable
fun AuthScreen(navController: NavController) {
    var isLoginTab by remember { mutableStateOf(true) }

    val authRepository = remember { AuthRepository() }
    val factory = remember(authRepository) { AuthViewModelFactory(authRepository) }
    val viewModel: AuthViewModel = viewModel(factory = factory)

    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        FarwayBlueDark,
                        FarwayBlueLight
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo e Título
            Icon(
                imageVector = Icons.Filled.Flight,
                contentDescription = "Farway Logo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FarwayAccent)
                    .padding(12.dp),
                tint = FarwayCardBackground
            )

            Text(
                text = "Farway",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FarwayTextLight
            )

            Text(
                text = "Conecte-se com o mundo",
                fontSize = 16.sp,
                color = FarwayAccent
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card de Conteúdo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FarwayCardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Toggle Login/Cadastro
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TabButton(
                            text = "Login",
                            isSelected = isLoginTab,
                            onClick = { isLoginTab = true },
                            modifier = Modifier.weight(1f),
                            selectedColor = FarwayBlueDark,
                            unselectedColor = FarwayCardBackground,
                            selectedContentColor = FarwayCardBackground,
                            unselectedContentColor = FarwayTextDark,
                            hasBorder = !isLoginTab
                        )
                        TabButton(
                            text = "Cadastro",
                            isSelected = !isLoginTab,
                            onClick = { isLoginTab = false },
                            modifier = Modifier.weight(1f),
                            selectedColor = FarwayBlueDark,
                            unselectedColor = FarwayCardBackground,
                            selectedContentColor = FarwayCardBackground,
                            unselectedContentColor = FarwayTextDark,
                            hasBorder = isLoginTab
                        )
                    }

                    if (isLoginTab) {
                        LoginContent(navController = navController, viewModel = viewModel,snackbarHostState = snackbarHostState)
                    } else {
                        CadastroContent(navController = navController)
                    }
                }
            }

            Text(
                text = "Conectando viajantes a experiências autênticas",
                fontSize = 14.sp,
                color = FarwayAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    selectedContentColor: Color,
    unselectedContentColor: Color,
    hasBorder: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .then(
                if (hasBorder) Modifier.border(
                    1.dp,
                    Color.LightGray,
                    RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedColor else unselectedColor,
            contentColor = if (isSelected) selectedContentColor else unselectedContentColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun LoginContent(
    navController: NavController,
    viewModel: AuthViewModel,
    snackbarHostState: SnackbarHostState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is AuthViewModel.AuthState.Loading

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // E-mail
        Text("E-mail", fontWeight = FontWeight.SemiBold, color = FarwayTextDark)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("seu@email.com") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = FarwayInputBackground,
                unfocusedContainerColor = FarwayInputBackground
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Senha
        Text("Senha", fontWeight = FontWeight.SemiBold, color = FarwayTextDark)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = FarwayInputBackground,
                unfocusedContainerColor = FarwayInputBackground
            ),
            shape = RoundedCornerShape(8.dp)
        )

        // Esqueceu a senha?
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "Esqueceu a senha?",
                color = FarwayAccent,
                modifier = Modifier.clickable { /* Ação de esqueci a senha */ }
            )
        }

        // Botão Entrar
        Button(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = FarwayAccent),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            } else {
                Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarwayTextLight)
            }
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                val role = (authState as AuthViewModel.AuthState.Success).role

                // Navegação Condicional Baseada no Papel
                val destinationRoute = when (role) {
                    "Guia" -> Destinations.GUIDE_DASHBOARD_ROUTE
                    "Anfitrião" -> Destinations.HOST_DASHBOARD_ROUTE
                    "Membro" -> Destinations.EXPLORE_ROUTE
                    else -> Destinations.SOCIAL_AMIGOS_ROUTE
                }

                navController.navigate(destinationRoute) {
                    popUpTo(Destinations.AUTH_ROUTE) { inclusive = true }
                }
            }
            is AuthViewModel.AuthState.Error -> {
                val errorMessage = (authState as AuthViewModel.AuthState.Error).message
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }
}

@Composable
fun CadastroContent(navController: NavController) {
    var selectedProfile by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedProfile) {
        when (selectedProfile) {
            "Guia" -> {
                navController.navigate("sign_up/${selectedProfile}") {
                    // Implementar a lógica para salvar a role selecionada
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
            "Anfitrião" -> {
                navController.navigate("sign_up/${selectedProfile}") {
                    // Implementar a lógica para salvar a role selecionada
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
            "Membro"-> {
                navController.navigate("sign_up/${selectedProfile}") {
                    // Implementar a lógica para salvar a role selecionada
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
            "Amigo" -> {
                navController.navigate("sign_up/${selectedProfile}") {
                    // Implementar a lógica para salvar a role selecionada
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }

    // Botões de seleção de perfil
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileButton(
                text = "Membro",
                modifier = Modifier.weight(1f),
                isSelected = selectedProfile == "Membro",
                onClick = { selectedProfile = "Membro" }
            )
            ProfileButton(
                text = "Guia",
                modifier = Modifier.weight(1f),
                isSelected = selectedProfile == "Guia",
                onClick = { selectedProfile = "Guia" }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileButton(
                text = "Anfitrião",
                modifier = Modifier.weight(1f),
                isSelected = selectedProfile == "Anfitrião",
                onClick = { selectedProfile = "Anfitrião" }
            )
            ProfileButton(
                text = "Amigo",
                modifier = Modifier.weight(1f),
                isSelected = selectedProfile == "Amigo",
                onClick = { selectedProfile = "Amigo" }
            )
        }
    }
}

@Composable
fun ProfileButton(text: String, modifier: Modifier = Modifier, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) FarwayAccent else FarwayCardBackground,
            contentColor = if (isSelected) FarwayCardBackground else FarwayTextDark
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}