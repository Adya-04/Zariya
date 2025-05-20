package com.example.zariya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zariya.navigation.AppNavigation
import com.example.zariya.ui.WelcomeScreen
import com.example.zariya.ui.theme.ZariyaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZariyaTheme {
                AppNavigation()
            }
        }
    }
}