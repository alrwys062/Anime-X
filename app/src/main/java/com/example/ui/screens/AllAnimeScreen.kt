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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.Anime
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TagDubBg
import com.example.ui.theme.TagDubText
import com.example.ui.theme.TagSubBg
import com.example.ui.theme.TagSubText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AllAnimeScreen(
    animeList: List<Anime>,
    selectedCategoryName: String? = null,
    onAnimeClick: (Anime) -> Unit,
    onBackClick: () -> Unit
) {
    var isDubSelected by remember { mutableStateOf(true) }

    val filteredList = animeList.filter { anime ->
        val matchesCategory = if (selectedCategoryName != null) {
            anime.genres.contains(selectedCategoryName) || anime.genres.any { it.contains(selectedCategoryName) }
        } else true

        val matchesDubSub = if (isDubSelected) anime.hasDub else anime.hasSub
        matchesCategory && matchesDubSub
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
                modifier = Modifier.testTag("all_anime_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = selectedCategoryName ?: "جميع الأنميات",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(28.dp))
        }

        // Toggle Filter: [مدبلج] and [مترجم]
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
                    .background(if (isDubSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                    .clickable { isDubSelected = true }
                    .testTag("filter_dubbed"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "مدبلج",
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
                    .background(if (!isDubSelected) RedGradient else androidx.compose.ui.graphics.SolidColor(DarkSurfaceCard))
                    .clickable { isDubSelected = false }
                    .testTag("filter_subbed"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "مترجم",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vertical List of Anime Cards
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList) { anime ->
                AnimeListItem(
                    anime = anime,
                    onClick = { onAnimeClick(anime) }
                )
            }
        }
    }
}

@Composable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceCard)
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("all_anime_item_${anime.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster Thumbnail on Left
        Box(
            modifier = Modifier
                .width(68.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            when (anime.id) {
                "naruto" -> Image(
                    painter = painterResource(id = R.drawable.naruto_hero_cover_1790893315091),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "one_piece" -> Image(
                    painter = painterResource(id = R.drawable.one_piece_banner_1790893298945),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "demon_slayer" -> Image(
                    painter = painterResource(id = R.drawable.demon_slayer_poster_1790893345068),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "solo_leveling" -> Image(
                    painter = painterResource(id = R.drawable.solo_leveling_poster_1790893358568),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "jujutsu_kaisen" -> Image(
                    painter = painterResource(id = R.drawable.jujutsu_kaisen_poster_1790893330776),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                "attack_on_titan" -> Image(
                    painter = painterResource(id = R.drawable.aot_poster_1790893372017),
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                else -> AsyncImage(
                    model = anime.posterUrl,
                    contentDescription = anime.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = anime.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = anime.titleAr,
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Subbed / Dubbed Badges
                BadgePill(text = "مترجم", isDub = false)
                Spacer(modifier = Modifier.width(6.dp))
                BadgePill(text = "مدبلج", isDub = true)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${anime.totalEpisodes}+ حلقة",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Right Chevron
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Details",
            tint = TextSecondary,
            modifier = Modifier.size(24.dp)
        )
    }
}
