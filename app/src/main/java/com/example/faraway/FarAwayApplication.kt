package com.example.faraway

import android.app.Application
import com.google.firebase.FirebaseApp

class FarawayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Esta linha deve ser executada antes de tudo
        FirebaseApp.initializeApp(this)
    }
}
