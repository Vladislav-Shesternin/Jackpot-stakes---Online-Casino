package spintimez.slot.com

import android.app.Application
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import spintimez.slot.com.game.util.DENSITY
import spintimez.slot.com.game.util.GameColor
import spintimez.slot.com.game.util.Layout
import kotlin.system.exitProcess

lateinit var appContext: Context

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}
