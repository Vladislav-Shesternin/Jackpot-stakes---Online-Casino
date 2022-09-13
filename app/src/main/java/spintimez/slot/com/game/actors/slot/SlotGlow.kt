package spintimez.slot.com.game.actors.slot

import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.R
import spintimez.slot.com.game.util.Group
import spintimez.slot.com.game.util.Layout
import spintimez.slot.com.game.util.setBounds
import spintimez.slot.com.util.toMS
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import spintimez.slot.com.game.util.Layout.Glow as LG

class SlotGlow(override val activity: FragmentActivity) : Group(activity) {

    companion object {
        const val GLOW_ITEM_COUNT = 4
    }

    private val glowItemImageList = List(GLOW_ITEM_COUNT) { ImageView(activity) }



    override fun ViewGroup.addActorsOnGroup() {
        addGlowItemList()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------
    private fun ViewGroup.addGlowItemList() {
        var newY = LG.item.y
        glowItemImageList.onEach { image ->
            addView(image)
            image.alpha = 0f
            image.setImageResource(R.drawable.glow)
            image.setBounds(LG.item.x, newY, LG.item.w, LG.item.h)
            newY += LG.item.h + LG.item.vs
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------
    suspend fun glowIn(glowItemIndex: Int, time: Float = 0f) = CompletableDeferred<Boolean>().also { continuation ->
        glowItemImageList[glowItemIndex].animate()
            .setDuration(time.toMS)
            .alpha(1f)
            .withEndAction { continuation.complete(true) }
            .start()
    }.await()

    suspend fun glowOut(glowItemIndex: Int, time: Float = 0f) = CompletableDeferred<Boolean>().also { continuation ->
        glowItemImageList[glowItemIndex].animate()
            .setDuration(time.toMS)
            .alpha(0f)
            .withEndAction { continuation.complete(true) }
            .start()
    }.await()

    suspend fun glowInAll(time: Float = 0f, timeBetween: Float = 0f) {
        val completableList = List(glowItemImageList.size) { CompletableDeferred<Boolean>() }

        glowItemImageList.onEachIndexed { index, glow ->
            glow.animate()
                .setDuration(time.toMS)
                .alpha(1f)
                .withEndAction { completableList[index].complete(true) }
                .start()
            delay(timeBetween.toMS)
        }
        completableList.onEach { it.await() }
    }

    suspend fun glowOutAll(time: Float = 0f, timeBetween: Float = 0f) {
        val completableList = List(glowItemImageList.size) { CompletableDeferred<Boolean>() }

        glowItemImageList.onEachIndexed { index, glow ->
            glow.animate()
                .setDuration(time.toMS)
                .alpha(0f)
                .withEndAction { completableList[index].complete(true) }
                .start()
            delay(timeBetween.toMS)
        }

        completableList.onEach { it.await() }
    }
}