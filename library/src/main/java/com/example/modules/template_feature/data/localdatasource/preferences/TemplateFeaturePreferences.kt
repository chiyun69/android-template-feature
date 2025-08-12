package com.example.modules.template_feature.data.localdatasource.preferences

import android.content.Context
import android.content.SharedPreferences

class TemplateFeaturePreferences(
    context: Context
) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    
    fun setLastSyncTime(timestamp: Long) {
        preferences.edit().putLong(KEY_LAST_SYNC_TIME, timestamp).apply()
    }
    
    fun getLastSyncTime(): Long {
        return preferences.getLong(KEY_LAST_SYNC_TIME, 0L)
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }
    
    fun areNotificationsEnabled(): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }
    
    fun setFirstLaunch(isFirstLaunch: Boolean) {
        preferences.edit().putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch).apply()
    }
    
    fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }
    
    fun clearAllPreferences() {
        preferences.edit().clear().apply()
    }
    
    companion object {
        private const val PREFERENCES_NAME = "template_feature_preferences"
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }
}