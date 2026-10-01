package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

@Composable
fun AnimeXDrawer(
    currentUser: FirebaseUser?,
    isAdmin: Boolean,
    currentScreen: String,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isNightMode by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(DarkBackground)
            .statusBarsPadding()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header Profile Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate("settings")
                        onCloseDrawer()
                    }
                    .padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF282D3D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = Color(0xFF8E95A5),
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    AnimeXLogo(fontSize = 18.sp, iconSize = 20.dp, showIcon = false)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = currentUser?.displayName ?: "مرحباً بك",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderStroke, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Items
            DrawerMenuItem(
                title = "الرئيسية",
                icon = Icons.Default.Home,
                isSelected = currentScreen == "home",
                onClick = {
                    onNavigate("home")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                title = "جميع الأنميات",
                icon = Icons.Default.Tv,
                isSelected = currentScreen == "all_anime",
                onClick = {
                    onNavigate("all_anime")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                title = "التصنيفات",
                icon = Icons.Default.Category,
                isSelected = currentScreen == "categories",
                onClick = {
                    onNavigate("categories")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                title = "قائمتي",
                icon = Icons.Default.Bookmark,
                isSelected = currentScreen == "watchlist",
                onClick = {
                    onNavigate("watchlist")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                title = "المفضلة",
                icon = Icons.Default.Favorite,
                isSelected = currentScreen == "favorites",
                onClick = {
                    onNavigate("favorites")
                    onCloseDrawer()
                }
            )

            // Admin Dashboard access (visible to admin or accessible for management)
            DrawerMenuItem(
                title = "لوحة التحكم (الإدارة)",
                icon = Icons.Default.AdminPanelSettings,
                isSelected = currentScreen == "admin_dashboard",
                highlightColor = AnimeRedPrimary,
                onClick = {
                    onNavigate("admin_dashboard")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                title = "الإعدادات",
                icon = Icons.Default.Settings,
                isSelected = currentScreen == "settings",
                onClick = {
                    onNavigate("settings")
                    onCloseDrawer()
                }
            )
        }

        // Bottom Footer (Night mode toggle + Version)
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الوضع الليلي",
                    color = TextPrimary,
                    fontSize = 14.sp
                )
                Switch(
                    checked = isNightMode,
                    onCheckedChange = { isNightMode = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AnimeRedPrimary,
                        uncheckedTrackColor = DarkSurface
                    ),
                    modifier = Modifier.testTag("drawer_night_mode_switch")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AnimeX v1.0",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    highlightColor: Color = AnimeRedPrimary,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isSelected) {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF330B11))
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = backgroundModifier
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) highlightColor else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
