package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.WatchRecord
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WatchlistScreen(
    watchlist: List<WatchRecord>,
    onAnimeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var isSeriesSelected by remember { mutableStateOf(true) }

    val filteredList = watchlist.filter {
        if (isSeriesSelected) !it.isMovie else it.isMovie
    }

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
                modifier = Modifier.testTag("watchlist_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "قائمتي",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("watchlist_menu_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Toggle Tabs: [المسلسلات] and [الأفلام]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(if (isSeriesSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                    .clickable { isSeriesSelected = true }
                    .testTag("tab_series"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "المسلسلات",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(if (!isSeriesSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                    .clickable { isSeriesSelected = false }
                    .testTag("tab_movies"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "الأفلام",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Watchlist Items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { item ->
                WatchlistCard(
                    record = item,
                    onClick = { onAnimeClick(item.animeId) }
                )
            }
        }
    }
}

@Composable
fun WatchlistCard(
    record: WatchRecord,
    onClick: () -> Unit
) {
    val progress = (record.currentEpisode.toFloat() / record.totalEpisodes.coerceAtLeast(1).toFloat()).coerceIn(0.05f, 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceCard)
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("watchlist_item_${record.animeId}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail on Left
        Box(
            modifier = Modifier
                .width(74.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            when (record.animeId) {
                "one_piece" -> Image(
                    painter = painterResource(id = R.drawable.one_piece_banner_1790893298945),
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "naruto" -> Image(
                    painter = painterResource(id = R.drawable.naruto_hero_cover_1790893315091),
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "attack_on_titan" -> Image(
                    painter = painterResource(id = R.drawable.aot_poster_1790893372017),
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "demon_slayer" -> Image(
                    painter = painterResource(id = R.drawable.demon_slayer_poster_1790893345068),
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "jujutsu_kaisen" -> Image(
                    painter = painterResource(id = R.drawable.jujutsu_kaisen_poster_1790893330776),
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                else -> AsyncImage(
                    model = record.posterUrl,
                    contentDescription = record.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center Details & Progress
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = record.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = record.titleAr,
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "المتابعة: ${record.currentEpisode} / ${record.totalEpisodes} حلقة",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Red Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = AnimeRedPrimary,
                trackColor = Color(0xFF2E3446)
            )
        }

        // Circular Red Play Button on Right
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF33090F)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Resume",
                tint = AnimeRedPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
