package com.example.zariya.ui.recruiter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.R
import com.example.zariya.models.ApplicationData
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.ButtonGradient
import com.example.zariya.ui.theme.Poppins

@Preview(showBackground = true)
@Composable
fun PreviewApplicationsScreen() {
    val dummyList = listOf(
        ApplicationData("Amina Khan", "Mumbai, Maharashtra", 60),
        ApplicationData("Rekha Sharma", "Delhi, India", 55)
    )
    ApplicationsReceivedScreen(applications = dummyList)
}

@Composable
fun ApplicationsReceivedScreen(applications: List<ApplicationData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Application Received",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = Color(0xFF4F4F4F),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(applications) { applicant ->
                ApplicantCard(applicant = applicant)
            }
        }
    }
}

@Composable
fun ApplicantCard(applicant: ApplicationData) {
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
                        Column (modifier = Modifier.padding(start = 12.dp)){
                            Text(
                                text = applicant.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "${applicant.location}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Age - ${applicant.age}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActionChip(text = "Reject") { /* Reject action */ }
                        ActionChip(text = "Approve") { /* Approve action */ }
                    }
                }
            }
        }
    }
}


@Composable
fun ActionChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFFDEB8B))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}
