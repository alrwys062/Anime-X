package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Anime
import com.example.ui.components.AnimeXTopBar
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TagDubBg
import com.example.ui.theme.TagDubText
import com.example.ui.theme.TagSubBg
import com.example.ui.theme.TagSubText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    animeList: List<Anime>,
    onAnimeClick: (Anime) -> Unit,
    onWatchClick: (Anime) -> Unit,
    onViewAllClick: () -> Unit,
    onOpenDrawer: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val featuredAnime = animeList.find { it.featured } ?: animeList.firstOrNull()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Bar
        AnimeXTopBar(
            onOpenDrawer = onOpenDrawer
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchQueryChange(it)
                    },
                    placeholder = {
                        Text(
                            text = "...ابحث عن أنمي، اسم أو تصنيف",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(DarkSurfaceCard)
                        .testTag("home_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BorderStroke,
                        unfocusedBorderColor = BorderStroke,
                        cursorColor = AnimeRedPrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Featured Hero Card (Matching One Piece Card in Template)
            if (featuredAnime != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(205.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onAnimeClick(featuredAnime) }
                ) {
                    // Banner Image
                    Image(
                        painter = painterResource(id = R.drawable.one_piece_banner_1790893298945),
                        contentDescription = featuredAnime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x88000000),
                                        Color(0xEE0C0E14)
                                    )
                                )
                            )
                    )

                    // Card Content at Bottom
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = featuredAnime.titleAr.ifEmpty { featuredAnime.title },
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Sub / Dub Badges
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BadgePill(text = "مترجم", isDub = false)
                            BadgePill(text = "مدبلج", isDub = true)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Watch Now Red Button
                        Button(
                            onClick = { onWatchClick(featuredAnime) },
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(RedGradient)
                                .height(38.dp)
                                .testTag("hero_watch_now_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "مشاهدة الآن",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Carousel Dots
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(AnimeRedPrimary)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3C4252))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3C4252))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3C4252))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section "أحدث الإضافات" (Latest Additions)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "عرض الكل >",
                    color = AnimeRedPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = onViewAllClick)
                        .testTag("view_all_latest_button")
                )

                Text(
                    text = "أحدث الإضافات",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontal Anime Posters List
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                items(animeList.filter { it.id != "one_piece" }) { anime ->
                    PosterCard(
                        anime = anime,
                        onClick = { onAnimeClick(anime) }
                    )
                }
            }

            // Carousel dots below latest additions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(AnimeRedPrimary)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3C4252))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3C4252))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3C4252))
                )
            }
        }
    }
}

@Composable
fun PosterCard(
    anime: Anime,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
            .testTag("anime_card_${anime.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(170.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurface)
        ) {
            when (anime.id) {
                "jujutsu_kaisen" -> {
                    Image(
                        painter = painterResource(id = R.drawable.jujutsu_kaisen_poster_1790893330776),
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                "demon_slayer" -> {
                    Image(
                        painter = painterResource(id = R.drawable.demon_slayer_poster_1790893345068),
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                "solo_leveling" -> {
                    Image(
                        painter = painterResource(id = R.drawable.solo_leveling_poster_1790893358568),
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                "attack_on_titan" -> {
                    Image(
                        painter = painterResource(id = R.drawable.aot_poster_1790893372017),
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                "naruto" -> {
                    Image(
                        painter = painterResource(id = R.drawable.naruto_hero_cover_1790893315091),
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    AsyncImage(
                        model = anime.posterUrl,
                        contentDescription = anime.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
            text = anime.title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Sub / Dub Badges matching template
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            BadgePill(text = "مترجم", isDub = false)
            BadgePill(text = "مدبلج", isDub = true)
        }
    }
}

@Composable
fun BadgePill(text: String, isDub: Boolean) {
    val bgColor = if (isDub) TagDubBg else TagSubBg
    val textColor = if (isDub) TagDubText else TagSubText
    val borderColor = if (isDub) Color(0xFF6E1825) else Color(0xFF1B4E59)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.5.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
