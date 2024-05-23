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

class AppPreferences(val context: Context) {
    companion object {
        val isFirstTime = booleanPreferencesKey("NAME")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    /**
     * TODO
     *
     * @param user
     */
    suspend fun saveUser(user: UserPreferences) {
        context.dataStore.edit { preferences ->
            preferences[isFirstTime] = user.isFirstTime
        }
    }

    /**
     * TODO
     *
     * @return isFirstTime
     */
    fun isDataStored(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences.contains(isFirstTime)
    }
}
