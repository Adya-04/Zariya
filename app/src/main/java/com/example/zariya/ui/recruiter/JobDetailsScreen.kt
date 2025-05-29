package com.example.zariya.ui.recruiter

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zariya.models.JobPostRequest
import com.example.zariya.models.JobPostResponse
import com.example.zariya.navigation.Recruiter
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.RecruiterViewModel
import com.example.zariya.utils.NetworkResult

@Composable
fun JobDetailsScreen(
    navController: NavController,
    viewModel: RecruiterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(50)
    val fieldBackground = Color(0xFFFDF8F3)
    val scrollState = rememberScrollState()

    val token by viewModel.token.collectAsState(initial = "")
    val jobPostState by viewModel.jobPostLiveData.collectAsState()

    val workTypes = listOf("Full-time", "Part-time")
    var expanded by remember { mutableStateOf(false) }
    var selectedWorkType by remember { mutableStateOf(workTypes[0]) }

    // Form state
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var workingHours by remember { mutableStateOf("") }
    var skillsRequired by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Job Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Job Title
        InputField(
            label = "Job Title",
            value = title,
            onValueChange = { title = it },
            placeholder = "Enter Job Title",
            shape = shape
        )
        // Work Type Dropdown
        TextFieldLabel("Work Type")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(fieldBackground)
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Text(
                text = selectedWorkType,
                fontFamily = Poppins,
                fontSize = 16.sp
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                workTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type, fontFamily = Poppins) },
                        onClick = {
                            selectedWorkType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        // Salary and Contact Row
        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Salary",
                value = salary,
                onValueChange = { salary = it },
                placeholder = "Rs. 100",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            InputField(
                label = "Contact",
                value = contact,
                onValueChange = { contact = it },
                placeholder = "705541XXXX",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        InputField(
            label = "Description",
            value = description,
            onValueChange = { description = it },
            placeholder = "Describe the job",
            shape = shape
        )

        InputField(
            label = "Location",
            value = location,
            onValueChange = { location = it },
            placeholder = "Enter Job Location",
            shape = shape
        )

        InputField(
            label = "Working hours/Shift",
            value = workingHours,
            onValueChange = { workingHours = it },
            placeholder = "eg. 9 am–6 pm",
            shape = shape
        )

        InputField(
            label = "Skills (If required)",
            value = skillsRequired,
            onValueChange = { skillsRequired = it },
            placeholder = "Enter required skills",
            shape = shape
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Box(
            modifier = Modifier
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Color.White)
                .clickable {
                    if (title.isBlank() || description.isBlank() || salary.isBlank() ||
                        contact.isBlank() || location.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please fill all required fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val request = JobPostRequest(
                            contact = contact,
                            description = description,
                            salary = salary,
                            skillsRequired = skillsRequired,
                            title = title,
                            workType = selectedWorkType,
                            workingHours = workingHours
                        )
                        viewModel.postJob(token,request)
                    }
                }
                .padding(horizontal = 32.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SUBMIT",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        when(jobPostState) {
            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NetworkResult.Success -> {
                LaunchedEffect(jobPostState) {
                    val response = (jobPostState as NetworkResult.Success<JobPostResponse>).data
                    if (response != null) {
                        Toast.makeText(
                            context,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (response.success) {
                            navController.navigate(Recruiter.RecruiterDashboard.route) {
                                popUpTo(Recruiter.JobDetail.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
            is NetworkResult.Error -> {
                LaunchedEffect(jobPostState) {
                    val errorMessage = (jobPostState as NetworkResult.Error).message
                    Toast.makeText(
                        context,
                        "Error: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is NetworkResult.Start<*> -> {
                // Initial state - do nothing
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            TextFieldLabel(label)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(placeholder, color = Color.Gray, fontFamily = Poppins)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun TextFieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = Poppins,
        color = Color(0xFF555555),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.Start)
            .padding(bottom = 4.dp)
    )
}


@Composable
fun InputBox(
    label: String,
    placeholder: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            TextFieldLabel(label)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Color(0xFFFDF8F3))
                .height(50.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = Poppins
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (true) { // show placeholder
                        Text(placeholder, color = Color.Gray, fontFamily = Poppins)
                    }
                    innerTextField()
                }
            )
        }
    }
}
