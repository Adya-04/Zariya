package com.example.zariya.ui.onboarding


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zariya.R
import com.example.zariya.models.LoginRequest
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.AuthViewModel
import com.example.zariya.utils.NetworkResult

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(role = "Candidate", authViewModel = hiltViewModel())
}

@Composable
fun LoginScreen(
    role: String?,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit = {}
){
    val shape = RoundedCornerShape(50)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Log.d("LoginScreen", "Role: $role")

    val loginState  by authViewModel.loginResultLiveData.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.signup_bg),
            contentDescription = "Sign Up Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "LOG IN",
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = Color(0xFF4F4F4F)
            )

            Spacer(modifier = Modifier.height(180.dp))
            InputField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                shape = shape
            )
            InputField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                shape = shape
            )

            Spacer(modifier = Modifier.height(44.dp))

            // LOGIN Button
            Box(
                modifier = Modifier
                    .shadow(8.dp, shape)
                    .clip(shape)
                    .background(if (isButtonEnabled) Color.White else Color.LightGray)
                    .then(
                        if (isButtonEnabled) Modifier.clickable {
                            if (email.isNotBlank() && password.isNotBlank() && role != null) {
                                Log.d("Login", "Email: $email, Password: $password, Role: $role")
                                isButtonEnabled = false
                                authViewModel.login(
                                    LoginRequest(email, password, role.lowercase())
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill all fields and select role",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else Modifier
                    )
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "LOGIN",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }
            // State handling
            when (loginState) {
                is NetworkResult.Error -> {
                    isButtonEnabled = true
                    Row(
                        modifier = Modifier
                            .padding(20.dp, 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Error",
                            modifier = Modifier.size(12.dp).padding(end = 4.dp)
                        )
                        Text(
                            text = (loginState as NetworkResult.Error<*>).message.toString(),
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = Color.Red
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    isButtonEnabled = false
                    Row(
                        modifier = Modifier
                            .padding(20.dp, 10.dp)
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color(0xFF7F7769)
                        )
                    }
                }

                is NetworkResult.Success -> {
                    isButtonEnabled = true
                    val response = (loginState as NetworkResult.Success).data
                    response?.token?.let { token ->
                        authViewModel.saveToken(token)
                    }

                    authViewModel.saveRole(role ?: "")
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }

                is NetworkResult.Start<*> -> {}
            }

            Spacer(modifier = Modifier.height(52.dp))
        }
    }
}