package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.data.repository.AnimeRepository
import com.example.ui.theme.AnimeRedPrimary
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.RedGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun AdminAnimeEditScreen(
    anime: Anime?,
    animeRepository: AnimeRepository,
    onSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var title by remember { mutableStateOf(anime?.title ?: "") }
    var titleAr by remember { mutableStateOf(anime?.titleAr ?: "") }
    var description by remember { mutableStateOf(anime?.description ?: "") }
    var posterUrl by remember { mutableStateOf(anime?.posterUrl ?: "") }
    var coverUrl by remember { mutableStateOf(anime?.coverUrl ?: "") }
    var genresText by remember { mutableStateOf(anime?.genres?.joinToString(", ") ?: "أكشن, مغامرات") }
    var yearText by remember { mutableStateOf((anime?.year ?: 2024).toString()) }
    var ratingText by remember { mutableStateOf((anime?.rating ?: 8.5).toString()) }
    var totalEpisodesText by remember { mutableStateOf((anime?.totalEpisodes ?: 12).toString()) }
    var seasonsText by remember { mutableStateOf((anime?.seasonsCount ?: 1).toString()) }
    var hasSub by remember { mutableStateOf(anime?.hasSub ?: true) }
    var hasDub by remember { mutableStateOf(anime?.hasDub ?: true) }
    var isPublished by remember { mutableStateOf(anime?.published ?: true) }
    var isFeatured by remember { mutableStateOf(anime?.featured ?: false) }

    val scrollState = rememberScrollState()

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
                modifier = Modifier.testTag("admin_edit_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = if (anime == null) "إضافة أنمي جديد" else "تعديل ${anime.titleAr}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    if (title.isEmpty() || titleAr.isEmpty()) {
                        errorMessage = "يرجى إدخال اسم الأنمي بالإنجليزية والعربية"
                        return@IconButton
                    }
                    isSaving = true
                    scope.launch {
                        val animeId = anime?.id ?: "anime_${System.currentTimeMillis()}"
                        val genres = genresText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        val newAnime = Anime(
                            id = animeId,
                            title = title,
                            titleAr = titleAr,
                            description = description,
                            posterUrl = posterUrl,
                            coverUrl = coverUrl,
                            genres = genres,
                            year = yearText.toIntOrNull() ?: 2024,
                            rating = ratingText.toDoubleOrNull() ?: 8.5,
                            totalEpisodes = totalEpisodesText.toIntOrNull() ?: 12,
                            seasonsCount = seasonsText.toIntOrNull() ?: 1,
                            hasSub = hasSub,
                            hasDub = hasDub,
                            published = isPublished,
                            featured = isFeatured
                        )
                        animeRepository.saveAnime(newAnime)
                        isSaving = false
                        onSaved()
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.testTag("admin_save_anime_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    tint = AnimeRedPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = AnimeRedPrimary,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

            AdminFormField(
                label = "اسم الأنمي بالإنجليزية (Title)",
                value = title,
                onValueChange = { title = it },
                testTag = "input_anime_title"
            )

            AdminFormField(
                label = "اسم الأنمي بالعربية (Arabic Title)",
                value = titleAr,
                onValueChange = { titleAr = it },
                testTag = "input_anime_title_ar"
            )

            AdminFormField(
                label = "القصة / الوصف (Description)",
                value = description,
                onValueChange = { description = it },
                singleLine = false,
                testTag = "input_anime_description"
            )

            AdminFormField(
                label = "رابط البوستر (Poster URL)",
                value = posterUrl,
                onValueChange = { posterUrl = it },
                testTag = "input_anime_poster"
            )

            AdminFormField(
                label = "رابط الغلاف (Cover Banner URL)",
                value = coverUrl,
                onValueChange = { coverUrl = it },
                testTag = "input_anime_cover"
            )

            AdminFormField(
                label = "التصنيفات مفصولة بفواصل (Genres)",
                value = genresText,
                onValueChange = { genresText = it },
                testTag = "input_anime_genres"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminFormField(
                    label = "سنة الإصدار",
                    value = yearText,
                    onValueChange = { yearText = it },
                    modifier = Modifier.weight(1f)
                )
                AdminFormField(
                    label = "التقييم (8.5)",
                    value = ratingText,
                    onValueChange = { ratingText = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminFormField(
                    label = "عدد الحلقات",
                    value = totalEpisodesText,
                    onValueChange = { totalEpisodesText = it },
                    modifier = Modifier.weight(1f)
                )
                AdminFormField(
                    label = "عدد المواسم",
                    value = seasonsText,
                    onValueChange = { seasonsText = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // Flags: Subbed, Dubbed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasDub,
                        onCheckedChange = { hasDub = it },
                        colors = CheckboxDefaults.colors(checkedColor = AnimeRedPrimary)
                    )
                    Text("مدبلج", color = Color.White, fontSize = 14.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hasSub,
                        onCheckedChange = { hasSub = it },
                        colors = CheckboxDefaults.colors(checkedColor = AnimeRedPrimary)
                    )
                    Text("مترجم", color = Color.White, fontSize = 14.sp)
                }
            }

            // Publish switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceCard)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = isPublished,
                    onCheckedChange = { isPublished = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AnimeRedPrimary)
                )
                Text("نشر الأنمي في التطبيق", color = Color.White, fontSize = 14.sp)
            }

            // Featured switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceCard)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = isFeatured,
                    onCheckedChange = { isFeatured = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AnimeRedPrimary)
                )
                Text("تمييز في الواجهة الرئيسية (Featured Hero)", color = Color.White, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (title.isEmpty() || titleAr.isEmpty()) {
                        errorMessage = "يرجى إدخال اسم الأنمي بالإنجليزية والعربية"
                        return@Button
                    }
                    isSaving = true
                    scope.launch {
                        val animeId = anime?.id ?: "anime_${System.currentTimeMillis()}"
                        val genres = genresText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        val newAnime = Anime(
                            id = animeId,
                            title = title,
                            titleAr = titleAr,
                            description = description,
                            posterUrl = posterUrl,
                            coverUrl = coverUrl,
                            genres = genres,
                            year = yearText.toIntOrNull() ?: 2024,
                            rating = ratingText.toDoubleOrNull() ?: 8.5,
                            totalEpisodes = totalEpisodesText.toIntOrNull() ?: 12,
                            seasonsCount = seasonsText.toIntOrNull() ?: 1,
                            hasSub = hasSub,
                            hasDub = hasDub,
                            published = isPublished,
                            featured = isFeatured
                        )
                        animeRepository.saveAnime(newAnime)
                        isSaving = false
                        onSaved()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(RedGradient)
                    .testTag("admin_save_button_submit"),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = if (isSaving) "جاري الحفظ..." else "حفظ البيانات في Firebase",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AdminFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    testTag: String = ""
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkSurfaceCard)
                .testTag(testTag),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AnimeRedPrimary,
                unfocusedBorderColor = BorderStroke,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = AnimeRedPrimary
            ),
            singleLine = singleLine
        )
    }
}
