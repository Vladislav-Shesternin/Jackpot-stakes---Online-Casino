package spintimez.slot.com.game.actors.slot

import spintimez.slot.com.game.util.FillResult
import spintimez.slot.com.game.util.FillStrategy
import spintimez.slot.com.util.log
import spintimez.slot.com.util.probability

class SlotFillManager(val slotList: List<Slot>) {

    var winFillResult: FillResult? = null
        private set



    private fun fillMix(isUseWild: Boolean = true) {
        log("fillMix")

        val combinationMatrixEnum: CombinationMatrixEnum3x4 = if (isUseWild && probability(20)) {
            log("fillMix use wild")
            Combination3x3.MixWithWild.values().toList().shuffled().first()
        } else Combination3x3.Mix.values().toList().shuffled().first()

        combinationMatrixEnum.logCombinationMatrixEnum()
        val combinationMatrix = combinationMatrixEnum.matrix.initialize()

        slotList.onEachIndexed { index, slotManager -> slotManager.slotItemWinList = combinationMatrix.generateSlot(index) }
    }

    private fun fillWin() {
        log("fillWin")

        val combinationMatrixEnum = listOf<CombinationMatrixEnum3x4>(
            *Combination3x3.WinMonochrome.values(),
            *Combination3x3.WinMonochromeWithWild.values(),
            *Combination3x3.WinColorful.values(),
            *Combination3x3.WinColorfulWithWild.values(),
        ).shuffled().first()

        combinationMatrixEnum.logCombinationMatrixEnum()
        val combinationMatrix = combinationMatrixEnum.matrix.initialize()

        slotList.onEachIndexed { index, slotManager -> slotManager.slotItemWinList = combinationMatrix.generateSlot(index) }

        log("""
            scheme = ${combinationMatrix.scheme}
            slotIndex = ${combinationMatrix.intersectionList?.map { it.slotIndex }}
            rowIndex = ${combinationMatrix.intersectionList?.map { it.rowIndex }}
        """.trimIndent())

        winFillResult = with(combinationMatrix) {
            if (winSlotItemList != null) FillResult(winSlotItemList!!, intersectionList!!)
            else null
        }
    }


    fun fill(fillStrategy: FillStrategy) {
        winFillResult = null

        when (fillStrategy) {
            FillStrategy.MIX -> fillMix()
            FillStrategy.WIN -> fillWin()
        }
    }



    private fun CombinationMatrixEnum3x4.logCombinationMatrixEnum() {
        val combinationEnumName = when (this) {
            is Combination3x3.Mix                   -> "Mix $name"
            is Combination3x3.MixWithWild           -> "Mix WILD $name"
            is Combination3x3.WinMonochrome         -> "Win MONOCHROME $name"
            is Combination3x3.WinMonochromeWithWild -> "Win MONOCHROME WILD $name"
            is Combination3x3.WinColorful           -> "Win COLORFUL $name"
            is Combination3x3.WinColorfulWithWild   -> "Win COLORFUL WILD $name"
            else                                    -> "Noname"
        }
        log(combinationEnumName)
    }

}