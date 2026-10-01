package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.UserRepository
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

@Composable
fun SettingsScreen(
    currentUser: FirebaseUser?,
    isAdmin: Boolean,
    userRepository: UserRepository,
    onNavigateToAdmin: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var autoPlayNext by remember { mutableStateOf(true) }
    var enableNotifications by remember { mutableStateOf(true) }
    var nightMode by remember { mutableStateOf(true) }
    var authError by remember { mutableStateOf<String?>(null) }
    var authSuccessMsg by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("settings_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "الإعدادات",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: الحساب (Account)
            SettingSectionHeader(title = "الحساب")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceCard)
                    .clickable {
                        if (currentUser == null) {
                            userRepository.signInWithGoogle(
                                activity = context as Activity,
                                onSuccess = { authSuccessMsg = "تم تسجيل الدخول بنجاح!" },
                                onError = { authError = it },
                                scope = scope
                            )
                        } else {
                            userRepository.signOut(scope) {
                                authSuccessMsg = "تم تسجيل الخروج"
                            }
                        }
                    }
                    .padding(16.dp)
                    .testTag("settings_account_row"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (currentUser != null) Icons.Default.Logout else Icons.Default.Person,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (currentUser != null) (currentUser.displayName ?: currentUser.email ?: "حسابك") else "تسجيل الدخول",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (currentUser != null) {
                        Text(
                            text = if (isAdmin) "مدير النظام (Admin) - اضغط لتسجيل الخروج" else "مستخدم مسجل - اضغط لتسجيل الخروج",
                            color = if (isAdmin) AnimeRedPrimary else TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            if (authError != null) {
                Text(
                    text = authError ?: "",
                    color = AnimeRedPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            if (authSuccessMsg != null) {
                Text(
                    text = authSuccessMsg ?: "",
                    color = Color(0xFF38D9A9),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Section 2: جودة الفيديو (Video Quality)
            SettingSectionHeader(title = "جودة الفيديو")
            SettingItemCard(
                icon = Icons.Default.AspectRatio,
                title = "تلقائي"
            )

            // Section 3: اللغة (Language)
            SettingSectionHeader(title = "اللغة")
            SettingItemCard(
                icon = Icons.Default.Language,
                title = "العربية"
            )

            // Section 4: التشغيل التلقائي (Auto Play)
            SettingSectionHeader(title = "التشغيل التلقائي")
            SettingToggleCard(
                title = "تشغيل تلقائي للحلقة التالية",
                checked = autoPlayNext,
                onCheckedChange = { autoPlayNext = it }
            )

            // Section 5: التنزيلات (Downloads)
            SettingSectionHeader(title = "التنزيلات")
            SettingItemCard(
                icon = Icons.Default.Download,
                title = "جودة التنزيل: HD"
            )

            // Section 6: إشعارات (Notifications)
            SettingSectionHeader(title = "إشعارات")
            SettingToggleCard(
                title = "تفعيل الإشعارات",
                checked = enableNotifications,
                onCheckedChange = { enableNotifications = it }
            )

            // Section 7: مظهر التطبيق (App Appearance)
            SettingSectionHeader(title = "مظهر التطبيق")
            SettingToggleCard(
                title = "الوضع الليلي",
                checked = nightMode,
                onCheckedChange = { nightMode = it }
            )

            // Admin Management Panel Button
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onNavigateToAdmin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2B0E14))
                    .testTag("open_admin_dashboard_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "لوحة تحكم الإدارة (Admin Dashboard)",
                        color = AnimeRedPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin",
                        tint = AnimeRedPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingSectionHeader(title: String) {
    Text(
        text = title,
        color = TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.End
    )
}

@Composable
fun SettingItemCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceCard)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SettingToggleCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceCard)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AnimeRedPrimary,
                uncheckedTrackColor = DarkSurface
            )
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
