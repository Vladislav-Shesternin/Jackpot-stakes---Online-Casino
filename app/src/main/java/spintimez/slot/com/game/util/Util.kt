package spintimez.slot.com.game.util

import spintimez.slot.com.game.actors.slot.Matrix3x4
import spintimez.slot.com.util.length

interface Disposable {
    fun dispose()
}

data class SpinResult(
    val winSlotItemSet: Set<SlotItem>?,
)

data class FillResult(
    val winSlotItemList : List<SlotItem>,
    val intersectionList: List<Matrix3x4.Intersection>,
)

data class SlotItem(
    val id       : Int,
    val resId    : Int,
    var priceCoff: Float,
)

enum class FillStrategy {
     MIX, WIN
}

enum class AutospinState {
    DEFAULT, GO,
}

fun Long.transformToBalanceFormat(): String {
    val balance = toString().toMutableList()

    when (length) {
        4    -> balance.add(1, ' ')
        5    -> balance.add(2, ' ')
        6    -> balance.add(3, ' ')
        7    -> {
            balance.add(1, ' ')
            balance.add(5, ' ')
        }
        8    -> {
            balance.add(2, ' ')
            balance.add(6, ' ')
        }
        9    -> {
            balance.add(3, ' ')
            balance.add(7, ' ')
        }
        10   -> {
            balance.add(1, ' ')
            balance.add(5, ' ')
            balance.add(9, ' ')
        }
        11   -> {
            balance.add(2, ' ')
            balance.add(6, ' ')
            balance.add(10, ' ')
        }
        12   -> {
            balance.add(3, ' ')
            balance.add(7, ' ')
            balance.add(11, ' ')
        }
        else -> toString()
    }

    return balance.joinToString("")
}