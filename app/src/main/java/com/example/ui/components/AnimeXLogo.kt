package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.RedGradient

@Composable
fun AnimeXLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 26.dp,
    fontSize: TextUnit = 22.sp,
    showIcon: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showIcon) {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(6.dp))
                    .background(RedGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "AnimeX Logo",
                    tint = Color.White,
                    modifier = Modifier.size(iconSize * 0.75f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("Anime")
                }
                withStyle(
                    SpanStyle(
                        color = AnimeRedPrimary,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("X")
                }
            },
            fontSize = fontSize,
            letterSpacing = 0.5.sp
        )
    }
}
