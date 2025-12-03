package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faraway.ui.data.Connection


@Composable
fun ConnectionCard(
    user: Connection,
    onAccept: (() -> Unit)? = null,
    onReject: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD5EEF5), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar com iniciais
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFB7DDE7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(user.initials, fontSize = 14.sp, color = Color(0xFF0A1417))
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nome + País
        Column(modifier = Modifier.weight(1f)) {
            Text(user.name, fontSize = 15.sp)
            Text(user.location, fontSize = 13.sp, color = Color.Gray)
        }

        // Se for solicitação → mostra ✔ e x
        if (user.isRequest) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF00D4FF), shape = RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = "Aceitar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .border(width = 2.dp, color = Color(0xFFE7000B), shape = RoundedCornerShape(20.dp)),
                      contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Recusar",
                    tint = Color(0xFFE7000B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color(0xFF099FC4)
            )

            Spacer(Modifier.height(8.dp))

            Icon(
                Icons.Default.ChatBubble,
                contentDescription = "Chat",
                tint = Color(0xFF099FC4)
            )
        }
    }

}
}

/* ------------------------------ TELA COMPLETA ------------------------------ */

@Composable
fun ConexoesScreen(navController: NavController? = null) {

    val novasSolicitacoes = listOf(
        Connection("MS", "Maria Silva", "Grécia", isRequest = true)
    )

    val conexoes = listOf(
        Connection("JS", "João Santos", "Suíça"),
        Connection("AC", "Ana Costa", "Suécia")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F7FA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Ícone Voltar
        IconButton(onClick = { navController?.navigateUp() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                tint = Color(0xFF065395) // cor azul personalizada
            )
        }

        Spacer(Modifier.height(4.dp))

        // Card branco grande
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp))
                .padding(20.dp)
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF099FC4), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Minhas Conexões",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    Text("Minhas Conexões", fontSize = 18.sp, color = Color(0xFF1E4A57))
                    Text("2 conexões ativas", fontSize = 13.sp, color = Color.Gray)
                }
            }

         //   Spacer(Modifier.height(2.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = { /* ação de adicionar */ },
                    modifier = Modifier
                        .wrapContentWidth()   //  ajusta ao conteúdo
                        .height(40.dp)
                        .width(140.dp),
                      //  .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0891B2))
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Adicionar", fontSize = 13.sp, color = Color.White)
                }
            }


            Spacer(Modifier.height(14.dp))

            /* NOVAS SOLICITAÇÕES */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween //empurra o "notificações" para a direita
            ) {
                Text("Novas Solicitações", fontSize = 15.sp)

                Box(
                    modifier = Modifier
                        .background(Color(0xFF00D4FF), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "${novasSolicitacoes.size}",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            novasSolicitacoes.forEach {
                ConnectionCard(
                    user = it,
                    onAccept = { /* aceitar solicitação */ },
                    onReject = { /* rejeitar solicitação */ }
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(10.dp))

            /* CONEXÕES */
            Text("Conexões", fontSize = 15.sp)
            Spacer(Modifier.height(10.dp))

            conexoes.forEach {
                ConnectionCard(user = it)
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConexoes() {
    ConexoesScreen()
}
