package com.example.zariya.ui.recruiter

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.R
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.ButtonGradient
import com.example.zariya.ui.theme.Poppins
import kotlinx.coroutines.launch

@Preview
@Composable
fun DashboardScreen() {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = { ModalDrawerSheet { DrawerContent() } },
        drawerState = drawerState
    ) {
        Scaffold(topBar = {
            TopBar(onOpenDrawer = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            })
        }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(AppLinearGradient)
            ) {
                ScreenContent()
            }
        }
    }

}

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
    Column {
        Text("Profile", modifier = Modifier.clickable { })
        Text("Settings", modifier = Modifier.clickable { })
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Dashboard",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp, color = Color(0xFF666666)
            )
            Text(
                "23/05/2025",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp, color = Color(0xFF000000)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatCard(
                iconResId = R.drawable.jobs_posted_icon,
                title = "Jobs\nPosted",
                value = "350",
                percentage = "+15%"
            )

            StatCard(
                iconResId = R.drawable.candi_app_icon,
                title = "Candidates Applied",
                value = "200",
                percentage = "+10%"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Active Jobs",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp, color = Color(0xFF666666)
            )
            Text(
                "Manage all",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp, color = Color(0xFF000000)
            )
        }
        JobCard(
            title = "House helper",
            location = "Mumbai, Maharashtra",
            salary = "15000/month",
            datePosted = "10th May",
            applicants = "5 applicants"
        )

        JobCard(
            title = "Cook",
            location = "Delhi, India",
            salary = "18000/month",
            datePosted = "5th May",
            applicants = "2 applicants"
        )
        Spacer(modifier = Modifier.weight(1f)) // pushes the button to the bottom

        PostJobButton(
            onClick = { /* TODO: Navigate to post job screen */ }
        )

        Spacer(modifier = Modifier.height(16.dp)) // bottom padding
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onOpenDrawer: () -> Unit) {
    Box(
        modifier = Modifier
            .background(AppLinearGradient)
    ) {
        TopAppBar(
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 8.dp)
                        .clickable {
                            onOpenDrawer()
                        }
                )
            },
            title = {
                // Centered app icon
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Replace with your app icon resource
                    Icon(
                        painter = painterResource(id = R.drawable.zariya_logo),
                        contentDescription = "App Icon",
                        modifier = Modifier.size(120.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: notification action */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}

@Composable
fun StatCard(
    iconResId: Int,
    title: String,
    value: String,
    percentage: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified, // Green tint
    percentColor: Color = Color(0xFF00C851)
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(140.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(brush = ButtonGradient)
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = percentage,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = percentColor
                )
            }
        }
    }
}


@Composable
fun JobCard(
    title: String,
    location: String,
    salary: String,
    datePosted: String,
    applicants: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {// Gradient Background Box
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(brush = ButtonGradient)
                .padding(16.dp)
        ) {
            Column {
                // Title and applicants
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = title,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF2C2), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = applicants,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Location and salary
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon), // Replace with your icon
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = location,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(text = "•", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = salary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Posted Date and View Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Posted on $datePosted",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF2C2), RoundedCornerShape(8.dp))
                            .clickable { /* Handle View click */ }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "View",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PostJobButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, shape)
            .clip(shape)
            .background(ButtonGradient)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Post Job",
            fontFamily = Poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF666666)
        )
    }
}

