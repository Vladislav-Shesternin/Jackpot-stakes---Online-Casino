package spintimez.slot.com.game.util

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.util.cancelCoroutinesAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class Group(open val activity: FragmentActivity): Disposable {

    val coroutineMain = CoroutineScope(Dispatchers.Main)

    open val rootViewGroup: ViewGroup by lazy { FrameLayout(activity).apply { id = View.generateViewId() } }


    abstract fun ViewGroup.addActorsOnGroup()

    open fun initialize() {
        coroutineMain.launch {
            rootViewGroup.initializeSize()
            rootViewGroup.addActorsOnGroup()
        }
    }

    override fun dispose() {
        cancelCoroutinesAll(coroutineMain)
    }

}