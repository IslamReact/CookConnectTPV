package com.islamelmrabet.cookconnect.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.islamelmrabet.cookconnect.model.localModels.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class: AppPreferences.
 *
 * @property context
 */
class AppPreferences(val context: Context) {
    companion object {
        val isFirstTime = booleanPreferencesKey("NAME")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    /**
     * Save user in dataStore
     *
     * @param user
     */
    suspend fun saveUser(user: UserPreferences) {
        context.dataStore.edit { preferences ->
            preferences[isFirstTime] = user.isFirstTime
        }
    }

    /**
     * Returns the value of attribute in dataStore
     *
     * @return isFirstTime
     */
    fun isDataStored(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences.contains(isFirstTime)
    }
}
