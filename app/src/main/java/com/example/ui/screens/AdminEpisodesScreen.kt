package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Anime
import com.example.data.model.Episode
import com.example.data.model.SubtitleTrack
import com.example.data.model.VideoSource
import com.example.data.repository.AnimeRepository
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun AdminEpisodesScreen(
    anime: Anime,
    episodes: List<Episode>,
    animeRepository: AnimeRepository,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isAddingNew by remember { mutableStateOf(false) }

    var epNumberText by remember { mutableStateOf("${episodes.size + 1}") }
    var epTitleAr by remember { mutableStateOf("الحلقة ${episodes.size + 1}") }
    var videoUrl by remember { mutableStateOf("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
    var serverName by remember { mutableStateOf("سيرفر رئيسي (Server 1)") }
    var quality by remember { mutableStateOf("720p") }
    var durationText by remember { mutableStateOf("24:00") }
    var seasonText by remember { mutableStateOf("1") }

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
                modifier = Modifier.testTag("admin_episodes_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "حلقات ${anime.titleAr}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { isAddingNew = !isAddingNew },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AnimeRedPrimary)
                    .size(34.dp)
                    .testTag("admin_toggle_add_episode")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Episode",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isAddingNew) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "إضافة حلقة جديدة وسيرفر بث",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.End)
                            )

                            AdminFormField(
                                label = "رقم الحلقة",
                                value = epNumberText,
                                onValueChange = {
                                    epNumberText = it
                                    epTitleAr = "الحلقة $it"
                                }
                            )

                            AdminFormField(
                                label = "عنوان الحلقة",
                                value = epTitleAr,
                                onValueChange = { epTitleAr = it }
                            )

                            AdminFormField(
                                label = "رابط الفيديو المعتمد (Video URL)",
                                value = videoUrl,
                                onValueChange = { videoUrl = it }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AdminFormField(
                                    label = "اسم السيرفر",
                                    value = serverName,
                                    onValueChange = { serverName = it },
                                    modifier = Modifier.weight(1f)
                                )
                                AdminFormField(
                                    label = "الجودة",
                                    value = quality,
                                    onValueChange = { quality = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AdminFormField(
                                    label = "الموسم",
                                    value = seasonText,
                                    onValueChange = { seasonText = it },
                                    modifier = Modifier.weight(1f)
                                )
                                AdminFormField(
                                    label = "المدة",
                                    value = durationText,
                                    onValueChange = { durationText = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = {
                                    val epNum = epNumberText.toIntOrNull() ?: (episodes.size + 1)
                                    val epId = "${anime.id}_ep_$epNum"
                                    val source = VideoSource(
                                        id = "src_1",
                                        name = serverName,
                                        url = videoUrl,
                                        quality = quality,
                                        type = "sub",
                                        language = "ar",
                                        isActive = true
                                    )
                                    val newEpisode = Episode(
                                        id = epId,
                                        animeId = anime.id,
                                        episodeNumber = epNum,
                                        title = "Episode $epNum",
                                        titleAr = epTitleAr,
                                        thumbnailUrl = anime.coverUrl,
                                        videoUrl = videoUrl,
                                        duration = durationText,
                                        season = seasonText.toIntOrNull() ?: 1,
                                        sources = listOf(source),
                                        subtitles = listOf(
                                            SubtitleTrack("sub_ar", "الترجمة العربية", "ar", "", true),
                                            SubtitleTrack("sub_en", "الترجمة الإنجليزية", "en", "", false)
                                        ),
                                        published = true
                                    )
                                    scope.launch {
                                        animeRepository.saveEpisode(newEpisode)
                                        isAddingNew = false
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(23.dp))
                                    .background(RedGradient)
                                    .testTag("admin_submit_episode_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text("حفظ الحلقة في السيرفر", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "الحلقات المتوفرة (${episodes.size})",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

            items(episodes) { ep ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    animeRepository.deleteEpisode(anime.id, ep.id)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFFF4D6A),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = ep.titleAr,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "الموسم ${ep.season} • مدة ${ep.duration} • سيرفر (${ep.sources.size.coerceAtLeast(1)})",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
