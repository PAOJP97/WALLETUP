package com.example.walletup.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walletup.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (
    val dataStore: DataStore<Preferences>
): ViewModel() {
    val allowNotificationsKey = booleanPreferencesKey("AllowNotifications")
    val allowWidgetKey = booleanPreferencesKey("AllowWidget")

    fun loadPreferences() {
        viewModelScope.launch {
            dataStore.edit {
                MainActivity.allowNotifications = it[allowNotificationsKey] == true
                MainActivity.allowWidget = it[allowWidgetKey] == true
            }
        }
    }

}