package spintimez.slot.com.util.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import spintimez.slot.com.appContext
import spintimez.slot.com.util.DataStoreElement
import kotlinx.coroutines.flow.first

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DATA_STORE")

    private val ADVERTISING_ID_KEY = stringPreferencesKey("advertising_id_key")
    private val VISITOR_ID_KEY     = stringPreferencesKey("visitor_id_key")



    object AdvertisingId: DataStoreElement<String> {
        override suspend fun collect(block: suspend (String?) -> Unit) {
            appContext.dataStore.data.collect { block(it[ADVERTISING_ID_KEY]) }
        }

        override suspend fun update(block: suspend (String?) -> String) {
            appContext.dataStore.edit { it[ADVERTISING_ID_KEY] = block(it[ADVERTISING_ID_KEY]) }
        }

        override suspend fun get(): String? {
            return appContext.dataStore.data.first()[ADVERTISING_ID_KEY]
        }
    }

    object VisitorId: DataStoreElement<String> {
        override suspend fun collect(block: suspend (String?) -> Unit) {
            appContext.dataStore.data.collect { block(it[VISITOR_ID_KEY]) }
        }

        override suspend fun update(block: suspend (String?) -> String) {
            appContext.dataStore.edit { it[VISITOR_ID_KEY] = block(it[VISITOR_ID_KEY]) }
        }

        override suspend fun get(): String? {
            return appContext.dataStore.data.first()[VISITOR_ID_KEY]
        }
    }

}

