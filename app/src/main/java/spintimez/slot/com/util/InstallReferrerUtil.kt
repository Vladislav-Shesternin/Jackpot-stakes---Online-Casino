package spintimez.slot.com.util

import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import spintimez.slot.com.appContext

object InstallReferrerUtil {

    private val referrerClient = InstallReferrerClient.newBuilder(appContext).build()

    suspend fun startConnection() = CompletableDeferred<ReferrerDetails>().also { continuation ->
        val restartFlow = MutableStateFlow(0)

        CoroutineScope(Dispatchers.IO).launch {
            restartFlow.collect {
                referrerClient.startConnection(object : InstallReferrerStateListener {
                    override fun onInstallReferrerSetupFinished(responseCode: Int) {
                        when(responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK ->{
                                cancel()
                                continuation.complete(referrerClient.installReferrer)
                            }
                        }
                    }

                    override fun onInstallReferrerServiceDisconnected() {
                        restartFlow.value++
                    }
                })
            }
        }

    }.await()

}


