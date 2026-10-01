package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.model.Anime
import com.example.data.model.Episode
import com.example.data.repository.AnimeRepository
import com.example.data.repository.UserRepository
import com.example.ui.components.AnimeXBottomNav
import com.example.ui.components.AnimeXDrawer
import com.example.ui.components.NavTab
import com.example.ui.screens.AdminAnimeEditScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminEpisodesScreen
import com.example.ui.screens.AdminUsersScreen
import com.example.ui.screens.AllAnimeScreen
import com.example.ui.screens.AnimeDetailsScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingFeaturesScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.AnimeXTheme
import com.example.ui.theme.DarkBackground
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val animeRepository = AnimeRepository(applicationContext)
        val userRepository = UserRepository(applicationContext)

        setContent {
            AnimeXTheme {
                AnimeXApp(
                    animeRepository = animeRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}

@Composable
fun AnimeXApp(
    animeRepository: AnimeRepository,
    userRepository: UserRepository
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var currentScreen by remember { mutableStateOf("splash") }
    var screenBackStack by remember { mutableStateOf(listOf("splash")) }

    var selectedAnime by remember { mutableStateOf<Anime?>(null) }
    var selectedEpisode by remember { mutableStateOf<Episode?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var initialSearchQuery by remember { mutableStateOf("") }

    val allAnime by animeRepository.observeAllAnime().collectAsState(initial = emptyList())
    val categories by animeRepository.observeCategories().collectAsState(initial = emptyList())
    val watchlist by userRepository.observeWatchlist().collectAsState(initial = emptyList())
    val favorites by userRepository.observeFavorites().collectAsState(initial = emptyList())
    val admins by userRepository.observeAdmins().collectAsState(initial = emptyList())
    val currentUser by userRepository.currentUserState.collectAsState()
    val isAdmin by userRepository.isAdminState.collectAsState()

    // Current anime episodes
    val episodes by if (selectedAnime != null) {
        animeRepository.observeEpisodes(selectedAnime!!.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    // Auto sign-in attempt
    LaunchedEffect(Unit) {
        userRepository.attemptSilentSignIn(scope)
    }

    fun navigateTo(screen: String) {
        screenBackStack = screenBackStack + screen
        currentScreen = screen
    }

    fun navigateBack() {
        if (screenBackStack.size > 1) {
            screenBackStack = screenBackStack.dropLast(1)
            currentScreen = screenBackStack.last()
        }
    }

    BackHandler(enabled = screenBackStack.size > 1) {
        navigateBack()
    }

    val showBottomNav = currentScreen in listOf("home", "categories", "watchlist", "settings")

    val selectedBottomNavTab = when (currentScreen) {
        "categories" -> NavTab.CATEGORIES
        "watchlist" -> NavTab.MY_LIST
        "settings" -> NavTab.MORE
        else -> NavTab.HOME
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = currentScreen !in listOf("splash", "onboarding", "video_player"),
            drawerContent = {
                AnimeXDrawer(
                    currentUser = currentUser,
                    isAdmin = isAdmin,
                    currentScreen = currentScreen,
                    onNavigate = { route ->
                        when (route) {
                            "home" -> navigateTo("home")
                            "all_anime" -> {
                                selectedCategoryFilter = null
                                navigateTo("all_anime")
                            }
                            "categories" -> navigateTo("categories")
                            "watchlist" -> navigateTo("watchlist")
                            "favorites" -> navigateTo("favorites")
                            "admin_dashboard" -> navigateTo("admin_dashboard")
                            "settings" -> navigateTo("settings")
                        }
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground),
                containerColor = DarkBackground,
                bottomBar = {
                    if (showBottomNav) {
                        AnimeXBottomNav(
                            selectedTab = selectedBottomNavTab,
                            onTabSelected = { tab ->
                                when (tab) {
                                    NavTab.HOME -> navigateTo("home")
                                    NavTab.CATEGORIES -> navigateTo("categories")
                                    NavTab.MY_LIST -> navigateTo("watchlist")
                                    NavTab.MORE -> scope.launch { drawerState.open() }
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentScreen) {
                        "splash" -> {
                            SplashScreen(
                                onStartClick = { navigateTo("onboarding") },
                                onSkipClick = { navigateTo("home") }
                            )
                        }

                        "onboarding" -> {
                            OnboardingFeaturesScreen(
                                onContinue = { navigateTo("home") }
                            )
                        }

                        "home" -> {
                            HomeScreen(
                                animeList = allAnime,
                                onAnimeClick = { anime ->
                                    selectedAnime = anime
                                    navigateTo("anime_details")
                                },
                                onWatchClick = { anime ->
                                    selectedAnime = anime
                                    val firstEp = episodes.firstOrNull() ?: Episode(
                                        id = "${anime.id}_ep_1",
                                        animeId = anime.id,
                                        episodeNumber = 1,
                                        title = "الحلقة 1",
                                        titleAr = "الحلقة 1"
                                    )
                                    selectedEpisode = firstEp
                                    navigateTo("video_player")
                                },
                                onViewAllClick = {
                                    selectedCategoryFilter = null
                                    navigateTo("all_anime")
                                },
                                onOpenDrawer = {
                                    scope.launch { drawerState.open() }
                                },
                                onSearchQueryChange = { query ->
                                    initialSearchQuery = query
                                    navigateTo("search")
                                }
                            )
                        }

                        "categories" -> {
                            CategoriesScreen(
                                categories = categories,
                                onCategoryClick = { category ->
                                    selectedCategoryFilter = category.nameAr
                                    navigateTo("all_anime")
                                },
                                onBackClick = { navigateBack() },
                                onSearchClick = {
                                    initialSearchQuery = ""
                                    navigateTo("search")
                                }
                            )
                        }

                        "all_anime" -> {
                            AllAnimeScreen(
                                animeList = allAnime,
                                selectedCategoryName = selectedCategoryFilter,
                                onAnimeClick = { anime ->
                                    selectedAnime = anime
                                    navigateTo("anime_details")
                                },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "anime_details" -> {
                            val anime = selectedAnime ?: allAnime.firstOrNull()
                            if (anime != null) {
                                val isFav = favorites.any { it.animeId == anime.id }
                                AnimeDetailsScreen(
                                    anime = anime,
                                    episodes = episodes,
                                    isFavorite = isFav,
                                    onToggleFavorite = {
                                        scope.launch {
                                            userRepository.toggleFavorite(
                                                animeId = anime.id,
                                                title = anime.title,
                                                titleAr = anime.titleAr,
                                                posterUrl = anime.posterUrl,
                                                genres = anime.genres,
                                                rating = anime.rating
                                            )
                                        }
                                    },
                                    onAddToWatchlist = {
                                        scope.launch {
                                            userRepository.updateWatchProgress(
                                                animeId = anime.id,
                                                title = anime.title,
                                                titleAr = anime.titleAr,
                                                posterUrl = anime.posterUrl,
                                                episodeNumber = 1,
                                                totalEpisodes = anime.totalEpisodes,
                                                progressSec = 0,
                                                durationSec = 1440,
                                                isMovie = anime.type == "movie"
                                            )
                                        }
                                    },
                                    onWatchNowClick = { ep ->
                                        selectedEpisode = ep ?: episodes.firstOrNull() ?: Episode(
                                            id = "${anime.id}_ep_1",
                                            animeId = anime.id,
                                            episodeNumber = 1,
                                            title = "الحلقة 1",
                                            titleAr = "الحلقة 1"
                                        )
                                        navigateTo("video_player")
                                    },
                                    onEpisodeClick = { ep ->
                                        selectedEpisode = ep
                                        navigateTo("video_player")
                                    },
                                    onBackClick = { navigateBack() },
                                    onMenuClick = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                            }
                        }

                        "video_player" -> {
                            val anime = selectedAnime ?: allAnime.firstOrNull()
                            val episode = selectedEpisode ?: episodes.firstOrNull()
                            if (anime != null && episode != null) {
                                VideoPlayerScreen(
                                    anime = anime,
                                    episode = episode,
                                    allEpisodes = episodes,
                                    onEpisodeSelected = { ep ->
                                        selectedEpisode = ep
                                    },
                                    onBackClick = { navigateBack() }
                                )
                            }
                        }

                        "watchlist" -> {
                            WatchlistScreen(
                                watchlist = watchlist,
                                onAnimeClick = { animeId ->
                                    val anime = allAnime.find { it.id == animeId }
                                    if (anime != null) {
                                        selectedAnime = anime
                                        navigateTo("anime_details")
                                    }
                                },
                                onBackClick = { navigateBack() },
                                onMenuClick = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }

                        "favorites" -> {
                            FavoritesScreen(
                                favorites = favorites,
                                onAnimeClick = { animeId ->
                                    val anime = allAnime.find { it.id == animeId }
                                    if (anime != null) {
                                        selectedAnime = anime
                                        navigateTo("anime_details")
                                    }
                                },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "search" -> {
                            SearchScreen(
                                animeList = allAnime,
                                initialQuery = initialSearchQuery,
                                onAnimeClick = { anime ->
                                    selectedAnime = anime
                                    navigateTo("anime_details")
                                },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "settings" -> {
                            SettingsScreen(
                                currentUser = currentUser,
                                isAdmin = isAdmin,
                                userRepository = userRepository,
                                onNavigateToAdmin = { navigateTo("admin_dashboard") },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "admin_dashboard" -> {
                            AdminDashboardScreen(
                                animeList = allAnime,
                                admins = admins,
                                animeRepository = animeRepository,
                                onNavigateToAddAnime = {
                                    selectedAnime = null
                                    navigateTo("admin_edit_anime")
                                },
                                onNavigateToEditAnime = { anime ->
                                    selectedAnime = anime
                                    navigateTo("admin_edit_anime")
                                },
                                onNavigateToEpisodes = { anime ->
                                    selectedAnime = anime
                                    navigateTo("admin_episodes")
                                },
                                onNavigateToAdminUsers = { navigateTo("admin_users") },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "admin_edit_anime" -> {
                            AdminAnimeEditScreen(
                                anime = selectedAnime,
                                animeRepository = animeRepository,
                                onSaved = { navigateBack() },
                                onBackClick = { navigateBack() }
                            )
                        }

                        "admin_episodes" -> {
                            val anime = selectedAnime ?: allAnime.firstOrNull()
                            if (anime != null) {
                                AdminEpisodesScreen(
                                    anime = anime,
                                    episodes = episodes,
                                    animeRepository = animeRepository,
                                    onBackClick = { navigateBack() }
                                )
                            }
                        }

                        "admin_users" -> {
                            AdminUsersScreen(
                                admins = admins,
                                userRepository = userRepository,
                                onBackClick = { navigateBack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
