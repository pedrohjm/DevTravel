plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // PLUGIN DO GOOGLE SERVICES
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.faraway"
    // Corrigido: Usando a versão diretamente, assumindo que 'release(36)' é uma função auxiliar
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.faraway"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // -----------------------------------------------------------------
    // 1. DEPENDÊNCIAS DO COMPOSE E CORE
    // -----------------------------------------------------------------

    // BOM do Compose
    implementation(platform(libs.androidx.compose.bom))

    // Core KTX e Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI e Material
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)

    // Ícones Estendidos (Corrigido: Usando string direta)
    implementation("androidx.compose.material:material-icons-extended")

    // -----------------------------------------------------------------
    // 2. DEPENDÊNCIAS DO VIEWMODEL E COMPOSE
    // -----------------------------------------------------------------
    // Para usar o Composable viewModel() e o ViewModelProvider.Factory
    // (Corrigido: Usando string direta)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // -----------------------------------------------------------------
    // 3. DEPENDÊNCIAS DO FIREBASE
    // -----------------------------------------------------------------
    // Firebase BOM (para gerenciar versões)
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Firebase Auth (Corrigido: Usando string direta SEM versão para que o BOM gerencie)
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")

    // Outras dependências do Firebase (já estavam sem versão, o que é correto com o BOM)
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    // Corrotinas para integração com Firebase (para usar .await())
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")

    // -----------------------------------------------------------------
    // 4. DEPENDÊNCIAS DE NAVEGAÇÃO
    // -----------------------------------------------------------------
    // (Corrigido: Usando string direta)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // -----------------------------------------------------------------
    // 5. DEPENDÊNCIAS ADICIONAIS
    // -----------------------------------------------------------------
    implementation(libs.generativeai)
    implementation(libs.androidx.foundation)

    // -----------------------------------------------------------------
    // 6. DEPENDÊNCIAS DE TESTE
    // -----------------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
