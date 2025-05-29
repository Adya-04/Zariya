package com.example.zariya.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zariya.R
import com.example.zariya.enums.Roles
import com.example.zariya.ui.theme.AppLinearGradient
import com.example.zariya.ui.theme.ButtonGradient
import com.example.zariya.ui.theme.Poppins

@Preview(showBackground = true)
@Composable
fun RoleSelectScreenPreview() {
    RoleSelectScreen(onRoleSelected = {})
}

@Composable
fun RoleSelectScreen(
    onRoleSelected: (role: String) -> Unit
) {
    val spacing = 24.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppLinearGradient)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SIGN UP",
            fontFamily = Poppins,
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF7F7769),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        RoleItem(
            iconResId = R.drawable.as_recruiter_icon,
            text = "As Recruiter"
        ) {
            onRoleSelected(Roles.RECRUITER)
        }

        Spacer(modifier = Modifier.height(spacing))

        RoleItem(
            iconResId = R.drawable.as_helper_icon,
            text = "As Helper"
        ) {
            onRoleSelected(Roles.HELPER)
        }

        Spacer(modifier = Modifier.height(spacing))

        RoleItem(
            iconResId = R.drawable.as_candidate_icon,
            text = "As Candidate"
        ) {
            onRoleSelected(Roles.CANDIDATE)
        }
    }
}

@Composable
fun RoleItem(
    iconResId: Int,
    text: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(50)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                tint = Color.Black,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .width(200.dp)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(ButtonGradient)
                .clickable { onClick() }
                .padding(vertical = 14.dp)
                .padding(start = 24.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = text,
                fontFamily = Poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666)
            )
        }
    }
}

