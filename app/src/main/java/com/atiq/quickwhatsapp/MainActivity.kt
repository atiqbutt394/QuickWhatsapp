package com.atiq.quickwhatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atiq.quickwhatsapp.ui.screen.HomeScreen
import com.atiq.quickwhatsapp.ui.theme.QuickWhatsappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickWhatsappTheme {
                HomeScreen()
            }
        }
    }
}