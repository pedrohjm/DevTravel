package com.example.faraway.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.faraway.Destinations
import com.example.faraway.travelerNavItems
import com.example.faraway.ui.theme.PrimaryDark

// --- Definições de Cores e Temas (Assumindo que estão definidas no projeto original) ---
val TagColor = Color(0xFFE0E0E0) // Cor de fundo para as tags


// --- BottomNavBar COMPONENT
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomNavBar(
    navController: NavController,
    navItems: List<NavItem>,
    startRoute: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true
            val iconColor = if (selected) AccentColor else Color.Gray

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(startRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                color = if (selected) AccentColor.copy(alpha = 0.1f) else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = iconColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// --- 1. Data Class para o Perfil Social ---
data class SocialUser(
    val id: Int,
    val name: String,
    val nationality: String,
    val location: String,
    val description: String,
    val tags: List<String>,
    val imageUrl: Int // Usaremos Int para um drawable placeholder
)

// --- 2. Composable para o Card de Usuário Social ---
@Composable
fun SocialUserCard(user: SocialUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Imagem do Perfil (Placeholder)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.Person, // Ícone mais apropriado
                    contentDescription = "Foto de Perfil",
                    tint = Color.Gray,
                    modifier = Modifier.align(Alignment.Center).size(40.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            // Conteúdo do Card
            Column(modifier = Modifier.weight(1f)) {
                // Nome e Botão "Ver Perfil"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TagColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Ver Perfil",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = "Ver Perfil", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(4.dp))

                // Localização e Nacionalidade
                Text(text = "${user.nationality}", fontSize = 12.sp, color = Color.Gray)
                Text(text = user.location, fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))

                // Descrição
                Text(text = user.description, fontSize = 14.sp, maxLines = 3)
                Spacer(Modifier.height(8.dp))

                // Tags e Botão Chat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tags
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        user.tags.forEach { tag ->
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color(0xFF8200DB),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF8200DB).copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Botão Chat
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TagColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = "Chat", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// --- 3. Composable para a Barra de Busca (Adaptada) ---
@Composable
fun SocialSearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Ícone de Busca",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        // TextField para a entrada de texto
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text("Buscar por nome...", color = Color.Gray, fontSize = 14.sp)
                }
                innerTextField()
            },
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Ícone de Filtro",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

// --- 4. Composable Principal da Tela Social ---
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SocialScreen(navController: NavController) {
    // Dados de exemplo
    val socialUsers = remember {
        listOf(
            SocialUser(
                id = 1,
                name = "Marcos Lima",
                nationality = "Nacionalidade brasileira",
                location = "Lisboa, Portugal",
                description = "Meu passatempo favorito é colecionar carimbos no passaporte e amigos pelo mundo.",
                tags = listOf("Cultura", "Música"),
                imageUrl = 0 // Placeholder
            ),
            SocialUser(
                id = 2,
                name = "Laura Ribeiro",
                nationality = "Nacionalidade brasileira",
                location = "Lisboa, Portugal",
                description = "Para mim, viajar é sinônimo de festa, novas culturas e muitas histórias para contar.",
                tags = listOf("Café e Conversa", "Música"),
                imageUrl = 0 // Placeholder
            )
        )
    }

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Explorar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Ação de Voltar */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark // Fundo azul
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navItems = travelerNavItems,
                startRoute = Destinations.SOCIAL_ROUTE
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)) // Fundo cinza claro
        ) {
            // Barra de Busca
            SocialSearchBar(
                searchText = searchText,
                onSearchTextChanged = { searchText = it }
            )

            // Lista de Usuários
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(socialUsers) { user ->
                    SocialUserCard(user = user)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialScreenPreview() {
    SocialScreen(navController = rememberNavController())
}