package com.example.zariya.ui.recruiter

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.Poppins

@Preview
@Composable
fun CandidateDetailsScreen() {
    val shape = RoundedCornerShape(50)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Candidate Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(24.dp))

        InputFieldCand("Name", shape)

        Row(modifier = Modifier.fillMaxWidth()) {
            InputBoxCand(
                label = "Age",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            InputBoxCand(
                label = "Gender",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
        InputBoxCand("Address", shape)

        Row(modifier = Modifier.fillMaxWidth()) {
            InputBoxCand(
                label = "Skills",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            InputBoxCand(
                label = "Basic Education",
                shape = shape,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
        InputBoxCand("Contact", shape)
        InputBoxCand("*Helper", shape)


        Spacer(modifier = Modifier.height(24.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Box(
                modifier = Modifier
                    .shadow(8.dp, shape)
                    .clip(shape)
                    .background(Color.White)
                    .clickable { /* handle click */ }
                    .padding(horizontal = 32.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reject",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .shadow(8.dp, shape)
                    .clip(shape)
                    .background(Color.White)
                    .clickable { /* handle click */ }
                    .padding(horizontal = 32.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Approve",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun InputFieldCand(
    label: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
) {
    var text by remember { mutableStateOf("") }

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
                value = text,
                onValueChange = { text = it },
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


@Composable
fun InputBoxCand(
    label: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
) {
    var text by remember { mutableStateOf("") }

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
                value = text,
                onValueChange = { text = it },
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

