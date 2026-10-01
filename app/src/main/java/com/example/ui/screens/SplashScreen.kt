package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AnimeXLogo
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextSecondary

@Composable
fun SplashScreen(
    onStartClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Luffy background artwork with red clouds
        Image(
            painter = painterResource(id = R.drawable.luffy_welcome_bg_1790893265867),
            contentDescription = "Luffy Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for dark cinematic feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x66000000),
                            Color(0xCC0C0E14),
                            Color(0xFF0C0E14)
                        )
                    )
                )
        )

        // Central & Bottom Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // AnimeX Brand Logo
            AnimeXLogo(
                iconSize = 52.dp,
                fontSize = 38.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title Slogan
            Text(
                text = "عالم الأنمي بين يديك",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "مشاهدة أنمي مترجم و مدبلج بجودة عالية",
                color = TextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Red Rounded Button: ابدأ الآن
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(RedGradient)
                    .testTag("start_now_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "ابدأ الآن",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip button: تخطي
            Text(
                text = "تخطي",
                color = TextSecondary,
                fontSize = 15.sp,
                modifier = Modifier
                    .clickable(onClick = onSkipClick)
                    .padding(8.dp)
                    .testTag("skip_button")
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
