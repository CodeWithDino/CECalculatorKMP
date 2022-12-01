package dev.dk.currency.exchange.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath

class AppDataStore(private val dataStore: DataStore<Preferences>) {

    private val scope = CoroutineScope(Dispatchers.Default)

    val lasSuccessTimeStamp: Flow<Long> = getLong(LAST_SUCCESS_TIMESTAMP)
    fun updateLastSuccessTimeStamp(value: Long) = saveToDatastore(LAST_SUCCESS_TIMESTAMP, value)

    private fun getLong(key: Preferences.Key<Long>): Flow<Long> = dataStore.data.map { pref ->
        pref[key] ?: 0L
    }

    private fun <T> saveToDatastore(key: Preferences.Key<T>, value: T) = scope.launch {
        dataStore.edit { settings ->
            settings[key] = value
        }
    }

    companion object {
        val LAST_SUCCESS_TIMESTAMP = longPreferencesKey("LAST_SUCCESS_TIMESTAMP")
    }
}

fun createMainDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        produceFile = { producePath().toPath() },
    )
}

internal const val dataStoreFileName = "currencyconversion.preferences_pb"