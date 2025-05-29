package com.example.zariya.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.R
import com.example.zariya.ui.theme.AppLinearGradient

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onGetStartedClick = {})
}

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_txt),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .height(32.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.zariya_logo),
                contentDescription = "Zariya Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(100.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.welcome_pg),
                contentDescription = "Welcome Paragraph",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .height(100.dp)
            )
        }

        GradientButtonWithBorder(
            text = "Get Started",
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
        )
    }
}

@Composable
fun GradientButtonWithBorder(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    gradientBrush: Brush = AppLinearGradient
) {
    Box(
        modifier = modifier
            .padding(4.dp) // Space to show shadow
    ) {
        // Fake shadow using a blurred background box, offset to bottom-right
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 8.dp, y = 8.dp)
                .background(
                    color = Color(0x28000000), // semi-transparent black
                    shape = shape
                )
                .blur(22.dp)
        )

        // Actual button content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradientBrush, shape = shape)
                .border(width = 4.dp, color = Color.White, shape = shape)
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
