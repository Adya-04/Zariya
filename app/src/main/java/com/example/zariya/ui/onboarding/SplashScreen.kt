package com.example.zariya.ui.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zariya.R
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    navigateToHome: (role: String) -> Unit = {},
    navigateToWelcome: () -> Unit = {}
) {

    val token by authViewModel.token.collectAsState(initial = "")
    val role by authViewModel.role.collectAsState(initial = "")

    // Check token when it's available
    LaunchedEffect(token, role) {
        Log.d("SPLASH_SCREEN", "Token: $token, Role: $role")
        delay(2000)
        if (token.isNotEmpty() && role.isNotEmpty()) {
            // Token exists, navigate to home
            navigateToHome(role)
        } else {
            // No token, navigate to welcome
            navigateToWelcome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppLinearGradient)
    ) {
        Image(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center),
            painter = painterResource(R.drawable.zariya_logo),
            contentDescription = "Zariya Logo"
        )
    }

}

@Preview
@Composable
fun PreviewSplashScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = AppLinearGradient)
    ) {
        Image(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center),
            painter = painterResource(R.drawable.zariya_logo),
            contentDescription = "Zariya Logo"
        )
    }
}
