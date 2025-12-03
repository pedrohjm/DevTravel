package com.example.faraway.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faraway.ui.data.DocumentItem
import com.example.faraway.ui.data.DocumentStatus


@Composable
fun DocumentosScreen(navController: NavController? = null) {

    val obrigatorios = listOf(
        DocumentItem("RG ou CNH", DocumentStatus.AUSENTE),
        DocumentItem("Comprovante de Residência", DocumentStatus.REJEITADO, reason = "Documento ilegível")
    )

    val meusDocs = listOf(
        DocumentItem("Certificado_Profissional.pdf", DocumentStatus.APROVADO, date = "10/11/2025", size = "1.2 MB")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F7FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone Voltar
            IconButton(onClick = { navController?.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF099FC4) // cor azul personalizada
                )
            }

            Spacer(Modifier.width(8.dp))

        }


        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)

                .background(Color.White, shape = RoundedCornerShape(18.dp))
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Círculo azul
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF58A8CF), shape = RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    // Erica, coloca imagem do icone aqui!!!!
                    // Icon(Icons.Default.Description, contentDescription = null, tint = Color.White)
                }

                Spacer(Modifier.width(12.dp))

                // Textos alinhados ao círculo
                Column {
                    Text(
                        text = "Documentos Profissionais",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "Gerencie seus documentos profissionais",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { /* abrir picker/upload */ },
                modifier = Modifier
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally), // centraliza horizontalmente
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0891B2))
            ) {
                Text("Adicionar Novo Documento", fontSize = 14.sp)
            }


            Spacer(Modifier.height(18.dp))

            Text("Documentos Obrigatórios", color = Color(0xFFD32F2F), fontSize = 15.sp)
            Spacer(Modifier.height(8.dp))
            obrigatorios.forEach {
                Spacer(Modifier.height(12.dp))
                DocumentCard(item = it)
            }

            Spacer(Modifier.height(18.dp))

            Text("Meus Documentos", fontSize = 15.sp)
            Spacer(Modifier.height(8.dp))
            meusDocs.forEach {
                Spacer(Modifier.height(12.dp))
                DocumentCard(item = it)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun DocumentCard(item: DocumentItem, onDelete: () -> Unit = {}) {
    val (bgColor, labelColor) = when (item.status) {
        DocumentStatus.AUSENTE -> Pair(Color(0xFFFFE8E8), Color(0xFFD32F2F))
        DocumentStatus.REJEITADO -> Pair(Color(0xFFFFE6E6), Color(0xFFD32F2F))
        DocumentStatus.APROVADO -> Pair(Color(0xFFE8F7EA), Color(0xFF137A3D))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp) // altura mínima para permitir posicionamento
            .border(width = 1.dp, color = labelColor, shape = RoundedCornerShape(16.dp))
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // Conteúdo principal centralizado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        //    horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            item.reason?.let {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Motivo: $it",
                    color = Color(0xFF7A1F1F),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            if (item.date != null && item.size != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${item.date} • ${item.size}",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Status no canto superior direito
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(labelColor.copy(alpha = 0.12f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .width(56.dp) //  largura fixa
                .height(16.dp), //  altura fixa
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (item.status) {
                    DocumentStatus.AUSENTE -> "Ausente"
                    DocumentStatus.REJEITADO -> "Rejeitado"
                    DocumentStatus.APROVADO -> "Aprovado"
                },
                color = labelColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


        // Lixeira no canto inferior direito
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Gray)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DocumentosPreview() {
    DocumentosScreen()
}
