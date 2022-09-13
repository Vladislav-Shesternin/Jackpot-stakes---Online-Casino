package spintimez.slot.com.game.actors.slot

import spintimez.slot.com.R
import spintimez.slot.com.game.util.SlotItem

object SlotItemContainer {

    const val SLOT_ITEM_WILD_ID = 10

    val wild = SlotItem(SLOT_ITEM_WILD_ID, R.drawable.wild, 2.0f)

    val list = listOf<SlotItem>(
        SlotItem(1, R.drawable._1,10f),
        SlotItem(2, R.drawable._2,10f),
        SlotItem(3, R.drawable._3,10f),
        SlotItem(4, R.drawable._4,10f),
        SlotItem(5, R.drawable._5,10f),
        SlotItem(6, R.drawable._6,10f),
        SlotItem(7, R.drawable._7,10f),
        SlotItem(8, R.drawable._8,10f),
    )

}