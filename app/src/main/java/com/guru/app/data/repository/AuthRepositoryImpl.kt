package com.guru.app.data.repository

import com.guru.app.data.database.dao.UserProfileDao
import com.guru.app.data.database.entity.UserProfileEntity
import com.guru.app.data.datastore.UserPreferencesRepository
import com.guru.app.domain.model.UserProfile
import com.guru.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val userPreferences: UserPreferencesRepository
) : AuthRepository {

    override fun getUserProfile(): Flow<UserProfile> {
        return userProfileDao.getUserProfile().map { entity ->
            entity?.toDomain() ?: UserProfile()
        }
    }

    override suspend fun login(email: String, pass: String): Boolean {
        // Local auth validation for Phase 1
        if (email.isNotBlank() && pass.length >= 4) {
            val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
            val profile = UserProfile(username = name, email = email)
            userProfileDao.insertOrUpdateProfile(UserProfileEntity.fromDomain(profile))
            userPreferences.setLoggedIn(true, email, name)
            return true
        }
        return false
    }

    override suspend fun signup(name: String, email: String, pass: String): Boolean {
        if (name.isNotBlank() && email.isNotBlank() && pass.length >= 4) {
            val profile = UserProfile(username = name, email = email)
            userProfileDao.insertOrUpdateProfile(UserProfileEntity.fromDomain(profile))
            userPreferences.setLoggedIn(true, email, name)
            return true
        }
        return false
    }

    override suspend fun loginAsGuest(): Boolean {
        val guestProfile = UserProfile(username = "Guest Scholar", email = "guest@guru.app")
        userProfileDao.insertOrUpdateProfile(UserProfileEntity.fromDomain(guestProfile))
        userPreferences.setLoggedIn(true, "guest@guru.app", "Guest Scholar")
        return true
    }

    override suspend fun logout() {
        userPreferences.clearSession()
    }

    override suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(UserProfileEntity.fromDomain(profile))
    }
}
