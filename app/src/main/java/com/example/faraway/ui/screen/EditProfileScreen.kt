package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.faraway.ui.viewmodel.ProfileViewModel
import com.example.faraway.ui.viewmodel.ProfileViewModelFactory
import com.example.faraway.ui.viewmodel.AuthViewModel
import com.example.faraway.ui.data.AuthRepository


import com.example.faraway.ui.theme.PrimaryDark
import com.example.faraway.ui.theme.AccentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    repository: AuthRepository = AuthRepository(),
    factory: ProfileViewModelFactory = ProfileViewModelFactory(repository),
    profileViewModel: ProfileViewModel = viewModel(factory = factory),
    authViewModel: AuthViewModel
) {
    val user by profileViewModel.user.collectAsState()
    val profileImageUrl by profileViewModel.profileImageUrl.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {

            profileViewModel.uploadProfileImage(it) {
                authViewModel.fetchUserData()
            }
        }
    }

    var description by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var interestsText by rememberSaveable { mutableStateOf("") }
    var languagesText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            description = it.description ?: ""
            location = it.location ?: ""
            interestsText = it.interests?.joinToString(", ") ?: ""
            languagesText = it.languages?.joinToString(", ") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val newInterests = interestsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    val newLanguages = languagesText.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    profileViewModel.saveProfile(
                        description = description,
                        location = location,
                        interests = newInterests,
                        languages = newLanguages,
                        onSuccess = {
                            authViewModel.fetchUserData()
                        }
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar Alterações", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, PrimaryDark, CircleShape)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUrl.isNullOrEmpty()) {
                    Icon(Icons.Filled.Person, contentDescription = "Foto de Perfil", tint = Color.Gray, modifier = Modifier.size(70.dp))
                } else {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AccentColor)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Mudar Foto", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição (Sobre Mim)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 200.dp),
                singleLine = false
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Localização (Cidade, País)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = interestsText,
                onValueChange = { interestsText = it },
                label = { Text("Interesses (separados por vírgula)") },
                placeholder = { Text("Ex: Futebol, Culinária, Fotografia") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = languagesText,
                onValueChange = { languagesText = it },
                label = { Text("Línguas (separadas por vírgula)") },
                placeholder = { Text("Ex: Português, Inglês, Espanhol") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}