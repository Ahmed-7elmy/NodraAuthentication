package com.example.nodrah_project

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// At the top level of your file, create a DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "accessibility_settings")

class AccessibilitySettingsManager(private val context: Context) {

    private object PreferencesKeys {
        val LINE_HEIGHT = doublePreferencesKey("line_height")
        val FONT_SIZE = doublePreferencesKey("font_size")
        val LETTER_SPACING = doublePreferencesKey("letter_spacing")
        val IS_CONTRAST = booleanPreferencesKey("is_contrast")
        val IS_MONOCHROME = booleanPreferencesKey("is_monochrome")
        val USE_DYSLEXIC_FONT = booleanPreferencesKey("use_dyslexic_font")
        val CONTENT_SCALE = doublePreferencesKey("content_scale")

    }

    val accessibilitySettingsFlow: Flow<AccessibilitySettings> = context.dataStore.data
        .map { preferences ->
            AccessibilitySettings(
                lineHeight = preferences[PreferencesKeys.LINE_HEIGHT] ?: 24.0,
                fontSize = preferences[PreferencesKeys.FONT_SIZE] ?: 20.0,
                letterSpacing = preferences[PreferencesKeys.LETTER_SPACING] ?: 0.0,
                isContrast = preferences[PreferencesKeys.IS_CONTRAST] ?: false,
                isMonochrome = preferences[PreferencesKeys.IS_MONOCHROME] ?: false,
                useDyslexicFont = preferences[PreferencesKeys.USE_DYSLEXIC_FONT] ?: false,
                contentScale = preferences[PreferencesKeys.CONTENT_SCALE] ?: 400.0

            )
        }

    suspend fun saveSettings(settings: AccessibilitySettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LINE_HEIGHT] = settings.lineHeight
            preferences[PreferencesKeys.FONT_SIZE] = settings.fontSize
            preferences[PreferencesKeys.LETTER_SPACING] = settings.letterSpacing
            preferences[PreferencesKeys.IS_CONTRAST] = settings.isContrast
            preferences[PreferencesKeys.IS_MONOCHROME] = settings.isMonochrome
            preferences[PreferencesKeys.USE_DYSLEXIC_FONT] = settings.useDyslexicFont
            preferences[PreferencesKeys.CONTENT_SCALE] = settings.contentScale

        }
    }
}