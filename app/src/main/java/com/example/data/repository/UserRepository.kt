package com.example.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.example.data.model.AdminUser
import com.example.data.model.Favorite
import com.example.data.model.UserProfile
import com.example.data.model.WatchRecord
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRepository(private val context: Context) {

    private val TAG = "UserRepository"
    private val auth: FirebaseAuth = Firebase.auth

    private val db: FirebaseFirestore by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            if (dbId.isNotEmpty()) {
                FirebaseFirestore.getInstance(dbId)
            } else {
                FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            FirebaseFirestore.getInstance()
        }
    }

    private val credentialManager = CredentialManager.create(context)

    private val _currentUserState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUserState: StateFlow<FirebaseUser?> = _currentUserState.asStateFlow()

    private val _isAdminState = MutableStateFlow<Boolean>(false)
    val isAdminState: StateFlow<Boolean> = _isAdminState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUserState.value = firebaseAuth.currentUser
            checkAdminStatus(firebaseAuth.currentUser)
        }
        checkAdminStatus(auth.currentUser)
    }

    private fun checkAdminStatus(user: FirebaseUser?) {
        if (user == null) {
            _isAdminState.value = false
            return
        }
        // Super Admin hard check
        if (user.email.equals("zaim9002@gmail.com", ignoreCase = true)) {
            _isAdminState.value = true
            return
        }
        // Check if user is registered in admins collection or user role
        CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val adminDoc = db.collection("admins").document(user.uid).get().await()
                if (adminDoc.exists()) {
                    _isAdminState.value = true
                    return@launch
                }
                val userDoc = db.collection("users").document(user.uid).get().await()
                val role = userDoc.getString("role")
                if (role == "admin" || role == "superadmin") {
                    _isAdminState.value = true
                    return@launch
                }
                // Also check if any admin exists at all; if not, first user can be admin
                val allAdmins = db.collection("admins").limit(1).get().await()
                if (allAdmins.isEmpty) {
                    _isAdminState.value = true
                }
            } catch (e: Exception) {
                // If offline or permission check, allow admin state if email matches or default
                _isAdminState.value = true
            }
        }
    }

    fun setAdminDirect(isAdmin: Boolean) {
        _isAdminState.value = isAdmin
    }

    fun observeWatchlist(): Flow<List<WatchRecord>> = flow {
        val initial = DefaultAnimeData.getInitialWatchlist()
        emit(initial)
        val uid = auth.currentUser?.uid
        if (uid != null) {
            try {
                db.collection("users").document(uid).collection("watchlist")
                    .snapshots()
                    .map { snap ->
                        val list = snap.toObjects(WatchRecord::class.java)
                        if (list.isNotEmpty()) list else initial
                    }
                    .catch { emit(initial) }
                    .collect { emit(it) }
            } catch (e: Exception) {
                emit(initial)
            }
        }
    }

    suspend fun updateWatchProgress(animeId: String, title: String, titleAr: String, posterUrl: String, episodeNumber: Int, totalEpisodes: Int, progressSec: Long, durationSec: Long, isMovie: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        try {
            val record = WatchRecord(
                animeId = animeId,
                title = title,
                titleAr = titleAr,
                posterUrl = posterUrl,
                currentEpisode = episodeNumber,
                totalEpisodes = totalEpisodes,
                progressSeconds = progressSec,
                durationSeconds = durationSec,
                isMovie = isMovie,
                updatedAt = Timestamp.now()
            )
            db.collection("users").document(uid).collection("watchlist").document(animeId).set(record).await()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update watch progress", e)
        }
    }

    fun observeFavorites(): Flow<List<Favorite>> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(emptyList())
            return@flow
        }
        try {
            db.collection("users").document(uid).collection("favorites")
                .snapshots()
                .map { snap -> snap.toObjects(Favorite::class.java) }
                .catch { emit(emptyList()) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun toggleFavorite(animeId: String, title: String, titleAr: String, posterUrl: String, genres: List<String>, rating: Double): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val favRef = db.collection("users").document(uid).collection("favorites").document(animeId)
        return try {
            val existing = favRef.get().await()
            if (existing.exists()) {
                favRef.delete().await()
                false
            } else {
                val fav = Favorite(
                    animeId = animeId,
                    title = title,
                    titleAr = titleAr,
                    posterUrl = posterUrl,
                    genres = genres,
                    rating = rating,
                    createdAt = Timestamp.now()
                )
                favRef.set(fav).await()
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Toggle favorite failed", e)
            false
        }
    }

    fun signInWithGoogle(
        activity: Activity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        scope: CoroutineScope
    ) {
        val clientId = try {
            context.getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            onError("Google Sign-In configuration missing: default_web_client_id")
            return
        }

        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val authResult = auth.signInWithCredential(authCredential).await()
                    val user = authResult.user
                    if (user != null) {
                        saveUserProfile(user)
                    }
                    onSuccess()
                } else {
                    onError("نوع تسجيل الدخول غير مدعوم")
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w(TAG, "Google Sign-In cancelled: ${e.message}", e)
                onError("تم إلغاء تسجيل الدخول")
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In failed", e)
                onError(e.localizedMessage ?: "فشل تسجيل الدخول")
            }
        }
    }

    fun attemptSilentSignIn(scope: CoroutineScope) {
        if (auth.currentUser != null) return
        val clientId = try {
            context.getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            return
        }
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    auth.signInWithCredential(authCredential).await()
                }
            } catch (e: Exception) {
                // Silent check expected to fail if no saved credentials
            }
        }
    }

    private suspend fun saveUserProfile(user: FirebaseUser) {
        try {
            val profile = UserProfile(
                userId = user.uid,
                email = user.email ?: "",
                displayName = user.displayName ?: "مستخدم AnimeX",
                photoUrl = user.photoUrl?.toString() ?: "",
                role = "user",
                createdAt = Timestamp.now()
            )
            db.collection("users").document(user.uid).set(profile).await()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to write user profile", e)
        }
    }

    fun signOut(scope: CoroutineScope, onComplete: () -> Unit) {
        auth.signOut()
        _isAdminState.value = false
        scope.launch {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Log.e(TAG, "Clear credential state error", e)
            } finally {
                onComplete()
            }
        }
    }

    fun observeAdmins(): Flow<List<AdminUser>> = flow {
        val defaultAdmins = listOf(
            AdminUser("superadmin_zaim", "zaim9002@gmail.com", "المسؤول العام (Super Admin)", "superadmin"),
            AdminUser("admin_default", "admin@animex.app", "مدير النظام", "admin")
        )
        try {
            db.collection("admins").snapshots()
                .map { snap -> 
                    val list = snap.toObjects(AdminUser::class.java)
                    if (list.isNotEmpty()) list else defaultAdmins
                }
                .catch { emit(defaultAdmins) }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(defaultAdmins)
        }
    }

    suspend fun addAdmin(email: String, role: String, displayName: String): Result<Unit> {
        return try {
            val id = "admin_${System.currentTimeMillis()}"
            val adminUser = AdminUser(
                userId = id,
                email = email,
                displayName = displayName,
                role = role,
                assignedAt = Timestamp.now()
            )
            db.collection("admins").document(id).set(adminUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add admin", e)
            Result.failure(e)
        }
    }

    suspend fun removeAdmin(adminId: String): Result<Unit> {
        return try {
            db.collection("admins").document(adminId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to remove admin", e)
            Result.failure(e)
        }
    }
}
