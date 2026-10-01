package com.example.ui.screens

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Anime
import com.example.data.model.Episode
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TagDubBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerScreen(
    anime: Anime,
    episode: Episode,
    allEpisodes: List<Episode>,
    onEpisodeSelected: (Episode) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0.48f) }
    var currentTimeString by remember { mutableStateOf("12:34") }
    val totalTimeString = "22:15"

    var selectedQuality by remember { mutableStateOf("720p") }
    var showQualityMenu by remember { mutableStateOf(false) }

    var isDubSelected by remember { mutableStateOf(false) }
    var selectedSubtitle by remember { mutableStateOf("ar") }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    // Simulation progress timer for video playback UI
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            if (currentProgress < 1.0f) {
                currentProgress += 0.002f
                val totalSec = 1335 // 22:15
                val currentSec = (currentProgress * totalSec).toInt()
                val min = currentSec / 60
                val sec = currentSec % 60
                currentTimeString = String.format("%02d:%02d", min, sec)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Video View Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
                .background(Color.Black)
        ) {
            // Android VideoView wrapped in AndroidView for playback
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        val videoUri = Uri.parse(
                            episode.videoUrl.ifEmpty {
                                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                            }
                        )
                        setVideoURI(videoUri)
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            start()
                        }
                        videoViewRef = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Fallback Poster image if video isn't loaded
            if (!isPlaying) {
                Image(
                    painter = painterResource(id = R.drawable.naruto_hero_cover_1790893315091),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Top Bar over video (Back button and Quality dropdown)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x66000000))
                        .testTag("player_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Quality Dropdown
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x880C0E14))
                            .clickable { showQualityMenu = true }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedQuality,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Quality",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showQualityMenu,
                        onDismissRequest = { showQualityMenu = false },
                        modifier = Modifier.background(DarkSurfaceCard)
                    ) {
                        listOf("1080p", "720p", "480p", "360p").forEach { quality ->
                            DropdownMenuItem(
                                text = { Text(quality, color = Color.White) },
                                onClick = {
                                    selectedQuality = quality
                                    showQualityMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Player Center Controls (Rewind 10, Play/Pause, Forward 10)
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        currentProgress = (currentProgress - 0.02f).coerceAtLeast(0f)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x55000000))
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind 10s",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        videoViewRef?.let {
                            if (isPlaying) it.start() else it.pause()
                        }
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x88000000))
                        .testTag("player_play_pause_btn")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }

                IconButton(
                    onClick = {
                        currentProgress = (currentProgress + 0.02f).coerceAtMost(1f)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x55000000))
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10s",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Bottom Player Scrubber & Time Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0x77000000))
                    .padding(horizontal = 14.dp, vertical = 2.dp)
            ) {
                Slider(
                    value = currentProgress,
                    onValueChange = { currentProgress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = AnimeRedPrimary,
                        activeTrackColor = AnimeRedPrimary,
                        inactiveTrackColor = Color(0x66FFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$currentTimeString / $totalTimeString",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Episode",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    val nextEp = allEpisodes.find { it.episodeNumber == episode.episodeNumber + 1 }
                                    if (nextEp != null) onEpisodeSelected(nextEp)
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Fullscreen",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Details below player (Scrollable Content)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Episode Title & Season
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "الحلقة ${episode.episodeNumber} - ${anime.title}",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "الموسم ${episode.season}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            item {
                // Audio Switch Tabs: [مترجم] (Subbed) and [مدبلج] (Dubbed)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isDubSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                            .clickable { isDubSelected = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "مترجم",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDubSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                            .clickable { isDubSelected = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "مدبلج",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                // Section "اللغة و الترجمة" (Language & Subtitles)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اللغة و الترجمة",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Subtitle list items
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SubtitleSelectionItem(
                        title = "الترجمة العربية",
                        isSelected = selectedSubtitle == "ar",
                        onClick = { selectedSubtitle = "ar" }
                    )
                    SubtitleSelectionItem(
                        title = "الترجمة الإنجليزية",
                        isSelected = selectedSubtitle == "en",
                        onClick = { selectedSubtitle = "en" }
                    )
                    SubtitleSelectionItem(
                        title = "الترجمة الفرنسية",
                        isSelected = selectedSubtitle == "fr",
                        onClick = { selectedSubtitle = "fr" }
                    )
                }
            }

            item {
                // Section "الحلقات" (Episodes list)
                Text(
                    text = "الحلقات",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

            // Episode rows with thumbnails
            items(allEpisodes) { ep ->
                val isActive = ep.episodeNumber == episode.episodeNumber
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceCard)
                        .border(
                            1.dp,
                            if (isActive) AnimeRedPrimary else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onEpisodeSelected(ep) }
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isActive) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = AnimeRedPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ep.titleAr,
                            color = if (isActive) AnimeRedPrimary else Color.White,
                            fontSize = 14.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Episode thumbnail
                        Box(
                            modifier = Modifier
                                .width(74.dp)
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.naruto_hero_cover_1790893315091),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubtitleSelectionItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceCard)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        1.5.dp,
                        if (isSelected) AnimeRedPrimary else TextSecondary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AnimeRedPrimary)
                    )
                }
            }
        }
    }
}
