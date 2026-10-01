package com.example.data.model

import com.google.firebase.Timestamp

data class Anime(
    val id: String = "",
    val title: String = "",
    val titleAr: String = "",
    val description: String = "",
    val posterUrl: String = "",
    val coverUrl: String = "",
    val genres: List<String> = emptyList(),
    val year: Int = 2024,
    val status: String = "ongoing", // ongoing, completed
    val rating: Double = 8.5,
    val type: String = "series", // series, movie
    val hasSub: Boolean = true,
    val hasDub: Boolean = true,
    val totalEpisodes: Int = 12,
    val featured: Boolean = false,
    val published: Boolean = true,
    val seasonsCount: Int = 1,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

data class VideoSource(
    val id: String = "",
    val name: String = "سيرفر رئيسي (Server 1)",
    val url: String = "",
    val quality: String = "720p", // 1080p, 720p, 480p, auto
    val type: String = "sub", // sub, dub
    val language: String = "ar",
    val isActive: Boolean = true
)

data class SubtitleTrack(
    val id: String = "",
    val language: String = "الترجمة العربية",
    val code: String = "ar",
    val url: String = "",
    val isDefault: Boolean = false
)

data class Episode(
    val id: String = "",
    val animeId: String = "",
    val episodeNumber: Int = 1,
    val title: String = "الحلقة 1",
    val titleAr: String = "الحلقة 1",
    val thumbnailUrl: String = "",
    val videoUrl: String = "",
    val duration: String = "24:00",
    val season: Int = 1,
    val sources: List<VideoSource> = emptyList(),
    val subtitles: List<SubtitleTrack> = emptyList(),
    val published: Boolean = true,
    val createdAt: Timestamp? = null
)

data class Category(
    val id: String = "",
    val name: String = "",
    val nameAr: String = "",
    val iconName: String = "explore",
    val imageUrl: String = "",
    val count: Int = 0
)

data class WatchRecord(
    val animeId: String = "",
    val title: String = "",
    val titleAr: String = "",
    val posterUrl: String = "",
    val currentEpisode: Int = 1,
    val totalEpisodes: Int = 12,
    val progressSeconds: Long = 0,
    val durationSeconds: Long = 1440,
    val isMovie: Boolean = false,
    val updatedAt: Timestamp? = null
)

data class Favorite(
    val animeId: String = "",
    val title: String = "",
    val titleAr: String = "",
    val posterUrl: String = "",
    val genres: List<String> = emptyList(),
    val rating: Double = 8.5,
    val createdAt: Timestamp? = null
)

data class AdminUser(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val role: String = "admin", // superadmin, admin, editor
    val permissions: List<String> = listOf("manage_anime", "manage_episodes", "manage_categories"),
    val assignedAt: Timestamp? = null
)

data class UserProfile(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val role: String = "user",
    val createdAt: Timestamp? = null
)
