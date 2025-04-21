package com.trivaris.votechain.models.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class Preference<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T
)

object PreferenceKeys {
    val DARK_MODE = Preference(
        key     = booleanPreferencesKey("dark_mode_enabled"),
        defaultValue = false
    )
    val DYNAMIC_COLOR = Preference(
        key     = booleanPreferencesKey("dynamic_color_enabled"),
        defaultValue = false
    )
    val USERNAME = Preference(
        key     = stringPreferencesKey("username"),
        defaultValue = "Trivaris"
    )
}