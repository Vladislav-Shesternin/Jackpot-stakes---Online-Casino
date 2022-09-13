package spintimez.slot.com

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import spintimez.slot.com.databinding.ActivityMainBinding
import spintimez.slot.com.util.*
import spintimez.slot.com.util.manager.DataStoreManager
import spintimez.slot.com.util.network.Network
import spintimez.slot.com.webView.webViewFragment
import com.android.installreferrer.api.ReferrerDetails
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var binding      : ActivityMainBinding
        lateinit var navController: NavController

        @IdRes
        var startFragmentId = R.id.webViewFragment
            private set
        var webViewURL = "google.com"

        private val coroutineLoader = CoroutineScope(Dispatchers.Default)

        fun showLoader() {
            coroutineLoader.launch(Dispatchers.Main) { binding.lottie.apply {
                isVisible = true
                playAnimation()
            } }
        }

        fun hideLoader() {
            coroutineLoader.launch(Dispatchers.Main) { binding.lottie.apply {
                isVisible = false
                cancelAnimation()
            } }
        }

    }

    private var referrerDetails: ReferrerDetails? = null
    private val coroutineMain                     = CoroutineScope(Dispatchers.Main)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
        showLoader()

        coroutineMain.launch {
            withContext(Dispatchers.IO) {
                DataStoreManager.AdvertisingId.update { AdvertisingIdClient.getAdvertisingIdInfo(this@MainActivity).id ?: "" }
                OneSignalUtil.initialize(this@MainActivity)
                AppsflyerUtil.initialize(this@MainActivity)
                InstallReferrerUtil.startConnection().also { referrerDetails = it }

                callServer()
            }

            when (startFragmentId) {
                R.id.gameFragment    -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                R.id.webViewFragment -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            }
            setStartDestination(startFragmentId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webViewFragment?.onActivityResult(requestCode, resultCode, data)
    }




    private suspend fun checkServerResult(json: JSONObject) {
        try {
            when {
                json.isNull("user")           -> {
                    startFragmentId = R.id.gameFragment
                    log("json.isNull(user)")
                }
                json.getBoolean("user").not() -> {
                    logEvent(this, "user_false")
                    startFragmentId = R.id.gameFragment
                    OneSignal.setSubscription(false)
                    log("json.getBoolean(user).not()")
                }
                else                          -> {
                    val visitorId = json.getString("visitor_id")
                    OneSignal.setExternalUserId(visitorId)
                    DataStoreManager.VisitorId.update { visitorId }

                    val requestBody = JSONObject(
                        mapOf(
                            "bundle_id" to BuildConfig.APPLICATION_ID,
                            "visitor_id" to visitorId
                        )
                    )

                    Network.postJsonObject(Network.UrlPost.DATA, requestBody).also {
                        when (it) {
                            is Network.VolleyResult.VolleyJSONObject -> {
                                log("SUCCESS DATA: $it")
                            }
                            is Network.VolleyResult.VolleyError      -> {
                                log("SUCCESS DATA error: $it")
                                val resultTxt = it.toString()
                                val bodyTxt = resultTxt.substring(resultTxt.indexOf("[") + 1, resultTxt.lastIndexOf("]"))
                                log("bodyTxt = $bodyTxt")

                                val jsonObject = JSONObject(bodyTxt)
                                val productUrl = jsonObject.getString("product_url")
                                log("productUrl = $productUrl")

                                webViewURL = productUrl
                            }
                        }
                    }

                }
            }
        } catch (e: Exception) {
            log("EXCEPTION: $e")
            startFragmentId = R.id.gameFragment
        }
    }

    private suspend fun callServer() {
        val advertisingId      = DataStoreManager.AdvertisingId.get() ?: ""
        val appsflyerDeviceId  = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        val campaign           = AppsflyerUtil.conversionData?.getOrDefault("campaign", "null").toString()
        var utm_source         = "null"
        var utm_medium         = "null"

        referrerDetails?.installReferrer?.apply {
            log(this)
            utm_source = substring(indexOf('=') + 1, indexOf('&'))
            utm_medium = substring(lastIndexOf('=') + 1, length)
        }
        log("utm_source = $utm_source | utm_medium = $utm_medium")

        try {

            val requestBody = JSONObject(
                mapOf<String, String>(
                    "bundle_id"           to BuildConfig.APPLICATION_ID,
                    "advertising_id"      to advertisingId,
                    "appsflyer_device_id" to appsflyerDeviceId,
                    "campaign"            to campaign,
                    "utm_source"          to utm_source,
                    "utm_medium"          to utm_medium,
                )
            )
            Network.postJsonObject(Network.UrlPost.CHECK, requestBody).also {
                when (it) {
                    is Network.VolleyResult.VolleyJSONObject -> {
                        log("SUCCESS CHECK: $it")
                        checkServerResult(it.jsonObject)
                    }
                    is Network.VolleyResult.VolleyError      -> {
                        log("ERROR CHECK: $it")
                        throw Exception()
                    }
                }
            }

        } catch (e: Exception) {
            log("EXCEPTION: $e")
            startFragmentId = R.id.gameFragment
        }
    }


    private fun initialize() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment)
    }



    private fun setStartDestination(
        @IdRes destinationId: Int,
        args: Bundle? = null
    ) {
        with(navController) {
            navInflater.inflate(R.navigation.nav_graph).apply { setStartDestination(destinationId) }.also { setGraph(it, args) }
        }
    }
}