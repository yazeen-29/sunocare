package com.sunocare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SunoCareApp() }
    }
}

private enum class Destination {
    Welcome, Patient, Caregiver, DemoRequest;

    fun previous(): Destination = when (this) {
        DemoRequest -> Caregiver
        else -> Welcome
    }
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF145B4B),
    onPrimary = Color.White,
    surface = Color(0xFFF8FAF7),
    onSurface = Color(0xFF17221E),
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF9EDBC6),
    onPrimary = Color(0xFF00382B),
    surface = Color(0xFF121C18),
    onSurface = Color(0xFFE1EDE6),
)

@Composable
private fun SunoCareApp() {
    var destination by rememberSaveable { mutableStateOf(Destination.Welcome) }
    val canGoBack = destination != Destination.Welcome
    fun goBack() {
        destination = destination.previous()
    }

    BackHandler(enabled = canGoBack, onBack = ::goBack)
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors) {
        Scaffold(containerColor = MaterialTheme.colorScheme.surface) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (canGoBack) {
                    OutlinedButton(
                        onClick = ::goBack,
                        modifier = Modifier.padding(horizontal = 24.dp).heightIn(min = 48.dp),
                    ) {
                        Text(if (destination == Destination.DemoRequest) "Back to requests" else "Back to welcome")
                    }
                }
                when (destination) {
                    Destination.Welcome -> WelcomeScreen(
                        onPatient = { destination = Destination.Patient },
                        onCaregiver = { destination = Destination.Caregiver },
                    )
                    Destination.Patient -> PatientScreen()
                    Destination.Caregiver -> CaregiverScreen(
                        onOpenRequest = { destination = Destination.DemoRequest },
                    )
                    Destination.DemoRequest -> DemoRequestScreen()
                }
            }
        }
    }
}

@Composable
private fun WelcomeScreen(onPatient: () -> Unit, onCaregiver: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "SunoCare",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics { heading() },
        )
        Text("Choose a role to explore the demo.", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onPatient, modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)) {
            Text("I am a Patient", style = MaterialTheme.typography.titleLarge)
        }
        OutlinedButton(onClick = onCaregiver, modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)) {
            Text("I am a Caregiver", style = MaterialTheme.typography.titleLarge)
        }
    }
}
