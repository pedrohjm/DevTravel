package com.example.faraway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.faraway.ui.theme.FarAwayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarAwayTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ChatPage(
                        messages = listOf(
                            ChatMessage("Carlos Silva", "Que ótimo! Nos vemos às 10h então.", "10:30", 0, true),
                            ChatMessage("Mariana Costa", "Sua reserva está confirmada!", "Ontem", 2, false),
                            ChatMessage("Lucas Martins", "Vamos nos encontrar no café amanhã?", "2d atrás", 0, true)
                        )
                    )
                }
            }
        }
    }
}
