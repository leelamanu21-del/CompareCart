package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthProvider {
    NONE,
    GUEST,
    PHONE,
    GOOGLE,
    EMAIL
}

data class UserSession(
    val isLoggedIn: Boolean = false,
    val isGuest: Boolean = false,
    val authProvider: AuthProvider = AuthProvider.NONE,
    val userName: String = "CompareCart Shopper",
    val userContact: String = "",
    val avatarInitials: String = "CC"
)

class UserSessionManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _session = MutableStateFlow(loadSession())
    val session: StateFlow<UserSession> = _session.asStateFlow()

    private fun loadSession(): UserSession {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val isGuest = prefs.getBoolean(KEY_IS_GUEST, false)
        val providerStr = prefs.getString(KEY_AUTH_PROVIDER, AuthProvider.NONE.name)
        val provider = try {
            AuthProvider.valueOf(providerStr ?: AuthProvider.NONE.name)
        } catch (_: Exception) {
            AuthProvider.NONE
        }
        val name = prefs.getString(KEY_USER_NAME, "CompareCart Shopper") ?: "CompareCart Shopper"
        val contact = prefs.getString(KEY_USER_CONTACT, "") ?: ""
        val initials = prefs.getString(KEY_AVATAR_INITIALS, "CC") ?: "CC"

        return UserSession(
            isLoggedIn = isLoggedIn,
            isGuest = isGuest,
            authProvider = provider,
            userName = name,
            userContact = contact,
            avatarInitials = initials
        )
    }

    fun hasCompletedWelcome(): Boolean {
        val s = _session.value
        return s.isLoggedIn || s.isGuest
    }

    fun loginWithPhone(phoneNumber: String, name: String = "Shopper") {
        val cleanPhone = phoneNumber.trim()
        val initials = if (cleanPhone.length >= 2) cleanPhone.takeLast(2) else "PH"
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_AUTH_PROVIDER, AuthProvider.PHONE.name)
            .putString(KEY_USER_NAME, if (name.isBlank()) "Shopper (${cleanPhone.takeLast(4)})" else name)
            .putString(KEY_USER_CONTACT, cleanPhone)
            .putString(KEY_AVATAR_INITIALS, initials)
            .apply()
        _session.value = loadSession()
    }

    fun loginWithGoogle(email: String, name: String) {
        val cleanEmail = email.trim()
        val displayName = name.ifBlank { cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() } }
        val initials = displayName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercase() }
            .joinToString("")
            .ifBlank { "G" }

        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_AUTH_PROVIDER, AuthProvider.GOOGLE.name)
            .putString(KEY_USER_NAME, displayName)
            .putString(KEY_USER_CONTACT, cleanEmail)
            .putString(KEY_AVATAR_INITIALS, initials)
            .apply()
        _session.value = loadSession()
    }

    fun loginWithEmail(email: String, name: String = "") {
        val cleanEmail = email.trim()
        val displayName = name.ifBlank { cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() } }
        val initials = displayName.take(2).uppercase()

        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_AUTH_PROVIDER, AuthProvider.EMAIL.name)
            .putString(KEY_USER_NAME, displayName)
            .putString(KEY_USER_CONTACT, cleanEmail)
            .putString(KEY_AVATAR_INITIALS, initials)
            .apply()
        _session.value = loadSession()
    }

    fun continueAsGuest() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putBoolean(KEY_IS_GUEST, true)
            .putString(KEY_AUTH_PROVIDER, AuthProvider.GUEST.name)
            .putString(KEY_USER_NAME, "Guest Shopper")
            .putString(KEY_USER_CONTACT, "Guest Access (No Cloud Sync)")
            .putString(KEY_AVATAR_INITIALS, "GS")
            .apply()
        _session.value = loadSession()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_AUTH_PROVIDER, AuthProvider.NONE.name)
            .putString(KEY_USER_NAME, "CompareCart Shopper")
            .putString(KEY_USER_CONTACT, "")
            .putString(KEY_AVATAR_INITIALS, "CC")
            .apply()
        _session.value = loadSession()
    }

    companion object {
        private const val PREFS_NAME = "comparecart_auth_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_GUEST = "is_guest"
        private const val KEY_AUTH_PROVIDER = "auth_provider"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_CONTACT = "user_contact"
        private const val KEY_AVATAR_INITIALS = "avatar_initials"

        @Volatile
        private var instance: UserSessionManager? = null

        fun getInstance(context: Context): UserSessionManager {
            return instance ?: synchronized(this) {
                instance ?: UserSessionManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
