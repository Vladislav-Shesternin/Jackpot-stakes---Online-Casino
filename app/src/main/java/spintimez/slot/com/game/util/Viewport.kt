package spintimez.slot.com.game.util

import android.view.View
import androidx.core.view.updateLayoutParams
import spintimez.slot.com.util.log

class Viewport(parent: View) {

    private val figmaOnePercentX = parent.figmaW / 100f
    private val figmaOnePercentY = parent.figmaH / 100f

    private val gameOnePercentX = parent.width / 100f
    private val gameOnePercentY = parent.height / 100f



    private fun getFigmaPercentX(x: Float) = x / figmaOnePercentX
    private fun getFigmaPercentY(y: Float) = y / figmaOnePercentY

    fun getX(x: Float): Float = getFigmaPercentX(x) * gameOnePercentX
    fun getY(y: Float): Float = getFigmaPercentY(y) * gameOnePercentY

    fun setPosition(view: View, x: Float, y: Float) {
        view.x = getX(x)
        view.y = getY(y)
    }

    fun setSize(view: View, width: Float, height: Float) {
        view.updateLayoutParams {
            this.width  = getX(width).toInt()
            this.height = getY(height).toInt()
        }
    }

}

object Viewport2 {

    private lateinit var parent: View

    private val figmaOnePercentX get() = parent.figmaW / 100f
    private val figmaOnePercentY get() = parent.figmaH / 100f

    private val gameOnePercentX get() = parent.width / 100f
    private val gameOnePercentY get() = parent.height / 100f



    private fun getFigmaPercentX(x: Float) = x / figmaOnePercentX
    private fun getFigmaPercentY(y: Float) = y / figmaOnePercentY

    private fun getX(x: Float): Float = getFigmaPercentX(x) * gameOnePercentX
    private fun getY(y: Float): Float = getFigmaPercentY(y) * gameOnePercentY

    fun setPosition(parent: View, view: View, x: Float, y: Float) {
        this.parent = parent

        view.x = getX(x)
        view.y = getY(y)
    }

    fun setSize(parent: View, view: View, width: Float, height: Float) {
        this.parent = parent

        view.updateLayoutParams {
            this.width  = getX(width).toInt()
            this.height = getY(height).toInt()
        }
    }

}

