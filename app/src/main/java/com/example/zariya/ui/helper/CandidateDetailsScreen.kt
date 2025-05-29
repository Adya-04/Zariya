package com.example.zariya.ui.helper

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.zariya.R
import com.example.zariya.models.CreateApplicationRequest
import com.example.zariya.models.CreateApplicationResponse
import com.example.zariya.navigation.Helper
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.HelperViewModel
import com.example.zariya.utils.NetworkResult

@Composable
fun CandidateDetailsScreenforHelper(
    navController: NavController,
    jobId: String,
    helperViewModel: HelperViewModel
) {
    val context = LocalContext.current
    val token by helperViewModel.token.collectAsState(initial = "")
    val applicationState by helperViewModel.applicationLiveData.collectAsState()

    val shape = RoundedCornerShape(20.dp)
    val fieldBg = Color(0xFFFDF8F3)
    val textColor = Color(0xFF333333)

    val yesNoOptions = listOf("Yes", "No")
    val genderOptions = listOf("male", "female", "other")

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("male") }
    var address by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("Yes") }
    var contact by remember { mutableStateOf("") }

    var genderExpanded by remember { mutableStateOf(false) }
    var eduExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF1DA), Color(0xFFFFF5E8))))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Application Form",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(id = R.drawable.candidate_picture),
            contentDescription = "Profile",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            shape = shape
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Age",
                value = age,
                onValueChange = { newAge ->
                    if (newAge.isEmpty() || newAge.toIntOrNull() != null) {
                        age = newAge
                    }
                },
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            )

            CustomDropdown(
                label = "Gender",
                options = genderOptions,
                selected = gender,
                onSelect = { gender = it },
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it },
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        InputField(
            label = "Address",
            value = address,
            onValueChange = { address = it },
            shape = shape
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Skills",
                value = skills,
                onValueChange = { skills = it },
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            )

            CustomDropdown(
                label = "Basic Edu.",
                options = yesNoOptions,
                selected = education,
                onSelect = { education = it },
                expanded = eduExpanded,
                onExpandedChange = { eduExpanded = it },
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        InputField(
            label = "Contact",
            value = contact,
            onValueChange = { contact = it },
            shape = shape
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit button
        Box(
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {
                    val ageInt = age.toIntOrNull() ?: 0
                    if (ageInt > 0 && name.isNotBlank() && contact.isNotBlank()) {
                        helperViewModel.submitApplication(
                            token = token,
                            CreateApplicationRequest(
                                jobId = jobId,
                                name = name,
                                age = ageInt,
                                gender = gender,
                                address = address,
                                skills = skills,
                                education = education,
                                contact = contact
                            )
                        )
                    }
                }
                .padding(horizontal = 32.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Submit Application",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                color = Color.Black
            )
        }
        when (applicationState) {
            is NetworkResult.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (applicationState as NetworkResult.Error<*>).message.toString()
                        )
                    }
            }

            is NetworkResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Color(0xFF7F7769)
                    )
            }

            is NetworkResult.Success -> {
                val result =
                    (applicationState as NetworkResult.Success<CreateApplicationResponse>).data
                Log.d("Candidate Details Screen", "Application submitted RESULT: $result")
                Toast.makeText(context, result?.message, Toast.LENGTH_SHORT).show()
                navController.navigate(Helper.HelperDashboard.route)
            }

            is NetworkResult.Start<*> -> {}
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, shape)
                .background(Color.White, shape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = Poppins
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, shape)
                .background(Color.White, shape)
                .clickable { onExpandedChange(true) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = selected, fontFamily = Poppins, fontSize = 16.sp)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it, fontFamily = Poppins) },
                        onClick = {
                            onSelect(it)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}