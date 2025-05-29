package com.example.zariya.ui.recruiter

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.ButtonGradient
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zariya.R
import com.example.zariya.models.Application
import com.example.zariya.utils.NetworkResult
import com.example.zariya.viewmodel.RecruiterViewModel


@Composable
fun CandidatesManagementScreen(
    jobId: String,
    navController: NavController,
    viewModel: RecruiterViewModel
) {
    val context = LocalContext.current
    val token by viewModel.token.collectAsState(initial = "")

    // Collect state from ViewModel
    val appliedCandidatesState by viewModel.appliedCandidatesLiveData.collectAsState()
    val approvedCandidatesState by viewModel.approvedCandidatesLiveData.collectAsState()
    val hiredCandidatesState by viewModel.hiredCandidatesLiveData.collectAsState()


    LaunchedEffect(token, jobId) {
        if (token.isNotBlank()) {
            viewModel.fetchCandidates(token, jobId)
        }
    }

    // Handle error states
    LaunchedEffect(appliedCandidatesState, approvedCandidatesState, hiredCandidatesState) {
        when {
            appliedCandidatesState is NetworkResult.Error -> {
                val error = (appliedCandidatesState as NetworkResult.Error).message
                Toast.makeText(context, "Applied: $error", Toast.LENGTH_SHORT).show()
            }

            approvedCandidatesState is NetworkResult.Error -> {
                val error = (approvedCandidatesState as NetworkResult.Error).message
                Toast.makeText(context, "Approved: $error", Toast.LENGTH_SHORT).show()
            }

            hiredCandidatesState is NetworkResult.Error -> {
                val error = (hiredCandidatesState as NetworkResult.Error).message
                Toast.makeText(context, "Hired: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
    ) {
        when {
            // Show loading if any of the states are loading
            appliedCandidatesState is NetworkResult.Loading ||
                    approvedCandidatesState is NetworkResult.Loading ||
                    hiredCandidatesState is NetworkResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                // Extract data from states
                val appliedCandidates =
                    (appliedCandidatesState as? NetworkResult.Success)?.data ?: emptyList()
                val approvedCandidates =
                    (approvedCandidatesState as? NetworkResult.Success)?.data ?: emptyList()
                val hiredCandidates =
                    (hiredCandidatesState as? NetworkResult.Success)?.data ?: emptyList()

                CandidateManagementContent(
                    appliedCandidates = appliedCandidates,
                    approvedCandidates = approvedCandidates,
                    hiredCandidates = hiredCandidates
                )
            }
        }
    }
}


@Composable
fun ApplicantCardforApplied(
    application: Application,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(brush = ButtonGradient)
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.candidate_picture),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = application.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = application.address,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Age - ${application.age}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                // Buttons for Applied tab
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionChip(text = "Reject", onClick = onReject)
                    ActionChip(text = "Approve", onClick = onApprove)
                }
            }
        }
    }
}

@Composable
private fun CandidateManagementContent(
    appliedCandidates: List<Application>,
    approvedCandidates: List<Application>,
    hiredCandidates: List<Application>
) {
    val tabs = listOf("Applied", "Approved", "Hired")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(2.dp)
                        .background(Color.Black, RoundedCornerShape(1.dp))
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 18.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            when (selectedTabIndex) {
                0 -> items(appliedCandidates.size) { index ->
                    ApplicantCardforApplied(
                        application = appliedCandidates[index],
                        onApprove = { /* TODO */ },
                        onReject = { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                1 -> items(approvedCandidates.size) { index ->
                    ApplicantCardforApproved(
                        application = approvedCandidates[index],
                        onHire = { /* TODO */ },
                        onReject = { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                2 -> items(hiredCandidates.size) { index ->
                    ApplicantCardforHired(application = hiredCandidates[index])
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ApplicantCardforApproved(
    application: Application,
    onHire: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(brush = ButtonGradient)
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.candidate_picture), // Replace with real image
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = application.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = application.address,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Age - ${application.age}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                // Buttons for Approved tab
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionChip(text = "Reject", onClick = onReject)
                    ActionChip(text = "Hire", onClick = onHire)
                }
            }
        }
    }
}

@Composable
fun ApplicantCardforHired(
    application: Application
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Transparent container
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Gradient background with white border like JobDetailCard
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .background(brush = ButtonGradient)  // Use your gradient brush here
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.candidate_picture), // Replace with real image
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = application.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                text = application.address,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Age - ${application.age}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}