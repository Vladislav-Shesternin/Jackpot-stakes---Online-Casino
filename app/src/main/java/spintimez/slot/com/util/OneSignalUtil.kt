package spintimez.slot.com.util

import android.content.Context
import com.onesignal.OneSignal

object OneSignalUtil {

    fun initialize(context: Context) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(context)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
    }

}