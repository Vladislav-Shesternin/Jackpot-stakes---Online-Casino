package spintimez.slot.com.util

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import spintimez.slot.com.util.log
import java.util.Objects.requireNonNull

object AppsflyerUtil {

    private const val APPSFLYER_DEV_KEY = "iqu4KZGw8eqAEu85xnRnXD"

    var conversionData: Map<String, Any>? = null
        private set


    private val startAppsflyerRequestListener = object : AppsFlyerRequestListener {
        override fun onSuccess() {
            log("Launch sent successfully")
        }

        override fun onError(errorCode: Int, errorDesc: String) {
            log("""
                Launch failed to be sent:
                Error code: $errorCode
                Error description: $errorDesc
           """.trimIndent()
            )
        }

    }

    private val conversionListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(conversionDataMap: Map<String, Any>) {
            for (attrName in conversionDataMap.keys) log("Conversion attribute: $attrName = ${conversionDataMap[attrName]}")
            val status = requireNonNull(conversionDataMap["af_status"]).toString()
            if (status == "Non-organic") {
                if (requireNonNull(conversionDataMap["is_first_launch"]).toString() == "true") {
                    log("Conversion: First Launch")
                } else {
                    log("Conversion: Not First Launch")
                }
            } else {
                log("Conversion: This is an organic install.")
            }
            conversionData = conversionDataMap
        }

        override fun onConversionDataFail(errorMessage: String) {
            log("error getting conversion data: $errorMessage")
        }

        override fun onAppOpenAttribution(attributionData: Map<String, String>) {
            log("onAppOpenAttribution: This is fake call.")
        }

        override fun onAttributionFailure(errorMessage: String) {
            log("error onAttributionFailure : $errorMessage")
        }
    }



    fun initialize(context: Context) {
        with(AppsFlyerLib.getInstance()) {
            setDebugLog(true)
            setMinTimeBetweenSessions(0)
            init(APPSFLYER_DEV_KEY, conversionListener, context)
            start(context, APPSFLYER_DEV_KEY, startAppsflyerRequestListener)
        }
    }

}