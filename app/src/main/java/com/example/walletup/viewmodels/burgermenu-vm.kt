package com.example.walletup.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walletup.MainActivity
import com.example.walletup.removeScheduledNotification
import com.example.walletup.scheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BurgerMenuViewModel @Inject constructor (
    val dataStore: DataStore<Preferences>,
    @ApplicationContext val context: Context,
): ViewModel() {
    val allowNotificationsKey = booleanPreferencesKey("AllowNotifications")
    val allowWidgetKey = booleanPreferencesKey("AllowWidget")

    fun changeAllowNotifications(
        allow: Boolean = false
    ) {
        viewModelScope.launch {
            dataStore.edit {
                it[allowNotificationsKey] = allow
                MainActivity.allowNotifications = allow
            }
            if (allow) {
                scheduleNotification(context, 5, TimeUnit.MINUTES)
            } else {
                removeScheduledNotification(context)
            }
        }
    }

    fun changeAllowWidget(
        allow: Boolean = false
    ) {
        viewModelScope.launch {
            dataStore.edit {
                it[allowWidgetKey] = allow
                MainActivity.allowWidget = allow
            }
        }
    }
}