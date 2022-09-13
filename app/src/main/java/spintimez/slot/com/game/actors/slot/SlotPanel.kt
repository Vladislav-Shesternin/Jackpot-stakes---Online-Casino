package spintimez.slot.com.game.actors.slot

import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.R
import spintimez.slot.com.game.util.*
import spintimez.slot.com.util.log
import spintimez.slot.com.util.toMS
import kotlinx.coroutines.*
import spintimez.slot.com.game.util.Layout.SlotPanel as LSP

class SlotPanel(
    override val activity: FragmentActivity
) : Group(activity) {

    companion object {
        const val SLOT_COUNT = 3
        const val GLOW_COUNT = 3

        const val TIME_BETWEEN_SPIN_SLOT = 0.3f
        const val TIME_WAIT_AFTER_SHOW_WIN = 1f
        const val TIME_GLOW_IN = 0.5f
        const val TIME_GLOW_OUT = 0.5f
        const val TIME_BETWEEN_GLOW_IN = 0.3f
    }

    private val maskImage       = ImageView(activity)
    private val slotList        = List(SLOT_COUNT) { Slot(activity) }
    private val glowList        = List(GLOW_COUNT) { SlotGlow(activity) }

    private var winNumber      = (1..5).shuffled().first()
    private var spinWinCounter = 0

    private val fillManager by lazy { SlotFillManager(slotList) }



    override fun ViewGroup.addActorsOnGroup() {
        addGlowList()
        addSlotList()
        addMaskImage()
    }

    override fun dispose() {
        super.dispose()
        slotList.onEach { it.dispose() }
    }


    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun ViewGroup.addMaskImage() {
        addView(maskImage)
        maskImage.apply {
            setImageResource(R.drawable.mask)
            setBounds(LSP.mask.x, LSP.mask.y, LSP.mask.w, LSP.mask.h)
        }
    }

    private fun ViewGroup.addSlotList() {
        var newX = LSP.slot.x

        slotList.onEach { slot ->
            addView(slot.rootViewGroup)
            slot.rootViewGroup.setBounds(newX, LSP.slot.y, LSP.slot.w, LSP.slot.h)
            slot.initialize()
            newX += LSP.slot.w + LSP.slot.hs
        }
    }

    private fun ViewGroup.addGlowList() {
        var newX = LSP.glow.x

        glowList.onEach { glow ->
            addView(glow.rootViewGroup)
            glow.rootViewGroup.setBounds(newX, LSP.glow.y, LSP.glow.w, LSP.glow.h)
            glow.initialize()
            newX += LSP.glow.w + LSP.glow.hs
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------
    suspend fun spin() = CompletableDeferred<SpinResult>().also { continuation ->
        spinWinCounter++
        logCounterWin()
        fillSlots()

        val completableList = List(SLOT_COUNT) { CompletableDeferred<Boolean>() }

        slotList.onEachIndexed { index, slot ->
            CoroutineScope(Dispatchers.Main).launch {
                slot.spin()
                completableList[index].complete(true)
            }
            delay(TIME_BETWEEN_SPIN_SLOT.toMS)
        }
        completableList.onEach { it.await() }

        val winSlotItemSet = fillManager.winFillResult?.apply {
            showWin()
            delay(TIME_WAIT_AFTER_SHOW_WIN.toMS)
            hideWin()
        }?.winSlotItemList?.toSet()

        continuation.complete(SpinResult(winSlotItemSet?.apply { resetWin() }))

    }.await()

    private fun resetWin() {
        spinWinCounter = 0
        winNumber      = (1..5).shuffled().first()
    }

    private fun logCounterWin() {
        log("spinWinCounter = $spinWinCounter | winNumber = $winNumber")
    }

    private fun fillSlots() {
        when (spinWinCounter) {
            winNumber -> fillManager.fill(FillStrategy.WIN)
            else      -> fillManager.fill(FillStrategy.MIX)
        }
    }

    private suspend fun FillResult.showWin() {
        val completableList = List(intersectionList.size) { CompletableDeferred<Boolean>() }

        intersectionList.onEachIndexed { index, intersection ->
            CoroutineScope(Dispatchers.Main).launch {
                glowList[intersection.slotIndex].glowIn(intersection.rowIndex, TIME_GLOW_IN)
                completableList[index].complete(true)
            }
            delay(TIME_BETWEEN_GLOW_IN.toMS)
        }

        completableList.onEach { it.await() }
    }

    private fun hideWin() {
        glowList.onEach { CoroutineScope(Dispatchers.Main).launch { it.glowOutAll(TIME_GLOW_OUT) } }
    }

}