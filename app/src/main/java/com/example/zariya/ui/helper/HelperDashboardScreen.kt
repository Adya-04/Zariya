package com.example.zariya.ui.helper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zariya.R
import com.example.zariya.models.JobY
import com.example.zariya.models.JobforHelper
import com.example.zariya.navigation.Helper
import com.example.zariya.ui.recruiter.DrawerContent
import com.example.zariya.ui.recruiter.TopBar
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.Poppins
import com.example.zariya.viewmodel.HelperViewModel
import com.example.zariya.utils.NetworkResult
import kotlinx.coroutines.launch

@Preview
@Composable
fun HelperDashboardScreenPreview() {
    HelperDashboardScreen(navController = NavController(LocalContext.current) , helperViewModel = hiltViewModel())
}

@Composable
fun HelperDashboardScreen(
    navController: NavController,
    helperViewModel: HelperViewModel
) {
    val token by helperViewModel.token.collectAsState(initial = "")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Fetch jobs when token is available
    LaunchedEffect(key1 = token) {
        if (token.isNotEmpty()) {
            helperViewModel.fetchJobs(token)
        }
    }

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
        }) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(AppLinearGradient)
            ) {
                HelperScreenContent(helperViewModel = helperViewModel,
                    navController = navController)
            }
        }
    }
}

@Composable
fun HelperScreenContent(
    helperViewModel: HelperViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    val jobsState by helperViewModel.jobsLiveData.collectAsState()
    val token by helperViewModel.token.collectAsState(initial = "")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        SearchBar(
            query = searchText,
            onQueryChange = { searchText = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        when (jobsState) {
            is NetworkResult.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Error",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (jobsState as NetworkResult.Error<*>).message.toString(),
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { helperViewModel.fetchJobs(token) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is NetworkResult.Loading -> {
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

            is NetworkResult.Success ->{
                val jobs = (jobsState as NetworkResult.Success<List<JobY>>).data
                if (jobs != null) {
                    if (jobs.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No jobs available",
                                color = Color.White
                            )
                        }
                    } else {
                        // Filter jobs based on search text
                        val filteredJobs = if (searchText.isBlank()) {
                            jobs
                        } else {
                            jobs.filter { job ->
                                job.title.contains(searchText, ignoreCase = true) ||
                                        job.companyId.name.contains(searchText, ignoreCase = true) ||
                                        job.skillsRequired.contains(searchText, ignoreCase = true)
                            }
                        }

                        if (filteredJobs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No jobs match your search",
                                    color = Color.White
                                )
                            }
                        } else {
                            LazyColumn {
                                items(filteredJobs) { job ->
                                    JobsDetailCard(
                                        job = job.toJobforHelper(),
                                        navController = navController
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }

            is NetworkResult.Start<*> -> {}
        }
    }
}

// Extension function to convert JobY to JobforHelper
fun JobY.toJobforHelper(): JobforHelper {
    return JobforHelper(
        id = this._id,
        title = this.title,
        type = this.workType,
        salary = this.salary,
        location = "${this.companyId.name}, ${this.companyId.location}",
        distance = "", // You might need to calculate this based on user's location
        foodProvided = false, // Update based on your actual data model
        stayProvided = false  // Update based on your actual data model
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(4.dp, shape = RoundedCornerShape(24.dp))
            .background(Color.White, shape = RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text("Search a Job", color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun JobsDetailCard(
    job: JobforHelper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .padding(12.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFDF6E3), Color(0xFFFFFFFF))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                // Title and badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF2C2), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = job.type,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Salary and location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "• ${job.salary}", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = job.location,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Food & Stay Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.food_icon),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (job.foodProvided) " Food provided" else " Food not provided",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.home_icon),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (job.stayProvided) " Stay provided" else " Stay not provided",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apply Now Button
                Box(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .align(Alignment.CenterHorizontally)
                        .clickable { navController.navigate(Helper.CandidateDetails.createRoute(job.id)) }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Apply Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        color = Color.Black
                    )
                }
            }
        }
    }
}