package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminUser
import com.example.data.model.Anime
import com.example.data.repository.AnimeRepository
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun AdminDashboardScreen(
    animeList: List<Anime>,
    admins: List<AdminUser>,
    animeRepository: AnimeRepository,
    onNavigateToAddAnime: () -> Unit,
    onNavigateToEditAnime: (Anime) -> Unit,
    onNavigateToEpisodes: (Anime) -> Unit,
    onNavigateToAdminUsers: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var statusMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
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
                modifier = Modifier.testTag("admin_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "لوحة تحكم الإدارة",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onNavigateToAddAnime,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AnimeRedPrimary)
                    .size(36.dp)
                    .testTag("admin_add_anime_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Anime",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Grid (2x2)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "إجمالي الأنميات",
                        value = "${animeList.size}",
                        icon = Icons.Default.Movie,
                        accentColor = AnimeRedPrimary
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "الأنميات المنشورة",
                        value = "${animeList.count { it.published }}",
                        icon = Icons.Default.Public,
                        accentColor = Color(0xFF38D9A9)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "المديرون المعتمدون",
                        value = "${admins.size.coerceAtLeast(1)}",
                        icon = Icons.Default.AdminPanelSettings,
                        accentColor = Color(0xFFFFB800)
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "سيرفرات البث",
                        value = "متصل (Firebase)",
                        icon = Icons.Default.CloudDone,
                        accentColor = Color(0xFF4DABF7)
                    )
                }
            }

            // Quick Actions Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onNavigateToAddAnime,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RedGradient),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("إضافة أنمي جديد +", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onNavigateToAdminUsers,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceCard),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("إدارة المشرفين", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (statusMessage != null) {
                item {
                    Text(
                        text = statusMessage ?: "",
                        color = Color(0xFF38D9A9),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Anime Catalog Management Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "(${animeList.size}) إدارة الأنميات",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // List of Anime items with Edit, Episodes, Publish toggle, Delete
            items(animeList.size) { index ->
                val anime = animeList[index]
                AdminAnimeItemRow(
                    anime = anime,
                    onEditClick = { onNavigateToEditAnime(anime) },
                    onEpisodesClick = { onNavigateToEpisodes(anime) },
                    onTogglePublish = {
                        scope.launch {
                            animeRepository.togglePublish(anime.id, !anime.published)
                            statusMessage = "تم تحديث حالة نشر ${anime.titleAr}"
                        }
                    },
                    onDeleteClick = {
                        scope.launch {
                            animeRepository.deleteAnime(anime.id)
                            statusMessage = "تم حذف ${anime.titleAr}"
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AdminAnimeItemRow(
    anime: Anime,
    onEditClick: () -> Unit,
    onEpisodesClick: () -> Unit,
    onTogglePublish: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF4D6A), modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onEpisodesClick, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "Episodes", tint = AnimeRedPrimary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onTogglePublish, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (anime.published) Icons.Default.Public else Icons.Default.PublicOff,
                        contentDescription = "Publish",
                        tint = if (anime.published) Color(0xFF38D9A9) else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Info on Right
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = anime.titleAr.ifEmpty { anime.title },
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${anime.title} • ${anime.totalEpisodes} حلقة • ${if (anime.published) "منشور" else "مسودة"}",
                    color = if (anime.published) Color(0xFF38D9A9) else TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
