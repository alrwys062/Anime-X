package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.Anime
import com.example.data.model.Category
import com.example.data.model.Episode
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AnimeRepository(private val context: Context) {

    private val TAG = "AnimeRepository"

    private val db: FirebaseFirestore by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            if (dbId.isNotEmpty()) {
                FirebaseFirestore.getInstance(dbId)
            } else {
                FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Falling back to default Firestore: ${e.message}")
            FirebaseFirestore.getInstance()
        }
    }

    private val animeCollection by lazy { db.collection("anime") }
    private val categoriesCollection by lazy { db.collection("categories") }

    init {
        // Seed initial anime catalog into Firestore in background if not already seeded
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    suspend fun seedInitialDataIfNeeded() {
        try {
            val snapshot = animeCollection.limit(1).get().await()
            if (snapshot.isEmpty) {
                Log.d(TAG, "Seeding initial anime and categories...")
                for (anime in DefaultAnimeData.getInitialAnimeList()) {
                    animeCollection.document(anime.id).set(anime).await()
                    // Seed initial episodes
                    val episodes = DefaultAnimeData.getEpisodesForAnime(anime.id)
                    for (ep in episodes) {
                        animeCollection.document(anime.id).collection("episodes").document(ep.id).set(ep).await()
                    }
                }
                for (cat in DefaultAnimeData.defaultCategories) {
                    categoriesCollection.document(cat.id).set(cat).await()
                }
                Log.d(TAG, "Seeding completed successfully")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Seed check failed: ${e.message}")
        }
    }

    fun observeAllAnime(): Flow<List<Anime>> = flow {
        val initial = DefaultAnimeData.getInitialAnimeList()
        emit(initial)
        try {
            animeCollection.whereEqualTo("published", true).snapshots()
                .map { snap ->
                    val list = snap.toObjects(Anime::class.java)
                    if (list.isNotEmpty()) list else initial
                }
                .catch { emit(initial) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(initial)
        }
    }

    fun observeAllAnimeForAdmin(): Flow<List<Anime>> = flow {
        val initial = DefaultAnimeData.getInitialAnimeList()
        emit(initial)
        try {
            animeCollection.snapshots()
                .map { snap ->
                    val list = snap.toObjects(Anime::class.java)
                    if (list.isNotEmpty()) list else initial
                }
                .catch { emit(initial) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(initial)
        }
    }

    fun observeAnimeById(id: String): Flow<Anime?> = flow {
        val defaultAnime = DefaultAnimeData.getInitialAnimeList().find { it.id == id }
        emit(defaultAnime)
        try {
            animeCollection.document(id).snapshots()
                .map { snap -> snap.toObject(Anime::class.java) ?: defaultAnime }
                .catch { emit(defaultAnime) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(defaultAnime)
        }
    }

    fun observeEpisodes(animeId: String): Flow<List<Episode>> = flow {
        val initialEpisodes = DefaultAnimeData.getEpisodesForAnime(animeId)
        emit(initialEpisodes)
        try {
            animeCollection.document(animeId).collection("episodes").snapshots()
                .map { snap ->
                    val list = snap.toObjects(Episode::class.java)
                    if (list.isNotEmpty()) list.sortedBy { it.episodeNumber } else initialEpisodes
                }
                .catch { emit(initialEpisodes) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(initialEpisodes)
        }
    }

    fun observeCategories(): Flow<List<Category>> = flow {
        val initial = DefaultAnimeData.defaultCategories
        emit(initial)
        try {
            categoriesCollection.snapshots()
                .map { snap ->
                    val list = snap.toObjects(Category::class.java)
                    if (list.isNotEmpty()) list else initial
                }
                .catch { emit(initial) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(initial)
        }
    }

    suspend fun saveAnime(anime: Anime): Result<Unit> {
        return try {
            val updated = anime.copy(updatedAt = Timestamp.now())
            animeCollection.document(anime.id).set(updated).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving anime", e)
            Result.failure(e)
        }
    }

    suspend fun deleteAnime(animeId: String): Result<Unit> {
        return try {
            animeCollection.document(animeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting anime", e)
            Result.failure(e)
        }
    }

    suspend fun togglePublish(animeId: String, published: Boolean): Result<Unit> {
        return try {
            animeCollection.document(animeId).update("published", published).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling publish", e)
            Result.failure(e)
        }
    }

    suspend fun saveEpisode(episode: Episode): Result<Unit> {
        return try {
            animeCollection.document(episode.animeId)
                .collection("episodes")
                .document(episode.id)
                .set(episode)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving episode", e)
            Result.failure(e)
        }
    }

    suspend fun deleteEpisode(animeId: String, episodeId: String): Result<Unit> {
        return try {
            animeCollection.document(animeId)
                .collection("episodes")
                .document(episodeId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting episode", e)
            Result.failure(e)
        }
    }
}
