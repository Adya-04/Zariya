package com.example.zariya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.zariya.navigation.RootNavGraph
import com.example.zariya.ui.theme.ZariyaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZariyaTheme {
                RootNavGraph(rememberNavController(), activity = this)
            }
        }
    }
}