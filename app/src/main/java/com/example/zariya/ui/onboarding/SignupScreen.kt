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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zariya.R
import com.example.zariya.models.SignupRequest
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.AuthViewModel
import com.example.zariya.utils.NetworkResult

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(role = "Candidate", authViewModel = hiltViewModel())
}

@Composable
fun SignUpScreen(
    role: String,
    authViewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(50)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    Log.d("SignUpScreen", "Role: $role")
    val signupState  by authViewModel.signupLiveData.collectAsState()

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
                text = "SIGN UP",
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = Color(0xFF4F4F4F)
            )

            Text(
                text = "Already have an account? Log in!",
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8F8F8F),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                    .clickable {
                        onLoginClick()
                    }
            )

            Spacer(modifier = Modifier.height(100.dp))

            InputField(
                label = "Name",
                value = name,
                onValueChange = { name = it },
                shape = shape)
            InputField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                shape = shape
            )
            InputField(
                label = "Contact",
                value = contact,
                onValueChange = { contact = it },
                shape = shape
            )
            InputField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                shape = shape
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NEXT Button
            Box(
                modifier = Modifier
                    .shadow(8.dp, shape)
                    .clip(shape)
                    .background(if (isButtonEnabled) Color.White else Color.LightGray)
                    .then(
                        if (isButtonEnabled) Modifier.clickable {
                            if (role != null && name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                                Log.d(
                                    "Signup",
                                    "Role: $role, Name: $name, Email: $email, Contact: $contact, Password: $password"
                                )
                                isButtonEnabled = false
                                authViewModel.signup(
                                    SignupRequest(
                                        name,
                                        email,
                                        contact,
                                        password,
                                        role.lowercase()
                                    )
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
            ){
                Text(
                    text = "NEXT",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }
            // Handle different states
            when (signupState) {
                is NetworkResult.Error -> {
                    isButtonEnabled = true
                    Row(
                        modifier = Modifier
                            .padding(20.dp, 10.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = (signupState as NetworkResult.Error<*>).message.toString(),
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            color = Color.Red // Your error color
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    isButtonEnabled = false
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 10.dp, 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color(0xFF7F7769) // Your loading color
                        )
                    }
                }

                is NetworkResult.Success -> {
                    isButtonEnabled = true

                    val response = (signupState as NetworkResult.Success).data
                    response?.token?.let { token ->
                        authViewModel.saveToken(token)
                    }

                    authViewModel.saveRole(role)

                    Toast.makeText(
                        context,
                        "Signup successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSignUpSuccess()
                }

                is NetworkResult.Start<*> -> {}
            }

            Spacer(Modifier.height(52.dp))
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    shape: RoundedCornerShape
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {

        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = Color(0xFF555555)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Color(0xFFFDF8F3))
                .height(50.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = Poppins
                ),
                singleLine = true
            )
        }
    }
}
