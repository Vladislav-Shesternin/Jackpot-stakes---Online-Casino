package spintimez.slot.com.game.manager

import android.app.Activity
import spintimez.slot.com.game.util.Screen
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.system.exitProcess

object NavigationManager {

    private val backStack = mutableListOf<Screen>()

    val screenFlow = MutableSharedFlow<Screen>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun navigate(to: Screen, from: Screen? = null) {
        screenFlow.tryEmit(to)

        backStack.remove(to)

        from?.let {
            backStack.remove(it)
            backStack.add(it)
        }
    }

    fun back(activity: Activity) {
        if (backStack.isEmpty()) exit(activity)
        else screenFlow.tryEmit(backStack.removeLast())
    }

    fun exit(activity: Activity) {
        activity.finishAndRemoveTask()
        exitProcess(0)
    }

    fun clearAllBackstack() {
        backStack.clear()
    }

}