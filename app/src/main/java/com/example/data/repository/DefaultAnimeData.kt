package com.example.data.repository

import com.example.R
import com.example.data.model.Anime
import com.example.data.model.Category
import com.example.data.model.Episode
import com.example.data.model.SubtitleTrack
import com.example.data.model.VideoSource
import com.example.data.model.WatchRecord

object DefaultAnimeData {

    // Resource URIs for local drawables
    const val RES_PREFIX = "android.resource://com.aistudio.animex.vxyzp/"

    val onePieceBannerUri = "${RES_PREFIX}${R.drawable.one_piece_banner_1790893298945}"
    val narutoCoverUri = "${RES_PREFIX}${R.drawable.naruto_hero_cover_1790893315091}"
    val jjkPosterUri = "${RES_PREFIX}${R.drawable.jujutsu_kaisen_poster_1790893330776}"
    val demonSlayerPosterUri = "${RES_PREFIX}${R.drawable.demon_slayer_poster_1790893345068}"
    val soloLevelingPosterUri = "${RES_PREFIX}${R.drawable.solo_leveling_poster_1790893358568}"
    val aotPosterUri = "${RES_PREFIX}${R.drawable.aot_poster_1790893372017}"

    val defaultCategories = listOf(
        Category("adventures", "Adventures", "مغامرات", "explore", onePieceBannerUri, 142),
        Category("action", "Action", "أكشن", "flash", narutoCoverUri, 280),
        Category("comedy", "Comedy", "كوميديا", "mood", jjkPosterUri, 95),
        Category("drama", "Drama", "دراما", "theater", aotPosterUri, 110),
        Category("romance", "Romance", "رومانسي", "favorite", demonSlayerPosterUri, 76),
        Category("scifi", "Sci-Fi", "خيال علمي", "science", soloLevelingPosterUri, 84),
        Category("mystery", "Mystery", "غموض", "visibility", aotPosterUri, 62),
        Category("horror", "Horror", "رعب", "psychology", demonSlayerPosterUri, 49)
    )

    fun getInitialAnimeList(): List<Anime> = listOf(
        Anime(
            id = "naruto",
            title = "Naruto",
            titleAr = "ناروتو",
            description = "تدور القصة حول ناروتو فتى صغير يعيش في قرية الورق، ويحلم بأن يصبح هوكاجي، أقوى نينجا في القرية.",
            posterUrl = narutoCoverUri,
            coverUrl = narutoCoverUri,
            genres = listOf("أكشن", "مغامرات", "فانتازيا"),
            year = 2002,
            status = "completed",
            rating = 8.3,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 220,
            featured = false,
            published = true,
            seasonsCount = 3
        ),
        Anime(
            id = "one_piece",
            title = "One Piece",
            titleAr = "ون بيس",
            description = "تبدأ القصة بإعدام غول دي روجر، ملك القراصنة، الذي أعلن قبل إعدامه عن وجود كنزه الأسطوري ون بيس، ما أطلق عصر القراصنة العظيم.",
            posterUrl = onePieceBannerUri,
            coverUrl = onePieceBannerUri,
            genres = listOf("مغامرات", "أكشن", "كوميديا"),
            year = 1999,
            status = "ongoing",
            rating = 8.9,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 1125,
            featured = true,
            published = true,
            seasonsCount = 20
        ),
        Anime(
            id = "jujutsu_kaisen",
            title = "Jujutsu Kaisen",
            titleAr = "جوجوتسو كايسن",
            description = "يوجي إيتادوري طالب في المدرسة الثانوية يمتلك قوة جسدية هائلة، يبتلع إصبعاً ملعوناً لحماية أصدقائه، فيصبح مضيفاً لملك اللعنات سوكونا.",
            posterUrl = jjkPosterUri,
            coverUrl = jjkPosterUri,
            genres = listOf("أكشن", "خوارق", "غموض"),
            year = 2020,
            status = "ongoing",
            rating = 8.6,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 24,
            featured = false,
            published = true,
            seasonsCount = 2
        ),
        Anime(
            id = "demon_slayer",
            title = "Demon Slayer",
            titleAr = "قاتل الشياطين",
            description = "تانجيرو كامادو يعيش مع عائلته بسلام حتى تذبحهم الشياطين وتتحول أخته نيزوكو إلى شيطانة، فينطلق في رحلة ليصبح قاتل شياطين ويعيدها بشرية.",
            posterUrl = demonSlayerPosterUri,
            coverUrl = demonSlayerPosterUri,
            genres = listOf("أكشن", "فانتازيا", "دراما"),
            year = 2019,
            status = "ongoing",
            rating = 8.7,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 63,
            featured = false,
            published = true,
            seasonsCount = 4
        ),
        Anime(
            id = "solo_leveling",
            title = "Solo Leveling",
            titleAr = "سولو ليفلينج",
            description = "في عالم ظهرت فيه بوابات مليئة بالوحوش، يُعرف سونغ جين وو بأضعف صياد في العالم، حتى يخوض تجربة غامضة تمنحه نظاماً يتيح له الارتقاء بقوته دون حدود.",
            posterUrl = soloLevelingPosterUri,
            coverUrl = soloLevelingPosterUri,
            genres = listOf("أكشن", "فانتازيا", "خيال علمي"),
            year = 2024,
            status = "ongoing",
            rating = 8.5,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 12,
            featured = false,
            published = true,
            seasonsCount = 2
        ),
        Anime(
            id = "bleach",
            title = "Bleach",
            titleAr = "بليتش",
            description = "إيتشيغو كوروساكي مراهق يمتلك القدرة على رؤية الأرواح، يكتسب قوى حاصد أرواح للدفاع عن مدينته ضد الأرواح الشريرة (الهولو).",
            posterUrl = aotPosterUri,
            coverUrl = aotPosterUri,
            genres = listOf("أكشن", "فانتازيا", "مغامرات"),
            year = 2004,
            status = "completed",
            rating = 8.2,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 366,
            featured = false,
            published = true,
            seasonsCount = 16
        ),
        Anime(
            id = "attack_on_titan",
            title = "Attack on Titan",
            titleAr = "هجوم العمالقة",
            description = "بعد تدمير مسقط رأسه وموت والدته، ينضم إيرين ييغر إلى فيلق الاستطلاع للقضاء على العمالقة الذين يهددون بقاء البشرية خلف الأسوار.",
            posterUrl = aotPosterUri,
            coverUrl = aotPosterUri,
            genres = listOf("أكشن", "غموض", "دراما"),
            year = 2013,
            status = "completed",
            rating = 9.1,
            type = "series",
            hasSub = true,
            hasDub = true,
            totalEpisodes = 87,
            featured = false,
            published = true,
            seasonsCount = 4
        )
    )

    fun getEpisodesForAnime(animeId: String): List<Episode> {
        val sampleVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val sampleBackupUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        
        val defaultSources = listOf(
            VideoSource("src_1", "سيرفر رئيسي (Server 1)", sampleVideoUrl, "720p", "sub", "ar", true),
            VideoSource("src_2", "سيرفر فائق (Server 2)", sampleBackupUrl, "1080p", "sub", "ar", true),
            VideoSource("src_3", "سيرفر مدبلج (Server Dub)", sampleVideoUrl, "720p", "dub", "ar", true)
        )

        val defaultSubtitles = listOf(
            SubtitleTrack("sub_ar", "الترجمة العربية", "ar", "", true),
            SubtitleTrack("sub_en", "الترجمة الإنجليزية", "en", "", false),
            SubtitleTrack("sub_fr", "الترجمة الفرنسية", "fr", "", false)
        )

        return (1..15).map { epNum ->
            Episode(
                id = "${animeId}_ep_$epNum",
                animeId = animeId,
                episodeNumber = epNum,
                title = "الحلقة $epNum",
                titleAr = "الحلقة $epNum",
                thumbnailUrl = when (animeId) {
                    "naruto" -> narutoCoverUri
                    "one_piece" -> onePieceBannerUri
                    "demon_slayer" -> demonSlayerPosterUri
                    else -> jjkPosterUri
                },
                videoUrl = sampleVideoUrl,
                duration = "22:15",
                season = 1,
                sources = defaultSources,
                subtitles = defaultSubtitles,
                published = true
            )
        }
    }

    fun getInitialWatchlist(): List<WatchRecord> = listOf(
        WatchRecord("one_piece", "One Piece", "ون بيس", onePieceBannerUri, 320, 1125, 750, 1440, false),
        WatchRecord("naruto", "Naruto", "ناروتو", narutoCoverUri, 45, 220, 920, 1440, false),
        WatchRecord("attack_on_titan", "Attack on Titan", "هجوم العمالقة", aotPosterUri, 12, 87, 450, 1440, false),
        WatchRecord("demon_slayer", "Demon Slayer", "قاتل الشياطين", demonSlayerPosterUri, 16, 63, 1100, 1440, false),
        WatchRecord("jujutsu_kaisen", "Jujutsu Kaisen", "جوجوتسو كايسن", jjkPosterUri, 5, 24, 600, 1440, false)
    )
}
