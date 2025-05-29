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
import com.example.zariya.models.CompanyRegisRequest
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.AuthViewModel
import com.example.zariya.utils.NetworkResult

@Preview
@Composable
fun CompanyRegisterScreenPreview(){
    CompanyRegisterScreen(authViewModel = hiltViewModel())
}

@Composable
fun CompanyRegisterScreen(
    authViewModel: AuthViewModel,
    onCompanyRegisterSuccess: () -> Unit = {}
){
    val context = LocalContext.current
    val shape = RoundedCornerShape(50)

    Box(modifier = Modifier.fillMaxSize()) {
        var companyName by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var website by remember { mutableStateOf("") }
        var isButtonEnabled by remember { mutableStateOf(true) }

        val registerState by authViewModel.registerCompanyLiveData.collectAsState()
        val token by authViewModel.token.collectAsState(initial = "")

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
                text = "As Recruiter",
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8F8F8F),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )
            Spacer(modifier = Modifier.height(110.dp))

            InputField(
                label = "Company Name",
                value = companyName,
                onValueChange = { companyName = it },
                shape = shape)
            InputField(
                label = "Description",
                value = description,
                onValueChange = { description = it },
                shape = shape
            )
            InputField(
                label = "Location",
                value = location,
                onValueChange = { location = it },
                shape = shape
            )
            InputField(
                label = "Website",
                value = website,
                onValueChange = { website = it },
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
                            if (companyName.isNotBlank() && description.isNotBlank() &&
                                location.isNotBlank() && website.isNotBlank()) {
                                Log.d(
                                    "Company Register",
                                    "Company Name: $companyName, Description: $description, " +
                                            "Location: $location, Website: $website"
                                )
                                isButtonEnabled = false
                                authViewModel.registerCompany(
                                    token,
                                    CompanyRegisRequest(companyName, description, location, website)
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else Modifier
                    )
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NEXT",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }

            // State handling
            when (registerState) {
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
                            text = (registerState as NetworkResult.Error<*>).message.toString(),
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
                    Toast.makeText(context, "Company registered successfully!", Toast.LENGTH_SHORT).show()
                    onCompanyRegisterSuccess()
                }

                is NetworkResult.Start<*> -> {}
            }

            Spacer(modifier = Modifier.height(52.dp))
        }
    }
}