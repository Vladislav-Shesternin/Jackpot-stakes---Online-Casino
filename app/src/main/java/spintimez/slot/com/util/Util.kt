package spintimez.slot.com.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.appsflyer.AppsFlyerLib
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import spintimez.slot.com.util.manager.DataStoreManager
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.random.Random

val Number.length: Int get() = toString().length

val Float.toMS: Long get() = (this * 1000).toLong()



fun log(message: String) {
    Log.i("VLAD", message)
}

fun cancelCoroutinesAll(vararg coroutine: CoroutineScope) {
    coroutine.forEach { it.cancel() }
}

fun probability(percent: Int, block: () -> Unit = {}): Boolean {
    val randomNum = (0..100).shuffled().first()
    return if (randomNum <= percent) {
        block()
        true
    } else false
}

fun FirebaseAnalytics.logFragment(fragment : Fragment){
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, fragment.javaClass.simpleName)
    bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, fragment.javaClass.simpleName)
    logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
}

fun logEvent(context : Context, eventName: String){
    CoroutineScope(Dispatchers.IO).launch {
        // log firebase
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName)
        FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle)


        //log appsflyer
        val eventValues = HashMap<String, Any>()
        eventValues["af_visitor_id"] = DataStoreManager.VisitorId.get() ?: "no_visitor_id"
        AppsFlyerLib.getInstance().logEvent(context, eventName, eventValues)
    }
}

interface DataStoreElement<T>{
    suspend fun collect(block: suspend (T?) -> Unit)

    suspend fun update(block: suspend (T?) -> T)

    suspend fun get(): T?
}