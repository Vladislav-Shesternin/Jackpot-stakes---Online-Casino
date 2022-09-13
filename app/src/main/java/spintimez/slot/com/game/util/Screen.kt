package spintimez.slot.com.game.util

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.game.manager.NavigationManager
import spintimez.slot.com.util.cancelCoroutinesAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class Screen(
    open val activity: FragmentActivity,
    val ratio: String = "2:1",
    val figmaW: Float = 1400f,
    val figmaH: Float = 700f,
) {

    open val rootViewGroup: ViewGroup by lazy { FrameLayout(activity) }

    val coroutineMain = CoroutineScope(Dispatchers.Main)

    val disposableList = mutableListOf<Disposable>()



    open fun show(parent: ConstraintLayout) {
        onBackPressed()
        parent.addView(rootViewGroup)

        rootViewGroup.apply {
            id           = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(0, 0)
            this.figmaW  = this@Screen.figmaW
            this.figmaH  = this@Screen.figmaH
        }

        parent.apply {
            removeAllViews()
            addView(rootViewGroup)
        }

        ConstraintSet().apply {
            clone(parent)

            val rootId   = rootViewGroup.id
            val parentId = parent.id

            connect(rootId, ConstraintSet.START, parentId, ConstraintSet.START)
            connect(rootId, ConstraintSet.END, parentId, ConstraintSet.END)
            connect(rootId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
            connect(rootId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)

            setDimensionRatio(rootId, ratio)

            applyTo(parent)
        }

        coroutineMain.launch {
            rootViewGroup.initializeSize()
            rootViewGroup.addActorsOnRootViewGroup()
        }
    }

    open fun hide(parent: ConstraintLayout) {
        disposableList.onEach { it.dispose() }
        cancelCoroutinesAll(coroutineMain)
        parent.removeView(rootViewGroup)
    }

    open fun onBackPressed() {
        activity.onBackPressedDispatcher.addCallback(activity) {
            NavigationManager.back(activity)
        }
    }

    abstract fun ViewGroup.addActorsOnRootViewGroup()

}