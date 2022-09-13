package spintimez.slot.com.game.actors.slot

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.fragment.app.FragmentActivity
import spintimez.slot.com.game.util.*
import spintimez.slot.com.util.toMS
import kotlinx.coroutines.CompletableDeferred
import spintimez.slot.com.game.util.Layout.Slot as LS

class Slot(override val activity: FragmentActivity): Group(activity) {

    companion object {
        const val SLOT_ITEM_VISIBLE_COUNT      = 4
        const val SLOT_ITEM_INTERMEDIATE_COUNT = 52

        const val TIME_SPIN_SLOT = 4f
    }

    private val intermediateSlotItemImageList = List(SLOT_ITEM_INTERMEDIATE_COUNT) { ImageView(activity) }
    private val winSlotItemImageList          = List(SLOT_ITEM_VISIBLE_COUNT) { ImageView(activity) }
    private val fakeSlotItemImageList         = List(SLOT_ITEM_VISIBLE_COUNT) { ImageView(activity) }

    private val slotItemImageList: List<ImageView> by lazy { generateSlotItemImageList() }

    private val viewport by lazy { Viewport(rootViewGroup.parent as View) }

    var slotItemWinList = listOf<SlotItem>()
        set(value) {
            field = value
            value.onEachIndexed { index, slotItem -> winSlotItemImageList[index].setImageResource(slotItem.resId) }
        }



    override fun ViewGroup.addActorsOnGroup() {
        addSlotItemList()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------
    private fun ViewGroup.addSlotItemList() {
        var newY = LS.item.y
        slotItemImageList.onEach { slotItemImage ->
            addView(slotItemImage)
            slotItemImage.setBounds(LS.item.x, newY, LS.item.w, LS.item.h)
            newY += LS.item.h + LS.item.vs
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------
    private fun generateSlotItemImageList() = mutableListOf<ImageView>().apply {
        addAll(generateWinItems())
        addAll(generateIntermediateItems())
        addAll(generateFakeItems())
    }

    private fun generateIntermediateItems() = intermediateSlotItemImageList.apply { onEach { it.setUpSlotItem() } }

    private fun generateWinItems() = winSlotItemImageList.apply { onEach { it.setUpSlotItem() } }

    private fun generateFakeItems() = fakeSlotItemImageList.apply { onEach { it.setUpSlotItem() } }

    private fun ImageView.setUpSlotItem() {
        val tmpSlotItemList = SlotItemContainer.list + SlotItemContainer.wild
        setImageResource(tmpSlotItemList.shuffled().first().resId)
    }

    private fun reset() {
        winSlotItemImageList.onEachIndexed { index, image -> fakeSlotItemImageList[index].setImageDrawable(image.drawable) }
        rootViewGroup.y = viewport.getY(LS.slotMinY)
    }



    suspend fun spin() = CompletableDeferred<Boolean>().also { continuation ->
        rootViewGroup.animate()
            .y(viewport.getY(LS.slotMaxY))
            .setDuration(TIME_SPIN_SLOT.toMS)
            .setInterpolator(PathInterpolatorCompat.create(0.3f, 0f, 0.4f, .9f))
            .withEndAction {
                reset()
                continuation.complete(true)
            }
            .start()
    }.await()
}