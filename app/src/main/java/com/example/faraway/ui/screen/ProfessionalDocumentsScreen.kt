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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentPaste
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

val DocPrimaryBlue = Color(0xFF192F50)
val DocAccentColor = Color(0xFF00BCD4)
val DocBackground = Color(0xFFE0F7FA)
val DocRed = Color(0xFFD32F2F)
val DocGreen = Color(0xFF388E3C)

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
            .background(DocBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone Voltar
            IconButton(onClick = { navController!!.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = DocPrimaryBlue // cor azul personalizada
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Card Branco Principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centraliza tudo
            ) {

                // --- ÍCONE E TÍTULO
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Círculo Azul Claro com Ícone
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFE0F2F1), CircleShape), // Fundo do ícone
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentPaste, // Ícone de Prancheta
                            contentDescription = null,
                            tint = Color(0xFF00695C), // Verde/Azul escuro do ícone
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Documentos Profissionais",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Gerencie seus documentos profissionais",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Botão "Adicionar Novo Documento"
                Button(
                    onClick = { /* ação */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D9CAB)), // Azul acinzentado da imagem
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Adicionar Novo Documento", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(24.dp))

                // Seção Obrigatórios
                Text(
                    text = "Documentos Obrigatórios",
                    color = DocRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(8.dp))

                obrigatorios.forEach { doc ->
                    DocumentCard(item = doc)
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(16.dp))

                // Seção Meus Documentos
                Text(
                    text = "Meus Documentos",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(8.dp))

                meusDocs.forEach { doc ->
                    DocumentCard(item = doc)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DocumentCard(item: DocumentItem, onDelete: () -> Unit = {}) {
    // Cores baseadas no status
    val (bgColor, borderColor, textColor) = when (item.status) {
        DocumentStatus.AUSENTE -> Triple(Color(0xFFFFEBEE), DocRed, DocRed) // Fundo vermelho claro
        DocumentStatus.REJEITADO -> Triple(Color(0xFFFFEBEE), DocRed, DocRed)
        DocumentStatus.APROVADO -> Triple(Color(0xFFE8F5E9), DocGreen, DocGreen) // Fundo verde claro
    }

    // Status Texto
    val statusText = when (item.status) {
        DocumentStatus.AUSENTE -> "Ausente"
        DocumentStatus.REJEITADO -> "Rejeitado"
        DocumentStatus.APROVADO -> "Aprovado"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            // Badge de Status (No topo direito)
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = statusText,
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Título do Documento
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            // Motivo da Rejeição (se houver)
            if (item.reason != null) {
                Text(
                    text = "Motivo: ${item.reason}",
                    fontSize = 12.sp,
                    color = DocRed
                )
            }

            // Data e Tamanho (se houver)
            if (item.date != null) {
                Text(
                    text = "${item.date} • ${item.size ?: ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Ícone de Lixeira (Canto inferior direito)
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Excluir",
            tint = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(20.dp)
                .clickable { onDelete() }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DocumentosPreview() {
    DocumentosScreen()
}