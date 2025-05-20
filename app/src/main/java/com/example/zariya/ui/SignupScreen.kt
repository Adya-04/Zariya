package com.example.zariya.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zariya.ui.theme.AppLinearGradient

@Preview
@Composable
fun SignupScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
    ){
        Text("Signup Screen")
    }

}