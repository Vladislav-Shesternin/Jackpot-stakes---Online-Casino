package spintimez.slot.com.game.util

import android.view.View
import android.view.ViewTreeObserver
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import spintimez.slot.com.util.toMS

private val figmaDimensionsMap = mutableMapOf<String, Float>()

var View.figmaW: Float
    get() = figmaDimensionsMap["W$id"] ?: 0f
    set(value) { figmaDimensionsMap["W$id"] = value }

var View.figmaH: Float
    get() = figmaDimensionsMap["H$id"] ?: 0f
    set(value) { figmaDimensionsMap["H$id"] = value }

suspend fun View.initializeSize() = CompletableDeferred<Unit>().also { continuation ->
    if (width.toFloat() != 0f && height.toFloat() != 0f) continuation.complete(Unit)
    else viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            continuation.complete(Unit)
        }
    })
}.await()

fun View.setSize(width: Float, height: Float) {
    figmaW = width
    figmaH = height
    Viewport2.setSize((parent as View),this, width, height)
}

fun View.setPosition(x: Float, y: Float) {
    Viewport2.setPosition((parent as View), this, x, y)
}

fun View.setBounds(x: Float, y: Float, width: Float, height: Float) {
    setPosition(x, y)
    setSize(width, height)
}



fun View.animationInfinityFlyRotate(scope: CoroutineScope, angle: Float, time: Float) {
    scope.launch {
        MutableStateFlow(0).apply {
            collect {
                animate().apply {
                    rotation(angle)
                    duration = time.toMS

                    withEndAction {
                        animate().apply {
                            rotation(-angle)
                            duration = time.toMS

                            withEndAction { value++ }
                        }
                    }
                }
            }
        }
    }
}