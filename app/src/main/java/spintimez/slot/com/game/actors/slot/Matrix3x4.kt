package spintimez.slot.com.game.actors.slot

import spintimez.slot.com.game.util.SlotItem

class Matrix3x4(
    private val winItemList: List<Item>? = null,
    val scheme: String = "",

    a0: Item,
    a1: Item,
    a2: Item,
    a3: Item,

    b0: Item,
    b1: Item,
    b2: Item,
    b3: Item,

    c0: Item,
    c1: Item,
    c2: Item,
    c3: Item,

    ) {

    private val slotA = listOf<Item>(a0, a1, a2, a3)
    private val slotB = listOf<Item>(b0, b1, b2, b3)
    private val slotC = listOf<Item>(c0, c1, c2, c3)

    private val slotList = listOf(slotA, slotB, slotC)

    private lateinit var shuffledSlotItemList: List<SlotItem>

    var intersectionList: List<Intersection>? = null
        private set
    var winSlotItemList: List<SlotItem>?      = null
        private set



    private fun generateAndSetData() {
        if (this::shuffledSlotItemList.isInitialized.not()) throw Exception("Используй метод initialize")

        val tmpIntersectionList = mutableListOf<Intersection>()
        val tmpWinSlotItemList  = mutableListOf<SlotItem>()

        winItemList?.let { winList ->

            val tmpWinItemList = winList + Item.W

            slotList.onEachIndexed { slotIndex, slot ->
                slot.onEachIndexed { itemIndex, item ->

                    if (tmpWinItemList.contains(item)) {
                        tmpIntersectionList.add(Intersection(
                            slotIndex = slotIndex,
                            rowIndex  = itemIndex,
                        ))
                    }

                    when {
                        winList.contains(item) -> tmpWinSlotItemList.add(shuffledSlotItemList[item.index])
                        item == Item.W         -> tmpWinSlotItemList.add(SlotItemContainer.wild)
                    }
                }
            }
        }

        intersectionList = tmpIntersectionList.ifEmpty { null }
        winSlotItemList  = tmpWinSlotItemList.ifEmpty { null }

    }



    fun initialize(): Matrix3x4 {
        shuffledSlotItemList = SlotItemContainer.list.shuffled()
        generateAndSetData()
        return this
    }

    fun generateSlot(slotIndex: Int): List<SlotItem> = mutableListOf<SlotItem>().apply {
        if (this@Matrix3x4::shuffledSlotItemList.isInitialized.not()) throw Exception("Используй метод initialize")

        var slotItem: SlotItem
        slotList[slotIndex].onEach { item ->
            slotItem = when (item) {
                Item.W -> SlotItemContainer.wild
                else   -> shuffledSlotItemList[item.index]
            }
            add(slotItem)
        }
    }



    data class Intersection(
        val slotIndex: Int,
        val rowIndex : Int,
    )

    enum class Item(val index: Int) {
        W(SlotItemContainer.SLOT_ITEM_WILD_ID),

        a1(0),
        a2(1),
        a3(2),
        a4(3),
        a5(4),
        a6(5),
        a7(6),
        a8(7),
    }

}
