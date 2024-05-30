package com.islamelmrabet.cookconnect.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.islamelmrabet.cookconnect.model.localModels.UserPreferences
import com.islamelmrabet.cookconnect.preferences.AppPreferences
import kotlinx.coroutines.launch

/**
 * Class PreferencesViewModel
 *
 * @constructor
 * TODO
 *
 * @param application
 */
class PreferencesViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = AppPreferences(application.applicationContext)

    private val _isFirstTime = MutableLiveData<Boolean>()
    val firstTime: LiveData<Boolean> = _isFirstTime

    /**
     * TODO
     *
     * @param isFirstTime
     */
    fun onUserNameChanged(isFirstTime: Boolean) {
        _isFirstTime.value = isFirstTime
    }

    /**
     * TODO
     *
     * @param isFirstTime
     */
    fun saveUser(isFirstTime: Boolean) {
        viewModelScope.launch {
            preferences.saveUser(UserPreferences(isFirstTime))
        }
    }

    /**
     * TODO
     *
     * @param onCollected
     */
    suspend fun isDataStored(onCollected: (Boolean) -> Unit) {
        viewModelScope.launch() {
            preferences.isDataStored().collect {
                onCollected(it)
            }
        }
    }

}

