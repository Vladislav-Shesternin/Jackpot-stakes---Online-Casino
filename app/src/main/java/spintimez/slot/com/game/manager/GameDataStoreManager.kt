package spintimez.slot.com.game.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import spintimez.slot.com.appContext
import spintimez.slot.com.util.DataStoreElement
import kotlinx.coroutines.flow.first

object GameDataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "GAME_DATA_STORE")

    private val BALANCE_KEY = longPreferencesKey("balance_key")



    object Balance: DataStoreElement<Long> {
        override suspend fun collect(block: suspend (Long?) -> Unit) {
            appContext.dataStore.data.collect { block(it[BALANCE_KEY] ?: 50_000L) }
        }

        override suspend fun update(block: suspend (Long?) -> Long) {
            appContext.dataStore.edit { it[BALANCE_KEY] = block(it[BALANCE_KEY] ?: 50_000L) }
        }

        override suspend fun get(): Long? {
            return appContext.dataStore.data.first()[BALANCE_KEY] ?: 50_000L
        }
    }

}

